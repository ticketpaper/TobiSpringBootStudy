package com.study.tobispringboot;

import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class MySpringApplication {
    public static void run(Class<?> applicationClass, String[] args) {
        //        SpringApplication.run(TobiSpringBootApplication.class, args);

        // 비어있는 Servlet Container 만들기
        // 서블릿 이라는 것은 자바의 표준기술이고, 이 표준 기술을 구현한 컨테이너가 여러가지가 있는데 톰캣이 대표적
        // 스프링부트는 내장형 톰캣을 제공함

        // 스프링부트에서 톰캣 서블릿 컨테이너를 내장해서 프로그램 코드로 쉽게 시작해 사용가능하게 해준 클래스가 있다.
        // TomcatServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
//        ServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
        // Factory란 뭐냐 톰캣 서블릿 웹서버를 만드는 복잡한 생성 과정, 설정등을 지원해주고
        // 설정을 다 마치고 톰캣 서블릿 웹서버를 생성해다라는 요청을 하면 그때 만들어주는 역할을 해주는 도우미 클래스

//        WebServer webServer = serverFactory.getWebServer(servletContext -> {
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

//            HelloController helloController = new HelloController();
        // helloController 를 직접 생성하는것이 아닌, 스프링 컨테이너한테 요청 가져와 사용하는 방식으로 변경
        // 스프링 컨테이너를 대표하는 인터페이스 ApplicationContext 애플리케이션을 구성하는 많은 정보, 어떤 빈이 등록, 리소스 접근, 내부 이벤트 전달, 구독
        // 애플리케이션이라면 필요한 많은 작업들을 담고있는 오브젝트들이 구현해야하는 것이 ApplicationContext = 스프링 컨테이너
//        GenericWebApplicationContext applicationContext = new GenericWebApplicationContext() {
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext() {
            // 익명클래스를 사용
            @Override
            protected void onRefresh() {
                // 서블릿 컨테이너를 만들고 서블릿을 초기화하는 등의 작업을 스프링 컨테이너가 초기화 되는 과정중에 일어나도록 변경
                super.onRefresh();
                ServletWebServerFactory serverFactory = this.getBean(ServletWebServerFactory.class);
                DispatcherServlet dispatcherServlet = this.getBean(DispatcherServlet.class);
//                dispatcherServlet.setApplicationContext(this);

                WebServer webServer = serverFactory.getWebServer(servletContext -> {
                    servletContext.addServlet("dispatcherServlet", dispatcherServlet
                            // 매핑 작업, 쿼리스트링으로 들어오는 파라미터를 메서드 파라미터로 넘겨 변환하는 바인딩 작업을 코드를 다 집어넣을수 없다.
                            // Dispatcher Servlet는 지금까지 작업했던 프론트 컨트롤러의 많은 기능들을 수행해주는 Servlet이다.
                    ).addMapping("/*"); // 중앙화된 처리를 하기위해 모든 요청을 받아야함
                });
                // getWebServer() 메서드로 WebServer 타입으로 받을수 있다.
                // WebServer는 스프링부트에서 제티, 언더토우 같은 다른 서블릿 컨테이너를 지원하고 모두 일관된 방식으로 동작하기 위해 추상화를 해뒀음
                // ServletWebServerFactory jettyServerFactory = new JettyServletWebServerFactory();
                // ServletWebServerFactory undertowServerFactory = new UndertowServletWebServerFactory();

                webServer.start();

            }
        };
//        applicationContext.registerBean(HelloController.class); // 빈을 등록, 빈 클래스를 지정
//        applicationContext.registerBean(SimpleHelloService.class);
        applicationContext.register(applicationClass);
        applicationContext.refresh(); // 구성정보를 이용해 컨테이너를 초기화, 빈 오브젝트를 만들어줌
//         refresh()는 템플릿 메서드로 만들어져 있다.
//         템플릿 메서드 패턴을 사용하면 그 안에 여러 개의 Hook 메서드를 주입해 넣기도 한다.
//         그래서 템플릿 메서드 안에서 일정한 순서에 의해 작업들이 호출이 되는데 그 중에 서브 클래스에서 확장하는 방법을 통해서 특정 시점에 어떤 작업을 수행해 기능을 유연하게 확장하는 기법
//        Hook 메서드 이름이 onRefresh() refresh가 일어나는 중에 즉 스프링 컨테이너를 초기화하는 중에 부가적으로 어떤 작업을 수행할 필요가 있다면 사용


        // 스프링 컨테이너가 초기화 될때 그 정보를 이용해 오브젝트를 만들게 해둠
        // 스프링 컨테이너를 통해 컨테이너가 가지고 있는 오브젝트를 getBean이라는 메서드를 통해전달 받아 이걸 사용함
        // new 해서 사용하는거랑 다른게 뭐냐?
        // 여러가지 중요한 스프링 컨테이너가 할 수 있는 일들을 이후에 계속 적용하는 기본 구조를 짜놨다는갓이 의미가 있다.
        // 스프링 컨테이너는 기본적으로 이 아안에 어떤 타입의 오브젝트를 만들 때 딱 한 번만 만든다.
        // 한 번만 만드는게 무슨 의미 ?? -> 스프링 컨테이너가 가지고 있는 오브젝트를 필요로 하는 오브젝트들이 요청 할때마다
        // 새로운 오브젝트를 만들어 전달하는게 아니라 처음에 만들어진 오브젝트를 가져다가 쓰는것
        // 여러 오브젝트들이 요청을 해도 동일한 오브젝트를 가져다가 쓰는것
        // 우리는 애플리케이션에서 딱 하나의 오브젝트만 만들어두고 이를 재사용하게 만드는 패턴을 "싱글톤 패턴"이라고 한다.
        // 스프링 컨테이너는 이런 싱글톤 패턴을 사용한 것과 유사하게 어떤 타입의 오브젝트를 딱 한번만 만들어두고 이를 재사용할 수 있게 해주는 기능을 제공한다.
        // 그래서 스프링 컨테이너를 싱글톤 레지스트리 라고도 한다.
        // 싱글톤 패턴을 사용하지 않고도 마치 싱글톤 패턴을 쓰는 것 처럼 굳이 매 요청마다 계속 재사용하게 해줌


//            servletContext.addServlet("frontController", new HttpServlet() {
//                @Override
//                protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//                    // 인증, 보안, 다국어처리 ,,, 등등 공통 기능 처리 함
//                    if (req.getRequestURI().equals("/hello") && req.getMethod().equals(HttpMethod.GET.name())) {
//                        // 각 URI와 맞는 Http 메서드에 따라 맞는 로직을 처리 (매핑)
//                        String name = req.getParameter("name"); // 바인딩 작업
//                        HelloController helloController = applicationContext.getBean(HelloController.class); // 빈 가져오기, 이름 또는 클래스로 지정
//                        String ret = helloController.hello(name);
//
//                        // 실질적인 웹 애플리케이션 로직은 다른 오브젝트 한테 위임 해야 한다.
//                        // 실제 요청을 받아서 처리하는 코드를 동작시키는 동안에 2가지 중요한 작업이 수행된다.
//                        // 이를 매핑과 바인딩이라고 한다.
//                        // 매핑 : 웹 요청에 들어있는 정보를 활용해서 어떤 로직을 수행하는 코드를 호출할 것인가 결정하는 작업
//                        // 바인딩 : 쿼리스트링 또는 폼을 통해 넘어오는 데이터를 처리하는 오브젝트에 파라미터로 넘겨주는 등의 작업들을 바인딩이라고 한다.
//
////                        resp.setStatus(HttpStatus.OK.value());
//                        // 명시적으로 넣지 않아도 알아서 200으로 설정해서 넘겨줌
//
////                        resp.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
//                        resp.setContentType(MediaType.TEXT_PLAIN_VALUE); // header 이름 생략 가능
//                        resp.getWriter().print(ret);
//                    } else if (req.getRequestURI().equals("/user")) {
//                        //
//                    } else {
//                        resp.setStatus(HttpStatus.NOT_FOUND.value());
//                    }
//                }
//            }).addMapping("/*"); // 중앙화된 처리를 하기위해 모든 요청을 받아야함


        // 웹기술을 이용한 개발을 하는 동안에는 웹의 표준 프로토콜을 이용해
        // 요청이 어떻게 들어가고 어떻게 응답을 받는지 그게 어떤 내용으로 구성 되어 있는지를 잘 알아야한다.
        // 그래야 어떠한 종류의 웹 기술을 만나더라도 이 기술에서는 요청을 받아 메서드로 전달해주는구나를 파악 해야한다.
        // 내가 코드를 작성하면 어떻세 응답이 만들어지는지 생각해야한다.

        // 자바 메서드의 파라미터와 리턴값 이게 요청과 응답하고 어떻게 매핑이 되는가 파악해야 한다.
    }
}
