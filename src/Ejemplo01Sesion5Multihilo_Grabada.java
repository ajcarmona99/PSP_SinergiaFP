public class Ejemplo01Sesion5Multihilo_Grabada {
    static class Control{
        // Main escribe esta bandera y el trabajador o el hilo la consulta.
        // volatile da visibilidad a la escritura entre los hilos.
        volatile boolean parar = false;
    }

    public static void main(String[] args) throws InterruptedException {
        // Ambos hilos van a utilizar el mismo objeto control
        Control control = new Control();

        Thread trabajador = new Thread(() -> {
           // Esta variable solo pertenece al trabajador.
           // Main no la consulta mientras el trabajador la modifica.
           long vueltas = 0;

           // El trabajador comprueba la bandera en cada vuelta
            while (!control.parar){
                vueltas++;
            }

            // Llegamos aquí cuando el trabajador observa el true.
            System.out.println("Bandera observada; vueltas = " + vueltas);
        }, "trabajador");

        trabajador.start();

        try {
            // Pausa de la demostración: permite que se aprecie el bucle
            // La visibilidad la proporciona volatile, no sleep()
            Thread.sleep(100);
        }finally {
            // Pedimos el cierre incluso si se interrumpe la pasusa del main
            control.parar = true;

            // Esperamos la terminación del trabajador
            trabajador.join();
        }

        System.out.println("Main: el trabajador ha terminado.");
    }
}

// El numero de vueltas cambia entre ejecuciones. Incluso podría ser cero si el trabajador empieza a comprobar la condicion
// cuando el main ya ha escrito true. Lo que comprobamos realmente con este ejemplo es que sale del bucle al observar la señal
// y que el ultimo mensaje aparece despues del join.

// ¿Qué ocurre si quitamos el volatile?
// Si quitasemos el volatile, una ejecucion podría seguir terminando, pero ya no tendríamos la garantía de visibilidad para las lecturas
// del bucle.
// También quiero que os fijeis que este bucle consulta la bandera continuamente y consume CPU. Lo usamos porque deja ver muy bien la idea.
// Si el trabajador estuviera bloqueado, escribir parar = true no lo despertaría. Tendriamos que usar un mecanismo adecuado, como pueda ser
// una cola o una interrupción.
