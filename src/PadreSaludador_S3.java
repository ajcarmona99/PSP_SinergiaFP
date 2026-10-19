import java.io.*;
import java.nio.charset.StandardCharsets;

// Este proceso envia un nombre y recibe el saludo
public class PadreSaludador_S3 {
    public static void main(String[] args) {
        // Configuro el comando del hijo
        ProcessBuilder constructor = new ProcessBuilder(
          "java",
          "-Dfile.encoding=UTF-8",
          "-cp",
          System.getProperty("java.class.path"),
          "HijoSaludador_S3"
        );

        // Los errores del hijo lo muestras en consola
        constructor.redirectError(ProcessBuilder.Redirect.INHERIT);

        try {
            // Arranco el proceso hijo
            Process hijo = constructor.start();

            // Creo un escritor conectado a la entrada del proceso hijo
            // Creo un lector conectado a la salida del proceso hijo
            try(
                    BufferedWriter haciaHijo = new BufferedWriter(
                            new OutputStreamWriter(
                                    hijo.getOutputStream(),
                                    StandardCharsets.UTF_8
                            )
                    );

                    BufferedReader desdeHijo = new BufferedReader(
                            new InputStreamReader(
                                    hijo.getInputStream(),
                                    StandardCharsets.UTF_8
                            )
                    );
                    ) {

                // Este es el dato que enviamos al hijo
                String nombre = "Ana";

                // Escribo el nombre en la entrada al hijo
                haciaHijo.write(nombre);

                // Añado el final de linea
                haciaHijo.newLine();

                // Enviamos lo datos que puedan estar en buffer
                haciaHijo.flush();

                // Informamos al usario del padre
                System.out.println("Padre envia: " + nombre);

                // Esperamos la respuesta del hijo
                String respuesta = desdeHijo.readLine();
                if(respuesta != null){
                    // Mostramos el resultado
                    System.out.println("Hijo responde: " + respuesta);
                }else {

                    // El hijo cierra su salida sin responder
                    System.out.println("El hijo termino sin responder");
                }

                // Esperamos la finalizacion del proceso hijo
                int codigo = hijo.waitFor();

                // Mostramos como termina
                System.out.println("Codigo del hijo: " + codigo);
            }
        }catch (IOException e){
            System.out.println("Ha habido un error al arrancar el proceso hijo");
        }catch (InterruptedException e){
            // Restauramos la marca de interrupcion
            Thread.currentThread().interrupt();

            // Informamos de la interrupcion
            System.err.println("Espera interrumpida.");
        }
    }
}
