package com.example.taskmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {

    // TODO: Check if another technique can be used instead of declaring all the front-end paths
    @RequestMapping({"/", "/home", "/login", "/register"})
    public String index() {
        return "forward:/index.html";
    }
}
