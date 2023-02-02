package no.sandramoen.ggj2023oslo.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import no.sandramoen.ggj2023oslo.actors.map.TiledMapActor;
import no.sandramoen.ggj2023oslo.actors.utils.BaseActor;

public class Player extends BaseActor {
    public boolean isDead;
    public boolean isMoving;

    private final float SPEED = .035f;
    private BaseActor collisionBox;

    public Player(float x, float y, Stage stage) {
        super(x, y, stage);
        loadImage("whitePixel");
        setColor(Color.MAGENTA);
        setSize(2, 4);
        setBoundaryRectangle();

        collisionBox = new BaseActor(0, 0, stage);
        collisionBox.setSize(1 / 2f, 1 / 2f);
        collisionBox.setPosition(
                getWidth() / 2 - collisionBox.getWidth() / 2,
                getHeight() / 3 - collisionBox.getHeight() / 2
        );
        collisionBox.setBoundaryRectangle();
        // collisionBox.setDebug(true);
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

}
