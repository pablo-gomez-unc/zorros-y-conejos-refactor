import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.awt.Color;

/**
 * Un modelo sencillo de simulación predador-presa con zorros y conejos
 * dentro de un campo cerrado.
 * 
 * @author David J. Barnes and Michael Kolling
 * @author Traducción: Maximiliano A. Eschoyez
 * @version 2006.03.30
 */
public class Simulador
{
    // Se definen las constantes que representan la información de
    // configuración para la simulación.
    // Ancho por defecto de la malla.
    private static final int ANCHO_POR_DEFECTO = 50;
    // Largo por defecto de la malla.
    private static final int LARGO_POR_DEFECTO = 50;
    // La probabilidad que se cree un zorro en cualquier posición de la malla.
    private static final double PROBABILIDAD_DE_CREACION_DEL_ZORRO = 0.015;
    // La probabilidad que se cree un conejo en cualquier posición de la malla.
    private static final double PROBABILIDAD_DE_CREACION_DEL_CONEJO = 0.065;
    private static final double PROBABILIDAD_DE_CREACION_DE_HIERBA = 0.02;


    // La lista de los animales en el campo
    private List<Animal> animales;
    // La lista de los animales recién nacidos
    private List<Animal> nuevosAnimales;
    // El estado actual del campo.
    private Campo campo;
    // Un segundo campo que se usa para construir el siguiente escenario
    // de la simulación.
    private Campo campoActualizado;
    // El paso actual de la simulación.
    private int paso;
    // Una vista gráfica de la simulación.
    private VisorDelSimulador visor;

    /**
     * Crea un campo de simulación del tamaño por defecto.
     */
    public Simulador()
    {
        this(LARGO_POR_DEFECTO, ANCHO_POR_DEFECTO);
    }
    
    /**
     * Crea un campo de simulación de un determinado tamaño.
     * @param largo El largo del campo. Debe ser mayor que cero.
     * @param ancho El ancho del campo. Debe ser mayor que cero.
     */
    public Simulador(int largo, int ancho)
    {
        if(ancho <= 0 || largo <= 0) {
            System.out.println("Las dimensiones deben ser mayores que cero.");
            System.out.println("Uso de valores por defecto.");
            largo = LARGO_POR_DEFECTO;
            ancho = ANCHO_POR_DEFECTO;
        }
        animales = new ArrayList<Animal>();
        nuevosAnimales = new ArrayList<Animal>();
        campo = new Campo(largo, ancho);
        campoActualizado = new Campo(largo, ancho);

        // Crea un visor del estado de cada ubicacin en el campo.
        visor = new VisorDelSimulador(largo, ancho);
        visor.setColor(Conejo.class, Color.orange);
        visor.setColor(Zorro.class, Color.blue);
        visor.setColor(Hierba.class, Color.green);
        // Establece un punto de inicio válido.
        inicializar();
    }
    
    /**
     * Ejecuta la simulación por un período razonablemente largo a partir
     * del estado actual, ej. 500 pasos.
     */
    public void ejecutarSimulacionLarga()
    {
        simular(500);
    }
    
    /**
     * Ejecuta la simulación un número determinado de pasos a partir
     * del estado actual.
     * Se detiene antes del número dado de pasos si deja de ser viable.
     * @param numeroDePasos El número de pasos a realizar.
     */
    public void simular(int numeroDePasos)
    {
        for(int paso = 1; paso <= numeroDePasos && visor.esViable(campo); paso++) {
            simularUnPaso();
        }
    }
    
    /**
     * Ejecuta un solo paso de la simulación a partir del estado actual.
     * Recorre el campo completo actualizando el estado de cada zorro
     * y cada conejo.
     */
    public void simularUnPaso()
    {
        paso++;
        nuevosAnimales.clear();
        
        // Deja actuar a todos loa animales
        for(Iterator<Animal> it = animales.iterator(); it.hasNext(); ) {
            Animal animal = it.next();
            animal.act(campo, campoActualizado, nuevosAnimales);
            // Remueve de la simulación a los animales muertos
            if(! animal.estaVivo()) {
                it.remove();
            }
        }
        // Agrega los animales recién nacidos a la lista.
        animales.addAll(nuevosAnimales);
        
        // Intercambia el campo y el campoActualizado al final del paso.
        Campo temp = campo;
        campo = campoActualizado;
        campoActualizado = temp;
        campoActualizado.limpiar();

        // Visualiza el nuevo campo en la pantalla.
        visor.mostrarEstado(paso, campo);
    }
        
    /**
     * Incializa la simulación en un punto de inicio.
     */
    public void inicializar()
    {
        paso = 0;
        animales.clear();
        nuevosAnimales.clear();
        campo.limpiar();
        campoActualizado.limpiar();
        poblar(campo);
        
        // Muestra el estado inicial en el visor.
        visor.mostrarEstado(paso, campo);
    }
    
    /**
     * Puebla un campo con zorros y conejos.
     * @param campo El campo que se poblará.
     */
    private void poblar(Campo campo)
    {
        Random rand = new Random();
        campo.limpiar();
        for(int fila = 0; fila < campo.getLargo(); fila++) {
            for (int columna = 0; columna < campo.getAncho(); columna++) {
                if (rand.nextDouble() <= PROBABILIDAD_DE_CREACION_DE_HIERBA) {
                    Hierba hierba = new Hierba();
                    hierba.setUbicacion(fila, columna);
                    animales.add(hierba);
                    campo.ubicar(hierba);
                } else if (rand.nextDouble() <= PROBABILIDAD_DE_CREACION_DEL_ZORRO) {
                    Zorro zorro = new Zorro(true);
                    zorro.setUbicacion(fila, columna);
                    animales.add(zorro);
                    campo.ubicar(zorro);
                } else if (rand.nextDouble() <= PROBABILIDAD_DE_CREACION_DEL_CONEJO) {
                    Conejo conejo = new Conejo(true);
                    conejo.setUbicacion(fila, columna);
                    animales.add(conejo);
                    campo.ubicar(conejo);
                }
                // de lo contrario, la ubicación queda vacía.
            }
        }
        Collections.shuffle(animales);
    }
}
