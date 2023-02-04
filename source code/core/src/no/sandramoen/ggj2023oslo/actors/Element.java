package no.sandramoen.ggj2023oslo.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

import no.sandramoen.ggj2023oslo.actors.map.TiledMapActor;
import no.sandramoen.ggj2023oslo.actors.utils.BaseActor;

public class Element extends BaseActor {
    public boolean isDead;
    public boolean isActive;
    public Types type;

    private enum Types {RED, YELLOW, BLUE}

    public Element(float x, float y, Stage stage) {
        super(x, y, stage);
        loadImage("whitePixel");
        setSize(4, 4);
        setBoundaryRectangle();
        setOrigin(Align.center);

        setType();
        setDebug(true);
    }

    private void setType() {
        int random = MathUtils.random(0, 2);
        if (random == 0) {
            type = Types.RED;
            setColor(Color.RED);
        } else if (random == 1) {
            type = Types.YELLOW;
            setColor(Color.YELLOW);
        } else if (random == 2) {
            type = Types.BLUE;
            setColor(Color.CYAN);
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
}
