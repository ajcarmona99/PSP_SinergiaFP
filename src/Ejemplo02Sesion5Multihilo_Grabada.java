import javax.swing.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Ejemplo02Sesion5Multihilo_Grabada {

    private static final int REPETICIONES = 10_000;

    public static void main(String[] args) throws InterruptedException {

        // Creamos un unico contador para compatirlo entre los hilos
        AtomicInteger total = new AtomicInteger(0);

        // Ambos trabajadores ejecutaran esta misma tarea
        Runnable contar = () -> {
          for (int i=0; i < REPETICIONES; i++){

              // Incrementamos de forma atómica el contador compartido
              total.incrementAndGet();
          }
        };

        Thread primero = new Thread(contar, "contador-1");
        Thread segundo = new Thread(contar, "contador-2");

        // Arrancamos los dos antes de esperar sus resultados
        primero.start();
        segundo.start();

        // Los dos deben terminar antes de consultar
        primero.join();
        segundo.join();

        System.out.println("Esperado="+ (2*REPETICIONES));

        //get() obtiene el valor actual del contador
        System.out.println("Obtenido=" + total.get());
    }
}

// get() y set() son operaciones totalmente individuales. Entre una y otra puede entrar un hilo.
// Por ejemplo, si ambos leen 8, ambos pueden escribir 9 y volvemos a perder una actualizacion.
// Que el objeto se llame AtomicIntefer no convierte cualquier secuencia de llamadas en una operacion.
//
// join() cumple otra función: nos permite saber que ya se han hecho todos los incrementos. Podriamos consultar total.get() antes,
// pero claro eso haría que estuvieramos leyendo un resultado totalmente parcial.
