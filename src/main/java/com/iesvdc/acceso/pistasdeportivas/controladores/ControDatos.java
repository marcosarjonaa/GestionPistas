package com.iesvdc.acceso.pistasdeportivas.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/mis-datos")
public class ControDatos {
    
    @GetMapping("")
    public String getMisDatos() {
        return "mis-datos/mis-datos";
    }

    
}
