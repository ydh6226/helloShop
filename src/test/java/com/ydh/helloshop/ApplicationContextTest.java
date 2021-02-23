package com.ydh.helloshop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootTest
public class ApplicationContextTest {

    @Autowired
    ApplicationContext ac;
    
    @Test
    public void test() throws Exception {
        String[] names = ac.getBeanDefinitionNames();
        for (String name : names) {
            Object bean = ac.getBean(name);
            System.out.println("bean = " + bean);
        }
    }

}
