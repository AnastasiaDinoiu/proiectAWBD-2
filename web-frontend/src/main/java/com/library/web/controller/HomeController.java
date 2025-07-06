package com.library.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class HomeController {

    @GetMapping("/")
    public String home() {
        log.info("Accessing home page");
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        log.info("Accessing login page");
        return "login";
    }
}