import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Representa una malla rectangular de posiciones de campo.
 * Cada posición puede contener sólo un animal.
 * 
 * @author David J. Barnes and Michael Kolling
 * @author Traducción: Maximiliano A. Eschoyez
 * @version 2006.03.30
 */
public class Campo
{
    private static final Random rand = new Random();
    
    // El largo y el ancho del campo.
    private int largo, ancho;
    // Almacenamiento de los animales.
    private Animal[][] campo;

    /**
     * Representa un campo de las dimensiones dadas.
     * @param largo El largo del campo.
     * @param ancho El ancho del campo.
     */
    public Campo(int largo, int ancho)
    {
        this.largo = largo;
        this.ancho = ancho;
        campo = new Animal[largo][ancho];
    }
    
    /**
     * Limpiar el campo.
     */
    public void limpiar()
    {
        for(int fila = 0; fila < largo; fila++) {
            for(int columna = 0; columna < ancho; columna++) {
                campo[fila][columna] = null;
            }
        }
    }
    
    /**
     * Coloca un animal en una posición dada.
     * Si ya existe un animal en esa posición, se perderá.
     * @param animal El animal a ubicar.
     */
    public void ubicar(Animal animal)
    {
        Ubicacion ubicacion = animal.getUbicacion();
        campo[ubicacion.getFila()][ubicacion.getColumna()] = animal;
    }
    
    /**
     * Devuelve el animal ubicado en la posicion dada, si hay uno.
     * @param posicion Ubicacion dentro del campo.
     * @return El animal ubicado en la posicion, o null si no hay uno.
     */
    public Animal getAnimalEn(Ubicacion ubicacion)
    {
        return getAnimalEn(ubicacion.getFila(), ubicacion.getColumna());
    }
    
    /**
     * Devuelve el animal ubicado en la posicion dada, si hay uno.
     * @param fila La fila deseada.
     * @param columna La columna deseada.
     * @return El animal ubicado en la posicion, o null si no hay uno.
     */
    public Animal getAnimalEn(int fila, int columna)
    {
        return campo[fila][columna];
    }
    
    /**
     * Genera aleatoriamente una posicion adyacente a la posicion
     * dada, o la misma posicion.
     * La posicion devuelta debera estar dentro de los limites
     * del campo.
     * @param ubicacion La posicion desde la cual se genera una adyacencia.
     * @return Una posicion valida dentro de la grilla del area. Este debe
     *         ser del mismo tipo de objecto que el parametro de la posicion.
     */
    public Ubicacion direccionAdyacentePorAzar(Ubicacion ubicacion)
    {
        int fila = ubicacion.getFila();
        int columna = ubicacion.getColumna();
        // Genera un desplazamiento de -1, 0, o +1 para la fila y columna dadas.
        int filaSiguiente = fila + rand.nextInt(3) - 1;
        int columnaSiguiente = columna + rand.nextInt(3) - 1;
        // Verifica si la nueva posicion esta fuera de los limites.
        if(filaSiguiente < 0 || filaSiguiente >= largo || columnaSiguiente < 0 || columnaSiguiente >= ancho) {
            return ubicacion;
        }
        else if(filaSiguiente != fila || columnaSiguiente != columna) {
            return new Ubicacion(filaSiguiente, columnaSiguiente);
        }
        else {
            return ubicacion;
        }
    }
    
    /**
     * Intenta encontrar una posicion libre adyancente a la posicion
     * actual. Si no existe, devuelve la posicion actual si esta libre.
     * Si esta ocupada, devuelve null.
     * La posicion devuelta debera estar entre los limites validos del
     * campo.
     * @param posicion La posicion para generar una adyacencia.
     * @return Una posicion valida dentro del area de la malla. Este puede
     *         ser el mismo objeto como el parametro de la posicion o null
     *         si todas las posiciones alrededor estan ocupadas.
     */
    public Ubicacion direccionAdyacenteLibre(Ubicacion ubicacion)
    {
        Iterator<Ubicacion> adyacente = direccionesAdyacentes(ubicacion);
        while(adyacente.hasNext()) {
            Ubicacion siguiente = adyacente.next();
            if(campo[siguiente.getFila()][siguiente.getColumna()] == null) {
                return siguiente;
            }
        }
        // Verifica si la posicion actual esta libre
        if(campo[ubicacion.getFila()][ubicacion.getColumna()] == null) {
            return ubicacion;
        } 
        else {
            return null;
        }
    }

    /**
     * Genera un iterador sobre una lista mezclada ("shuffled") de
     * posiciones adyacentes a la dada. Esta lista no contiene a la
     * posicion dada. Todas las posiciones estan dentro de la malla.
     * @param posicion La posicion para la que se generan las adyacencias.
     * @return Un iterador sobre las posiciones adyacentes a la dada.
     */
    public Iterator<Ubicacion> direccionesAdyacentes(Ubicacion ubicacion)
    {
        int fila = ubicacion.getFila();
        int columna = ubicacion.getColumna();
        List<Ubicacion> ubicaciones = new LinkedList<Ubicacion>();
        for(int deltaFila = -1; deltaFila <= 1; deltaFila++) {
            int filaSiguiente = fila + deltaFila;
            if(filaSiguiente >= 0 && filaSiguiente < largo) {
                for(int deltaColumna = -1; deltaColumna <= 1; deltaColumna++) {
                    int columnaSiguiente = columna + deltaColumna;
                    // Excluye posiciones no validas y la posicion original.
                    if(columnaSiguiente >= 0 && columnaSiguiente < ancho && (deltaFila != 0 || deltaColumna != 0)) {
                        ubicaciones.add(new Ubicacion(filaSiguiente, columnaSiguiente));
                    }
                }
            }
        }
        Collections.shuffle(ubicaciones, rand);
        return ubicaciones.iterator();
    }

    /**
     * Devuelve el largo del campo.
     * @return El largo del campo.
     */
    public int getLargo()
    {
        return largo;
    }
    
    /**
     * Devuelve el ancho del campo.
     * @return El ancho del campo.
     */
    public int getAncho()
    {
        return ancho;
    }
}
