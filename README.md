# Java Challenge 2025 

## Descripción

### Tecnologías Utilizadas

* Java 23
* Spring Boot 3.4.1
* MySQL 5.7.4
* Maven
* Redis Cache
* JPA

## Aclaraciones
* Usé DTOs y ModelMapper para mapear de DTO a entidad y viceversa ya que a pesar de que hay entidades que no están 
siendo persistidas me pareció una buena practica para mantener las validaciones de Jakarta Validation desacopladas de 
la entidad que quizá en algún momento se quiera persistir o modificar. 

## Suposiciones
* Asumo que al cargar un camino directo entre un punto A y un punto B, 
y ya se encontraba ese camino, funcionaria como una actualización de costo. 
* 