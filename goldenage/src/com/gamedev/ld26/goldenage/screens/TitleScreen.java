package com.gamedev.ld26.goldenage.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.gamedev.ld26.goldenage.Globals;
import com.gamedev.ld26.goldenage.GoldenAgeGame;
import com.gamedev.ld26.goldenage.core.Assets;
import com.gamedev.ld26.goldenage.utils.Config;
import com.gamedev.ld26.goldenage.utils.Utils;
import com.gamedev.ld26.goldenage.utils.Utils.STRING_JUSTIFICATION;

public class TitleScreen implements Screen {

	private final OrthographicCamera camera = new OrthographicCamera();
	private final GoldenAgeGame game;
	private float accum = 0.f;
	private float startGameDelay;
	private boolean _coinInserted;
	
	public TitleScreen(GoldenAgeGame game) {
		super();
		this.game = game;
		camera.setToOrtho(false, Config.window_width, Config.window_height);
		Utils.PlayMusic(Assets.titleMusic);
		startGameDelay = 0;
	}
	
	private boolean _started;
	
	public void update(float dt) {
		if (game.input.isKeyDown(Keys.ESCAPE)) {
			Gdx.app.exit();
		} else if (Gdx.input.justTouched() || startButton()) {
			Assets.coinReturnSound.play();
			_coinInserted = true;
			startGameDelay = 1.75f;
		} //else {
			//handleScreen();
		//}
		
		if (startGameDelay > 0)
		{
			startGameDelay -=dt;
			if (startGameDelay <= 0)
			{
				game.setGame(Globals.Games.pong);
				_coinInserted = false;
				_started = false;
			}
		}
		accum += dt;
	}
	
	private boolean startButton() {
		if (_started)return false;
		
		_started = (game.input.isKeyDown(Keys.ENTER) ||
				game.input.isKeyDown(Keys.SPACE));
		return _started;
	}
	
	protected void handleScreen() {	
//		if (game.input.isKeyDown(Keys.NUM_1)) {
//			game.setGame(Globals.Games.pong);
//		} else if (game.input.isKeyDown(Keys.NUM_2)) {
//			game.setGame(Globals.Games.breakout);
//		} else if (game.input.isKeyDown(Keys.NUM_3)) {
//			game.setGame(Globals.Games.spaceinvaders);
//		} else if (game.input.isKeyDown(Keys.NUM_4)) {
//			game.setGame(Globals.Games.glitch);
//		} else if (game.input.isKeyDown(Keys.NUM_5)) {
//			game.setGame(Globals.Games.g1942);
//		} else if (game.input.isKeyDown(Keys.NUM_6)) {
//			game.setGame(Globals.Games.end);
//		} 
	}
	
	@Override
	public void render(float delta) {
		update(delta);
		
		
		Gdx.gl20.glClearColor(0.53f, 0.81f, 0.92f, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		Assets.shapes.begin(ShapeType.Filled);
		
		drawCanvas();
		
		Assets.shapes.identity();		
		Assets.shapes.end();
		
		final String testString1 = "Golden Age";
		final String testString2 = "Relive the classics";
		final String coinsString = "INSERT COIN(S)";


		Rectangle r = Config.pong_window_bounds;
		
		Utils.drawText(testString1, Config.window_half_width, r.height - 300, 50, 50, Color.ORANGE, STRING_JUSTIFICATION.CENTER);
		Utils.drawText(testString2, Config.window_half_width, 200, 30, 30, Color.GREEN, STRING_JUSTIFICATION.CENTER);
		if (!_coinInserted && (int)(accum * 2) % 2 == 0) {
			Utils.drawText(coinsString, Config.window_half_width, 160, 25, 25, Color.WHITE, STRING_JUSTIFICATION.CENTER);
		}
	}
	
	private float _dr = 0f;
	
	private void drawCanvas() {
		Rectangle r = Config.pong_window_bounds;
		Assets.shapes.identity();
		Assets.shapes.setColor(Color.BLACK);
		Assets.shapes.rect(r.x, r.y, r.width, r.height);
		
		float boxWidth = 100;
		float boxHeight = 100;
		_dr += 5f;
		float x = r.x + (r.width/2);
		float y = r.y + (r.height/2);
		Assets.shapes.translate(x,  y,  0);		
		Assets.shapes.rotate(0, 0, 1, _dr);
		
		Assets.shapes.setColor(Color.RED);
		Assets.shapes.rect(-boxWidth/2, -boxHeight/2, boxWidth, boxHeight);
		
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		game.input.reset();
	}

	@Override
	public void hide() {
		game.input.reset();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		
	}

}
