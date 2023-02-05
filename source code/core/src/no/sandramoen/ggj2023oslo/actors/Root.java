package no.sandramoen.ggj2023oslo.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import no.sandramoen.ggj2023oslo.actors.utils.BaseActor;
import no.sandramoen.ggj2023oslo.utils.GameUtils;

public class Root extends BaseActor {
    private int growthDirection = 1;
    private Vector2 size;
    private boolean isGrowingLeft;
    private Array<Root> roots;

    public Root(float x, float y, Stage stage, boolean isGrowingLeft) {
        super(x, y, stage);
        this.isGrowingLeft = isGrowingLeft;
        loadImage("roots");
        setSize(5, 1);/*
        centerAtPosition(x, y);*/
        setOrigin(Align.left);/*
        setColor(Color.BROWN);*/

        if (!isGrowingLeft) {
            rotateBy(180);
            setPosition(getX() + getWidth() + 4, getY());
        }

        size = new Vector2(getWidth(), getHeight());
        // setDebug(true);

        roots = new Array<>();
        roots.add(this);
    }

    public void grow() {
        addAction(Actions.scaleBy(1, 1, 2, Interpolation.smoother));
        // Root root = new Root(getX(), getY(), getStage(), isGrowingLeft);
        /*float growX = .2f;
        float growY = .1f;
        float duration = 2f;
        size.x *= (1 + growX);
        size.y *= (1 + growY);

        for (Root root : roots)
            root.addAction(Actions.scaleBy(growX, growY, duration, Interpolation.smoother));

        growChild(this, size.x, size.y);*/

        // System.out.println(size.x + ", " + size.y);
    }

    public void growChild(Root parent, float width, float height) {
        int direction = -1;
        /*if (parent.isGrowingLeft) direction *= -1;*/

        Root root = null;
        for (int i = 0; i < MathUtils.random(0, 3); i++) {
            root = new Root(
                    MathUtils.random(parent.getX(), parent.getX() + getWidth() * direction),
                    MathUtils.random(parent.getY(), parent.getY() + getHeight() * direction),
                    getStage(),
                    isGrowingLeft
            );
            root.setSize(root.getWidth() / 2, root.getHeight() / 2);
            root.addAction(Actions.parallel(
                    Actions.sequence(
                            Actions.scaleTo(0, 0, 0),
                            Actions.scaleTo(1, 1, 1)
                    ),
                    Actions.rotateBy(MathUtils.random(-45, 45), 1)
            ));
            root.setColor(GameUtils.randomLightColor());
            roots.add(root);
        }
        if (root != null)
            growChild(root, root.getWidth(), root.getHeight());
    }
}
