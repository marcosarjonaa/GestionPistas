package com.iesvdc.acceso.pistasdeportivas.repos;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iesvdc.acceso.pistasdeportivas.modelos.Reserva;
import com.iesvdc.acceso.pistasdeportivas.modelos.Usuario;


@Repository
public interface RepoMReserva extends JpaRepository<Reserva,Long>{
    List<Reserva> findByUsuario(Usuario usuario);  // Método para buscar reservas por usuario
    boolean existsByUsuarioAndFecha(Usuario usuario, LocalDate fecha);
}