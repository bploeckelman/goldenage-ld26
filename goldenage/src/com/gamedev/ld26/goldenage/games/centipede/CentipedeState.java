package com.gamedev.ld26.goldenage.games.centipede;

import com.badlogic.gdx.math.Vector2;
import com.gamedev.ld26.goldenage.Globals;
import com.gamedev.ld26.goldenage.GoldenAgeGame;
import com.gamedev.ld26.goldenage.games.GameState;

public class CentipedeState extends GameState {

	static final Vector2 size = new Vector2(80, 80);
	
	public CentipedeState(GoldenAgeGame game, GameState previous) {
		super(game, previous);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void updateScreen(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void renderScreen(float delta) {
		// TODO Auto-generated method stub
		
	}

	public Globals.Games nextScreen() {
		return Globals.Games.g1942;
	}
}
