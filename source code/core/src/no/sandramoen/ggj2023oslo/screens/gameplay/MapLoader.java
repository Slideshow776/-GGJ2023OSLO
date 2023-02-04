package no.sandramoen.ggj2023oslo.screens.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import no.sandramoen.ggj2023oslo.actors.Element;
import no.sandramoen.ggj2023oslo.actors.ListA;
import no.sandramoen.ggj2023oslo.actors.ListB;
import no.sandramoen.ggj2023oslo.actors.map.ImpassableTerrain;
import no.sandramoen.ggj2023oslo.actors.map.TiledMapActor;
import no.sandramoen.ggj2023oslo.utils.BaseGame;

public class MapLoader {
    public Element element;
    public Array<ImpassableTerrain> impassables;
    public ListA listA;
    public ListB listB;

    private TiledMapActor tilemap;
    private Stage mainStage;

    public MapLoader(Stage mainStage, TiledMapActor tilemap,
                     Element player, Array<ImpassableTerrain> impassables, ListA listA, ListB listB) {
        this.tilemap = tilemap;
        this.mainStage = mainStage;

        this.element = player;
        this.impassables = impassables;
        this.listA = listA;
        this.listB = listB;

        initializeElement();
        initializeImpassables();
        initializeListA();
        initializeListB();
    }

    private void initializeImpassables() {
        if (tilemap.getTileList("actors", "impassable").size() > 0)
            for (MapObject mapObject : tilemap.getTileList("actors", "impassable")) {
                MapProperties mapProperties = mapObject.getProperties();
                float x = mapProperties.get("x", Float.class) * BaseGame.UNIT_SCALE;
                float y = mapProperties.get("y", Float.class) * BaseGame.UNIT_SCALE;
                float width = mapProperties.get("width", Float.class) * BaseGame.UNIT_SCALE;
                float height = mapProperties.get("height", Float.class) * BaseGame.UNIT_SCALE;
                impassables.add(new ImpassableTerrain(x, y, width, height, mainStage));
            }
    }

    private void initializeElement() {
        String layerName = "actors";
        String propertyName = "player";
        if (tilemap.getTileList(layerName, propertyName).size() == 1) {
            MapObject mapObject = tilemap.getTileList(layerName, propertyName).get(0);
            float x = mapObject.getProperties().get("x", Float.class) * BaseGame.UNIT_SCALE;
            float y = mapObject.getProperties().get("y", Float.class) * BaseGame.UNIT_SCALE;
            element = new Element(x, y, mainStage);
        } else if (tilemap.getTileList(layerName, propertyName).size() > 1) {
            Gdx.app.error(getClass().getSimpleName(), "Error => found more than one property: " + propertyName + " on layer: " + layerName + "!");
        } else {
            Gdx.app.error(getClass().getSimpleName(), "Error => found no property: " + propertyName + " on layer: " + layerName + "!");
            element = null;
        }
    }

    private void initializeListA() {
        String layerName = "actors";
        String propertyName = "listA";
        if (tilemap.getTileList(layerName, propertyName).size() == 1) {
            MapObject mapObject = tilemap.getTileList(layerName, propertyName).get(0);
            float x = mapObject.getProperties().get("x", Float.class) * BaseGame.UNIT_SCALE;
            float y = mapObject.getProperties().get("y", Float.class) * BaseGame.UNIT_SCALE;
            listA = new ListA(x, y, mainStage);
        } else if (tilemap.getTileList(layerName, propertyName).size() > 1) {
            Gdx.app.error(getClass().getSimpleName(), "Error => found more than one property: " + propertyName + " on layer: " + layerName + "!");
        } else {
            Gdx.app.error(getClass().getSimpleName(), "Error => found no property: " + propertyName + " on layer: " + layerName + "!");
            listA = null;
        }
    }

    private void initializeListB() {
        String layerName = "actors";
        String propertyName = "listB";
        if (tilemap.getTileList(layerName, propertyName).size() == 1) {
            MapObject mapObject = tilemap.getTileList(layerName, propertyName).get(0);
            float x = mapObject.getProperties().get("x", Float.class) * BaseGame.UNIT_SCALE;
            float y = mapObject.getProperties().get("y", Float.class) * BaseGame.UNIT_SCALE;
            listB = new ListB(x, y, mainStage);
        } else if (tilemap.getTileList(layerName, propertyName).size() > 1) {
            Gdx.app.error(getClass().getSimpleName(), "Error => found more than one property: " + propertyName + " on layer: " + layerName + "!");
        } else {
            Gdx.app.error(getClass().getSimpleName(), "Error => found no property: " + propertyName + " on layer: " + layerName + "!");
            listB = null;
        }
    }
}
