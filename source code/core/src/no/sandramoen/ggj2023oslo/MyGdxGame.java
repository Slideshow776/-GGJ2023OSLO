package no.sandramoen.ggj2023oslo;

import no.sandramoen.ggj2023oslo.screens.gameplay.LevelScreen;
import no.sandramoen.ggj2023oslo.screens.shell.LevelSelectScreen;
import no.sandramoen.ggj2023oslo.screens.shell.MenuScreen;
import no.sandramoen.ggj2023oslo.screens.shell.SplashScreen;
import no.sandramoen.ggj2023oslo.utils.BaseGame;

public class MyGdxGame extends BaseGame {

	@Override
	public void create() {
		super.create();
		// setActiveScreen(new SplashScreen());
		// setActiveScreen(new MenuScreen());
		setActiveScreen(new LevelScreen(BaseGame.testMap));
	}
}

