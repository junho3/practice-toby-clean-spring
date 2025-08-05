package tobyspring.learningtest.arcunit.adapter;

import tobyspring.learningtest.arcunit.application.MyService;

public class MyAdapter {
    MyService myService;

    void run() {
        myService = new MyService();
        System.out.println(myService);
    }
}
