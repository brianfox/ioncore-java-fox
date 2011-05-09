package testAOP;

import org.aspectj.lang.JoinPoint;

public class MyAspect {

    public void beforeGreeting(JoinPoint joinPoint) {
        System.out.println("before greeting...");
    }

    public void afterGreeting(JoinPoint joinPoint) {
        System.out.println("after greeting...");
    }
} 