package com.iesvdc.acceso.pistasdeportivas.repos;


import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iesvdc.acceso.pistasdeportivas.modelos.Horario;
import com.iesvdc.acceso.pistasdeportivas.modelos.Reserva;
import com.iesvdc.acceso.pistasdeportivas.modelos.Usuario;


@Repository
public interface RepoReserva extends JpaRepository<Reserva,Long>{

    boolean existsByUsuarioAndFechaAndHorario(Usuario usuario, LocalDate fecha, Horario horario);
    
}
