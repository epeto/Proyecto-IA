/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genetico;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;

/**
 *
 * @author emmanuel
 */
public class Pesos {
   LinkedList<String> lineas; //Lista que guarda las líneas del archivo pesos.txt
   int[][] distancias; //matriz que guarda las distancias entre las ciudades.
   int pesoMax=0;
   
   public Pesos(){
       lineas = new LinkedList();
       
       try{
            FileReader fr1 = new FileReader("pesos.txt");
            BufferedReader br1 = new BufferedReader(fr1);
            
            String linea = br1.readLine();
            
            while(linea!=null){
                lineas.add(linea);
                linea = br1.readLine();
            }
        }catch(Exception ex){}
   }
   
   /**
    * Función que separa una línea por espacios y lo guarda en un arreglo.
    * @param indice línea a separar
    * @return arreglo con números pero en tipo String
    */
   private String[] separar(int indice){
       String[] retVal = lineas.get(indice).split(" ");
       return retVal;
   }
   
   /**
    * Llena la matriz de distancias.
    */
   public void llenarMatriz(){
       distancias = new int[lineas.size()][lineas.size()];
       
       //En esta parte llenamos la mitad de la matriz (un triángulo)
       for(int i=0;i<lineas.size();i++){
           String[] lineaActual = separar(i);
           for(int j=0;j<lineaActual.length;j++){
               distancias[i][j] = Integer.parseInt(lineaActual[j]);
           }
       }
       
       //En esta parte llenamos la otra mitad de la matriz.
       for(int i=0;i<distancias.length;i++){
           for(int j=i;j<distancias.length;j++){
               distancias[i][j]=distancias[j][i];
           }
       }
       
       //ahora calculamos el peso máximo.
       for(int i=0;i<distancias.length;i++){
           for(int j=0;j<distancias.length;j++){
               if(pesoMax<distancias[i][j]){
                   pesoMax = distancias[i][j];
               }
           }
       }
   }
}
