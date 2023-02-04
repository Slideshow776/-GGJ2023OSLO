package no.sandramoen.ggj2023oslo.actors;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;

import no.sandramoen.ggj2023oslo.actors.utils.BaseActor;

public class List extends BaseActor {
    public boolean isScore;
    public final int NUM_SAME_TYPE = 3;

    private int maxCapacity;
    private Array<Element> elements;
    private BaseActor collisionBox;

    public List(float x, float y, Stage stage) {
        super(x, y, stage);
        loadImage("whitePixel");
        setSize(4, 32);
        centerAtPosition(x, y);
        setBoundaryRectangle();
        setCollisionBox();

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
        setElementsPositionWithDelay();
        checkNeighboursForScore();

        return true;
    }

    public boolean tryInsertOnBottom(Element element) {
        if (isMaxCapacity())
            return false;

        element.isActive = false;
        elements.insert(0, element);
        setElementsPositionWithDelay();
        checkNeighboursForScore();

        return true;
    }

    public BaseActor getCollisionBox() {
        collisionBox.setPosition(
                getX() + getWidth() / 2 - collisionBox.getWidth() / 2,
                getY() + getHeight() / 2 - collisionBox.getHeight() / 2
        );
        return collisionBox;
    }


    private void setCollisionBox() {
        int scale = 8;
        collisionBox = new BaseActor(0, 0, getStage());
        collisionBox.setSize(getWidth() * scale, getHeight() * scale);
        collisionBox.setPosition(
                getWidth() / 2 - collisionBox.getWidth() / 2,
                getHeight() / 2 - collisionBox.getHeight() / 2
        );
        collisionBox.setBoundaryRectangle();
        collisionBox.setDebug(true);
        addActor(collisionBox);
    }

    private void setElementsPositionWithDelay() {
        if (elements.isEmpty())
            return;

        float elementHeight = elements.get(0).getHeight();
        float listPosition = (getY() + getHeight() / 2);
        float listHeight = (elements.size * elementHeight / 2);

        for (int i = 0; i < elements.size; i++) {
            elements.get(i).addAction(Actions.moveTo(
                    getX(),
                    (i * elementHeight) + listPosition - listHeight,
                    MathUtils.random(.5f, 1f),
                    Interpolation.bounceOut
            ));
        }
    }

    private void setElementsPositionWithDelay(float duration) {
        new BaseActor(0, 0, getStage()).addAction(Actions.sequence(
                Actions.delay(duration),
                Actions.run(() -> setElementsPositionWithDelay())
        ));
    }

    private void checkNeighboursForScore() {
        if (elements.size < NUM_SAME_TYPE)
            return;

        boolean[] isSameType = new boolean[NUM_SAME_TYPE];
        for (int i = 0; i < elements.size - 1; i++) {
            boolean isNthInARow = true;

            if (i > elements.size - NUM_SAME_TYPE)
                break;

            for (int j = 0; j < isSameType.length; j++)
                isSameType[j] = elements.get(i).type == elements.get(i + j).type;

            for (boolean condition : isSameType)
                if (!condition)
                    isNthInARow = false;

            if (isNthInARow) {
                for (int j = 0; j < NUM_SAME_TYPE; j++)
                    elements.get(i + j).addAction(Actions.scaleTo(0, 0, 1));

                for (int j = 0; j < NUM_SAME_TYPE; j++)
                    elements.removeIndex(i);

                isScore = true;
                setElementsPositionWithDelay(.5f);
                break;
            }
        }
    }
}
