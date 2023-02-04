package no.sandramoen.ggj2023oslo.actors;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;

import no.sandramoen.ggj2023oslo.actors.utils.BaseActor;

public class List extends BaseActor {

    private Array<Element> elements;
    private int maxCapacity;

    public List(float x, float y, Stage stage) {
        super(x, y, stage);
        loadImage("whitePixel");
        setSize(4, 32);
        centerAtPosition(x, y);
        setBoundaryRectangle();

        elements = new Array<>();
    }

    public void setMaxCapacity(float elementHeight) {
        maxCapacity = (int) (getHeight() / elementHeight);
    }

    public boolean isMaxCapacity() {
        return elements.size == maxCapacity;
    }

    public boolean tryInsertOnTop(Element element) {
        if (isMaxCapacity())
            return false;

        element.isActive = false;
        elements.insert(elements.size, element);
        setElementsPosition();

        return true;
    }

    public boolean tryInsertOnBottom(Element element) {
        if (isMaxCapacity())
            return false;

        element.isActive = false;
        elements.insert(0, element);
        setElementsPosition();

        return true;
    }

    private void setElementsPosition() {
        for (int i = 0; i < elements.size; i++) {
            elements.get(i).addAction(Actions.moveTo(
                    getX(),
                    (i * elements.get(i).getHeight()) + (getY() + getHeight() / 2) - (elements.size * elements.get(i).getHeight() / 2),
                    .5f
            ));
        }
    }
}
