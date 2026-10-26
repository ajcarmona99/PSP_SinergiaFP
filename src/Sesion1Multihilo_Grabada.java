public class Sesion1Multihilo_Grabada {
    public static void main(String[] args) throws Exception {
        // El modo lo vamos a pasar por args si es start utilizamos start y si no run
        String modo = args.length == 0 ? "start" : args[0];

        // Si no es ninguno de estos modos lanza una excepcion
        if(!modo.equals("start") && !modo.equals("run")){
            throw new IllegalArgumentException(
                    "Uso: start | run"
            );
        }

        // Creamos los hilos
        Thread a = new Thread(tarea("Descarga"), "trabajador-A");
        Thread b = new Thread(tarea("Informe"), "trabajador-B");

        System.out.println("Modo= " + modo);
        System.out.println(
                "Principal = " + Thread.currentThread().getName()
        );

        // Calculamos el tiempo de ejecucion
        long inicio = System.nanoTime();

        // Diferenciamos entre los modos que hemos visto en esta sesion
        if (modo.equals("run")){
            a.run();
            b.run();
        }else {
            a.start();
            b.start();

            System.out.println("Main puede continuar antes de esperar");

            // Esperamos que terminen los dos hilos
            a.join();
            b.join();
        }

        // Calculamos en segundos el tiempo de ejecucion del programa
        double segundos = (System.nanoTime() - inicio) / 1e9;

        System.out.printf(
          "Fin de ambas tareas: %.2f s%n",
          segundos
        );
    }

    // static te permite en el interior de una clase poder utilizarla en otros metodos sin necesidad
    // de crear una instancia de la clase
    public static void punto1(){
        Runnable tarea = () -> {
            System.out.println("Hola desde el trabajador");
        };

        // Objeto que representa un hilo dentro de Java
        Thread hilo = new Thread(tarea);

        // Inicializamos el hilo
        hilo.start();

        System.out.println("Hola desde main");

        // main ya se ejecuta dentro de un hilo que es el hilo principal
        // Runnable describe una tarea que es una funcion lamda que la hace asincrona
        // Thread = new Thread(runnable) crea un hilo
        // start() inicializa ese hilo
    }

    public static void punto2() throws InterruptedException {
        // Realizar la definicion de la tarea
        Runnable tarea = () -> {
            // Realizar texto formateado
            System.out.printf("Soy %s y pertenezco al %d%n",
                    Thread.currentThread().getName(),
                    ProcessHandle.current().pid()
                    );
        };

        // Creo los hilos
        Thread a = new Thread(tarea, "trabajador-A");
        Thread b = new Thread(tarea, "trabajador-B");

        // Para ver que el main tambien es un hilo que es principal
        System.out.printf(
                "Soy %s y pertenezco al proceso %d%n",
                Thread.currentThread().getName(),
                ProcessHandle.current().pid()
        );

        // Inicializo los hilos
        a.start();
        b.start();

        // Hace que los el hilo principal no termine hasta que no termine los otros hilos
        a.join();
        b.join();

        // Thread.currentThread() devuelve el hilo que se esta ejecutando en esa instruccion
        // getName() permite identifficarlo y te da el nombre
        // Los tres hilos pertenecen al mismo proceso y muestran el mismo PID(identificador unico de un proceso)
        // join() hace que quien lo llama(mayoritariamente el hilo principal) hace que quien los llame espere a que termine
    }

    // Al hacer un throws Excepcion englobas las excepciones y te olvidas de ir individualizandolas
    public static void punto3() throws Exception{
        // Defino la tarea
        Runnable tarea = () -> {
          try {
              // Con la diferencia de los otros ejemplos vamos a visualizar que podemos introducir varias instrucciones
              for (int paso = 1; paso<=3; paso++){
                  System.out.printf(
                          "%s: paso %d%n",
                          Thread.currentThread().getName(),
                          paso
                  );

                  // Siempre que se utilice el metodo sleep para hacer simulaciones introducirlo todo el bloque
                  // en un try y un catch
                  Thread.sleep(1000); // Pausado durante unos 300 milisegundos
              }
          }catch (InterruptedException e){
              // Si ocurre un error se interrumpe el hilo
              Thread.currentThread().interrupt();
              // Muestro la informacion del error por pantalla
              System.out.println(
                      Thread.currentThread().getName() + " interrumpido"
              );
          }
        };

        // Creo los hilos
        Thread a = new Thread(tarea, "trabajador-A");
        Thread b = new Thread(tarea, "trabajador-B");

        // Inicializo los hilos
        a.start();
        b.start();

        // Espero que terminen los hilos para que termine el principal
        a.join();
        b.join();

        System.out.println("Ambos trabajadores han terminado");
    }

    public static void punto4() throws Exception{
        // Creo los hilos o la definicion de los hilos
        Thread a = new Thread(tarea("Descarga"), "trabajador-A");
        Thread b = new Thread(tarea("Informe"), "trabajador-B");

        // Segunda prueba: llamadas normales
        a.start();
        b.start();

        // Voy a esperar que los hilos anteriores finalicen para que termine el hilo principal
        a.join();
        b.join();

        System.out.println("Fin");
    }

    // Otra alternativa para crear un hilo es definir un metodo que devuelva
    // un Runnable

    // El método tarea(String nombre) permite reutilizar el mismo comportamientos con distintos nombres
    // de actividad
    public static Runnable tarea(String nombre){
        // Para devolver un objeto de tipo runnable simplemente voy a definir una funcion lamnda
        // que esto lo que hace es representar la tarea
        return () -> {
          // Defino toda la tarea que quiero que haga el hilo
            // como introduzco un sleep utilizo bloques de tipo try/catch
            try {
                for (int paso = 1; paso <=3; paso++){
                    System.out.printf(
                      "%s | hilo=%s | paso = %d%n",
                      nombre,
                      Thread.currentThread().getName(),
                      paso
                    );

                    Thread.sleep(300);
                }
            }catch (InterruptedException e){
                // Interrumpo el hilo actual si ha habido algun problema
                Thread.currentThread().interrupt();
                System.out.println(nombre + " interrumpida");
            }
        };
    }

    public static void punto5() throws Exception{
        // Voy iniciar la variable inicio que me va a permitir obtener los segundos
        // que tarda en realizar la ejecucion completa de este metodo
        long inicio = System.nanoTime();

        // Definir dos hilos mediante el Runnable que hemos devuelto en la funcion de tarea
        Thread a = new Thread(tarea("Descarga"), "trabajador-A");
        Thread b = new Thread(tarea("Informe"), "trabajador-B");

        // Voy a inicializar el hilo a y voy a hacer que el main espere al a
        a.start();
        a.join();

        // Voy a inicializar el hilo b y voy a hacer a hacer que el main espere al b
        b.start();
        b.join();

        // Vemos cuanto tarda la ejecucion de el metodo punto5
        double segundos = (System.nanoTime() - inicio) /1e9;

        System.out.printf("Duración: %2f s%n", segundos);

        // Cuando hemos hecho start hemos permite arrancar ambos para que progresen concurrentemente
        // Despues, el main espera a ambos.
        // Esperar primero a a no detiene a b, por b esta iniciado
        // System.nanoTime() sirve para medir el tiempo transcurrido.
        // Estos tiempos ilustan el solapamiento de la pausar; no prueba que cualquier calcular vaya a ser el doble de rapido
        // con dos hilos.
    }




}
