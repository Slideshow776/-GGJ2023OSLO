package no.sandramoen.ggj2023oslo.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import no.sandramoen.ggj2023oslo.actors.map.TiledMapActor;
import no.sandramoen.ggj2023oslo.actors.utils.BaseActor;

public class Player extends BaseActor {
    public boolean isDead;
    public boolean isMoving;

    private final float SPEED = 1f;
    private BaseActor collisionBox;

    public Player(float x, float y, Stage stage) {
        super(x, y, stage);
        loadImage("player");
        setBoundaryRectangle();

        collisionBox = new BaseActor(0, 0, stage);
        collisionBox.setSize(1 / 2f, 1 / 2f);
        collisionBox.setPosition(
                getWidth() / 2 - collisionBox.getWidth() / 2,
                getHeight() / 2 - collisionBox.getHeight() / 2
        );
        collisionBox.setBoundaryRectangle();
        collisionBox.setDebug(true);
        addActor(collisionBox);
        isShakyCam = true;
        System.out.println("isShakyCam: " + isShakyCam);
    }

    public BaseActor getCollisionBox() {
        collisionBox.setPosition(
                getX() + getWidth() / 2 - collisionBox.getWidth() / 2,
                getY() + getHeight() / 2 - collisionBox.getHeight() / 2
        );
        return collisionBox;
    }

    public void die() {
        isDead = true;
    }

<<<<<<< HEAD
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
=======
    private void shakeCameraABit() {
        isShakyCam = true;
        new BaseActor(0, 0, getStage()).addAction(Actions.sequence(
                Actions.delay(1),
                Actions.run(() -> {
                    isShakyCam = false;
                    TiledMapActor.centerCameraOnMap(getStage());
                })
        ));
    }

>>>>>>> 77fc0ed18aadbd09f2b051eac9902b08c27b6bf7
}
