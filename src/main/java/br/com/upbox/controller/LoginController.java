package br.com.upbox.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class LoginController {
    private static final Logger logger = Logger.getLogger(LoginController.class.getName());

    @GetMapping
    public String login() {
        logger.log(Level.INFO, "entrando na pagina de login");
        return "login-form";
    }
}
