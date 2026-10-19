import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

// Este proceso recibe numeros y calcula la suma
public class SumadorNumeros_S3 {
    public static void main(String[] args) {
        // Preparo el arranque del proceso hijo
        ProcessBuilder constructor = new ProcessBuilder(
                "java",
                "-Dfile.enconding=UTF-8",
                "-cp",
                System.getProperty("java.class.path"),
                "EmisorNumeros_S3"
        );

        // Mostrar los errores del hijo en la consola
        constructor.redirectError(ProcessBuilder.Redirect.INHERIT);

        try {
            // Arrancamos el proceso hijo
            Process hijo = constructor.start();

            // Acumulamos aquí los numeros validos
            int suma = 0;

            // Contar los numeros que hemos podido interpretar
            int cantidad = 0;

            try(BufferedReader lector = new BufferedReader(
                    new InputStreamReader(
                            hijo.getInputStream(),
                            StandardCharsets.UTF_8
                    )
            )) {

                // Almacenamos cada linea recibida
                String linea;

                // Leemos hasta el final del flujo de la salida de la consola del hijo
                while ((linea = lector.readLine()) != null){

                    try {
                        // Eliminar espacios en los extremos y convertimos el texto a un entero
                        int numero = Integer.parseInt(linea.trim());

                        // Añadimo el número a la suma
                        suma += numero;

                        // Contamos un numero valido mas
                        cantidad++;

                        // Muestre el valor
                        System.out.println("Numero recibido: " + numero);
                    }catch (NumberFormatException e){
                        // La ignoramos y seguimos leyendo
                        System.err.println(
                                "Linea descartada: " + linea
                        );
                    }
                }

                // Esperamos a que termine el hijo
                int codigo = hijo.waitFor();

                // Mostramos los resultados
                System.out.println("Cantidad de numeros: " + cantidad);
                System.out.println("Suma total: " + suma);
                System.out.println("Codigo del hijo: " + codigo);
            }
        }catch (IOException e){
            System.out.println("Ha habido un error al arrancar el proceso hijo");
        }catch (InterruptedException e){
            // Restaurar la marca de interrupcion
            Thread.currentThread().interrupt();

            // Informamos del problema
            System.err.println("Espera interrumpida");
        }
    }
}
