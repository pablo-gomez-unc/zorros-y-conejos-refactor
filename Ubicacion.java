/**
 * Representa una posición en una malla rectangular.
 * 
 * @author David J. Barnes and Michael Kolling
 * @author Traducción: Maximiliano A. Eschoyez
 * @version 2006.03.30
 */
public class Ubicacion
{
    // Fila y columna de las posiciones.
    private int fila;
    private int columna;

    /**
     * Representa una fila y una columna.
     * @param fila La fila.
     * @param columna La columna.
     */
    public Ubicacion(int fila, int columna)
    {
        this.fila = fila;
        this.columna = columna;
    }
    
    /**
     * Implementa la igualdad del contenido.
     */
    public boolean equals(Object obj)
    {
        if(obj instanceof Ubicacion) {
            Ubicacion otro = (Ubicacion) obj;
            return fila == otro.getFila() && columna == otro.getColumna();
        }
        else {
            return false;
        }
    }
    
    /**
     * Devuelve un texto de la forma fila,columna
     * @return Un texto representando la posición.
     */
    public String toString()
    {
        return fila + "," + columna;
    }
    
    /**
     * Usa los 16 bits superiores para el valor de fila y los inferiores
     * para la columna. Excepto mallas muy grandes, esto debería producir
     * un código hash único para cada par (fila, columna).
     * @return Un código hash para la posición.
     */
    public int hashCode()
    {
        return (fila << 16) + columna;
    }
    
    /**
     * @return La fila.
     */
    public int getFila()
    {
        return fila;
    }
    
    /**
     * @return La columna.
     */
    public int getColumna()
    {
        return columna;
    }
}
