package testAOP;

import org.aspectj.lang.JoinPoint;

public class MyAspectWithAnnotations {

   /**
    * @Before execution(* testAOP.HelloWorld.greet(..))
    */
    public void beforeGreeting(JoinPoint joinPoint) {
        System.out.println("before greeting...");
    }

   /**
    * @After execution(* testAOP.HelloWorld.greet(..))
    */
    public void afterGreeting(JoinPoint joinPoint) {
        System.out.println("after greeting...");
    }
}