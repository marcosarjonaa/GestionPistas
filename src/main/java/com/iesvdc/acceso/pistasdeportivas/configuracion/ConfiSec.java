package com.iesvdc.acceso.pistasdeportivas.configuracion;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class ConfiSec {

    @Autowired
    DataSource dataSource;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.jdbcAuthentication()
        .dataSource(dataSource)
        .usersByUsernameQuery("select username, password, enabled "+
            "from usuario where username = ?")
        .authoritiesByUsernameQuery("select username, tipo as 'authority' "+
            "from usuario where username = ?" );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean 
    public SecurityFilterChain filtro(HttpSecurity httpSec) throws Exception {
        return httpSec.authorizeHttpRequests(
            (request) -> request
                .requestMatchers(
                    "/webjars/**", "/img/**", "/login", 
                    "/logout", "/acerca", "/denegado")
                    .permitAll()
                .requestMatchers(
                    "/horario/**", "/instalacion/**", "/reservas/**")
                    .hasAuthority("ADMIN")
                .requestMatchers(
                    "/mis-datos/**", "/mis-datos/*/**","/mis-reservas/**","mis-reservas/*/**" )
                    .authenticated())
                .exceptionHandling((exception)-> exception.
                    accessDeniedPage("/denegado") )
                .formLogin((formLogin) -> formLogin
                    .loginPage("/login")
                    .permitAll())
                //.rememberMe(
                //    Customizer.withDefaults())
                .logout((logout) -> logout
                    .invalidateHttpSession(true)
                    .logoutSuccessUrl("/")
                    .permitAll())
                .csrf((protection) -> protection
                    .disable())
                .build();
    }
}
