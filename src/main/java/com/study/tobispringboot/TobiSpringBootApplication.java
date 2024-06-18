package com.study.tobispringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

//@SpringBootApplication
@Configuration // 구성정보를 가지고 있는 클래스다.
@ComponentScan // 컴포넌트가 붙은 클래스를 찾아 빈으로 등록해달라는 어노테이션
// 이 클래스가 있는 패키지부터 시작해 하위 패키지까지 컴포넌트 어노테이션이 붙은 모든 클래스를 빈으로 등록, 필요시 의존 오브젝트를 찾아내고 이걸 생성자 호출할떄 파라미터로 넘겨주기도 함
public class TobiSpringBootApplication {
//    @Bean // 빈으로 등록
//    public HelloController helloController(HelloService helloService) { // 팩토리 메서드
//        return new HelloController(helloService);
//    }
//    @Bean // 빈으로 등록
//    public HelloService helloService() { // 팩토리 메서드
//        return new SimpleHelloService();
//    }

    @Bean
    public ServletWebServerFactory servletWebServerFactory() {
        return new TomcatServletWebServerFactory();
    }

    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }

    public static void main(String[] args) {
//        MySpringApplication.run(TobiSpringBootApplication.class, args);
        SpringApplication.run(TobiSpringBootApplication.class, args);
    }
}
