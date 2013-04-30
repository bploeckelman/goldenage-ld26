package com.gamedev.ld26.goldenage.games.spaceinvaders;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gamedev.ld26.goldenage.core.Assets;
import com.gamedev.ld26.goldenage.games.GameState;
import com.gamedev.ld26.goldenage.games.Player;

public class SpacePlayer extends Player {

	public SpacePlayer(GameState gs) {
		super(new Vector2(35, 45), Color.RED, gs);
		ShootSound = Assets.siPlayerLaser;
	}
	
	public void setAlive(boolean alive) {
		super.setAlive(alive);
	}

	public void render(float delta) {
		if (!_draw) return;
		Assets.shapes.setColor(_color);
		drawRect(_rect);
	}
	
	private void drawRect(Rectangle rect) {
		float side = rect.y + rect.height*0.6f;
		float half = rect.x + rect.width/2;		
		
		Assets.shapes.triangle(rect.x, side, half, rect.y + rect.height, rect.x + rect.width, side);
		Assets.shapes.triangle(rect.x, side, half, rect.y - rect.height, rect.x + rect.width, side);
		
		Assets.shapes.setColor(Color.BLUE);
		side -= 10;
		Assets.shapes.triangle(rect.x + 10, side, half, rect.y + rect.height-2, rect.x + rect.width - 10, side);
		
	}
}
