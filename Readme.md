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











