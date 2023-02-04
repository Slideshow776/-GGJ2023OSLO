package no.sandramoen.ggj2023oslo.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;

import no.sandramoen.ggj2023oslo.actors.utils.BaseActor;

public class ListB extends BaseActor {
    public ListB(float x, float y, Stage stage) {
        super(x, y, stage);
        loadImage("whitePixel");
        setSize(4, 16);
        setColor(Color.FIREBRICK);
        centerAtPosition(x, y);
    }
}
