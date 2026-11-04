import java.util.Optional;

// Vamos a proteger el ejemplo 01 pero con synchronized
public class Ejemplo02Sesion4Multihilo_Grabada {
    private static int NUMERO_HILOS = 4;
    private static final int REPETICIONES = 20_000;

    static class Contador{
        // Acceder a esta variable por los trabajadores bajo un monitor que me va a permitir obtener la sincronizacion entre los diferentes hilos que intenten acceder
        private int valor = 0;

        synchronized void incrementar(){
            // El monitor quien dice que debe de entrar o no es el propio ibjeto Contador
            //
            // Solo un hilo puede ejecutar a la vez este metodo sobre el mismo Contador (sobre la misma instancia de esta clase)
            valor++;
        }

        // Este método de arriza equivale a:
        // void incrementar(){
        //      synchronized (this) {
        //          valor++;
        //      }
        // }

        // NO GARANTIZA QUE LOS HILOS SE ALTERNEN POR TURNOS.
        // TAMPOCO IMPIDE EJECUTAR CUALQUIER OTRO MÉTODO DEL CONTADOR: OTRO METODO QUE NO ADQUIERA ESE MONITOR NO QUEDA PROTEGIDO DE MANERA AUTOMÁTICA.

        // Hilo A adquiere el monitor.
        // Hilo B intenta adquirlo y espera.
        // Hilo A incrementar y liberar el monitor.
        // Otro hilo puede adquirlo y realiza su incremento.

        int leerTrasJoin(){
            // La lectura se realiza despues de que main haya esperado a todos los trabajadores
            return valor;
        }

        // Si quereis consultar el contador mientras los trabajador lo modifican, necesariamos proteger tambien la lectura con el mismo protocolo correspondiente.
    }

    public static void main(String[] args) throws InterruptedException {

        // Todos los hilos comparten este objeto
        Contador contador = new Contador();

        Thread[] hilos = new Thread[NUMERO_HILOS];

        for (int i = 0; i < hilos.length; i++){

            hilos[i] = new Thread(() -> {

                for (int j = 0; j < REPETICIONES; j++){

                    // Cada llamada adquiere y libera el monitor del contador
                    contador.incrementar();
                }
            }, "sincronizado-" + i);
        }

        // Arrancamos los trabajadores
        for (Thread hilo : hilos){
            hilo.start();
        }

        // Esperamos su finalizacion
        for (Thread hilo : hilos){
            hilo.join();
        }

        System.out.println("Esperado: " + NUMERO_HILOS * REPETICIONES);
        System.out.println("Obtenido: " + contador.leerTrasJoin());
    }
}

// Un método de instancia declarado como synchronized utiliza el monitor del this.
// Este monitor del this, se refiere a la instancia de la clase: el objeto sobre el que invocamos
// el método.
// Como todos utilizan el mismo contador, todos compiten por el mismo monitor.
