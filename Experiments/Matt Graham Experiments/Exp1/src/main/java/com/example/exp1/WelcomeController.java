package com.example.exp1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "Hello and welcome to Matt's first experiment. To start, this page contains no information. Add parameters to the URL to see results.";
    }

    @GetMapping("/{name}/{year}/{school}/{major}")
    public String welcome(@PathVariable String name, @PathVariable String year, @PathVariable String school, @PathVariable String major) {
        return "" + name + " is a " + year + " at " + school + " studying " + major + ".";
    }

    @GetMapping("/{num}")
    public String welcome(@PathVariable int num) {
        String rand;
        switch (num) {
            case 0:
                rand = "cat";
                break;
            case 1:
                rand = "dog";
                break;
            case 2:
                rand = "fish";
                break;
            case 3:
                rand = "bird";
                break;
            default:
                rand = "hamster";
                break;
        }
        return "Your next pet should be a " + rand + ".";
    }

    @GetMapping("/profile/{name}/{age}")
    public String welcome(@PathVariable String name, @PathVariable int age) {
        if (age < 0) {
            return "This person's name is " + name + " and they have not been born yet";
        } else if (age < 13) {
            return "This person's name is " + name + " and they are a child";
        } else if (age < 20) {
            return "This person's name is " + name + " and they are a teenager";
        } else if (age < 62) {
            return "This person's name is " + name + " and they are an adult";
        } else {
            return "This person's name is " + name + " and they are a senior citizen";
        }
    }
}

