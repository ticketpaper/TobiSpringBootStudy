package com.study.tobispringboot;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

//@Component
@Service // 스테레오 타입 어노테이션
public class SimpleHelloService implements HelloService {
    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
