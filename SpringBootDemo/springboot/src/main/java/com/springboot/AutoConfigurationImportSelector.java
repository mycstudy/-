package com.springboot;

import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class AutoConfigurationImportSelector implements DeferredImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        //SpringBoot默认的+第三方的jar 自动配置类的名称
        //问题-->如何获得第三方jar包的自动配置类呢？肯定不是检查每一个jar包中的所有类
        //SPI机制
        //jar包下的META-INF中有spring.factories是key-value对，其中有一个key是xxx.EnableAutoConfiguration，其值就是jar包提供的自动配置类

        return new String[0];
    }
}
