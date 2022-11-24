
## Aplicación de hillclimber con reinicio aleatorio al problema del agente viajero.

import random

## Genera una permutación aleatoria de una lista
# @param lista de la cual se genera una permutación
# @return los mismos elementos de la lista pero cambiando el orden
def permutacion(lista):
    nueva = []
    while lista != []:
        indice = random.randint(0, len(lista)-1)
        nueva.append(lista[indice])
        del lista[indice]
    return nueva

## Lee un ejemplar, el cual es una lista de pueblos.
# @param nombreArchivo nombre del archivo donde se encuentra la lista de pueblos.
# @return lista de pueblos
def leeEjemplar(nombreArchivo):
    archivo = open(nombreArchivo, "r")
    lineas = archivo.readlines()
    archivo.close()
    for i in range(len(lineas)):
        lineas[i] = lineas[i][:len(lineas[i])-1]
    return lineas

## Dado el archivo de distancias, construye una matriz de distancias.
# @param nombreArchivo nombre del archivo que contiene las distancias.
# @return matriz de distancias
def construyeMatriz(nombreArchivo):
    archivo = open(nombreArchivo, "r")
    lineas = archivo.readlines()
    archivo.close()
    matriz = []
    for i in range(1, len(lineas)):
        separado = lineas[i].split(",")
        vectorDist = []
        for j in range(1, len(separado)):
            vectorDist.append(float(separado[j]))
        matriz.append(vectorDist)
    return matriz

## Dado el ejemplar y la lista total de pueblos, transforma los nombres del ejemplar a números.
# @param ejemplar lista de pueblos del ejemplar
# @param pueblos lista total de pueblos
# @return ejemplar donde los pueblos fueron transformados a números.
def transformaEjemplar(ejemplar, pueblos):
    salida = []
    for nombre in ejemplar:
        salida.append(pueblos.index(nombre))
    return salida

## Dado un tour, calcula la suma de las distancias.
# @param inicial índice del pueblo inicial
# @param final índice del pueblo final
# @param tour lista de los pueblos intermedios (entre inicial y final)
# @param matriz de distancias
# @return distancia total del tour
def calculaDistancia(inicial, final, tour, matriz):
    distTotal = matriz[inicial][tour[0]]
    for i in range(1, len(tour)):
        distTotal += matriz[tour[i-1]][tour[i]]
    distTotal += matriz[tour[len(tour)-1]][final]
    return distTotal

## Intercambia dos elementos en un arreglo.
# @param i índice del primer elemento.
# @param j índice del segundo elemento.
# @param arreglo donde se intercambian los elementos.
def swap(i, j, arreglo):
    temp = arreglo[i]
    arreglo[i] = arreglo[j]
    arreglo[j] = temp

## Genera una lista de sucesores
# @param inicial índice del pueblo inicial
# @param final índice del pueblo final
# @param tour índices de los pueblos entre el inicial y el final
# @param matriz de distancias
# @return lista de ternas con: (i, j, distancia del tour)
# donde los índices i j se tienen que intercambiar para obtener el sucesor.
def generaSucesores(inicial, final, tour, matriz):
    sucesores = []
    for i in range(len(tour)):
        for j in range(i+1, len(tour)):
            swap(i, j, tour)
            distanciaActual = calculaDistancia(inicial, final, tour, matriz)
            triplet = (i, j, distanciaActual)
            sucesores.append(triplet)
            swap(i, j, tour)
    return sucesores

## Ejecuta el algoritmo de hillclimber.
# @param inicial índice del pueblo inicial
# @param final índice del pueblo final
# @param tour índices de los pueblos entre el inicial y el final
# @param matriz de distancias
# @return mejor tour encontrado.
def hillClimber(inicial, final, tour, matriz):
    cambios = True
    while cambios:
        dact = calculaDistancia(inicial, final, tour, matriz)
        sucs = generaSucesores(inicial, final, tour, matriz)
        menor = min(sucs, key = lambda x : x[2])
        if menor[2] < dact:
            swap(menor[0], menor[1], tour)
        else:
            cambios = False
    return tour

## Dado un tour en números y la lista total de pueblos, devuelve la lista de
# pueblos que representa ese tour (en el mismo orden).
# @param tour lista parcial de pueblos con string.
# @param pueblos lista total de pueblos con string.
# @return lista 'tour' con sus elementos transformados a números.
def convierteNumPueblo(tour, pueblos):
    retVal = []
    for indice in tour:
        retVal.append(pueblos[indice])
    return retVal

## Método principal
def main():
    total = leeEjemplar("input/pueblos")
    print("Ingrese el nombre del ejemplar")
    nomEj = input()
    ejemplar = leeEjemplar("input/"+nomEj)
    ejNum = transformaEjemplar(ejemplar, total)
    puebloIni = ejNum[0]
    puebloFin = ejNum[len(ejNum)-1]
    del ejNum[len(ejNum)-1]
    del ejNum[0]
    matDist = construyeMatriz("input/matriz_distancias.csv")
    print("Ingrese el número de iteraciones a realizar.")
    iteraciones = int(input())
    mejorTour = permutacion(ejNum.copy())
    dmt = calculaDistancia(puebloIni, puebloFin, mejorTour, matDist) #distancia del mejor tour
    for i in range(iteraciones):
        permAct = permutacion(ejNum.copy())
        tourAct = hillClimber(puebloIni, puebloFin, permAct, matDist)
        dta = calculaDistancia(puebloIni, puebloFin, tourAct, matDist)
        if dta < dmt:
            mejorTour.clear()
            mejorTour = tourAct.copy()
            dmt = dta
    mejorTour.insert(0, puebloIni)
    mejorTour.append(puebloFin)
    mtStr = convierteNumPueblo(mejorTour, total)
    print("Mejor tour obtenido:")
    print(mtStr)
    print("Distancia del tour:", dmt)

main()
