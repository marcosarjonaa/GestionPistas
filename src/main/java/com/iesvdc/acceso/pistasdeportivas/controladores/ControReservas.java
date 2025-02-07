package com.iesvdc.acceso.pistasdeportivas.controladores;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.iesvdc.acceso.pistasdeportivas.modelos.Horario;
import com.iesvdc.acceso.pistasdeportivas.modelos.Reserva;
import com.iesvdc.acceso.pistasdeportivas.repos.RepoHorario;
import com.iesvdc.acceso.pistasdeportivas.repos.RepoInstalacion;
import com.iesvdc.acceso.pistasdeportivas.repos.RepoReserva;
import com.iesvdc.acceso.pistasdeportivas.repos.RepoUsuario;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/reservas")
public class ControReservas {

    @Autowired
    RepoReserva repoReserva;

    @Autowired
    RepoUsuario repoUsuario;

    @Autowired
    RepoInstalacion repoInstalacion;

    @Autowired
    RepoHorario repoHorario;

    @GetMapping("")
    public String getReservas(
        Model model,
        @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        List<Reserva> reservas = repoReserva.findAll();
        model.addAttribute("reservas", reservas);
        return "reservas/reservas";
    }

    @GetMapping("/add")
    public String addReserva(Model modelo) {
        modelo.addAttribute("reserva", new Reserva());
        LocalDate fecha = LocalDate.now();
        LocalDate fechaMax = fecha.plusDays(7);
        modelo.addAttribute("min", fecha);
        modelo.addAttribute("max", fechaMax);
        modelo.addAttribute("operacion", "ADD");
        modelo.addAttribute("usuarios", repoUsuario.findAll());
        modelo.addAttribute("horarios", repoHorario.findAll());
        modelo.addAttribute("instalaciones", repoInstalacion.findAll());
        return "/reservas/add";
    }

    @PostMapping("/add")
    public String addReserva(
        @ModelAttribute("reserva") Reserva reserva)  {
        repoReserva.save(reserva);
        return "redirect:/reservas";
    }

    // formulario editar un horario
    @GetMapping("/edit/{id}")
    public String editReserva(@PathVariable @NonNull Long id, Model modelo) {
        Optional<Reserva> oReserva = repoReserva.findById(id);
        if (oReserva.isPresent()) {
            modelo.addAttribute("reserva", oReserva.get());
            LocalDate fecha = LocalDate.now();
            modelo.addAttribute("min", fecha);
            modelo.addAttribute("max", fecha.plusDays(7));
            modelo.addAttribute("operacion", "EDIT");
            modelo.addAttribute("horarios", repoHorario.findAll());
            modelo.addAttribute("usuarios", repoUsuario.findAll());
            return "/reservas/add";
        } else {
            modelo.addAttribute("mensaje", "La reserva no exsiste");
            modelo.addAttribute("titulo", "Error editando reserva.");
            return "/error";
        }
    }

    @PostMapping("/edit/{id}")
    public String editReserva(
        @ModelAttribute("reserva") Reserva reserva)  {
        boolean reservaExistente = repoReserva.existsByUsuarioAndFechaAndHorario(reserva.getUsuario(), reserva.getFecha(), reserva.getHorario());
        if (reservaExistente) {
            return "redirect:/error";
        }
        repoReserva.save(reserva);
        return "redirect:/reservas";
    }


    @GetMapping("/del/{id}")
    public String delReserva(@PathVariable @NonNull Long id, Model modelo) {
        Optional<Reserva> oReserva = repoReserva.findById(id);
        if (oReserva.isPresent()) {
            modelo.addAttribute("reserva", oReserva.get());
            modelo.addAttribute("operacion", "DEL");
            modelo.addAttribute("horarios", repoHorario.findAll());
            modelo.addAttribute("usuarios", repoUsuario.findAll());
            return "/reservas/add";
        } else {
            modelo.addAttribute("mensaje", "La reserva no exsiste");
            modelo.addAttribute("titulo", "Error borrando reserva.");
            return "/error";
        }
    }

    @PostMapping("/del/{id}")
    public String delReserva(
        @ModelAttribute("reserva") Reserva reserva)  {
        repoReserva.delete(reserva);
        return "redirect:/reservas";
    }
    
}
