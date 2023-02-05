package no.sandramoen.ggj2023oslo.screens.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import com.github.tommyettinger.textra.TypingLabel;

import no.sandramoen.ggj2023oslo.actors.Background;
import no.sandramoen.ggj2023oslo.actors.Element;
import no.sandramoen.ggj2023oslo.actors.List;
import no.sandramoen.ggj2023oslo.actors.Root;
import no.sandramoen.ggj2023oslo.actors.Vignette;
import no.sandramoen.ggj2023oslo.actors.map.ImpassableTerrain;
import no.sandramoen.ggj2023oslo.actors.map.TiledMapActor;
import no.sandramoen.ggj2023oslo.actors.particles.explosionEffect;
import no.sandramoen.ggj2023oslo.actors.utils.BaseActor;
import no.sandramoen.ggj2023oslo.screens.BaseScreen;
import no.sandramoen.ggj2023oslo.ui.MadeByLabel;
import no.sandramoen.ggj2023oslo.utils.BaseGame;
import no.sandramoen.ggj2023oslo.utils.GameUtils;

public class LevelScreen extends BaseScreen {
    private Array<ImpassableTerrain> impassables;
    private Element element;
    private List listA;
    private List listB;

    private Root rootA;
    private Root rootB;

    private boolean isGameOver;
    private Vector2 spawnPoint;
    private int score;

    private TypingLabel topLabel;
    private TypingLabel middleLabel;

    private TiledMapActor tilemap;
    private TiledMap currentMap;

    public LevelScreen(TiledMap tiledMap) {
        currentMap = tiledMap;
        this.tilemap = new TiledMapActor(currentMap, mainStage);

        new Background(0, 0, mainStage, new Vector3(.1f, .1f, .1f));
        initializeActors();
        initializeArt();
        initializeGUI();
        mapCenterCamera();
        GameUtils.playLoopingMusic(BaseGame.ambianceMusic);
        new Vignette(0, 0, uiStage);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void update(float delta) {
        if (!isGameOver) {
            if (listA.isMaxCapacity() && listB.isMaxCapacity()) {
                isGameOver = true;
                middleLabel.setVisible(true);
                middleLabel.restart();
            }

            if (checkAndUpdateScore(listA))
                rootA.grow();

            if (checkAndUpdateScore(listB))
                rootB.grow();
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 worldCoordinates = mainStage.getCamera().unproject(new Vector3(screenX, screenY, 0f));
        if (element.getCollisionBox().getBoundaryPolygon().contains(new Vector2(worldCoordinates.x, worldCoordinates.y))) {
            listA.appear();
            listB.appear();
            element.isActive = true;
            BaseGame.pickupSounds.get(MathUtils.random(0, BaseGame.pickupSounds.size - 1)).play(BaseGame.soundVolume);
            startEffect(worldCoordinates);
        }
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        element.isActive = false;
        if (isListCollisions()) {
            element.stopRotation();
            generateNewElement();
            BaseGame.placeSounds.get(MathUtils.random(0, BaseGame.placeSounds.size - 1)).play(BaseGame.soundVolume);
        } else {
            moveElementToSpawnPoint();
        }
        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 worldCoordinates = mainStage.getCamera().unproject(new Vector3(screenX, screenY, 0f));
        if (element.isActive)
            element.addAction(Actions.moveTo(worldCoordinates.x - element.getWidth() / 2, worldCoordinates.y - element.getHeight() / 2, .1f, Interpolation.smoother));
        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.ESCAPE || keycode == Keys.Q)
            Gdx.app.exit();
        else if (keycode == Keys.R)
            BaseGame.setActiveScreen(new LevelScreen(currentMap));
        /*else if (keycode == Keys.T)
            BaseGame.setActiveScreen(new LevelSelectScreen());*/
        /*else if (keycode == Keys.NUMPAD_0) {
            OrthographicCamera camera = (OrthographicCamera) mainStage.getCamera();
            camera.zoom += .1f;
        }*//* else if (keycode == Keys.NUM_3) {
            rootA.grow();
            rootB.grow();
        }*/
        return super.keyDown(keycode);
    }

    private void startEffect(Vector3 worldCoordinates) {
        explosionEffect effect = new explosionEffect();
        effect.setScale(.005f);
        effect.setPosition(worldCoordinates.x, worldCoordinates.y);
        mainStage.addActor(effect);
        effect.start();
    }

    private void moveElementToSpawnPoint() {
        BaseGame.reverseSound.play(BaseGame.soundVolume);
        element.addAction(Actions.moveTo(spawnPoint.x, spawnPoint.y, .5f, Interpolation.smoother));
    }

    private boolean isListCollisions() {
        if (element.overlaps(listA.getCollisionBox()) && element.getY() > (listA.getY() + listA.getHeight() / 2)) {
            return listA.tryInsertOnTop(element);
        } else if (element.overlaps(listA.getCollisionBox()) && element.getY() <= (listA.getY() + listA.getHeight() / 2)) {
            return listA.tryInsertOnBottom(element);
        } else if (element.overlaps(listB.getCollisionBox()) && element.getY() > (listB.getY() + listB.getHeight() / 2)) {
            return listB.tryInsertOnTop(element);
        } else if (element.overlaps(listB.getCollisionBox()) && element.getY() <= (listB.getY() + listB.getHeight() / 2)) {
            return listB.tryInsertOnBottom(element);
        }
        return false;
    }

    private void generateNewElement() {
        element = new Element(spawnPoint.x, spawnPoint.y, mainStage);
    }

    private boolean checkAndUpdateScore(List list) {
        if (list.isScore) {
            score += list.NUM_SAME_TYPE;
            topLabel.restart();
            topLabel.setText("" + score);
            list.isScore = false;
            BaseGame.threesSound.play(BaseGame.soundVolume);
            return true;
        }
        return false;
    }

    private void initializeActors() {
        impassables = new Array();
        loadActorsFromMap();
        spawnPoint = new Vector2(element.getX() - element.getWidth() / 2, element.getY() - element.getHeight() / 2);
        element.centerAtPosition(element.getX(), element.getY());
        /*element.setZIndex(3);*/
        listA.setMaxCapacity(element.getHeight());
        listA.setZIndex(3);
        listB.setMaxCapacity(element.getHeight());
        listB.setZIndex(2);
    }

    private void loadActorsFromMap() {
        MapLoader mapLoader = new MapLoader(mainStage, tilemap, element, impassables, listA, listB);
        element = mapLoader.element;
        listA = mapLoader.listA;
        listB = mapLoader.listB;
    }

    private void mapCenterCamera() {
        new BaseActor(0, 0, mainStage).addAction(Actions.run(() -> {
            TiledMapActor.centerPositionCamera(mainStage);
            OrthographicCamera camera = (OrthographicCamera) mainStage.getCamera();
            camera.zoom = 1f;
        }));
    }

    private void initializeArt() {
        int faceScale = 4;
        float faceOffsetX = 5;
        BaseActor faceA = new BaseActor(0, 0, mainStage);
        faceA.loadImage("faceA");
        faceA.setScale(faceScale);
        faceA.setPosition(listA.getX() + faceOffsetX, listA.getY() + 12);
        faceA.flip();
        faceA.setZIndex(listA.getZIndex() - 1);

        BaseActor faceB = new BaseActor(0, 0, mainStage);
        faceB.loadImage("faceB");
        faceB.setScale(faceScale);
        faceB.setPosition(listB.getX() - faceOffsetX, listB.getY() + 12);
        faceB.setZIndex(listB.getZIndex() - 1);

        /* ------------ */

        float speechBubbleOffsetX = 25;
        float speechBubbleScale = 2.5f;
        float speechBubbleAnimationSpeed = .5f;

        BaseActor speechBubbleA = new BaseActor(0, 0, mainStage);
        Array<TextureAtlas.AtlasRegion> animationImages = new Array();
        animationImages.add(BaseGame.textureAtlas.findRegion("speechBubbles1"));
        animationImages.add(BaseGame.textureAtlas.findRegion("speechBubbles2"));
        animationImages.add(BaseGame.textureAtlas.findRegion("speechBubbles3"));
        Animation animation = new Animation(speechBubbleAnimationSpeed, animationImages, Animation.PlayMode.LOOP_RANDOM);
        speechBubbleA.setAnimation(animation);
        speechBubbleA.setScale(speechBubbleScale);
        speechBubbleA.setPosition(listA.getX() + speechBubbleOffsetX, listA.getY() + 14);
        speechBubbleA.setZIndex(listA.getZIndex() - 1);


        BaseActor speechBubbleB = new BaseActor(0, 0, mainStage);
        speechBubbleB.loadImage("speechBubble0");

        animationImages.clear();
        animationImages.add(BaseGame.textureAtlas.findRegion("speechBubbles1"));
        animationImages.add(BaseGame.textureAtlas.findRegion("speechBubbles2"));
        animationImages.add(BaseGame.textureAtlas.findRegion("speechBubbles3"));
        animation = new Animation(speechBubbleAnimationSpeed, animationImages, Animation.PlayMode.LOOP_RANDOM);
        speechBubbleB.setAnimation(animation);
        speechBubbleB.setScale(speechBubbleScale);
        speechBubbleB.flip();
        speechBubbleB.setPosition(listB.getX() - speechBubbleOffsetX, listB.getY() + 14);
        speechBubbleB.setZIndex(listB.getZIndex() - 1);

        /* ------------ */

        rootA = new Root(faceA.getX(), 5, mainStage, true);
        rootB = new Root(faceB.getX(), 5, mainStage, false);
    }

    private void initializeGUI() {
        topLabel = new TypingLabel("", new Label.LabelStyle(BaseGame.mySkin.get("Play-Bold40white", BitmapFont.class), null));
        topLabel.setAlignment(Align.top);
        topLabel.setVisible(true);

        middleLabel = new TypingLabel("{SLOWER}G A M E   O V E R !", new Label.LabelStyle(BaseGame.mySkin.get("Play-Bold59white", BitmapFont.class), null));
        middleLabel.setAlignment(Align.top);
        middleLabel.setColor(new Color(0.647f, 0.188f, 0.188f, 1f));
        middleLabel.setVisible(false);

        uiTable.defaults().padTop(Gdx.graphics.getHeight() * .02f);
        uiTable.add(topLabel).height(topLabel.getPrefHeight() * 1.5f).top().width(Gdx.graphics.getWidth()).row();
        uiTable.add(middleLabel).height(middleLabel.getPrefHeight() * 1.5f).padTop(Gdx.graphics.getHeight() * .25f).expandY().top().row();
        uiTable.add(new MadeByLabel()).padBottom(Gdx.graphics.getHeight() * .02f);
        // uiTable.setDebug(true);
    }
}
