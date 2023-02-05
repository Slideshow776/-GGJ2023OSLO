package no.sandramoen.ggj2023oslo.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

import no.sandramoen.ggj2023oslo.actors.map.TiledMapActor;
import no.sandramoen.ggj2023oslo.actors.utils.BaseActor;
import no.sandramoen.ggj2023oslo.utils.BaseGame;

public class Background extends BaseActor {
    public float timeMultiplier = 1f;
    public float timeIncrement = 1.1f;
    public boolean increaseSpeed = false;
    public boolean decreaseSpeed = false;

    private String vertexShaderCode;
    private String fragmenterShaderCode;
    private ShaderProgram shaderProgram;
    private float time = .0f;
    private boolean disabled = false;
    private Vector3 colour;

    public Background(float x, float y, Stage stage, Vector3 colour) {
        super(x, y, stage);
        this.colour = colour;

        loadImage("whitePixel");

        setPosition(x, y);
        setSize(TiledMapActor.mapTileWidth, TiledMapActor.mapTileHeight);

        ShaderProgram.pedantic = false;
        vertexShaderCode = BaseGame.defaultShader;
        fragmenterShaderCode = BaseGame.backgroundShader;
        shaderProgram = new ShaderProgram(vertexShaderCode, fragmenterShaderCode);
        if (!shaderProgram.isCompiled())
            Gdx.app.error("Background", "Shader compile error: " + shaderProgram.getLog());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (disabled) {
            super.draw(batch, parentAlpha);
        } else if (BaseGame.isCustomShadersEnabled) {
            try {
                batch.setShader(shaderProgram);
                shaderProgram.setUniformf("u_time", time * timeMultiplier);
                shaderProgram.setUniformf("u_resolution", new Vector2(getWidth() * .125f, getHeight() * .125f));
                shaderProgram.setUniformf("u_color", colour);
                super.draw(batch, parentAlpha);
                batch.setShader(null);
            } catch (Throwable error) {
                super.draw(batch, parentAlpha);
            }
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        time += delta;

        if (increaseSpeed) {
            timeMultiplier += .001f;
        }

        if (increaseSpeed && timeMultiplier >= timeIncrement) {
            increaseSpeed = false;
            timeIncrement += .1f;
        }

        if (decreaseSpeed && timeMultiplier >= 1f) {
            timeMultiplier -= .02f;
        }
    }

    public void restart() {
        timeMultiplier = 1f;
        timeIncrement = 1.1f;
    }
}
