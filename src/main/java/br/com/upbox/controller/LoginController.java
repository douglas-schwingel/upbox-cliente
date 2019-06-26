package br.com.upbox.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private static final Marker marker = MarkerFactory.getMarker("login-controller");

    @GetMapping("/login")
    public String login() {
        logger.info(marker, "Entrando na pagina de login");
        return "login-form";
    }
}
