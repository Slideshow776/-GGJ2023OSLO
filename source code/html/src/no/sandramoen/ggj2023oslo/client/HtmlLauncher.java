package no.sandramoen.ggj2023oslo.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import no.sandramoen.ggj2023oslo.MyGdxGame;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                // Resizable application, uses available space in browser
                return new GwtApplicationConfiguration(true);

                // Fixed size application:
                // return new GwtApplicationConfiguration(688, 387);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new MyGdxGame();
        }
}