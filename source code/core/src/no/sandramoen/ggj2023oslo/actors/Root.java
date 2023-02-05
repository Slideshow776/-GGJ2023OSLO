package no.sandramoen.ggj2023oslo.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

import no.sandramoen.ggj2023oslo.actors.utils.BaseActor;

public class Root extends BaseActor {
    private int growthDirection = 1;
    private Vector2 size;

    public Root(float x, float y, Stage stage, boolean isGrowingLeft) {
        super(x, y, stage);
        loadImage("root");
        setSize(10, 2);
        centerAtPosition(x, y);
        setOrigin(Align.left);
        setColor(Color.BROWN);

        if (!isGrowingLeft) {
            rotateBy(180);
            setPosition(getX() + getWidth() + 4, getY());
        }

        size = new Vector2(getWidth(), getHeight());
    }

    public void grow() {
        float growX = .2f;
        float growY = .1f;
        float duration = 2f;

        addAction(Actions.scaleBy(growX, growY, duration, Interpolation.smoother));
        size.x *= (1 + growX);
        size.y *= (1 + growY);

        for (int i = 0; i < MathUtils.random(1, 3); i++) {

        }
        System.out.println(size.x + ", " + size.y);
    }
}
