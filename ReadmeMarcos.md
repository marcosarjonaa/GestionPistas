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

```html
<!DOCTYPE html>
<html lang="en">

<head th:replace="~{plantilla/fragmentos.html :: headfiles}">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nueva Reserva</title>
</head>
<body>
    <div class="container-fluid">
        <div th:replace="plantilla/fragmentos.html :: navigation"></div>
        <div class="container-fluid">
            <h3 th:switch="${operacion}">
                <span th:case="'ADD'">Añadir una nueva reserva</span>
                <span th:case="'DEL'">Eliminar una reserva</span>
                <span th:case="'EDIT'">Editar una reserva</span>
                <span th:case="*">Operación no soportada</span>
            </h3>
            <form method="post" th:object="${reserva}" class="needs-validation" novalidate>
                <input type="number" hidden name="id" th:field="*{id}" th:value="${reserva.id}" />

                <select class="form-select" th:field="*{usuario.id}" name="usuario" id="usuario"
                    th:attr="disabled=${operacion} == 'DEL' ? 'disabled' : null">
                    <option th:each="usuario : ${usuarios}" th:value="${usuario.id}"
                        th:text="${usuario.username}"></option>
                </select>
                <br/>
            
                <div class="mb-3">
                    <label for="fecha" class="form-label">Fecha</label>
                    <input type="date" min="${min}" max="${max}" id="fecha" name="fecha" class="form-control" th:value="*{fecha}"
                        th:attr="disabled=${operacion} == 'DEL' ? 'disabled' : null" />
                </div>
                
                <select class="form-select" 
                        th:field="*{horario.id}" 
                        id="horario" 
                        th:attr="disabled=${operacion} == 'DEL' ? 'disabled' : null">
                    <option th:each="horario : ${horarios}"
                            th:value="${horario.id}" 
                            th:text="${horario.instalacion.nombre} + ':  ' + ${horario.horaInicio} + ' : ' + ${horario.horaFin}">
                    </option>
                </select>
                <button type="submit" class="btn btn-primary">Enviar</button>
            </form>
        </div>
        <div th:replace="plantilla/fragmentos.html ::footer"></div>
    </div>
</body>

</html>
```

Este código se modifica segun el endpoint para permitirte añadir y acceder a todos 
parámetros disponibles. Como la fecha, la instalación, el horario y el usuario que reserva

En caso de que sea editar, pues te carga los datos de la propia reserva y puedes modificarlos. 

Y en el último caso, eliminar. Te deshabilita todo para que veas el contenido de los input para poder deshabilitarlo.

|   Rutas   |   ¿Qué hace?  |
|-----------|---------------|
|   /reservas   |   Enseña todas las reservas mostrando los datos   |
|   /reservas/add   |   Añade una nueva reserva |
|   /reservas/edit/1   |   Edita una reserva segun el id   |
|   /reservas/del/1   |   Elimina una reserva segun el id   |
