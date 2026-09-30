# Graduación UNAL 
## Descripción del proyecto
Graduación UNAL es un sistema orientado a la planificación académica
de estudiantes de pregrado de la Universidad Nacional, cuyo objetivo es
determinar el número mínimo de semestres necesarios para finalizar un
plan de estudios cumpliendo las restricciones académicas existentes.
El problema aborda la organización de asignaturas con relaciones de
prerrequisitos y la limitación del número máximo de materias que un
estudiante puede cursar por semestre. La solución busca modelar estas
dependencias y generar una ruta óptima de graduación.
El sistema permitirá registrar asignaturas, consultar información
específica mediante su identificador, analizar las restricciones entre
materias y establecer la distribución de cursos por semestre hasta
completar el plan académico.
Objetivo
Diseñar e implementar una solución basada en estructuras de datos que
permita:
- Registrar asignaturas con:
  - ID numérico.
  - Lista de asignaturas prerrequisito.
- Consultar cualquier asignatura mediante su ID.
- Determinar el número mínimo de semestres necesarios para completar
  el plan de estudios.
- Mostrar las asignaturas que deben cursarse en cada semestre.
- Respetar:
  - Todas las asignaturas obligatorias.
  - Las relaciones de prerrequisitos.
  - El límite máximo de asignaturas permitidas por semestre.
## Integrantes
- Integrante 1: Julian Alberto Fernandez Vera
- Integrante 2: \(Nombre\)
- Integrante 3: \(Nombre\)
- Integrante 4: \(Nombre\)
- Integrante 5: \(Nombre\)
## Lenguaje de programación
- Java
## Estructuras de datos utilizadas
Las estructuras de datos seleccionadas estarán orientadas a representar
eficientemente las relaciones entre asignaturas y permitir consultas y
análisis del plan académico.
Estructuras propuestas
- Grafo dirigido
  - Representa las dependencias entre asignaturas.
  - Cada nodo corresponde a una asignatura.
  - Cada arista representa una relación de prerrequisito.

La selección final de estructuras será justificada mediante análisis de
complejidad temporal y espacial.
1. Funcionamiento general
2. Registro de asignaturas y sus prerrequisitos.
3. Construcción de la estructura que representa el plan académico.
4. Análisis de dependencias entre materias.
5. Identificación de asignaturas disponibles para cada semestre.
6. Distribución de materias considerando el límite máximo permitido.
7. Generación del cronograma mínimo de semestres requerido para la
   graduación.
