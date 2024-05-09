import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * Un modelo sencillo de zorro.
 * Los zorros nacen, se mueven, comen conejos y mueren.
 * 
 * @author David J. Barnes and Michael Kolling
 * @author Traducción: Maximiliano A. Eschoyez
 * @version 2006.03.30
 */
public class Zorro extends Animal
{
    // Características compartidas por todos los zorros (campos estáticos).
    
    // La edad en que un zorro comienza a reproducirse
    private static final int EDAD_DE_REPRODUCCION = 10;
    // La edad que puede vivir un zorro.
    private static final int EDAD_MAX = 150;
    // La probabilidad de reproducción de un zorro.
    private static final double PROBABILIDAD_DE_REPRODUCCION = 0.09;
    // El número máximo de nacimientos.
    private static final int MAXIMO_TAMANIO_DE_CAMADA = 3;
    // El valor alimentario de un conejo. Este es el número de pasos
    // que un zorro puede dar antes de necesitar comer otro conejo.
    private static final int VALOR_COMIDA_CONEJO = 4;
    // Un número aleatorio para controlar la reproducción.
    private static final Random rand = new Random();
    
    // Características individuales (campos de instancia).

    // Nivel de comida del zorro, que se incrementa al comer conejos.
    private int nivelDeComida;

    /**
     * Crea un nuevo zorro. Se puede crear un zorro con edad
     * cero (un nuevo nacimiento y no tiene hambre) o con edad por azar.
     * 
     * @param edadPorAzar If true, the fox will have random edad and hunger level.
     */
    public Zorro(boolean edadPorAzar)
    {
        super();
        if(edadPorAzar) {
            setEdad(rand.nextInt(EDAD_MAX));
            nivelDeComida = rand.nextInt(VALOR_COMIDA_CONEJO);
        }
        else {
            // Dejar edad en cero
            nivelDeComida = VALOR_COMIDA_CONEJO;
        }
    }
    
    /**
     * Esto es lo que hace el zorro la mayor parte del tiempo: caza
     * conejos. En el proceso, puede reproducirse, morir de hambre
     * o morir de viejo.
     * @param campoActual El campo actualmente ocupado.
     * @param campoActualizado El campo al que se traslada.
     * @param nuevosAnimales Una lista en la que se agregan los nuevos
     *                  zorros que nacen.
     */
    public void act(Campo campoActual, Campo campoActualizado, List<Animal> nuevosAnimales)
    {
        incrementarEdad();
        incrementarHambre();
        if(estaVivo()) {
            // Nacieron nuevos zorros en direcciones adyacentes.
            int nacimientos = reproducir();
            for(int n = 0; n < nacimientos; n++) {
                Zorro nuevoZorro = new Zorro(false);
                nuevosAnimales.add(nuevoZorro);
                nuevoZorro.setUbicacion(
                        campoActualizado.direccionAdyacentePorAzar(getUbicacion()));
                campoActualizado.ubicar(nuevoZorro);
            }
            // Se mueve hacia la fuente de comida, si es que la encuentra.
            Ubicacion nuevaUbicacion = buscarComida(campoActual, getUbicacion());
            if(nuevaUbicacion == null) {  // no se encontró comida - se mueve aleatoriamente
                nuevaUbicacion = campoActualizado.direccionAdyacenteLibre(getUbicacion());
            }
            if(nuevaUbicacion != null) {
                setUbicacion(nuevaUbicacion);
                campoActualizado.ubicar(this);  // establece la posición
            }
            else {
                // no puede moverse ni estar, superpoblación, todas las
                // direcciones están ocupadas
                setMuerto();
            }
        }
    }
    
    /**
     * Aumenta la edad.
     * Podría dar por resultado la muerte del zorro.
     */
    private void incrementarEdad()
    {
        setEdad(getEdad() + 1);
        if(getEdad() > EDAD_MAX) {
            setMuerto();
        }
    }
    
    /**
     * Hacer más hambriento a este zorro.
     * Podría dar por resultado la muerte del zorro.
     */
    private void incrementarHambre()
    {
        nivelDeComida--;
        if(nivelDeComida <= 0) {
            setMuerto();
        }
    }
    
    /**
     * Decirle al zorro que busque conejos adyacentes a su ubicacion actual.
     * Sólo come el primer conejo que encuentra vivo.
     * @param campo El campo en el que debe buscar.
     * @param ubicacion El lugar del campo en el que está ubicado.
     * @return El lugar donde encontró comida o null si no encontró.
     */
    private Ubicacion buscarComida(Campo campo, Ubicacion ubicacion)
    {
        Iterator<Ubicacion> direccionesAdyacentes =
                          campo.direccionesAdyacentes(ubicacion);
        while(direccionesAdyacentes.hasNext()) {
            Ubicacion lugar = direccionesAdyacentes.next();
            Animal animal = campo.getAnimalEn(lugar);
            if(animal instanceof Conejo) {
                Conejo conejo = (Conejo) animal;
                if(conejo.estaVivo()) { 
                    conejo.setMuerto();
                    nivelDeComida = VALOR_COMIDA_CONEJO;
                    return lugar;
                }
            }
        }
        return null;
    }
        
    /**
     * Genera un número que representa el número de nacimientos,
     * si es que el zorro se puede reproducir.
     * @return El número de nacimientos (puede ser cero).
     */
    private int reproducir()
    {
        int nacimientos = 0;
        if(sePuedeReproducir() && rand.nextDouble() <= PROBABILIDAD_DE_REPRODUCCION) {
            nacimientos = rand.nextInt(MAXIMO_TAMANIO_DE_CAMADA) + 1;
        }
        return nacimientos;
    }
    
    /**
     * @return Un texto que representa al zorro.
     */
    public String toString()
    {
        return "Zorro, edad " + getEdad();
    }

    /**
     * El zorro se puede reproducir si alcanzó la edad de reproducción.
     */
    private boolean sePuedeReproducir()
    {
        return getEdad() >= EDAD_DE_REPRODUCCION;
    }
}
