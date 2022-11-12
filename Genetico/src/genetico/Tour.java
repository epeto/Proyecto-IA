/*
 * Clase que va a representar un tour, es decir, una permutación de las ciudades.
 */
package genetico;

import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author emmanuel
 */
public class Tour {
    LinkedList<Integer> ciudades;
    int costoTotal; //Guarda el costo del tour.
    
    public Tour(){
        ciudades = new LinkedList();
    }
    
    public Tour(Tour t2){
        costoTotal = t2.costoTotal;
        ciudades = new LinkedList();
        ciudades.addAll(t2.ciudades);
    }
    
    public Tour(int[] arr){
        ciudades = new LinkedList();
        for(int i=0;i<arr.length;i++){
            ciudades.add(arr[i]);
        }
    }
    
    /**
     * Recibe un tour y lo copia a este.
     * @param t2 tour a copiar
     */
    public void copia(Tour t2){
        ciudades.clear();
        ciudades.addAll(t2.ciudades);
        costoTotal = t2.costoTotal;
    }
    
    public void setCosto(int c){
        costoTotal = c;
    }
    
    /**
     * Función que genera un tour aleatorio
     * @param tam tamaño del tour a generar
     */
    public void genTourAl(int tam){
        LinkedList<Integer> numeros = new LinkedList(); //Lista de la cual se va a sacar el tour.
        Random r = new Random();
        
        for(int i=0;i<tam;i++){
            numeros.add(i);
        }
        
        //En esta parte sacamos un elemento al azar de la lista de números y lo agregamos a ciudades.
        for(int i=0;i<tam;i++){
            Integer temp = numeros.remove(r.nextInt(numeros.size()));
            ciudades.add(temp);
        }
    }
    
    @Override
    public String toString(){
        return ciudades.toString();
    }
}
