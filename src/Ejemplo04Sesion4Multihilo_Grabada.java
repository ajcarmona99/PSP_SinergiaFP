import java.util.concurrent.locks.ReentrantLock;

// Demostraciones.
// Demostración 1 : el bloqueo se libera aunque haya una excepcion
// El primer trabajador falla, pero libera el bloqueo.
// Despés, un hilo diferente puede adquirirlo y completar su operación.
// Utilizamos hilos diferentes porque un bloqueo reentrante permitiria que su propietario pueda volver a entrar;
// hacerlo todo desde el mismo hilo no demostraría lo mismo.

// Demostración 2: la lectura tras el join()
// Estamos provocando que un hilo comienze y otro hilo no comienze hasta que haya terminado el que anteriormente se ha iniciado.
// Esto ocurre porque estamos metiendo un start al mismo tiempo que un join
// ACLARACION: Lo que estamos programando en este ejemplo es una programacion secuencial
// que es lo que estabais realizando hasta el momento de esta asignatura
// y lo que estamos buscando en esta asignatura es la programacion por concurrencia.

// Demostración 3: Interpretar lo que son los tiempos.
// Los tiempos pueden cambiar por la compilacion y la optimizacion de la JVM, planificacion y sobre todo por el procesador que tengais.
// Esta medicion incluye la creación, el arranque y la espera de los hilos. No mide unicamente los metodos como el de incrementar.


public class Ejemplo04Sesion4Multihilo_Grabada {
    static class Servicio{

        // Creo mi ReentrantLock que es el que va a bloquear los bloqueos entre los hilos
        private final ReentrantLock bloqueo = new ReentrantLock();

        void ejecutar(boolean provocarFallo){
            bloqueo.lock();

            try {
                System.out.println(
                        Thread.currentThread().getName()
                        + ": bloqueo adquirido."
                );

                if (provocarFallo){
                    // Simulamos un fallo dentro de la seccion critica que es donde accedemos al desbloqueo en este caso
                    // del objeto
                    throw new IllegalStateException(
                      "Fallo en la demostración."
                    );
                }
            }finally {
                // También se ejecuta cuando aparece la excepción
                bloqueo.unlock();
                System.out.println(
                        Thread.currentThread().getName()
                        + ": bloqueo liberado."
                );
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Servicio servicio = new Servicio();

        Thread primero = new Thread(() -> {
           try {
               // Este trabajador provocará una excepción
               servicio.ejecutar(true);
           }catch (IllegalStateException e){
               System.out.println("Error atendido: " + e.getMessage());
           }
        }, "primero");

        Thread segundo = new Thread(() -> servicio.ejecutar(false), "segundo");

        // Ejecutamos primero el caso que falla
        primero.start();
        primero.join();

        // Despues, otro hilo debe poder adquirir el bloqueo
        segundo.start();
        segundo.join();
    }
}
