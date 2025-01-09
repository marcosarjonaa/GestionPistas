package com.iesvdc.acceso.pistasdeportivas.controladores;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.iesvdc.acceso.pistasdeportivas.modelos.Reserva;
import com.iesvdc.acceso.pistasdeportivas.repos.RepoInstalacion;
import com.iesvdc.acceso.pistasdeportivas.repos.RepoReserva;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/reservas")
public class ControReservas {

    @Autowired
    RepoReserva repoReserva;

    @Autowired
    RepoInstalacion repoInstalacion;

    @GetMapping("")
    public String getReservas(
        Model model,
        @PageableDefault(size = 10, sort = "id") Pageable pageable) {

        Page<Reserva> page = repoReserva.findAll(pageable);
        model.addAttribute("page", page);
        model.addAttribute("reservas", page.getContent());
        model.addAttribute("instalaciones", repoInstalacion.findAll());
        return "reservas/reservas";
    }

    // formulario añadir un horario a una instalación
    @GetMapping("/add")
    public String addReserva(Model modelo) {
        modelo.addAttribute("reservas", new Reserva());
        modelo.addAttribute("operacion", "ADD");
        modelo.addAttribute("instalaciones", repoInstalacion.findAll());
        return "/reservas/add";
    }

    @PostMapping("/add")
    public String addReserva(
        @ModelAttribute("Reserva") Reserva reserva)  {
        repoReserva.save(reserva);
        return "redirect:/reserva";
    }

    // formulario editar un horario
    @GetMapping("/edit/{id}")
    public String editReserva( 
        @PathVariable @NonNull Long id,
        Model modelo) {

        Optional<Reserva> oReserva = repoReserva.findById(id);
        if (oReserva.isPresent()) {
            modelo.addAttribute("reserva", oReserva.get());
            modelo.addAttribute("operacion", "EDIT");
            return "/reserva/add";
        } else {
            modelo.addAttribute("mensaje", "La instalación no exsiste");
            modelo.addAttribute("titulo", "Error editando instalación.");
            return "/error";
        }
    }

    @PostMapping("/edit/{id}")
    public String editHorario(
        @ModelAttribute("Reserva") Reserva reserva)  {
        repoReserva.save(reserva);
        return "redirect:/reserva";
    }

    // formulario borrar
    @GetMapping("/del/{id}")
    public String delReserva( 
        @PathVariable @NonNull Long id,
        Model modelo) {

        Optional<Reserva> oReserva = repoReserva.findById(id);
        if (oReserva.isPresent()) {
            modelo.addAttribute("operacion", "DEL");
            modelo.addAttribute("instalaciones", repoInstalacion.findAll());
            modelo.addAttribute("Reserva", oReserva.get());
            return "/reservas/add";
        } else {
            modelo.addAttribute("mensaje", "La instalación no exsiste");
            modelo.addAttribute("titulo", "Error borrando instalación.");
            return "/error";
        }
    }

    @PostMapping("/del/{id}")
    public String delReserva(
        @ModelAttribute("Reserva") Reserva reserva)  {
        repoReserva.delete(reserva);
        return "redirect:/reserva";
    }
    
}
