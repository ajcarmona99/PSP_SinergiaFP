import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

// Este proceso arranca nuestro EmisorLineas y lee todos sus mensajes
public class ReceptorLineas_S3 {
    public static void main(String[] args) {
        // Preparo el comando que ejecuta a la clase hijo
        ProcessBuilder constructor = new ProcessBuilder(
                "java",
                "-Dfile.encoding=UTF-8",
                "-cp",
                System.getProperty("java.class.path"),
                "EmisorLineas_S3"
        );

        try {
            // Arancado el proceso hijo
            Process hijo = constructor.start();
            // Abrir un lector conectado a la salida del hijo
            try(BufferedReader lector = new BufferedReader(new InputStreamReader(hijo.getInputStream(), StandardCharsets.UTF_8))) {

                // Guardamos cada linea recibida
                String linea;
                // Contamos cuantas lineas hemos recibido
                int contador = 0;

                while ((linea = lector.readLine()) != null){

                    // Aumento en uno la linea
                    contador++;

                    // Mostramos el mensaje recibido
                    System.out.println(
                            "Mensaje " + contador + ": " + linea
                    );
                }

                System.out.println("Total de lineas recibidas: " + contador);

                // Esperamos la finalizacion del hijo
                int codigo = hijo.waitFor();

                // Mostramos su codigo de salida
                System.out.println("Codigo de salida: " + codigo);
            }
        }catch (IOException e){
            System.out.println("Hubo un error");
        }catch (InterruptedException e){
            // Restauremos la marca de interrupcion
            Thread.currentThread().interrupt();
            System.err.println("Espera interrumpida");
        }
    }
}
