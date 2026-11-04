// Ahora vamos a proteger la misma operación con un objeto de bloqueo explicito.
// Con synchronized, Java gestiona automáticamente la entrada y la salida del monitor.
// Con ReentrantLock, nosotreos debemos llamar al lock() para adquirielo y a unlock() para liberarlo.

import java.util.concurrent.locks.ReentrantLock;

// ¿Porque se llama reentrante?
// Cada adquisición de este objeto debe tener su liberación correspondiente.
public class Ejemplo03Sesion4Multihilo_Grabada {

    private static final int NUMERO_HILOS = 4;
    private static final int REPETICIONES = 200_000;

    static class Contador{

        // Estado compartido
        private int valor = 0;

        // Todos los incrementos de este contador utilizan este mismo bloqueo
        private final ReentrantLock bloqueo = new ReentrantLock();

        void incrementar(){

            // Adquirimos el bloqueo.
            // Si otro lo posee, esperamos.
            bloqueo.lock();

            // Siempre que querais liberar lo que es un bloqueo con este tipo de objeto.
            // Vais a necesitar un try y un finally para poder realizarlo correctamente.
            try {
                // Seccion critica que se encuentra protegida
                valor++;
            }finally {

                // Una vez que haya intentado acceder a mi variable compartida
                // liberamos el bloqueo incluso si el cuerpo lanza una excepcion.
                bloqueo.unlock();
            }
        }

        // Se mete el lock antes del try. Porque el bloque finally corresponde a una adquisición
        // que ya se ha realizado finalmente.

        // Un fallo dentro de la sección crítica (que es cuando un hilo intenta acceder a la
        // variable compartida) no debe de dejar el bloqueo retenido.
        // finally garantiza que intentamos libterarlo cuando abandonamos ese bloque,
        // tanto si termina normalmente como si aparece una excepción.

        int leerTrasJoin(){
            // Solo se utiliza después de todos los join
            return valor;
        }
    }

    public static void main(String[] args) throws InterruptedException {

        // Un unico contador compartido
        Contador contador = new Contador();

        Thread[] hilos = new Thread[NUMERO_HILOS];

        for (int i = 0; i < hilos.length; i++){
            hilos[i] = new Thread(() -> {

                for(int j = 0; j < REPETICIONES; j++){
                    contador.incrementar();
                }
            }, "lock-" + i);
        }

        // Arrancamos todos
        for (Thread hilo:hilos){
            hilo.start();
        }

        // Esperamos a todos
        for (Thread hilo:hilos){
            hilo.join();
        }

        System.out.println("Obtenido: " + contador.leerTrasJoin());
    }
}
