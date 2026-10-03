package universidad.ordenamientos;

/**
 * Hello world!
 *
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class CentralDeAlgoritmos {

    static Random random = new Random();

    static Scanner sc = new Scanner(System.in);

    static List<int[]> vectores =  new ArrayList<>();
    static int[] vectorBase = new int[100_000];

    static void main(){

        iniciarAplicacion();
        
    }

    static void cargarVectores(){
        for(int i = 0; i<vectorBase.length; i++){
            vectorBase[i] = random.nextInt(1, 200_000);
        }

        for(int i = 0; i<5; i++){
            vectores.add(vectorBase.clone());
        }
    }

    static void iniciarAplicacion(){

        cargarVectores();
        int opcion = 0;

        do{
            System.out.println("\nAlgoritmos de ordenamiento");
            System.out.println("1. Ordenamiento de Burbuja");
            System.out.println("2. Ordenamiento de Burbuja Mejorado");
            System.out.println("3. Ordenamiento de Inserción");
            System.out.println("4. Ordenamiento de Selección");
            System.out.println("5. Ordenamiento Quick Sort");
            System.out.println("6. salir");

            opcion = pedirNumero("Ingrese uno de los Metodos de ordenamiento a ejecutar (1-6)", "Error: Número fuera de rango, ingrese un número valido (1-6)", 1, 6);

            ejecutarSegunOpcion(opcion);

        }while(opcion != 6);

    }

    static void ejecutarSegunOpcion(int opcion){
        switch(opcion){
            case 1: 
                List<Object> ordenamientoDeBurbujaNormal = ordenarPorBurbujaNormalYEstadisticas();
                mostrarEstadisticasDelAlgoritmo(ordenamientoDeBurbujaNormal);
                break;
            case 2: 
                List<Object> ordenamientoDeBurbujaMejorado;
                break; 
            case 3: 
                List<Object> ordenamientoDeInsertion;
                break;
            case 4:
                List<Object> ordenamientoDeSeleccion; 
                break; 
            case 5: 
                List<Object> ordenamientoQuickSort;
                break;
            case 6: 
                System.out.println("Cerrando aplicacion...");
                break; 
        }
    }

    static void mostrarEstadisticasDelAlgoritmo(List<Object> resultados){

        System.out.println("\033[034m\n====================");
        System.out.println("INFORME DE ALGORITMO");
        System.out.println("====================\n");

        System.out.println("\n==================================");

        System.out.printf("Algoritmo: %s%n", resultados.get(0));
        System.out.printf("Intercambios Realizados: %d%n", resultados.get(2));
        System.out.printf("Tiempo en Ordenar el vector (ms): %.2f%n ", resultados.get(3));
        System.out.printf("Numero Mayor: %d%n", resultados.get(4));
        System.out.printf("Numero Menor: %d%n", resultados.get(5));
        System.out.printf("Promedio: %.2f%n", resultados.get(6));

        System.out.println("\n==================================\033[0m");

    }

    static int pedirNumero(String mensaje, String mensajeDeError, int min, int max){
        
        int numero = 0; 
        
        do{
            System.out.println(mensaje + ": ");
            numero = sc.nextInt();
            sc.nextLine();

            if(numero > max || numero < min){
                System.out.println("\033[033m" + mensajeDeError + "\033[0m");
            }

        }while(numero > max || numero < min);

        return numero;

    }

    static double calcularPromedio(int[] vector){

        double suma = 0;

        for(int i = 0; i<vector.length; i++){
            suma += vector[i];
        }

        return suma/vector.length;
    }

    static List<Object> ordenarPorBurbujaNormalYEstadisticas(){

        List<Object> estadisticas = new ArrayList<>();
        int[] vector = vectores.get(0);

        long intercambios = 0;
        
        long inicio;
        long finalDeEjecucion;

        double promedio;

        inicio = System.nanoTime();
        
   
        for(int i = 0; i < vector.length - 1; i++){
            for(int j = 0; j < vector.length - 1 - i; j++){
                if(vector[j] > vector[j+1]){
                    int aux = vector[j];
                    vector[j] = vector[j+1];
                    vector[j+1] = aux; 
                    intercambios++;
                }
            }
        }


        finalDeEjecucion = System.nanoTime();

        double tiempoEnOrdenar = (finalDeEjecucion - inicio)/1_000_000.0;

        int numeroMayor = vector[vector.length-1];
        int numeroMenor = vector[0];
        promedio = calcularPromedio(vector);

        estadisticas.add("Burbuja Normal");
        estadisticas.add(vector);
        estadisticas.add(intercambios);
        estadisticas.add(tiempoEnOrdenar);
        estadisticas.add(numeroMayor);
        estadisticas.add(numeroMenor);
        estadisticas.add(promedio);

        return estadisticas;

         

    }
    
}
