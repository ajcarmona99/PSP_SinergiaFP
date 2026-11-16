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