import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

// Este proceso recibe un nombre y devuelve un saludo
public class HijoSaludador_S3 {
    public static void main(String[] args) {
        // System.in representa la entrada estándar de este proceso
        // El padre escribir datos sobre ella
        try(BufferedReader entrada = new BufferedReader(
                new InputStreamReader(
                        System.in,
                        StandardCharsets.UTF_8
                )
        )) {
            // Esperar una linea enviada por el padre
            String nombre = entrada.readLine();

            // Si se cierra la entrada sin recibir datos, terminamos
            if (nombre == null){
                return;
            }

            // Construimos y enviamos la respuesta al padre
            System.out.println("Hola, " + nombre + ".");

            // Forzar el envio de cualquier dato
            System.out.flush();
        }catch (IOException e){
            // Aqui escribo si hay algun error
            System.err.println(
                    "No se puede leer el nombre: " + e.getMessage()
            );

            // Indicar el proceso termian con error
            System.exit(1);
        }
    }
}
