import java.awt.Color;

/**
 * Provee un contador para cada participante de la simulación.
 * Esto incluye un texto identificador y la cantidad de
 * participantes de este tipo que existen en cada momento
 * de la simulación.
 * 
 * @author David J. Barnes and Michael Kolling
 * @author Traducción: Maximiliano A. Eschoyez
 * @version 2006.03.30
 */
public class Contador
{
    // Un nombre para este tipo de participante de la simulación
    private String nombre;
    // Cantidad de este tipo existente en la simulación
    private int cantidad;

    /**
     * Provee un nombre para uno de los tipos de la simulación.
     * @param nombre Un nombre, por ej. "Zorro".
     */
    public Contador(String nombre)
    {
        this.nombre = nombre;
        cantidad = 0;
    }
    
    /**
     * @return Descripción corta de este tipo.
     */
    public String getNombre()
    {
        return nombre;
    }

    /**
     * @return La cantidad actual de este tipo.
     */
    public int getCantidad()
    {
        return cantidad;
    }

    /**
     * Incrementar la cantidad en uno.
     */
    public void incrementar()
    {
        cantidad++;
    }
    
    /**
     * Reiniciar la cantidad a cero.
     */
    public void reiniciar()
    {
        cantidad = 0;
    }
}
