import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.HashMap;

/**
 * Un visor gráfico de la malla de simulación.
 * El visor muestra para cada ubicación un rectángulo coloreado
 * que representa su contenido. Utiliza un color de fondo por
 * defecto. Los colores para cada tipo de especie se pueden
 * definir utilizando el método setColor.
 * 
 * @author David J. Barnes and Michael Kolling
 * @author Traducción: Maximiliano A. Eschoyez
 * @version 2006.03.30
 */
public class VisorDelSimulador extends JFrame
{
    // Color utilizado para las ubicaciones vacías.
    private static final Color COLOR_VACIO = Color.white;

    // Color utilizado para los objetos que no tienen definido un color.
    private static final Color COLOR_DESCONOCIDO = Color.gray;

    private final String PREFIJO_DE_PASO = "Paso: ";
    private final String PREFIJO_DE_POBLACION = "Poblacion: ";
    private JLabel etiquetaDePaso, poblacion;
    private VisorDeCampo visorDeCampo;
    
    // Un mapa para almacenar los colores de los participantes de la simulación
    private HashMap<Class, Color> colores;
    // Un objeto para el cómputo y almacenamiento de estadísticas.
    private EstadisticasDelCampo estadisticas;

    /**
     * Crea un visor del largo y ancho indicados.
     * @param largo El largo del campo de simulacion.
     * @param ancho El ancho del campo de simulacion.
     */
    public VisorDelSimulador(int largo, int ancho)
    {
        estadisticas = new EstadisticasDelCampo();
        colores = new HashMap<Class, Color>();

        setTitle("Simulacion de zorros y conejos");
        etiquetaDePaso = new JLabel(PREFIJO_DE_PASO, JLabel.CENTER);
        poblacion = new JLabel(PREFIJO_DE_POBLACION, JLabel.CENTER);
        
        setLocation(100, 50);
        
        visorDeCampo = new VisorDeCampo(largo, ancho);

        Container contents = getContentPane();
        contents.add(etiquetaDePaso, BorderLayout.NORTH);
        contents.add(visorDeCampo, BorderLayout.CENTER);
        contents.add(poblacion, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }
    
    /**
     * Define el color a utilizar para la clasa de animal dada.
     * @param claseAnimal El objeto animal de la clase.
     * @param color El color a utilizar para el animal de la clase dada.
     */
    public void setColor(Class claseAnimal, Color color)
    {
        colores.put(claseAnimal, color);
    }

    /**
     * @return El color utilizado para el animal de la clase dada.
     */
    private Color getColor(Class claseAnimal)
    {
        Color col = colores.get(claseAnimal);
        if(col == null) {
            // no se definió color para esta clase
            return COLOR_DESCONOCIDO;
        }
        else {
            return col;
        }
    }

    /**
     * Muestra el estado actual del campo.
     * @param paso En qué paso de iteración se encuentra.
     * @param campo El campo cuye estado debe mostrarse.
     */
    public void mostrarEstado(int paso, Campo campo)
    {
        if(!isVisible())
            setVisible(true);
            
        etiquetaDePaso.setText(PREFIJO_DE_PASO + paso);
        estadisticas.inicializar();
        
        visorDeCampo.prepararParaPintar();

        for(int fila = 0; fila < campo.getLargo(); fila++) {
            for(int columna = 0; columna < campo.getAncho(); columna++) {
                Animal animal = campo.getAnimalEn(fila, columna);
                if(animal != null) {
                    estadisticas.incrementarContador(animal.getClass());
                    visorDeCampo.dibujarMarca(columna, fila, getColor(animal.getClass()));
                }
                else {
                    visorDeCampo.dibujarMarca(columna, fila, COLOR_VACIO);
                }
            }
        }
        estadisticas.cuentaFinalizada();

        poblacion.setText(PREFIJO_DE_POBLACION + estadisticas.getDetallesDePoblacion(campo));
        visorDeCampo.repaint();
    }

    /**
     * Determina si la simulacion debe continuar.
     * @return true Si sigue viva mas de una especie.
     */
    public boolean esViable(Campo campo)
    {
        return estadisticas.esViable(campo);
    }
    
    /**
     * Provee una vista grafica de un campo rectangular. Esta es
     * una clase anidada (una clase definida dentro de otra) que
     * define un componente ajustado a la interfaz del usuario.
     * Este componente muestra el campo.
     * Esto es un topico avanzado sobre interfaz de usuario - puede
     * ignorarse para el caso de este proyecto.
     */
    private class VisorDeCampo extends JPanel
    {
        private final int FACTOR_DE_ESCALA_DEL_VISOR_DE_MALLA = 6;

        private int anchoDeMalla, largoDeMalla;
        private int escalaX, escalaY;
        Dimension tamanio;
        private Graphics g;
        private Image imagenDelCampo;

        /**
         * Crea un nuevo componente de VisorDeCampo.
         */
        public VisorDeCampo(int largo, int ancho)
        {
            largoDeMalla = largo;
            anchoDeMalla = ancho;
            tamanio = new Dimension(0, 0);
        }

        /**
         * Indica a la IGU el tamaño que debe tener.
         */
        public Dimension getPreferredSize()
        {
            return new Dimension(anchoDeMalla * FACTOR_DE_ESCALA_DEL_VISOR_DE_MALLA,
                                 largoDeMalla * FACTOR_DE_ESCALA_DEL_VISOR_DE_MALLA);
        }

        /**
         * Prepara para un nuevo ciclo de pintura. Como el componente
         * puede redimensionarse, calcula nuevamente el factor de escala.
          */
        public void prepararParaPintar()
        {
            if(! tamanio.equals(getSize())) {  // si el tamaño a cambiado...
                tamanio = getSize();
                imagenDelCampo = visorDeCampo.createImage(tamanio.width, tamanio.height);
                g = imagenDelCampo.getGraphics();

                escalaX = tamanio.width / anchoDeMalla;
                if(escalaX < 1) {
                    escalaX = FACTOR_DE_ESCALA_DEL_VISOR_DE_MALLA;
                }
                escalaY = tamanio.height / largoDeMalla;
                if(escalaY < 1) {
                    escalaY = FACTOR_DE_ESCALA_DEL_VISOR_DE_MALLA;
                }
            }
        }
        
        /**
         * Pinta la ubicación con el color indicado.
         */
        public void dibujarMarca(int x, int y, Color color)
        {
            g.setColor(color);
            g.fillRect(x * escalaX, y * escalaY, escalaX-1, escalaY-1);
        }

        /**
         * El componente de visor de campo necesita redibujarse. Copia
         * la imagen interna en la pantalla.
         */
        public void paintComponent(Graphics g)
        {
            if(imagenDelCampo != null) {
                Dimension tamanioActual = getSize();
                if(tamanio.equals(tamanioActual)) {
                    g.drawImage(imagenDelCampo, 0, 0, null);
                }
                else {
                    // Escalar la imagen previa.
                    g.drawImage(imagenDelCampo, 0, 0, tamanioActual.width, tamanioActual.height, null);
                }
            }
        }
    }
}
