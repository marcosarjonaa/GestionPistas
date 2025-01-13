package com.iesvdc.acceso.pistasdeportivas.controladores;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.iesvdc.acceso.pistasdeportivas.modelos.Instalacion;
import com.iesvdc.acceso.pistasdeportivas.modelos.Reserva;
import com.iesvdc.acceso.pistasdeportivas.modelos.Usuario;
import com.iesvdc.acceso.pistasdeportivas.repos.RepoMReserva;
import com.iesvdc.acceso.pistasdeportivas.repos.RepoReserva;
import com.iesvdc.acceso.pistasdeportivas.repos.RepoUsuario;

@Controller
@RequestMapping("/mis-datos/mis-reservas")

public class ControMReservas {

    @Autowired
    RepoMReserva repoMReserva;

    private Usuario getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Usuario) authentication.getPrincipal();
    }

    @GetMapping("")
    public String listarReservas(Model model) {
        Usuario loggedUser = getLoggedUser();
        List<Reserva> reservas = repoMReserva.findByUsuario(loggedUser);
        model.addAttribute("reservas", reservas);
        return "mis-datos/mis-reservas";
    }

    @GetMapping("/crear")
    public String formularioReserva(Model modelo) {
        modelo.addAttribute("reserva", new Reserva());
        return "mis-datos/crear-reserva";
    }

    @PostMapping("/crear")
    public String crearReserva(@ModelAttribute Reserva reserva, Model model) {
        Usuario usuario = getLoggedUser();
        LocalDate hoy = LocalDate.now();

        // Validamos la fecha para reservar
        if (reserva.getFecha().isBefore(hoy) || reserva.getFecha().isAfter((hoy.plusWeeks(2)))) {

            model.addAttribute("error", "No se puede reservar el mismo dia que se hace la reserva");
            return "mis-datos/mis-reservas";
        }

        // Validar reservas existentes
        boolean reservaExistente = repoMReserva.existsByUsuarioAndFecha(usuario, reserva.getFecha());
        if (reservaExistente) {
            model.addAttribute("error", "Ya existe una reserva con esa fecha");
            return "mis-datos/crear-reserva";
        }

        reserva.setUsuario(usuario);
        repoMReserva.save(reserva);
        return "redirect:/mis-datos/mis-reservas";
    }

    @GetMapping("/edit/{id}")
    public String editarFormularioReserva(@PathVariable Long id, Model model) {

        Reserva reserva = repoMReserva.findById(id).orElse(null);

        if (reserva == null || !reserva.getUsuario().equals(getLoggedUser())) {

            return "redirect:/mis-datos/mis-reservas";
        }
        model.addAttribute("reserva", reserva);
        return "mis-datos/editar-reserva";
    }

    @PostMapping("/edit/{id}")
    public String editarReserva(@PathVariable Long id, @ModelAttribute Reserva reserva, Model model) {

        Usuario usuario = getLoggedUser();
        LocalDate hoy = LocalDate.now();

        // Validamos la fecha para reservar
        if (reserva.getFecha().isBefore(hoy) || reserva.getFecha().isAfter((hoy.plusWeeks(2)))) {

            model.addAttribute("error", "No se puede reservar el mismo dia que se hace la reserva");
            return "mis-datos/editar-reserva";
        }

        boolean reservaExistente = repoMReserva.existsByUsuarioAndFecha(usuario, reserva.getFecha());
        if (reservaExistente) {
            model.addAttribute("error", "Ya existe una reserva con esa fecha");
            return "mis-datos/crear-reserva";
        }

        Reserva reservaExiste = repoMReserva.findById(id).orElse(null);

        if (reservaExiste == null || !reservaExiste.getUsuario().equals(usuario)) {
            return "redirect:/mis-datos/mis-reservas";
        }

        reservaExiste.setFecha(reserva.getFecha());
        reservaExiste.setHorario(reserva.getHorario());
        repoMReserva.save(reservaExiste);
        return "redirect:/mis-datos/mis-reservas";

    }

    @PostMapping("/eliminar/{id}")
    public String eliminarReserva(@PathVariable Long id) {
        Reserva reserva = repoMReserva.findById(id).orElse(null);
        if (reserva != null && reserva.getUsuario().equals(getLoggedUser())) {
            repoMReserva.delete(reserva);
        }
        return "redirect:/mis-datos/mis-reservas";
    }
}
