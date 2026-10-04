package universidad.ordenamientos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class CentralDeAlgoritmos {

    static Random random = new Random();

    static Scanner sc = new Scanner(System.in);

    static List<int[]> vectores =  new ArrayList<>();
    static int[] vectorBase = new int[100_000];

    public static void main(String[] args){

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
                List<Object> ordenamientoDeBurbujaMejorado=  ordenamientoDeBurbujaMejoradoYEstadisticas();
                mostrarEstadisticasDelAlgoritmo(ordenamientoDeBurbujaMejorado);
                break; 
            case 3: 
                List<Object> ordenamientoDeInsertion= ordenamientoDeInsertionYEstadistica();
                mostrarEstadisticasDelAlgoritmo(ordenamientoDeInsertion);
                break;
            case 4:
                List<Object> ordenamientoDeSeleccion = ordenarPorSeleccionYEstadisticas();
                mostrarEstadisticasDelAlgoritmo(ordenamientoDeSeleccion); 
                break; 
            case 5: 
                List<Object> ordenamientoQuickSort = ordenarPorQuickSort();
                mostrarEstadisticasDelAlgoritmo(ordenamientoQuickSort);
                break;
            case 6: 
                System.out.println("\n\033[034mCerrando aplicacion...\033[0m");
                break; 
        }
    }

    static void mostrarEstadisticasDelAlgoritmo(List<Object> resultados){

        System.out.println("\033[034m\n====================");
        System.out.println("INFORME DE ALGORITMO");
        System.out.println("====================");

        System.out.println("\n==================================");

        System.out.printf("Algoritmo: %s%n", resultados.get(0));
        System.out.printf("Intercambios Realizados: %d%n", resultados.get(1));
        System.out.printf("Numero de compraciones: %d%n", resultados.get(2));
        System.out.printf("Tiempo en Ordenar el vector (ms): %.2f%n", resultados.get(3));

        System.out.println("\n==================================\033[0m");

    }

    static int pedirNumero(String mensaje, String mensajeDeError, int min, int max){
        
        int numero = 0; 
        
        do{
            System.out.print(mensaje + ": ");
            numero = sc.nextInt();
            sc.nextLine();

            if(numero > max || numero < min){
                System.out.println("\033[031m" + mensajeDeError + "\033[0m");
            }

        }while(numero > max || numero < min);

        return numero;

    }

    static List<Object> ordenarPorBurbujaNormalYEstadisticas(){

        List<Object> estadisticas = new ArrayList<>();
        int[] vector = vectores.get(0);

        long intercambios = 0;
        long comparaciones = 0;
        
        long inicio;
        long finalDeEjecucion;

        inicio = System.nanoTime();


        for(int i = 0; i < vector.length - 1; i++){
            for(int j = 0; j < vector.length - 1 - i; j++){
                comparaciones++;
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

        estadisticas.add("Burbuja Normal");
        estadisticas.add(intercambios);
        estadisticas.add(comparaciones);
        estadisticas.add(tiempoEnOrdenar);

        return estadisticas;

    }

    static List<Object> ordenarPorSeleccionYEstadisticas(){
        List<Object> estadisticas = new ArrayList<>();
        int[] vector = vectores.get(3);

        long intercambios = 0;
        long comparaciones = 0;
        
        long inicio;
        long finalDeEjecucion;

        inicio = System.nanoTime();        

        for(int i = 0; i< vector.length; i++){

            int minIndex = i;

            for(int j = i+1; j<vector.length; j++){
                comparaciones++;
                if(vector[j]<vector[minIndex]){
                    minIndex = j;
                }
            }

            int temp = vector[minIndex];
            vector[minIndex] = vector[i];
            vector[i] = temp;
            intercambios++;

        }
        

        finalDeEjecucion = System.nanoTime();

        double tiempoEnOrdenar = (finalDeEjecucion - inicio)/1_000_000.0;

        estadisticas.add("Seleccion");
        estadisticas.add(intercambios);
        estadisticas.add(comparaciones);
        estadisticas.add(tiempoEnOrdenar);

        return estadisticas;
    }

    static List<Object> ordenarPorQuickSort(){
        List<Object> estadisticas = new ArrayList<>();
        int[] vector = vectores.get(4);

        long[] estadisticasVector = new long[2];
        
        long inicio;
        long finalDeEjecucion;

        inicio = System.nanoTime();        

        quicksort(vector, 0, vector.length-1, estadisticasVector);

        finalDeEjecucion = System.nanoTime();

        double tiempoEnOrdenar = (finalDeEjecucion - inicio)/1_000_000.0;

        estadisticas.add("Quick Sort");
        estadisticas.add(estadisticasVector[1]);
        estadisticas.add(estadisticasVector[0]);
        estadisticas.add(tiempoEnOrdenar);

        return estadisticas;
    }

    static void quicksort(int[] vector, int low, int high, long[] estadisticas){
        if(low < high){
            int pi = partition(vector, low,high, estadisticas);

            quicksort(vector, low, pi - 1, estadisticas);
            quicksort(vector, pi + 1, high, estadisticas);
        }
    }

    static int partition(int[] vector, int low, int high, long[] estadisticas){
       
        int pivote = vector[high];
        int i = low - 1;

        for(int j = low; j<high; j++){
            estadisticas[0]++;
            if(vector[j] <= pivote){
                i++;
                estadisticas[1]++;
                int aux = vector[i];
                vector[i] = vector[j];
                vector[j] = aux;
            }
        }

        estadisticas[1]++;
        int aux = vector[i+1];
        vector[i+1] = vector[high];
        vector[high] = aux;

        return i+1;

    }

    static List<Object> ordenamientoDeBurbujaMejoradoYEstadisticas(){
        List<Object> estadisticas = new ArrayList<>();
        int[] vector = vectores.get(1);

        long intercambios = 0;

        long inicio;
        long finalDeEjecucion;
        long comparaciones = 0;
        inicio = System.nanoTime();


        for(int i = 0; i < vector.length - 1; i++){
            boolean huboIntercambios= false;
            for(int j = 0; j < vector.length - 1 - i; j++){
                comparaciones++;
                if(vector[j] > vector[j+1]){
                    int aux = vector[j];
                    vector[j] = vector[j+1];
                    vector[j+1] = aux;
                    intercambios++;
                    huboIntercambios= true;
                }
            }
            if(!huboIntercambios){
                break;
            }
        }


        finalDeEjecucion = System.nanoTime();

        double tiempoEnOrdenar = (finalDeEjecucion - inicio)/1_000_000.0;

        estadisticas.add("Burbuja Mejorado");
        estadisticas.add(intercambios);
        estadisticas.add(comparaciones);
        estadisticas.add(tiempoEnOrdenar);

        return estadisticas;

    }
    static List<Object> ordenamientoDeInsertionYEstadistica(){
        List<Object> estadisticas = new ArrayList<>();
        int[] vector = vectores.get(2);

        long intercambios = 0;
        long comparaciones = 0;

        long inicio;
        long finalDeEjecucion;


        inicio = System.nanoTime();


        for(int i=1; i<vector.length;i++){
            int actual= vector[i];
            int j= i-1;
            
            while(j>=0 && vector[j]> actual){
                comparaciones++;
                vector[j+1]= vector[j];
                j--;
                intercambios++;
            }
            comparaciones++;
            vector[j+1]= actual;
        }

        finalDeEjecucion = System.nanoTime();

        double tiempoEnOrdenar = (finalDeEjecucion - inicio)/1_000_000.0;

        estadisticas.add("Insertion");
        estadisticas.add(intercambios);
        estadisticas.add(comparaciones);
        estadisticas.add(tiempoEnOrdenar);


        return estadisticas;

    }


    
}
