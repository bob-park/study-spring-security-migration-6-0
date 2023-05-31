package org.bobpark.studyspringsecurity.domain.login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("login")
public class LoginController {

    @GetMapping(path = "")
    public String login(){
        return "user/login/login";
    }
}
