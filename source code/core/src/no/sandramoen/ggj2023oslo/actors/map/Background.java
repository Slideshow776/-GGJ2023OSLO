package no.sandramoen.ggj2023oslo.actors.map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import no.sandramoen.ggj2023oslo.actors.utils.BaseActor;
import no.sandramoen.ggj2023oslo.utils.BaseGame;

public class Background extends BaseActor {
    private Array<TextureAtlas.AtlasRegion> animationImages = new Array();

    public Background(float x, float y, Stage stage) {
        super(x, y, stage);
        animationImages.add(BaseGame.textureAtlas.findRegion("grass"));
        animation = new Animation(2f, animationImages, Animation.PlayMode.LOOP);
        setAnimation(animation);/*
        setSize(TiledMapActor.mapWidth, TiledMapActor.mapHeight);*/
    }
}
