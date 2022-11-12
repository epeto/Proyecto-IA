/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genetico;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author emmanuel
 */
public class Principal {

    Pesos pesos;
    LinkedList<Tour> poblacion; //La población se representa como una lista de tours.
    Tour mejorLocal; //es el mejor tour dentro de una población.
    Tour mejorGlobal; //es el mejor tour entre todas las poblaciones.
    int cantidad=20; //cantidad de habitantes por población.
    LinkedList<String> nomCiud; //Guarda los nombres de las ciudades.
    
    public Principal(){
        pesos = new Pesos();
        pesos.llenarMatriz();
        poblacion = new LinkedList();
        mejorLocal = new Tour();
        mejorGlobal = new Tour();
        
        nomCiud = new LinkedList();
        try{
            FileReader fr1 = new FileReader("ciudades.txt");
            BufferedReader br1 = new BufferedReader(fr1);
            
            String linea = br1.readLine();
            
            while(linea!=null){
                nomCiud.add(linea);
                linea = br1.readLine();
            }
        }catch(Exception ex){}
    }
    
    /**
     * Función de evaluación de un tour(calcula el costo total)
     * @param t1 tour que recibe
     */
    public void fitness(Tour t1){
        int total=0;
        int tam = t1.ciudades.size();
        for(int i=0;i<tam-1;i++){
            total+=pesos.distancias[t1.ciudades.get(i)][t1.ciudades.get(i+1)];
        }
        
        total+=pesos.distancias[t1.ciudades.get(0)][tam-1];
        
        t1.setCosto(total);
    }
    
    /**
     * Crea la población inicial de manera aleatoria.
     * @param n tamaño de la población
     */
    public void creaPoblacion(int n){
        mejorLocal.costoTotal = Integer.MAX_VALUE;
        mejorGlobal.costoTotal = Integer.MAX_VALUE;
        for(int i=0;i<n;i++){
            Tour temp = new Tour();
            temp.genTourAl(pesos.lineas.size()); //Hay 51 ciudades.
            fitness(temp);
            poblacion.add(temp);
        }
    }
    
    /**
     * Función que calcula el costo de cada elemento de la población.
     */
    public void calcCosto(){
        for(Tour to:poblacion){
            fitness(to);
        }
    }
    
    /**
     * Función que selecciona a los padres mediante una ruleta.
     * @param n número de elementos a seleccionar.
     * @return 
     */
    public LinkedList<Tour> seleccion(int n){
        mejorLocal.copia(poblacion.get(0));
        LinkedList<Tour> retVal = new LinkedList();
        int maxCost=0;
        //Primero elegimos al mejor local.
        for(Tour t:poblacion){
            if(t.costoTotal<mejorLocal.costoTotal){
                mejorLocal.copia(t);
            }
            
            if(t.costoTotal>maxCost){
                maxCost = t.costoTotal;
            }
        }
        
        //Luego elegimos al mejor global.
        if(mejorLocal.costoTotal<mejorGlobal.costoTotal){
            mejorGlobal.copia(mejorLocal);
        }
        
        Tour best = new Tour(mejorLocal); //El mejor tour siempre será seleccionado.
        retVal.add(mejorLocal);
        
        int[] ru = new int[poblacion.size()]; //Este arreglo servirá como ruleta.
        ru[0] = maxCost - poblacion.get(0).costoTotal + 100;
        for(int i=1;i<poblacion.size();i++){
            ru[i] = ru[i-1] + maxCost - poblacion.get(i).costoTotal + 100;
        }
        
        Random ran = new Random();
        for(int i=0;i<n-1;i++){
            int bola = ran.nextInt(ru[ru.length-1]);
            int j=0;
            boolean asignado = false;
            while(!asignado){
                if(bola<=ru[j]){
                    Tour nuevo = new Tour(poblacion.get(j));
                    retVal.add(nuevo);
                    asignado = true;
                }
                j++;
            }
        }
        return retVal;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Principal prin = new Principal();
        prin.creaPoblacion(prin.cantidad);
        Random ran = new Random();
        Scanner sc = new Scanner(System.in);
        System.out.print("Decida el número de iteraciones: ");
        int iter = sc.nextInt();
        
        for(int i=0;i<iter;i++){
            //Primero seleccionamos a los padres.
            LinkedList<Tour> padres = prin.seleccion(10);
            LinkedList<Tour> hijos = new LinkedList();
            //Se van a sacar 20 hijos para la nueva generación.
            for(int j=0;j<10;j++){
                int ico = ran.nextInt(3); //Para elegir al azar una combinación
                Tour padre1 = new Tour(padres.get(ran.nextInt(padres.size()))); //Elige al primer padre de forma aleatoria.
                Tour padre2 = new Tour(padres.get(ran.nextInt(padres.size()))); //Elige al segundo padre de forma aleatoria.
                
                LinkedList<Tour> ht = Reproduccion.crossover(padre1, padre2, ico);
                hijos.addAll(ht);
            }
            
            for(Tour h:hijos){
                int imu = ran.nextInt(100); //Para elegir una mutación aleatoria(solo si cae en 0 o 1).
                Reproduccion.mutar(h, imu);
            }
            
            prin.poblacion.clear(); //Vaciamos la generación vigente.
            
            prin.poblacion.addAll(hijos); //La nueva generación serán los hijos.
            prin.calcCosto(); //Calculamos los costos de la nueva generación
        }
        
        System.out.println("Mejor tour global:\n"+prin.mejorGlobal);
        System.out.print("[");
        for(int k=0;k<prin.nomCiud.size();k++){
            int indice = prin.mejorGlobal.ciudades.get(k);
            System.out.print(prin.nomCiud.get(indice)+", ");
        }
        System.out.println("]");
        System.out.println("Costo del tour: "+prin.mejorGlobal.costoTotal);
    }
    
}
