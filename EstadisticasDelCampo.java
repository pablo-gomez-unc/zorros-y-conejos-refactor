import java.awt.Color;
import java.util.HashMap;

/**
 * Esta clase recolecta y provee datos estadístidos respecto al estado
 * del campo. Es flexible: creará y mantendrá un contador para cualquier
 * clase de objeto que encuentre dentro del campo
 * 
 * @author David J. Barnes and Michael Kolling
 * @author Traducción: Maximiliano A. Eschoyez
 * @version 2006.03.30
 */
public class EstadisticasDelCampo
{
    // Contadores para cada tipo de entidad (zorro, conejo, etc.) en la simulación.
    private HashMap<Class, Contador> contadores;
    // Indicador de que los contadores están actualizados.
    private boolean cuentaValida;

    /**
     * Construye un objecto EstadisticasDelCampo.
     */
    public EstadisticasDelCampo()
    {
        // Establece una colección de contadores para cada tipo de
        // animal que podemos encontrar
        contadores = new HashMap<Class, Contador>();
        cuentaValida = true;
    }

    /**
     * Toma detalles de qué está ocurriendo en el campo.
     * @return Un texto describiendo que está ocurriendo en el campo.
     */
    public String getDetallesDePoblacion(Campo campo)
    {
        StringBuffer buffer = new StringBuffer();
        if(!cuentaValida) {
            generarCuentas(campo);
        }
        for(Class key : contadores.keySet()) {
            Contador info = contadores.get(key);
            buffer.append(info.getNombre());
            buffer.append(": ");
            buffer.append(info.getCantidad());
            buffer.append(' ');
        }
        return buffer.toString();
    }
    
    /**
     * Establece como no válidas las estadísticas actuales; reinicia
     * todos los contadores a cero.
     */
    public void inicializar()
    {
        cuentaValida = false;
        for(Class key : contadores.keySet()) {
            Contador contador = contadores.get(key);
            contador.reiniciar();
        }
    }

    /**
     * Incrementa el contador para una clase de animal.
     * @param claseAnimal La clase de animal a incrementar.
     */
    public void incrementarContador(Class claseAnimal)
    {
        Contador contador = contadores.get(claseAnimal);
        if(contador == null) {
            // Si no tenemos un contador para esta especie,
            // creamos uno.
            contador = new Contador(claseAnimal.getName());
            contadores.put(claseAnimal, contador);
        }
        contador.incrementar();
    }

    /**
     * Indica que la cuenta de un animal se ha completado.
     */
    public void cuentaFinalizada()
    {
        cuentaValida = true;
    }

    /**
     * Determina si la simulación todavía es viable. Esto es,
     * se puede continuar ejecutando.
     * @return true Si existe más de una especie viva.
     */
    public boolean esViable(Campo campo)
    {
        // Cuántos contadores son distintos de cero.
        int distintoCero = 0;
        if(!cuentaValida) {
            generarCuentas(campo);
        }
        for(Class key : contadores.keySet()) {
            Contador info = contadores.get(key);
            if(info.getCantidad() > 0) {
                distintoCero++;
            }
        }
        return distintoCero > 1;
    }
    
    /**
     * Genera cuentas del número de zorros y conejos.
     * Estos contadores no se mantienen actualizados cuando
     * se ubican zorros y conejos en el campo, se actualizan
     * únicamente cuando se pide esta información.
     * @param campo El campo sobre el cual generar las estadísticas.
     */
    private void generarCuentas(Campo campo)
    {
        inicializar();
        for(int fila = 0; fila < campo.getLargo(); fila++) {
            for(int columna = 0; columna < campo.getAncho(); columna++) {
                Animal animal = campo.getAnimalEn(fila, columna);
                if(animal != null) {
                    incrementarContador(animal.getClass());
                }
            }
        }
        cuentaValida = true;
    }
}
