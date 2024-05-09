import java.util.List;

public class Hierba extends Animal {
    public Hierba() {
    }

    @Override
    public void act(Campo campoActual, Campo campoActualizado, List<Animal> nuevosAnimales) {
        if (estaVivo()) campoActualizado.ubicar(this);
    }
}
