# Spring

## 2023.1.3

- ApplicationContext启动和扫描并获取BeanDefinition的逻辑模拟
  - ComponentScan注解的解析，value所指的包下各类的加载
  - Component、Scope注解的解析，beanDefinition的定义，beanDefinitionMap的创建
  - Scope类型的判断，getBean的实现，单例池的创建

## 2023.1.4

- Bean生命周期：实例化、属性赋值、初始化
  - ByName依赖注入
  - Aware回调模拟实现
  - 基于BeanPostProcessor和JDK动态代理的AOP模拟实现



# Mybatis

## 2023.1.5

- 基于JDBC和JDK动态代理的mybatis执行SQL模拟实现
  - 支持@Select注解
  - Mapper中的方法返回值为自定义对象或List<自定义对象>

- 动态代理的InvocationHandler执行逻辑
  - 1.解析Mapper中方法的参数：Map<参数名，参数值>
  - 2.SQL语句解析：parserSQL+List<变量>
  - 3.构造并执行preparedStatement:1+2
  - 4.获取查询到的字段名：ResultSet-->ResultSetMetaData
  - 5.获取当前执行的方法希望的返回类型
  - 6.构造返回值：4+5

- TypeHandlerMap
  - 泛型接口实现基于不同类型调用不同TypeHandler实现类的方法。



# Tomcat

## 2023.1.6

- Tomcat连接TCP
  - 异步BIO+线程池获取并处理socket连接

- 按Servlet规范实现Request与Response
  - 按HTTP协议解析InputStream字节流，生成Request对象
  - 暂存响应体，生成Response对象，并提供方法按HTTP协议构造OutputStream
  - Tips：实现Request和Response时，需实现HttpServletRexxxx，可以借助一个抽象类默认实现HttpServletRexxxx，然后再基础该抽象类，然后重写需要的方法
  - Servlet.server()匹配请求类型



# SpringBoot

## 2023.1.10

- SpringBoot启动模拟
  - 多态选择WebServer（Tomcat、Jetty）
  - 模拟WebServer自动配置类，@Bean注册所有WebServer，@Conditional注解设置生效条件，\<optional\>确定依赖传递
  - 条件注解优化，利用AnnotatedTypeMetadata中的Attribute值
  - 了解AutoConfigurationImportSelector.class，帮助导入所有jar包的AutoConfiguration类。由加载jar包的类加载器在META-INF/spring.factories中读取.EnableAutoConfiguration键对应的值，在selectImports()方法中返回所有的AutoConfiguration类名。（@SpringBootApplication注解下面的@EnableAutoConfiguration中的@Import(AutoConfigurationImportSelector.class)是核心）
  - @SpringBootApplication注解下面的@SpringBootConfiguration中的@Configuration是核心
  - @SpringBootApplication注解下面的@ComponentScan的作用是——扫描@SpringBootApplication这个注解修饰的类所在的包









