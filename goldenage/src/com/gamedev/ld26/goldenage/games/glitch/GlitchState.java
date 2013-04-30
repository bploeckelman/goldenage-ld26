package com.gamedev.ld26.goldenage.games.glitch;

import com.badlogic.gdx.graphics.Color;
import com.gamedev.ld26.goldenage.Globals.Games;
import com.gamedev.ld26.goldenage.GoldenAgeGame;
import com.gamedev.ld26.goldenage.core.Assets;
import com.gamedev.ld26.goldenage.games.GameState;

public class GlitchState extends GameState {

	public GlitchState(GoldenAgeGame game, GameState previous) {
		super(game, previous);
		Assets.glitchSound.play();
	}

	@Override
	protected void updateScreen(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void renderScreen(float delta) {
		// TODO Auto-generated method stub
		
	}
	
	private float _time;
	public void update(float delta) {
		_time += delta;
		if (_time > 3) {
			_gameWon = true;
			Assets.glitchSound.stop();
		}
	}
	

	public void render(float delta) {
		Assets.batch.begin();
		
		Assets.batch.setColor(Color.WHITE);
		Assets.batch.draw(Assets.glitchTexture, _windowBounds.x, _windowBounds.y, _windowBounds.width, _windowBounds.height);
		Assets.batch.end();
	}

	@Override
	public Games nextScreen() {
		return Games.end;
	}
	
	public void dispose() {
		Assets.glitchSound.stop();
	}
}
