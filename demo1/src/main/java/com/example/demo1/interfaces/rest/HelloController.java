package com.example.demo1.interfaces.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    
    @GetMapping("/")
    public String hello() {
        return "Hello World!";
    }

    @GetMapping("/name")
    public String name() {
        return "Demo1";
    }

    @GetMapping("/waiter")
    public String waiter() {
        return "Demo1 - Waiter";
    }

    @GetMapping("/customer")
    public String customer() {
        return "Demo1 - Customer";
    }

}