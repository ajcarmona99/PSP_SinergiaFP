import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class Ejemplo03Sesion5Multihilo_Grabada {
    static void contarLote(String [] palabras, ConcurrentHashMap<String, Integer> recuento){

        // Cada trabajador recorre su lote
        for (String palabra : palabras){

            // Si es la primera aparición, guardamos 1.
            // Si ya existe, sumamos 1 al valor anterior.
            recuento.merge(palabra, 1, Integer::sum);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Las claves son las palabras, los valores son sus recuentos
        ConcurrentHashMap<String, Integer> recuento = new ConcurrentHashMap<>();

        // Las claves son distintas. El mapa será el mismo para ambos hilos
        String[] loteA = {"java", "hilos", "java"};
        String[] loteB = {"redes", "java", "hilos"};

        Thread primero = new Thread(() -> contarLote(loteA, recuento), "lote-A");
        Thread segundo = new Thread(() -> contarLote(loteB, recuento), "lote-B");

        // Ahora incializamos los hilos
        primero.start();
        segundo.start();

        // Esperamos a que terminen antes de mostrar los recuentos definitivos
        primero.join();
        segundo.join();

        // Consultamos las claves en un orden elegido para la demostración
        System.out.println("java=" + recuento.get("java"));
        System.out.println("hilos=" + recuento.get("hilos"));
        System.out.println("redes=" + recuento.get("redes"));
    }
}

// int anterior = recuento.getOrDefault(palabra, 0)
// recuento.put(palabra, anterior + 1)  INCORRECTO para incrementar una entra desde varios hilos
// CORRECTO: recuento.merge(palabra, 1, Integer::sum);
//
// Aunque las dos prmeras llamadas se hacen sobre un mapa concurrente, siguen siendo dos operaciones totalmente separadas.
// Un hilo puede leer 2 y otro también puede leer 2. Si los dos guardan 3, hemos perdido una aparición.
// Con el merge() resuelve la actualización completa de esa clave.