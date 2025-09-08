package com.revisacaminhoes.site.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TesteController {

    @GetMapping("/api/hello")
    public String hello() {
        return "API Revisa Caminhões funcionando!";
    }
}
