// Esto es un contador inseguro
public class Ejemplo01Sesion4Multihilo_Grabada {
    // Numero de hilos que van a modificar el contador
    private static final int NUMERO_HILOS = 4;

    // Incrementos que realizará cada hilo
    private static final int REPETICIONES = 200_000;

    static class Contador{

        // Variable compartida por todos los trabajadores
        private int valor = 0;

        void incrementar(){

            // Operacion compuesta sin proteccion
            // Varios hilos puede interferir o entrar al mismo tiempo a este método
            valor++;
        }

        int leerTrasJoin(){
            // Main llamara a este metodo despues de esperar
            // la terminacion de todos los trabajadores.
            return valor;
        }
    }

    public static void main(String[] args) throws InterruptedException {

        // Creamos un unico contador compartido
        Contador contador = new Contador();

        // Guardar la referencias a todos los trabajadores (hilos)
        Thread[] hilos = new Thread[NUMERO_HILOS];

        // Creo los hilos
        for (int i=0; i < hilos.length; i++){
            hilos[i] = new Thread(() -> {

                // Cada trabajador realiza sus incrementos
                for (int j=0; j<REPETICIONES;j++){
                    contador.incrementar();
                }
            }, "contador - " + i);
        }

        // Arrancamos todos antes de esperar
        for (Thread hilo: hilos){
            hilo.start();
        }

        // Esperamos a que todos terminen
        for (Thread hilo : hilos){
            hilo.join();
        }

        // Calculamos cuantos incrementos se han solicitado
        int esperado = NUMERO_HILOS * REPETICIONES;

        // Mostramos los resultados
        System.out.println("Esperado: " + esperado);
        System.out.println("Obtenido: " + contador.leerTrasJoin());

    }

    // Si obtenemos menos de ochocientos mil, estamos observando actualizaciones perdidas.
    // Si alguna ejecucion por lo que sea en vuestro pc, obtiene ochocientos mil, el programa sigue incorrecto. Una ejecuciion favorable no garantiza que todas lo sean.
    // Si utilizaramos el join en el mismo for que cuando inicializamos el hilo, estariamos ejecutando esos trabajadores o hilos de manera secuencial.
}
