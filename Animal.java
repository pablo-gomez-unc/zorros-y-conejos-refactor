import java.util.List;

/**
 * Animal es una superclase abstracta para los animales.
 * Provee características comunes a todos los animales,
 * como ser la posición y la edad.
 * 
 * @author David J. Barnes and Michael Kolling
 * @author Traducción: Maximiliano A. Eschoyez
 * @version 2006.03.30
 */
public abstract class Animal
{
    // La edad del animal.
    private int edad;
    // Si el animal está vivo o no.
    private boolean vive;
    // La posición del animal.
    private Ubicacion ubicacion;

    /**
     * Crea un nuevo animal con edad cero (un nuevo nacimiento).
     */
    public Animal()
    {
        edad = 0;
        vive = true;
    }
    
    /**
     * Hace actuar al animal - esto es: hacer que haga lo
     * que necesita/desea hacer.
     * @param campoActual El campo actualmente ocupado.
     * @param campoActualizado El campo hacia donde se mueve.
     * @param nuevosAnimales Una lista donde se agregan los animales
     *        recien nacidos.
     */
    abstract public void act(Campo campoActual, 
                             Campo campoActualizado, List<Animal> nuevosAnimales);
    
    /**
     * Verifica si el animal esta vivo o no.
     * @return True si el animal sigue vivo.
     */
    public boolean estaVivo()
    {
        return vive;
    }

    /**
     * Avisa al animal que ahora está muerto :(
     */
    public void setMuerto()
    {
        vive = false;
    }
    
    /**
     * Devuelve la edad del animal.
     * @return La edad del animal.
     */
    public int getEdad()
    {
        return edad;
    }

    /**
     * Establece la edad del animal.
     * @param edad La edad del animal.
     */
    public void setEdad(int edad)
    {
        this.edad = edad;
    }
    
    /**
     * Devuelve la posicion del animal.
     * @return La posicion del animal.
     */
    public Ubicacion getUbicacion()
    {
        return ubicacion;
    }

    /**
     * Establece la posicion del animal.
     * @param fila La coordenada vertical de la posicion.
     * @param columna La coordenada horizontal de la posicion.
     */
    public void setUbicacion(int fila, int columna)
    {
        this.ubicacion = new Ubicacion(fila, columna);
    }

    /**
     * Establece la posicion del animal.
     * @param posicion La posicion del animal.
     */
    public void setUbicacion(Ubicacion ubicacion)
    {
        this.ubicacion = ubicacion;
    }
}
