import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class HijoCalculadora_S3 {
    public static void main(String[] args) throws IOException {
        // Leer la operaciones enviadas por el padre
        BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));

        // Enviar los resultados al padre
        PrintWriter salida = new PrintWriter(System.out, true);

        String operacion;

        // Leer las operaciones hasta recibir salir;
        while ((operacion = entrada.readLine())!=null){
            // Si operacion es salir
            if(operacion.equalsIgnoreCase("salir")){
                break;
            }

            try {
                // Separamos la operacion: numero operador operador numero
                String[] partes = operacion.trim().split("\\s+");

                double num1 = Double.parseDouble(partes[0]);
                String operador = partes[1];
                double num2 = Double.parseDouble(partes[2]);

                // Variable auxiliar para el resultado
                double resultado = 0;

                // Segun el operador calculamos el resultado
                switch (operador){
                    case "+":
                        resultado = num1 + num2;
                        break;
                    case "-":
                        resultado = num1 - num2;
                        break;
                    case "/":
                        // Comprobamos que el numero entre que estamos dividiendo no es 0
                        if (num2 == 0){
                            salida.println("Error: division por 0");
                            continue;
                        }
                    case "*":
                        resultado = num1 * num2;
                        break;
                    default:
                        salida.println("Error: operador no valido");
                        continue;
                }

                // Devolver el resultado como una unica linea al padre
                salida.println(resultado);
            }catch (Exception e){
                //  Responder con una sola linea si hay un error
                salida.println("Error: operación incorrecta");
            }


        }
    }
}
