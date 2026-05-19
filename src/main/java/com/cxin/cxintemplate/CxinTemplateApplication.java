package com.cxin.cxintemplate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
public class CxinTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(CxinTemplateApplication.class, args);
    }

}
