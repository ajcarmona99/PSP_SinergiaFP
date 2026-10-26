# Contenido

1) Crear un hilo y lo vamos a ejecutar.
2) Indetificar los hilos y el proceso.
3) Vamos a ver el progreso de los hilos mediante el método sleep.
4) Comparar el metodo run() con el método start() en los hilos.


| Llamada      | Que ocurre                                               |
|:-------------|:---------------------------------------------------------|
| hilo.start() | Se inicializa otro hilo que ejecuta la tarea             |
| hilo.run()   | El hilo actual ejecuta el método como una llamada normal |
| hilo.join()  | El hilo actual espera a que termine hilo                 |

5) Arrancar todos los hilos antes de esperar.
6) Programa completo: modos, progreso, PID y duracion.

# Tabla resumen

|           Aspecto           |                           Modo run                            |                       Modo start                        |
|:---------------------------:|:-------------------------------------------------------------:|:-------------------------------------------------------:|
| Hilo que ejecuta las tareas |                             main                              | Los trabajadores (es decir, creamos otros trabajadores) |
|    Progreso de las treas    | Una despues de otra ( al final es la programacion secuencial) |       Puede intercalarse ( programacion paralela)       |
|             PID             |                      El mismo para todas                      |                   El mismo para todas                   |
|    Duracion orientativa     |        Al ser secuencial, la duración suele tardar más        |    Al ser pararela ( la duracion suele tardar menos)    |


# Ejercicio para casa

Es un ejercicio para mezclar procesos e hilos.
- Crea un proceso que al menos debe de recibir por argumentos el numero en primer lugar y en segundo el modo en el que quiere que se 
ejecute.
- Tiene que crear y ejecutar tantos hilos en el modo que se le asigna como numero se le pase en el primer argumento.