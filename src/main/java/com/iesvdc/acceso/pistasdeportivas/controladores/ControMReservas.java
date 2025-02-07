package com.iesvdc.acceso.pistasdeportivas.controladores;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.lang.NonNull;

import com.iesvdc.acceso.pistasdeportivas.modelos.Reserva;
import com.iesvdc.acceso.pistasdeportivas.modelos.Usuario;
import com.iesvdc.acceso.pistasdeportivas.repos.RepoHorario;
import com.iesvdc.acceso.pistasdeportivas.repos.RepoInstalacion;
import com.iesvdc.acceso.pistasdeportivas.repos.RepoMReserva;
import com.iesvdc.acceso.pistasdeportivas.repos.RepoReserva;
import com.iesvdc.acceso.pistasdeportivas.repos.RepoUsuario;

@Controller
@RequestMapping("/mis-reservas")
public class ControMReservas {

    @Autowired
    RepoMReserva repoMReserva;

    @Autowired
    RepoUsuario repoUsuario;

    @Autowired
    RepoReserva repoReserva;

    @Autowired
    RepoHorario repoHorario;

    @Autowired
    RepoInstalacion repoInstalacion;

    private Usuario getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return repoUsuario.findByUsername(currentPrincipalName).stream().findFirst().orElse(null);
    }

    @GetMapping("")
    public String getReservas(Model model, @PageableDefault(size = 10, sort = "id") Pageable pageable,
            @RequestParam(value = "error", required = false) String error) {
        Usuario usuarioLogueado = getLoggedUser();
        List<Reserva> mreservas = repoMReserva.findByUsuario(usuarioLogueado, pageable);
        model.addAttribute("mreservas", mreservas);

        if (error != null) {
            model.addAttribute("error", error);
        }

        return "mis-reservas/mis-reservas";
    }

    @GetMapping("/add")
    public String addReserva(Model modelo) {
        Usuario usuarioLogueado = getLoggedUser();
        LocalDate fecha = LocalDate.now();
        LocalDate fechaMax = fecha.plusDays(14);
        modelo.addAttribute("min", fecha);
        modelo.addAttribute("max", fechaMax);
        modelo.addAttribute("loggedUser", usuarioLogueado);
        modelo.addAttribute("reserva", new Reserva());
        modelo.addAttribute("operacion", "ADD");
        modelo.addAttribute("usuarios", repoUsuario.findAll());
        modelo.addAttribute("horarios", repoHorario.findAll());
        modelo.addAttribute("instalaciones", repoInstalacion.findAll());
        return "mis-reservas/add";
    }

    @PostMapping("/add")
    public String addReserva(@ModelAttribute("reserva") Reserva reserva) {
        Usuario usuarioLogueado = getLoggedUser();
        LocalDate hoy = LocalDate.now();

        if (reserva.getFecha().isBefore(hoy) || reserva.getFecha().isAfter(hoy.plusWeeks(2))) {
            return "redirect:/mis-reservas?error=La+fecha+debe+estar+entre+hoy+y+dos+semanas+adelante.";
        }
        boolean reservaExistente = repoMReserva.existsByUsuarioAndFecha(usuarioLogueado, reserva.getFecha());
        if (reservaExistente) {
            return "redirect:/mis-reservas?error=Ya+existe+una+reserva+para+esa+fecha.";
        }

        reserva.setUsuario(usuarioLogueado);
        repoReserva.save(reserva);
        return "redirect:/mis-reservas";
    }

    @GetMapping("/edit/{id}")
    public String editReserva(@PathVariable @NonNull Long id, Model modelo) {
        Optional<Reserva> oReserva = repoReserva.findById(id);
        if (oReserva.isPresent()) {
            Reserva reserva = oReserva.get();

            if (!reserva.getFecha().isAfter(LocalDate.now())) {
                return "redirect:/mis-reservas?error=No+se+puede+editar+una+reserva+pasada+o+del+día+actual.";
            }

            modelo.addAttribute("reserva", reserva);
            modelo.addAttribute("operacion", "EDIT");
            modelo.addAttribute("horarios", repoHorario.findAll());
            modelo.addAttribute("usuarios", repoUsuario.findAll());
            return "/reservas/add";
        } else {
            modelo.addAttribute("mensaje", "La reserva no existe");
            modelo.addAttribute("titulo", "Error editando reserva.");
            return "/error";
        }
    }

    @PostMapping("/edit/{id}")
    public String editReserva(@ModelAttribute("reserva") Reserva reserva) {
        Usuario usuario = getLoggedUser();
        LocalDate hoy = LocalDate.now();

        if (reserva.getFecha().isBefore(hoy) || reserva.getFecha().isAfter(hoy.plusWeeks(2))) {
            return "redirect:/mis-reservas?error=La+fecha+debe+estar+entre+hoy+y+dos+semanas+adelante.";
        }

        boolean reservaExistente = repoMReserva.existsByUsuarioAndFecha(usuario, reserva.getFecha());
        if (reservaExistente) {
            return "redirect:/mis-reservas?error=Ya+existe+una+reserva+para+esa+fecha.";
        }

        reserva.setUsuario(usuario);
        repoReserva.save(reserva);
        return "redirect:/mis-reservas";
    }

    @GetMapping("/del/{id}")
    public String delReserva(@PathVariable @NonNull Long id, Model modelo) {
        Optional<Reserva> oReserva = repoReserva.findById(id);
        if (oReserva.isPresent()) {
            Reserva reserva = oReserva.get();

            if (!reserva.getFecha().isAfter(LocalDate.now())) {
                return "redirect:/mis-reservas?error=No+se+puede+eliminar+una+reserva+pasada+o+del+dia+actual.";
            }

            modelo.addAttribute("reserva", reserva);
            modelo.addAttribute("operacion", "DEL");
            modelo.addAttribute("horarios", repoHorario.findAll());
            modelo.addAttribute("usuarios", repoUsuario.findAll());
            return "/reservas/add";
        } else {
            modelo.addAttribute("mensaje", "La reserva no existe");
            modelo.addAttribute("titulo", "Error borrando reserva.");
            return "/error";
        }
    }

    @PostMapping("/del/{id}")
    public String delReserva(@ModelAttribute("reserva") Reserva reserva) {
        if (!reserva.getFecha().isAfter(LocalDate.now())) {
            return "redirect:/mis-reservas?error=No+se+puede+eliminar+una+reserva+pasada+o+del+dia+actual.";
        }

        repoReserva.delete(reserva);
        return "redirect:/mis-reservas";
    }
}
