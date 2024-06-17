package com.study.tobispringboot;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 직접 만든 애노테이션
@Retention(RetentionPolicy.RUNTIME) // 어디까지 살아 있을것인가, 언제까지 유지?
@Target(ElementType.TYPE) // 적용할 대상 지정 가능 ( 클래스나 인터페이스 같은 타입 )
@Component // 메타 에노테이션으로 등록
public @interface MyComponent {
}
