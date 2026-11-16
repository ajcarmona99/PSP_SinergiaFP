# Contenidos

1) Ejemplo 1: Deterner un hilo con una bandera volatile.
2) Ejemplo 2: Contar con AtomicInteger
3) Ejemplo 3: Contar palabras con ConcurrentHashMap.merge()
4) Ejemplo 4: Reunir todos los conceptos

# Detener un hilo con volatile

main va a arrancar un trabajador (hilo). El trabajado da vueltas a un bucle y 
revida una bandera en cada vuelta. main cambia la bandera para pedirle que termine.

El propio trabajador lee la repetición.

main            trabajador
|- start------------|
|-------------------|lee parar: false
|-------------------|leer parar:false
-parar=true
|-------------------| lee parar:true
join espera---------| termina

# Contar con AtomicItenger

Ahora cada trabajador tiene que refistrar diez mil operaciones. Dos trabajadores
pueden producir veinte mil en total. 

Aquí también se podría usar el método synchronized como las sesiones anteriores. 
Como aquí solo en este ejemplo queramos actualizar un número, podemos utilizar el AtomicInteger.

AtomicInteger total = new AtomicInteger(0);

total.incrementAndGet();

incrementAndGet() aumenta el valor en uno como podría ser una operación atómica
y devuelve el valor nuelo. Hoy por ejemplo el valor devuelto va a ser ignorado
porque solo necesitamos el resultado final.

# Contador por palabra

Lote A: java, hilos, java
Lote B: redes, java, hilos

Recuento final:
java -> 3
hilos -> 2
redes -> 1

Con un AtomicInteger puede contar un total. Ahora lo que necesito es distiguir
cuantas veces aparece cada palabra. Vamos un utilizar un mapa: la clave de ese mapa
es la palabra y el valor es su número de apariciones.

## merge

recuento.merge(palabra, 1, Integer:: sum)

"java" todavía no existe -> Con el merge iserta "java" con un valor de 1.
"java" ya tiene valor 2 -> Combina el 2 y 1 y guarda el 3.

Integer::sum significa que si ya existe un valor, combinamos el anterior y el nuevo
sumandolos. También podríamos escribir (anterior, nuevo) -> anterior + nuevo.

En el objeto que vamos a ver en este ejemplo el ConcurrentHashMap, esta llamada a 
merge() actualiza la entrada de esa clave de forma totalmente atómica.

# Reunir todos los conceptos. 

Vamos a reunir los ejemplos realizados. Vamos a tener un total de dos modos:

* Modo bandera: Demuestra un hilo pide a otro que se salga de un bucle.

* Modo contar: Usa un total de cuatro trabajadores para actualizar un total y el recuento de
la palabra java.

# Resumen con tres preguntas


| Si necesito... | Utilizo... | ¿Que garantiza nuestro ejemplos |
| -- | --- | --- |
| Que el trabador observe la orden de parar | Una variable volatile | Visibilidad de la bandera |
| Conservar todos los incrementos de un total | AtomicInteger.incrementAndGet() | Incremento atómico del contador |
| Conservar todas las apariciones de cada palabra | merge | Actualización atómica del recuento de esa clave |

Antes de elegir la herramienta, indentifica que se comparte y que operacion necesitas. Si solo publicas una
señal, piensa en visibilidad. Si varios hilos actualizan un mismo valor, piensa en qué operación debe 
completarse como una unicidad.

# Ejercicio para casa

Crea un programa que tenga tres departamentos que registran indidencias. Cada departamento representa un hilo
que procesa un lote:

Aula -> RED, EQUIPO, RED, SOFTWARE

Biblioteca -> SOFTWARE, RED, SOFTWARE, EQUIPO

Secretaría -> EQUIPO, EQUIPO, RED, SOFTWARE

El programa debe de mostrar el total en general y el numero de incidencias de cada tipo.

1) Crear tres hilos, uno por lote.
2) Compatir un unico AtomicInteger para el total y un mapa para el recuento por tipo.
3) Por cada incidencia, actualizar el total con incrementAndGet y un merge.
4) Arrancar todos los trabajadores antes de empezar los join.
5) Mostrar los resultados depues de todos los join. No utilizar sleep para esperar que termine.
