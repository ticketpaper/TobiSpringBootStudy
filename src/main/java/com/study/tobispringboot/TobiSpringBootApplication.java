package com.study.tobispringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.jetty.JettyReactiveWebServerFactory;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@SpringBootApplication
public class TobiSpringBootApplication {
    public static void main(String[] args) {
//        SpringApplication.run(TobiSpringBootApplication.class, args);

        // 비어있는 Servlet Container 만들기
        // 서블릿 이라는 것은 자바의 표준기술이고, 이 표준 기술을 구현한 컨테이너가 여러가지가 있는데 톰캣이 대표적
        // 스프링부트는 내장형 톰캣을 제공함

        // 스프링부트에서 톰캣 서블릿 컨테이너를 내장해서 프로그램 코드로 쉽게 시작해 사용가능하게 해준 클래스가 있다.
        // TomcatServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
        ServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
        // Factory란 뭐냐 톰캣 서블릿 웹서버를 만드는 복잡한 생성 과정, 설정등을 지원해주고
        // 설정을 다 마치고 톰캣 서블릿 웹서버를 생성해다라는 요청을 하면 그때 만들어주는 역할을 해주는 도우미 클래스

        WebServer webServer = serverFactory.getWebServer(servletContext -> {
//            servletContext.addServlet("hello", new HttpServlet() {
//                @Override
//                protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//                    String name = req.getParameter("name");
//
//                    resp.setStatus(HttpStatus.OK.value());
//                    resp.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
//                    resp.getWriter().print("Hello Servlet " + name);
//                }
//            }).addMapping("/hello");

            // 서블릿이 여러개가 늘어나고 모든 서블릿이 컨테이너로부터 요청을 직접받아 모든 작업을 수행하고 리턴하면
            // 공통적인 작업이 각 서블릿 코드안에 중복되어서 등장한다.
            // 서블릿은 웹 요청을 직접적으로 request, response object를 다뤄저야 하는 방식이기에 자연스럽지않다.

            // 이를 해결하기위해 프론트 컨트롤러가 등장
            // 원래 서블릿은 각 url에 맞게 매핑을 해가지고 각기 다른 서블릿이 다른 url로 들어오는 요청을 맡아서 처리하는 방식으로 동작
            // 이때 모든 서블리셍 공통적으로 등장하는 코드를 중앙화된 오브젝트를 프론트 컨트롤러라고 한다.
            // 이로인해 프론트 컨트롤러는 공통적인 작업을 다 처리하고 요청에 종류에 따라서 이 로직을 처리하는 다른 오브젝트 한테 요청을 위임하는 방식
            // 대표적인 공통작업은 인증, 보안, 다국어처리 모든 웹 요청에 대해 공통적인 작업

            HelloController helloController = new HelloController();
            servletContext.addServlet("frontController", new HttpServlet() {
                @Override
                protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                    // 인증, 보안, 다국어처리 ,,, 등등 공통 기능 처리 함
                    if (req.getRequestURI().equals("/hello") && req.getMethod().equals(HttpMethod.GET.name())) {
                        // 각 URI와 맞는 Http 메서드에 따라 맞는 로직을 처리 (매핑)
                        String name = req.getParameter("name");
                        String ret = helloController.hello(name);

                        // 실질적인 웹 애플리케이션 로직은 다른 오브젝트 한테 위임 해야 한다.
                        // 실제 요청을 받아서 처리하는 코드를 동작시키는 동안에 2가지 중요한 작업이 수행된다.
                        // 이를 매핑과 바인딩이라고 한다.
                        // 매핑 : 웹 요청에 들어있는 정보를 활용해서 어떤 로직을 수행하는 코드를 호출할 것인가 결정하는 작업
                        // 바인딩 : 쿼리스트링 또는 폼을 통해 넘어오는 데이터를 처리하는 오브젝트에 파라미터로 넘겨주는 등의 작업들을 바인딩이라고 한다.
                        resp.setStatus(HttpStatus.OK.value());
                        resp.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
                        resp.getWriter().print(ret);
                    } else if (req.getRequestURI().equals("/user")) {
                        //
                    } else {
                        resp.setStatus(HttpStatus.NOT_FOUND.value());
                    }

                }
            }).addMapping("/*"); // 중앙화된 처리를 하기위해 모든 요청을 받아야함

        });
        // getWebServer() 메서드로 WebServer 타입으로 받을수 있다.
        // WebServer는 스프링부트에서 제티, 언더토우 같은 다른 서블릿 컨테이너를 지원하고 모두 일관된 방식으로 동작하기 위해 추상화를 해뒀음
//        ServletWebServerFactory jettyServerFactory = new JettyServletWebServerFactory();
//        ServletWebServerFactory undertowServerFactory = new UndertowServletWebServerFactory();

        webServer.start();

        // 웹기술을 이용한 개발을 하는 동안에는 웹의 표준 프로토콜을 이용해
        // 요청이 어떻게 들어가고 어떻게 응답을 받는지 그게 어떤 내용으로 구성 되어 있는지를 잘 알아야한다.
        // 그래야 어떠한 종류의 웹 기술을 만나더라도 이 기술에서는 요청을 받아 메서드로 전달해주는구나를 파악 해야한다.
        // 내가 코드를 작성하면 어떻세 응답이 만들어지는지 생각해야한다.

        // 자바 메서드의 파라미터와 리턴값 이게 요청과 응답하고 어떻게 매핑이 되는가 파악해야 한다.
    }
}
