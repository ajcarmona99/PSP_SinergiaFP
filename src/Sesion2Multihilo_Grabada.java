import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class Sesion2Multihilo_Grabada {
    public static void main(String[] args) throws InterruptedException {
        ejemploSuma();
    }
    // Método que va a relizar el ejemplo 1
    static void ejemplo1() throws InterruptedException {
        // Creamos el objeto Thread y le asignamos una tarea.
        // En este momento, el hilo no ha arrancado.
        Thread trabajador = new Thread(() -> {

            // Este cdofigo se ejecutara en el nulo hilo trabado
            // cuando lo arranquemos con start().
            System.out.println(
                    "La tarea se ejecuta en: "
                        + Thread.currentThread().getName()
            );
        }, "trabajador");

        // Esta instruccion pertenece al hilo main
        System.out.println(
                "El programa empieza en: "
                    + Thread.currentThread().getName()
        );

        // Arrancamos el nuevo hilo de ejecucion
        trabajador.run();

        // Main puede continuar mientras que el trabajador se ejecuta
        System.out.println("Main continua.");

        // Main espera a que termine el trabajador
        trabajador.join();

        // Al lelgar aquí, el hilo trabajador termina
        System.out.println("Trabajador finalizado.");
    }
    // El orden de los mensaje "Main continua" y la "tarea se ejecuta" puede variar dependiendo del pc en el que realizamos la ejecucion.
    // En cambio, "Trabajador finalizado" aparecera despues de terminar el trabajador porque hemos utilizado el metodo join.
    // Llamar a run() directamente ejecuta un método en el hilo actual. Siempre que queramos arrancar un nuevo hilo vamos a utilizar
    // el método start

    // Ejemplo2. Observar un hilo
    static void ejemplo2() throws InterruptedException {

        // Creamos un hilo que permanecer un tiempo dormido
        Thread trabajador = new Thread(() -> {
            System.out.println("Trabajador: empiezo.");

            try {
                // Se duerme el hilo que ejecuta esta instruccion.
                // En este caso, se duerme el trabajador.
                Thread.sleep(2000);
            }catch (InterruptedException e){
                // Restauramoso la marca de interrupcion
                Thread.currentThread().interrupt();

                System.out.println("Trabajador: sueño interrumpido");
            }

            System.out.println("Trabajador: termino.");
        }, "trabajador");

        // Antes de inicializar el hilo, es estado es NEW
        System.out.println("Antes de start: " + trabajador.getState());

        // Inicializamos el hilo
        trabajador.start();

        // Damos una oportunidad al trabador para avanzar.
        // Esta pausa duerme el método main, pero no el trabajador.
        Thread.sleep(100);

        System.out.println("Durante la ejecución: " + trabajador.getState());

        // Main espera a que termine el trabajador
        trabajador.join();

        // Una vez terminado el join, el hilo termina
        System.out.println("Despues del join: " + trabajador.getState());

    }

    // Todo lo que este fuera del bloque donde se escribe la funcion la funcion lamda lo define o mas bien lo ejecuta el hilo princial.
    // ¿Podriamos volver llamar a start() sobre un mismo hilo despues de que se encuentre ejecutado?
    // Respuesta: No, un objeto de tipo Thread solo puede arrancarse una vez. Para otra ejecucion hay que crear otro.
    // Un join no arranca un trabajador, espera a que termine, el arranque se realiza con start().

    // Ejemplo. Interrupcion de un hilo
    static void interrupcionHilo() throws InterruptedException {
        // Creamos un trabajador que realiza pasos repetidos
        Thread trabajador = new Thread(() -> {

            try {
                // Consultamos primero la marca sin borrarla
                while (!Thread.currentThread().isInterrupted()){
                    System.out.println("Trabajador: realizo un paso");

                    // Simulamos una espera entre diferentes pasos.
                    // Si llega a una interrupcion durante el sueño, sleep lanza InterruptedException
                    Thread.sleep(5000);
                }
            }catch (InterruptedException e){
                // Al lanza esta excepcion, sleep interrumple el hilo
                // La restauramos antes de terminar este Runnable.
                Thread.currentThread().interrupt();

                System.out.println("Trabajador: ha recibido la interrupcion");
            }finally {

                // Aqui liberamos los recursos propios
                System.out.println("Trabajador: finalización y limpieza.");
            }
        }, "trabajador");

        // Arrancarmos el trabador
        trabajador.start();

        try {
            // Main deje trabaje durante un tiempo
            Thread.sleep(1600);
        }finally {
            // Solicitar que termine el hilo trabajador.
            // Tambien lo hacemos si se interrumple la espera del main.
            trabajador.interrupt();

            // Esperamos su finalización
            trabajador.join();
        }

        System.out.println("Main_ el trabajador ha terminador.");
    }

    // No capturamos la excepcion para ignorarla y seguir trabajando indefinidamente. En este ejemplo la atendemos, restaurmos la marca y terminamos la tarea.
    // Si nuestro método puediera propagar InterruptedException, otra opcion seria dejar que la atendiera su trabajador.

    // ¿interruppt() espera a que el trabajador finalize? No. Para esperar usamos un join.
    // ¿Necesitamos exactamente cuatro mensajes del trabajador? No, la planificacion y los tiempos pueden variar.

    // Ejemplo. Estados e interrupcion con una señal
    static void estados_senal() throws InterruptedException {
        // Main esperará a que el trabajador reduzca ese contador desde 1 hasta 0.
        CountDownLatch preparado = new CountDownLatch(1);

        // Creamos el hilo trabajador
        Thread trabajador = new Thread(() -> {

            // Avisamos de que hemos llegado a este punto
            preparado.countDown();

            try {

                // Despues de avisar, intentar dormir diez segundos
                Thread.sleep(10000);
            }catch (InterruptedException e){

                // Restauramos la marca y terminamos la ejecucion
                Thread.currentThread().interrupt();

                System.out.println("Trabajador: petición de interrupción atendida,");
            }
        }, "observable");

        // Todavia no hemos arrancado el hilo
        System.out.println("Antes de start: " + trabajador.getState());

        // Arrancamos el trabajador
        trabajador.start();

        try {

            // Esperamos la señal del trabajador
            preparado.await();

            // La señal garantiza que ejecuto countDown().
            // No garantiza que haya entrado en el estado en sleed.

            // Intemamos observar un TIMED_WAITING durante un tiempo máximo de un segundo.
            long limite = System.nanoTime() + TimeUnit.SECONDS.toNanos(1);

            // Este bucle solo muestra una observacion
            // No se utiliza como mecanismo de coordinación
            while (trabajador.isAlive() && trabajador.getState() != Thread.State.TIMED_WAITING && System.nanoTime() < limite) {

                // Sugerimos al hilo main que permita avanzar a otros hilos.
                // yield no garantiza que cambie el hilo ejecutado.
                Thread.yield();
            }

            System.out.println("Durante: " + trabajador.getState());
        }finally {

            // No esperamos diez segundo: pedimos la interrupcion
            trabajador.interrupt();

            // Esperamos a que termine el trabajador
            trabajador.join();
        }

        // Despues de join, el estado sera TERMINATED.
        System.out.println(
                "Despues de join: " + trabajador.getState()
        );
    }

    // ¿Que sucede si elimnamos preparado.countDown()? Main esperando en await, porque nadie reduce el contador.
    // ¿Que sucede si elminamos trabajador.interrupt()? En condiciones normales, habra que esperar a que termine el sueño de 10 segundos.
    // ¿Que instrucción permite asegurar que el trabajador ha terminado? trabajador.join()


    // EjemploSuma. Queremos sumar 1..100.
    // Vamos a crear un método sencillo, para sumar itnervalos, con ambos extremos.
    static long sumar(int desde, int hasta){
        // Cada invocación al método tiene su propio aculidor local.
        long resultado = 0;

        // Recorremos los números del intervalo
        for (int numero = desde; numero<=hasta; numero++){
            // Acumulamos el numero actual
            resultado += numero;
        }

        return resultado;
    }

    static void ejemploSuma() throws InterruptedException {
        // Lambda devuelve un valor.
        // FutureTask la recibe como una tarea Callable<Long>.
        FutureTask<Long> primera = new FutureTask<>( () -> sumar(1, 50));

        // Segunda tarea, con un intervalo diferente
        FutureTask<Long> segunda = new FutureTask<>( () -> sumar(51, 100));

        // Cada hilo ejecuta un FutureTasj
        Thread hiloA = new Thread(primera, "tramo-1");
        Thread hiloB = new Thread(segunda, "tramo-2");

        // Arrancamos ambos antes de esperar resultados
        hiloA.start();
        hiloB.start();

        try {

            // Si la primera tarea no termina, con el get espera que termina y toma el resultado
            long resultadoA = primera.get();
            long resultadoB = segunda.get();

            // Combinamos los resultados en main
            long total = resultadoB + resultadoA;

            // Mostramos cada parte y el total
            System.out.println("Primer tramo: " + resultadoA);
            System.out.println("Segundo tramo: " + resultadoB);
            System.out.println("Suma total: " + total);
        }catch (ExecutionException e){
            // La tarea ha lanzado una expcecion
            // getCause permite consultar la causa original.
            System.err.println("Error de tarea: " + e.getCause());
        }finally {
            // Si alguna tarea sigue pendiente, solicitamos cancelarla.
            // Si ya termino, calcel devuelve false y no cambia su resultado.
            primera.cancel(true);
            segunda.cancel(true);

            // Esperamos a que terminen los hilos
            hiloA.join();
            hiloB.join();
        }
    }
}
