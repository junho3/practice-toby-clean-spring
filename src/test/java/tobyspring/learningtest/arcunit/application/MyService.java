package tobyspring.learningtest.arcunit.application;

public class MyService {
    MyService2 myService2;

    void run() {
        myService2 = new MyService2();
        System.out.println(myService2);
    }
}
