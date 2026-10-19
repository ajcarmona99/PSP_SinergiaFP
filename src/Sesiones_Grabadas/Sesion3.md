# Objetivo

Como comunicamos informacion entre procesos.


PROCESO PADRE - - - PROCESO HIJO

                ProcessBuilder.start()

proceso = proceso hijo


| Desde el programa padre     | Para que sirve |
|-----------------------------|--|
| proceso.getInputStream()            | Lee la salida de consola del hijo |
| proceso.getOutputStream() | Escribir la entrada estandar del hijo |
| proceso.getErrorStream()  | Lee la salida de errores del hijo |
| proceso.waitFor()  | Espera a que se termine el hijo |


* 1er ejemplo <- EmisorLineas ( escribir diferentes lineas) y ReceptorLineas(
recibir la lineas de ese EmisorLineas)
* 2do ejmemplo <- Enviar números como texto, EmisorNumeros_S3 y un SumadorNumeros
* 3r ejemplo <- El padre envia informacion y el Hijo Responde

Padre - "Ana" -> Hijo
Padre <- "Hola, Ana" - Hijo

# Ejercicio para casa

Desarrollar dos programas:
* PadreCalculadora_S3
* HijoCalculadora_S3

El padre debe de arrancar un unico proceso hijo y permitir que el usuario envie
operaciones desde la consola.

El hijo debe de leer cada operacion, calcular el resultado y devolver una unica linea de respuesta.

