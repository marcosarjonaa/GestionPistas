package com.iesvdc.acceso.pistasdeportivas.repos;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iesvdc.acceso.pistasdeportivas.modelos.Horario;
import com.iesvdc.acceso.pistasdeportivas.modelos.Reserva;
import com.iesvdc.acceso.pistasdeportivas.modelos.Usuario;


@Repository
public interface RepoMReserva extends JpaRepository<Reserva,Long>{
    List<Reserva> findByUsuario(Usuario usuario);  
    boolean existsByUsuarioAndFecha(Usuario usuario, LocalDate fecha);
    List<Reserva> findByUsuario(Usuario usuarioLogueado, Pageable pageable);
    boolean existsByUsuarioAndFechaAndHorario(Usuario usuario, LocalDate fecha, Horario horario);
}