package com.bit2016.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)//어디에 붙일지를 결정
@Retention(RetentionPolicy.RUNTIME)
public @interface Auth {
	String role() default "user";
}
