
import math

def geodesica(latitud1, longitud1, latitud2, longitud2, radio):
    conversor = math.pi/180
    deltaLat = math.fabs(latitud1-latitud2)
    deltaLong = math.fabs(longitud1-longitud2)
    a = math.sin((deltaLat*conversor)/2)**2 + math.cos(latitud1*conversor) * math.cos(latitud2*conversor) * (math.sin((deltaLong*conversor)/2)**2)
    b = 2*math.asin(math.sqrt(a))
    return radio*b

def construyeCoordenadas(nombreArchivo):
    archivoEntrada = open(nombreArchivo, "r")
    pueblos = []
    for linea in archivoEntrada:
        listaLocal = linea.lstrip('\n').split(",")
        pueblos.append((listaLocal[0], float(listaLocal[1]), float(listaLocal[2])))
    return pueblos

def main1():
    radioTierra = 6371
    pueblos = construyeCoordenadas("input/coordenadas.csv")
    matrizDist = []
    for i in range(len(pueblos)):
        vectorLocal = []
        for j in range(len(pueblos)):
            vectorLocal.append(0)
        matrizDist.append(vectorLocal)

    for i in range(len(pueblos)):
        for j in range(len(pueblos)):
            if i != j:
                p1 = pueblos[i]
                p2 = pueblos[j]
                matrizDist[i][j] = geodesica(p1[1], p1[2], p2[1], p2[2], radioTierra)
    archivoSalida = open("input/matriz_distancias.csv", "w")
    linea1 = "nulo,"
    for pueblo in pueblos:
        linea1 += pueblo[0]+","
    archivoSalida.write(linea1[0:len(linea1)-1]+"\n")
    for i in range(len(matrizDist)):
        vec = matrizDist[i]
        siguiente = pueblos[i][0]+","
        for valor in vec:
            siguiente += str(valor)+","
        archivoSalida.write(siguiente[0:len(siguiente)-1]+"\n")
    archivoSalida.close()
# fin main1

    
main1()
