/*
 * Esta clase contiene los algoritmos de combinación y mutación.
 */
package genetico;

import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author emmanuel
 */
public class Reproduccion {
    
    /**
     * Recibe 2 padres, aplica partially mapped crossover y devuelve 2 hijos.
     * @param p1 padre1
     * @param p2 padre2
     * @return 
     */
    static LinkedList<Tour> partiallyMapped(Tour p1, Tour p2){
        int corte1=10,corte2=0;
        int[] h1 = new int[p1.ciudades.size()];
        int[] h2 = new int[p1.ciudades.size()];
        LinkedList<Integer> subsec1 = new LinkedList();
        LinkedList<Integer> subsec2 = new LinkedList();
        for(int i=0;i<h1.length;i++){
            h1[i] = -1;
            h2[i] = -1;
        }
        
        Random rn = new Random();
        //Esto es para asegurarnos de que el corte 2 sea mayor al corte 1.
        while(corte1 >= corte2){
            corte1 = rn.nextInt(p1.ciudades.size()-1);
            corte2 = rn.nextInt(p1.ciudades.size()-1);
        }
        
        for(int i=corte1+1;i<=corte2;i++){
            h1[i]=p2.ciudades.get(i);
            subsec1.add(p2.ciudades.get(i));
            h2[i]=p1.ciudades.get(i);
            subsec2.add(p1.ciudades.get(i));
        }
        
        for(int i=0;i<h1.length;i++){
            if(!subsec1.contains(p1.ciudades.get(i)) && (i<=corte1 || i>corte2)){
                h1[i] = p1.ciudades.get(i);
                subsec1.add(p1.ciudades.get(i));
            }
           
            if(!subsec2.contains(p2.ciudades.get(i)) && (i<=corte1 || i>corte2)){
                h2[i] = p2.ciudades.get(i);
                subsec2.add(p2.ciudades.get(i));
            }
        }
        
        p1.ciudades.removeAll(subsec2);
        p2.ciudades.removeAll(subsec1);
        
        for(int i=0;i<h1.length;i++){
            if(h1[i] == -1){
                h1[i] = p2.ciudades.removeFirst();
            }
            
            if(h2[i] == -1){
                h2[i] = p1.ciudades.removeFirst();
            }
        }
        
        Tour tn1 = new Tour(h1);
        Tour tn2 = new Tour(h2);
        
        LinkedList<Tour> ret = new LinkedList();
        ret.add(tn1);
        ret.add(tn2);
        
        return ret;
    }
    
    /**
     * Recibe 2 padres, aplica order crossover y devuelve 2 hijos.
     * @param p1
     * @param p2
     * @return 
     */
    static LinkedList<Tour> orderC(Tour p1, Tour p2){
        int corte1=10,corte2=0;
        int[] h1 = new int[p1.ciudades.size()];
        int[] h2 = new int[p1.ciudades.size()];
        LinkedList<Integer> subsec1 = new LinkedList();
        LinkedList<Integer> subsec2 = new LinkedList();
        for(int i=0;i<h1.length;i++){
            h1[i] = -1;
            h2[i] = -1;
        }
        
        Random rn = new Random();
        //Esto es para asegurarnos de que el corte 2 sea mayor al corte 1.
        while(corte1 >= corte2){
            corte1 = rn.nextInt(p1.ciudades.size()-1);
            corte2 = rn.nextInt(p1.ciudades.size()-1);
        }
        
        for(int i=corte1+1;i<=corte2;i++){
            h1[i]=p2.ciudades.get(i);
            subsec1.add(p2.ciudades.get(i));
            h2[i]=p1.ciudades.get(i);
            subsec2.add(p1.ciudades.get(i));
        }
        
        p1.ciudades.removeAll(subsec1);
        p2.ciudades.removeAll(subsec2);
        
        for(int i=0;i<h1.length;i++){
            if(h1[i]==-1){
                h1[i] = p1.ciudades.removeFirst();
            }
            
            if(h2[i]==-1){
                h2[i] = p2.ciudades.removeFirst();
            }
        }
        
        Tour tn1 = new Tour(h1);
        Tour tn2 = new Tour(h2);
        
        LinkedList<Tour> ret = new LinkedList();
        ret.add(tn1);
        ret.add(tn2);
        
        return ret;
    }
    
    /**
     * Recibe 2 padres, aplica cycle crossover y devuelve 2 hijos.
     * @param p1
     * @param p2
     * @return 
     */
    static LinkedList<Tour> cycle(Tour p1, Tour p2){
        int[] h1 = new int[p1.ciudades.size()];
        int[] h2 = new int[p1.ciudades.size()];
        
        LinkedList<Integer> subsec1 = new LinkedList();
        LinkedList<Integer> subsec2 = new LinkedList();
        for(int i=0;i<h1.length;i++){
            h1[i] = -1;
            h2[i] = -1;
        }
        
        h1[0] = p1.ciudades.get(0);
        subsec1.add(h1[0]);
        int next = p2.ciudades.get(0);
        
        //Llenamos el primer hijo
        while(!subsec1.contains(next)){
            int indice = p1.ciudades.indexOf(next);
            h1[indice] = next;
            subsec1.add(h1[indice]);
            next = p2.ciudades.get(indice);
        }
        
        for(int i=0;i<h1.length;i++){
            if(h1[i]==-1){
                h1[i] = p2.ciudades.get(i);
            }
        }
        
        //Ahora hacemos lo mismo con el segundo hijo.
        h2[0] = p2.ciudades.get(0);
        subsec2.add(h2[0]);
        int next2 = p1.ciudades.get(0);
        
        //Llenamos el primer hijo
        while(!subsec2.contains(next2)){
            int indice = p2.ciudades.indexOf(next2);
            h2[indice] = next2;
            subsec2.add(h2[indice]);
            next2 = p1.ciudades.get(indice);
        }
        
        for(int i=0;i<h1.length;i++){
            if(h2[i]==-1){
                h2[i] = p1.ciudades.get(i);
            }
        }
        
        LinkedList ret = new LinkedList();
        ret.add(new Tour(h1));
        ret.add(new Tour(h2));
        
        return ret;
    }
    
    public static LinkedList<Tour> crossover(Tour p1, Tour p2, int n){
        LinkedList<Tour> ret;
        switch(n){
            case 0: ret = partiallyMapped(p1,p2);
            break;
            
            case 1: ret = orderC(p1,p2);
            break;
            
            case 2: ret = cycle(p1,p2);
            break;
            
            default: ret = new LinkedList();
            break;
        }
        
        return ret;
    }
    
    /**
     * Aplica la mutación de inserción y desplazamiento al tour que recibe.
     * @param t 
     */
    public static void displacement(Tour t){
        Random rn = new Random();
        int i1 = rn.nextInt(t.ciudades.size());
        int i2 = rn.nextInt(t.ciudades.size());
        Integer temp = t.ciudades.remove(i1);
        t.ciudades.add(i2,temp);
    }
    
    /**
     * Aplica la mutación de intercambio al tour que recibe.
     * @param t 
     */
    public static void exchange(Tour t){
        Random rn = new Random();
        int i1 = rn.nextInt(t.ciudades.size());
        int i2 = rn.nextInt(t.ciudades.size());
        Integer temp = t.ciudades.get(i1);
        t.ciudades.set(i1, t.ciudades.get(i2));
        t.ciudades.set(i2, temp);
    }
    
    /**
     * Aplica la mutación a un tour.
     * @param t
     * @param n 
     */
    public static void mutar(Tour t, int n){
        switch(n){
            case 0: displacement(t);
            break;
            
            case 1: exchange(t);
            break;
        }
    }
    
}
