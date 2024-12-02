package com.iesvdc.acceso.pistasdeportivas.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iesvdc.acceso.pistasdeportivas.modelos.Usuario;

public interface RepoUsuario extends JpaRepository <Usuario, Long>{
    
}
