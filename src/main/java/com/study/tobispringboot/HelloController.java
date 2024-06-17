package com.study.tobispringboot;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController // 디스패처 서블릿이 클래스 레벨에 RequestMapping 사용 안해도 매핑 정보가가 담겨있을거라고 판단하고 메서드를 뒤짐
// @RestController 어노테이션이 붙어있는 클래스의 모든 메서드는 @ResponseBody 붙어있다고 가정

//@RequestMapping("/hello")
// 디스패처 서블릿이 매핑 정보를 만들때 기본적으로 클래스 레벨에 있는 정보를 먼저 참고하고

//@MyComponent // 메타 에노테이션 = 애토네이션 위에 붙은 애노테이션, 이것도 역시 빈으로 등록
// 왜 사용? 빈이 어떠한 종류다 라고 명시 (계층형 아키텍처) 어떤 역할을 하는 애노테이션이다
public class HelloController implements ApplicationContextAware{
    private final HelloService helloService;
    private final ApplicationContext applicationContext;

    public HelloController(HelloService helloService, ApplicationContext applicationContext) {
        this.helloService = helloService;
        this.applicationContext = applicationContext;
        System.out.println("applicationContext = " + applicationContext);
    }
    @GetMapping("/hello") // 메서드 레벨에 붙어있는 정보를 추가한다. 이런식으로 두가지 정보를 합성해서 최종적으로 hello라는 메서드가 어떤 요청에 매핑되는지 정보를 확인한다.
    // 디스패처 서블릿은 서블릿 컨테이너인 ApplicationContext를 생성자로 받았다.
    // 그러면 디스패처 서블릿은 등록되어있는 빈을 다뒤진다. 그 중에서 웹 요청을 처리할 수 있는 매핑 정보를 가지고 있는 클래스를 찾는다.
    // GetMapping 이나 RequestMapping 같은게 붙어 있으면 이거는 웹 요청을 처리할 수 있도록 만들어진 웹 컨트롤러구나 라고 판단하고 그 안에 요청 정보를 추출한다.
    // /hello 에 Get이면 hello 메서드 이걸 추출해 매핑에 사용할 매핑 테이블을 하나 만든다.
    // 그 이후 웹 요청이 들어오면 그걸 참고해서 이걸 담당한 빈 오브젝트와 메서드를 확인한다.
    // 하지만 이렇게만 쓰면 디스패처 서블릿이 찾을수 없다.
    // 메서드 레벨까지 다 뒤지려면 규모가 커질수록 많아지는데 그걸 매번 다 뒤지는 거는 번거로운 일
    // 그래서 클래스 레벨에 어노테이션을 하나 준다.
    @ResponseBody
    // 디스패처 서블릿은 @ResponseBody가 없고 반환이 String 타입이면 view 라는 html 템플릿찾아서 view를 리턴해줘라 이런 의미로 이해
    public String hello(String name) {
        return helloService.sayHello(Objects.requireNonNull(name));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("applicationContext = " + applicationContext);
    }
}
