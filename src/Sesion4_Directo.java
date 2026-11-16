public class Sesion4_Directo extends Thread{

    // Identifador de cada hilo
    private int numero;

    // Constructor del hilo que el usuario va a tener que indicar el numero exacto de los que quiere crear
    public Sesion4_Directo(int numero){
        this.numero = numero;
    }

    // Codigo que ejecuta cada hilo
    @Override // Esta anotación de Java sirve en particular para decir que vas a sobreescribir un método ya establecido
    public void run(){
        System.out.println("Hilo " + numero + " iniciado");

        try {
            // Simulamos un trabajo mediante una espera
            Thread.sleep(1000);
        }catch (InterruptedException e){
            System.out.println("Hilo interrumpido");
            Thread.currentThread().interrupt();
        }

        System.out.println("Hilo " + numero + " ha terminado");
    }

    public static void main(String[] args) {
        int cantidad;
        // Primer argumento: cantidad de hilos que van a ser creados
        try {
            cantidad = Integer.parseInt(args[0]);
        }catch (NumberFormatException e){
            System.out.println("El numero debe de ser un entero");
            return;
        }

        // Segundo argumento: modo de ejecucion
        String modo = args[1];
        if (modo.equalsIgnoreCase("secuencial")){
            // Los hilos se ejecutan uno detras de otro
            for (int i=1; i<=cantidad; i++){
                Sesion4_Directo hilo = new Sesion4_Directo(i);

                hilo.start(); // Esto hace que se inicialice un hilo detras de otro

                try {
                    // Esperamos a que termine un hilo antes de crear otro
                    hilo.join();
                }catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        } else if (modo.equalsIgnoreCase("paralelo")) {
            // Guardo todos los hilos en un array
            Sesion4_Directo[] hilos = new Sesion4_Directo[cantidad];

            // Creamos e iniciamos todos los hilos
            for (int i=0;i < cantidad; i++){
                hilos[i] = new Sesion4_Directo((i+1));
                hilos[i].start();
            }

            // Esperamos a que terminen todos
            for (int i = 0; i < cantidad; i++){
                try {
                    hilos[i].join();
                }catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }else {
            System.out.println("Modo incorrecto debe de ser secuencial o paralelo");
        }
    }
}
