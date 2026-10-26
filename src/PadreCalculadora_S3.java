import java.io.*;

public class PadreCalculadora_S3 {
    public static void main(String[] args) throws IOException, InterruptedException {
        // Crear un proceso hijo
        ProcessBuilder pb = new ProcessBuilder("java", "-cp", "src" ,"HijoCalculadora_S3");
        // Arrancamos el proceso
        Process proceso = pb.start();

        // Para enviar las operaciones al hijo
        BufferedWriter enviar = new BufferedWriter(new OutputStreamWriter(proceso.getOutputStream()));

        // Para recibir respuestas del hijo
        BufferedReader recibir = new BufferedReader(new InputStreamReader(proceso.getInputStream()));

        // Para leer lo que escribe el usuario
        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

        // Inicializamos una variable que la operacion a realizar
        String operacion;

        // Inicializo una variable que va a ser un booleano que marque si le digo salir o no
        boolean salir = false;

        while (!salir){
            System.out.println("Introduce una operacion o salir:");
            operacion = teclado.readLine();

            // Envia la operacion al hijo
            enviar.write(operacion);
            enviar.newLine();
            enviar.flush();

            // Si el usuario escribe salir
            if (operacion.equalsIgnoreCase("salir")){
                salir = true;
            }else {
                // Leemos el resultado del hijo
                String resultado = recibir.readLine();

                System.out.println("Resultado del hijo: " + resultado);
            }
        }

        // Esperar que termine el proceso hijo
        proceso.waitFor();

        // Cerrar los flujos
        enviar.close();
        recibir.close();

        System.out.println("Proceso padre terminado");
    }
}
