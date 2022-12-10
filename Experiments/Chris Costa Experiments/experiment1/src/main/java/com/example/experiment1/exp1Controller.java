package com.example.experiment1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class exp1Controller {

    @GetMapping("/")
    public String Message(){
        String s = "Hello World";
        return s;
    }
}
