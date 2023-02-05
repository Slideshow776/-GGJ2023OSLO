package no.sandramoen.ggj2023oslo.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.utils.Align;

import no.sandramoen.ggj2023oslo.actors.map.TiledMapActor;
import no.sandramoen.ggj2023oslo.actors.utils.BaseActor;

public class Element extends BaseActor {
    public boolean isActive;
    public Types type;

    private enum Types {PINK, YELLOW, BLUE, GREEN}

    private BaseActor collisionBox;
    private RepeatAction rotateForever;
    private ParallelAction straighten;

    public Element(float x, float y, Stage stage) {
        super(x, y, stage);
        loadImage("element");
        setSize(4, 4);
        setBoundaryRectangle();
        setOrigin(Align.center);
        rotateForever = Actions.forever(Actions.parallel(
                Actions.rotateBy(20f, 2f),
                Actions.sequence(
                        Actions.scaleTo(.9f, .9f, 1f),
                        Actions.scaleTo(1f, 1f, 1f)
                )
        ));
        addAction(rotateForever);

        straighten = Actions.parallel(
                Actions.rotateTo(0, 2f, Interpolation.bounce),
                Actions.scaleTo(1, 1, 0, Interpolation.bounce)
        );

        setType();

        setCollisionBox();
    }

    public void stopRotation() {
        removeAction(rotateForever);
        addAction(straighten);
    }

    public void stopStraighten() {
        removeAction(straighten);
    }

    public BaseActor getCollisionBox() {
        collisionBox.setPosition(
                getX() + getWidth() / 2 - collisionBox.getWidth() / 2,
                getY() + getHeight() / 2 - collisionBox.getHeight() / 2
        );
        return collisionBox;
    }

    private void setType() {
        int random = MathUtils.random(0, 3);
        if (random == 0) {
            type = Types.PINK;
            setColor(new Color(0.776f, 0.318f, 0.592f, 1f));
        } else if (random == 1) {
            type = Types.YELLOW;
            setColor(new Color(0.871f, 0.62f, 0.255f, 1f));
        } else if (random == 2) {
            type = Types.BLUE;
            setColor(new Color(0.451f, 0.745f, 0.827f, 1f));
        } else if (random == 3) {
            type = Types.GREEN;
            setColor(new Color(0.659f, 0.792f, 0.345f, 1f));
        }
    }

    private void shakeCamera(float duration) {
        isShakyCam = true;
        new BaseActor(0f, 0f, getStage()).addAction(Actions.sequence(
                Actions.delay(duration),
                Actions.run(() -> {
                    isShakyCam = false;
                    TiledMapActor.centerPositionCamera(getStage());
                })
        ));
    }

    private void setCollisionBox() {
        int scale = 4;
        collisionBox = new BaseActor(0, 0, getStage());
        collisionBox.setSize(getWidth() * scale, getHeight() * scale);
        collisionBox.setPosition(
                getWidth() / 2 - collisionBox.getWidth() / 2,
                getHeight() / 2 - collisionBox.getHeight() / 2
        );
        collisionBox.setBoundaryRectangle();
        addActor(collisionBox);
    }
}
