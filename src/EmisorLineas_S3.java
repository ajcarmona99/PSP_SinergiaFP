// Este proceso envías varias líneas mediante su salida estándar
public class EmisorLineas_S3 {
    //Punto de entrada del proceso hijo
    public static void main(String[] args) {
        System.out.println("Primer mensaje del hijo");
        System.out.println("Segundo mensaje del hijo");
        System.out.println("Tercer mensaje del hijo");

        // Cuando termina el main, el proceso EmisorLineas finaliza.
        // Desde un padre a este proceso hijo, el padre podra detectar que ya no quedan mensajes por leer
    }
}
