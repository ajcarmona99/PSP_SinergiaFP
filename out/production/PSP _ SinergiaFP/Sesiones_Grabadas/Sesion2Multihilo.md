# Contenido

1.- Repaso de los procesos e hilos, recordar tambien la diferencia que hay entre el método
start y el método run.

Ejemplo. Identificar el hilo que ejecuta cada instruccion.

2.- sleep(), join() y con ello vamos a observar los estados posibles que puede recorrer un hilo.

Ejemplo. Observar un hilo antes y despues de ejecutarlo.

3.- Coordinación de hilos con CountDownLath.

Ejemplo. Esperar una señal del trabajador.

4.- Resultados con Callable y FutureTask.

Ejemplo. Repartir una suma entre dos hilos.

# De los procesos a los hilos

PROCESO JAVA
|
|
_HILO MAIN
|   |_ CONTINUA EJECUTANDO INSTRUCCIONES
|
|_HILO TRABAJADOR
    |_EJECUTAR UNA TAREA

# Estados, sleep y join

Un hilo pasa por diferentes estados. Podemos consultarlos con getState(), pero el resultado
es una forografía: puede que esl estado de tu hilo puede cambiar justos despues de leerlo.


| Estado        | Significado                                                                           |
|---------------|---------------------------------------------------------------------------------------|
| NEW           | El hilo se ha creado pero todavia no se ha arrancado.                                 |
| RUNNABLE      | El hilo está ejecutándose o disponible para ejecutar.                                 |
| BLOCKED       | Espeerar adquir un monitor de sychronized.                                            |
| WAITING       | Espera sin un tiempo máximo, por ejemplo en determinadas llamadas a join() o await(). |
| TIMED_WAITING | Espera con tiempo, por ejmplo durante sleep(                                          |
| TERMINATED    | Su ejecucion ha terminado.                                                            |

# Interrumpir un hilo

| Situación del trabajador   | Como se puede detectar la peticion   |
|----------------------------|--------------------------------------|
| Está en sleep()            | Se lanza con un InterruptedException |
| Esta realizando un cálculo | Puede consultar is Interruted()      |

# Coordinar hilos mediante CountDownLatch

new CountDownLatch(1) -> Creamos un contador con valor 1.

señal.countDown() -> Reducimos el contador.

señal.await() -> Esperamos a que nuestro contador que hemos iniciado al principio llegue al 0.

# Obtener resultado con FutureTask

Callable<Long> -> Describe una tarea que devuelve un Long.

FutureTask<Long> -> Representa la ejecución y un resultado pendiente.

Thread -> Ejecuta la tarea.

get() -> Obtiene el resultado y espera si todavía no está disponible.