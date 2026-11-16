import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Ejemplo04_Sesion5Multihilo_Grabada {

    private static final int NUM_HILOS = 4;
    private static final int REPETICIONES = 10_000;

    static class Control{

        // Un hilo publica la señal y otro la consulta
        volatile boolean parar;
    }

    static void bandera() throws InterruptedException {

        Control control = new Control();

        Thread trabajador = new Thread(() -> {
           // Cada vuelta modifica una variable local, esta no es compartida
           long vueltas = 0;

           // La bandera se vuelve a consultar en cada iterración
            while (!control.parar){
                vueltas++;
            }

            System.out.println("Bandera observada; vueltas=" + vueltas);
        }, "controlado");

        trabajador.start();

        try {
            // Hace visible la fase de trabajado durante la demostración
            Thread.sleep(100);
        }finally {
            // Pedimos la terminación y esperamos al trabajador
            control.parar = true;
            trabajador.join();
        }
    }

    static void contar() throws InterruptedException {

        // Los cuatro trabajadores comparten el mismo total
        AtomicInteger total = new AtomicInteger(0);

        // También comparten el mismo mapa de recuentos
        ConcurrentHashMap<String, Integer> mapa = new ConcurrentHashMap<>();

        Runnable registrarJava = () -> {
          for (int j=0; j < REPETICIONES; j++){

              // Registramos una operacion en el total general
              total.incrementAndGet();

              // Registramos una apartición de "java" en el mapa
              mapa.merge("java", 1, Integer::sum);
          }
        };

        Thread[] hilos = new Thread[NUM_HILOS];

        // Creamos y arrancamos los cuatro trabajadores
        for (int i = 0; i < hilos.length; i++){
            hilos[i] = new Thread(registrarJava, "recuento-" + (i+1));
            hilos[i].start();
        }

        // Esperamos a todos antes de comparar los resultados
        for (Thread hilo:hilos){
            hilo.join();
        }

        System.out.println("Esperado=" + (NUM_HILOS*REPETICIONES));
        System.out.println("Atómico=" + total.get());
        System.out.println("Mapa=" + mapa.get("java"));
    }

    public static void main(String[] args) throws InterruptedException {
        // Si no indicamos argumentos, ejecutamos el recuento
        String modo = args.length == 0 ? "contar" : args[0];

        switch (modo){
            case "bandera":
                bandera();
                break;
            case "contar":
                contar();
                break;
            default:
                throw new IllegalArgumentException(
                        "Uso: Tiene que ser contar o bandera"
                );
        }
    }
}

// Si ejecutamos el modo contar los dos resultados coinciden al final porque cada trabajador ejecuta la dos intrucciones en cada vuelta
// y main espera a todos. Durante la ejecución podrían inferir: un hilo puede haber incrementado total y todavía no haber hecho el merge().
//
// Cada operación es atómica por separado. Juntas no forman una actualización indivisible. Si una pantalla necesitara leer un resumen
// coherente mientras los trabajadores siguen modificándolo, tendríamos en este caso que cordinar esas escrituras y lecuras con un
// mismo bloqueo, como en el resumen sincronizado.
