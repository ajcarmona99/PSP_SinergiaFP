# Contenido

1) Ejemplo 1: contador inseguro con cuatro hilos
2) Ejemplo 2: corrección de ese contador inseguro con cuatro hilos
3) Ejemplo 3: correccion con ReentrantLock
4) Ejemplo 4: programa completao con tres modos
5) Desmotraciones: excepciones, lecuta tras join() y tiempos.
6) Ejercicio.


| Aspecto                      |                      synchronized                      |                ReentrantLock |
|:-----------------------------|:------------------------------------------------------:|-----------------------------:|
| Adquirir                     | Al entrar en el método o en el bloque se haya definido |                       lock() |
| Liberar                      |                Automáticamente al salir                |                     unlock() |
| Liberación ante excepción    |                       Automática                       | Debemos colocarla en finally |
| Intenta adquirir sin esperar |                No ofrece una operación.                |                    tryLock() |
| Adquisición interrumpible    |           No al esperar entrar en el monitor           |          lockInterruptibly() |
| Reentrante                   |                           Sí                           |                           Sí |

# Ejercicio para casa 

Crea un programa que cumpla lo siguiente:

Una tienda empieza con 100000 unidades, y trabajan simultaneamente:
* Dos hilos de reposición: cada uno añade unas 20000 unidades.
* Dos hilos de venta: Cada una retirara una unidad 15000 veces.

Realiza dicho programa tanto con el synchronized como con el ReentrantLock.