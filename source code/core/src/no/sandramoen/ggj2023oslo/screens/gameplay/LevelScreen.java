package no.sandramoen.ggj2023oslo.screens.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import com.github.tommyettinger.textra.TypingLabel;

import no.sandramoen.ggj2023oslo.actors.Element;
import no.sandramoen.ggj2023oslo.actors.List;
import no.sandramoen.ggj2023oslo.actors.map.ImpassableTerrain;
import no.sandramoen.ggj2023oslo.actors.map.TiledMapActor;
import no.sandramoen.ggj2023oslo.actors.utils.BaseActor;
import no.sandramoen.ggj2023oslo.screens.BaseScreen;
import no.sandramoen.ggj2023oslo.screens.shell.LevelSelectScreen;
import no.sandramoen.ggj2023oslo.utils.BaseGame;

public class LevelScreen extends BaseScreen {
    private Array<ImpassableTerrain> impassables;
    private Element element;
    private List listA;
    private List listB;

    private boolean isGameOver;
    private Vector2 spawnPoint;

    private TypingLabel topLabel;

    private TiledMapActor tilemap;
    private TiledMap currentMap;

    public LevelScreen(TiledMap tiledMap) {
        currentMap = tiledMap;
        this.tilemap = new TiledMapActor(currentMap, mainStage);

        initializeActors();
        initializeGUI();
        mapCenterCamera();
    }

    @Override
    public void initialize() {
    }

    @Override
    public void update(float delta) {
        if (!isGameOver) {
            if (listA.isMaxCapacity() && listB.isMaxCapacity()) {
                isGameOver = true;
                topLabel.setVisible(true);
                topLabel.restart();
            }
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 worldCoordinates = mainStage.getCamera().unproject(new Vector3(screenX, screenY, 0f));
        if (element.getBoundaryPolygon().contains(new Vector2(worldCoordinates.x, worldCoordinates.y)))
            element.isActive = true;
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        element.isActive = false;
        if (isListCollisions())
            generateNewElement();
        else
            moveElementToSpawnPoint();
        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 worldCoordinates = mainStage.getCamera().unproject(new Vector3(screenX, screenY, 0f));
        if (element.isActive)
            element.setPosition(worldCoordinates.x - element.getWidth() / 2, worldCoordinates.y - element.getHeight() / 2);
        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.ESCAPE || keycode == Keys.Q)
            Gdx.app.exit();
        else if (keycode == Keys.R)
            BaseGame.setActiveScreen(new LevelScreen(currentMap));
        else if (keycode == Keys.T)
            BaseGame.setActiveScreen(new LevelSelectScreen());
        else if (keycode == Keys.NUMPAD_0) {
            OrthographicCamera camera = (OrthographicCamera) mainStage.getCamera();
            camera.zoom += .1f;
        }
        return super.keyDown(keycode);
    }

    private void moveElementToSpawnPoint() {
        element.addAction(Actions.moveTo(spawnPoint.x, spawnPoint.y, .5f, Interpolation.smoother));
    }

    private boolean isListCollisions() {
        if (element.overlaps(listA) && element.getY() > (listA.getY() + listA.getHeight() / 2)) {
            return listA.tryInsertOnTop(element);
        } else if (element.overlaps(listA) && element.getY() <= (listA.getY() + listA.getHeight() / 2)) {
            return listA.tryInsertOnBottom(element);
        } else if (element.overlaps(listB) && element.getY() > (listB.getY() + listB.getHeight() / 2)) {
            return listB.tryInsertOnTop(element);
        } else if (element.overlaps(listB) && element.getY() <= (listB.getY() + listB.getHeight() / 2)) {
            return listB.tryInsertOnBottom(element);
        }
        return false;
    }

    private void generateNewElement() {
        element = new Element(spawnPoint.x, spawnPoint.y, mainStage);
    }

    private void initializeActors() {
        impassables = new Array();
        loadActorsFromMap();
        spawnPoint = new Vector2(element.getX() - element.getWidth() / 2, element.getY() - element.getHeight() / 2);
        element.centerAtPosition(element.getX(), element.getY());
        listA.setMaxCapacity(element.getHeight());
        listB.setMaxCapacity(element.getHeight());
        // new Background(0, 0, mainStage);
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

    private void initializeGUI() {
        topLabel = new TypingLabel("{SLOWER}G A M E   O V E R !", new Label.LabelStyle(BaseGame.mySkin.get("Play-Bold59white", BitmapFont.class), null));
        topLabel.setAlignment(Align.top);
        topLabel.setVisible(false);

        uiTable.defaults().padTop(Gdx.graphics.getHeight() * .02f);
        uiTable.add(topLabel).height(topLabel.getPrefHeight() * 1.5f).expandY().top().row();
        // uiTable.setDebug(true);
    }
}
