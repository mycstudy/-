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