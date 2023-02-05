package no.sandramoen.ggj2023oslo.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import no.sandramoen.ggj2023oslo.actors.utils.BaseActor;

public class Vignette extends BaseActor {
    public Vignette(float x, float y, Stage stage) {
        super(x, y, stage);

        loadImage("vignette");
        setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        setTouchable(Touchable.disabled);
    }
}
