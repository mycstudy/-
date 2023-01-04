package com.spring;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class AnnotationConfigurationApplicationContext {
    private Class<?> configClass;
    private ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>();    //单例池
    private ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    private List<BeanPostProcessor> beanPostProcessors=new ArrayList<>();

    public AnnotationConfigurationApplicationContext(Class<?> configClass) {
        this.configClass = configClass;

        //解析配置类-->解析注解
        //解析ComponentScan-->获取扫描路径-->扫描（类加载）-->BeanDefinition-->BeanDefinitionMap-->单例池
        scan();
    }

    private Object createBean(String beanName, BeanDefinition beanDefinition) {
        //Bean生命周期
        Class clazz = beanDefinition.getClazz();
        try {
            //bean创建
            //此时调用的是默认的无参构造器进行对象构造，需要对参数赋值-->依赖注入
            Object instance = clazz.getDeclaredConstructor().newInstance();

            //依赖注入:bean属性赋值
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    //找对象，byName or byType-->byName
                    Object bean = getBean(field.getName());
                    if (field.getAnnotation(Autowired.class).required() && bean == null)
                        throw new NullPointerException();
                    field.setAccessible(true);
                    field.set(instance, bean);
                }
            }

            //Aware回调
            if (instance instanceof BeanNameAware) {
                ((BeanNameAware) instance).setBeanName(beanName);
            }

            //BeanPostProcessor前置处理
            for (BeanPostProcessor beanPostProcessor:beanPostProcessors){
                instance=beanPostProcessor.postProcessBeforeInitialization(instance,beanName);
            }

            //InitializingBean初始化
            if (instance instanceof InitializingBean) {
                try {
                    ((InitializingBean) instance).afterPropertiesSet();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            //BeanPostProcessor后置处理
            for (BeanPostProcessor beanPostProcessor:beanPostProcessors){
                instance=beanPostProcessor.postProcessAfterInitialization(instance,beanName);
            }

            return instance;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private void scan() {
        if (configClass.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScan = (ComponentScan) configClass.getDeclaredAnnotation(ComponentScan.class);
            String path = componentScan.value();    //扫描路径

            //扫描==加载
            //bootstrap-->jre/lib
            //ext-------->jre/ext/lib
            //app-------->classpath
            ClassLoader classLoader = AnnotationConfigurationApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(path.replace(".", "/"));
            File file = new File(resource.getFile());
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f : files) {
                    String fileName = f.getAbsolutePath();
                    if (fileName.endsWith(".class")) {
                        String className = fileName.substring(fileName.indexOf("com"), fileName.indexOf(".class"));
                        try {
                            Class<?> clazz = classLoader.loadClass(className.replace("\\", "."));
                            if (clazz.isAnnotationPresent(Component.class)) {
                                //当前类被定义为了一个Bean：解析Bean类型--> 单例Bean or 原型Bean
                                //单例Bean：map  <beanName,Bean对象>
                                //getBean方法也需要解析Bean类型，从className逆着解析，还存在重复解析问题-->BeanDefinition
                                BeanDefinition beanDefinition = new BeanDefinition();
                                Component componentAnnotation = clazz.getDeclaredAnnotation(Component.class);
                                String beanName = componentAnnotation.value();
                                beanDefinition.setClazz(clazz);

                                if (clazz.isAnnotationPresent(Scope.class)) {
                                    Scope scope = clazz.getDeclaredAnnotation(Scope.class);
                                    beanDefinition.setScope(scope.value());
                                } else {
                                    beanDefinition.setScope("singleton");
                                }
                                beanDefinitionMap.put(beanName, beanDefinition);

                                //单例池
                                if (beanDefinition.getScope().equals("singleton")) {
                                    Object o = createBean(beanName, beanDefinition);
                                    singletonObjects.put(beanName, o);
                                }

                                //处理BeanPostProcessor子类
                                if (BeanPostProcessor.class.isAssignableFrom(clazz)) {
                                    Object bean = getBean(beanName);
                                    beanPostProcessors.add((BeanPostProcessor) bean);
                                }
                            }
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

    public Object getBean(String beanName) {
        if (beanDefinitionMap.containsKey(beanName)) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if (beanDefinition.getScope().equals("singleton")) {
                //单例池中获取
                Object o = singletonObjects.get(beanName);
                return o;
            } else if (beanDefinition.getScope().equals("prototype")) {
                //创建bean
                Object bean = createBean(beanName, beanDefinition);
                return bean;
            }
        } else {
            //不存在bean，抛出异常
            throw new NullPointerException();
        }
        return null;
    }
}
