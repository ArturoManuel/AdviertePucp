
package com.example.adviertepucp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoguinController {
    public LoguinController() {
    }

    @GetMapping({""})
    public String index() {
        return "/loguin/loguin";
    }
}
