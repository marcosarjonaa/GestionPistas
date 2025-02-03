# CRUD Reservas ADMIN

### Hecho por Marcos Arjona Comino

---

En este ejercicio he hecho un CRUD de reservas que solo puede ver el admin,
 y en el que se puede comprobar cada reserva hecha por todos los usuarios y 
 poder listar, añadir, editar y eliminar.

A través de SpringBoot he hecho esas funciones, teniendo siempre en cuenta la
 seguridad , para que el admin sea el único que pueda acceder a ellos.

#### Listar

```html
<!DOCTYPE html>
<html lang="en">
<head th:replace="plantilla/fragmentos.html :: headfiles">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Listar Reservas</title>
</head>
<body>
    <div class="container-fluid">    
    <div th:replace="plantilla/fragmentos.html :: navigation"></div>
    <h3>Reservas</h3>
    <table class="table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Usuario</th>
                <th>Instalacion</th>
                <th>Hora inicio</th>
                <th>Hora fin</th>
                <th>Fecha</th>
                <th>Editar</th>
                <th>Borrar</th>
            </tr>
        </thead>
        <tbody>
            <tr th:each="reserva : ${reservas}">
                <td th:text="${reserva.id}">ID</td>
                <td th:text="${reserva.usuario.username}"></td>
                <td th:text="${reserva.horario.instalacion.nombre}"></td>
                <td th:text="${reserva.horario.horaInicio}"></td>
                <td th:text="${reserva.horario.horaFin}"></td>
                <td th:text="${reserva.fecha}">Fecha</td>
                <td> <a class="btn btn-success" th:href="|reservas/edit/${reserva.id}|"> editar </a> </td>
                <td> <a class="btn btn-danger" th:href="|reservas/del/${reserva.id}|"> borrar </a> </td>
            </tr>            
        </tbody>
    </table>
    <div th:replace="plantilla/fragmentos.html :: footer"></div>
    </div>
</body>
</html>
```

Aquí podemos ver como se muestran las reservas de todos los usuarios
 del sistema con su id, usuario, instalacion, hora_Inicio, hora_Fin, fecha, 
y el boton de editar y eliminar.


### Añadir, editar y eliminar
