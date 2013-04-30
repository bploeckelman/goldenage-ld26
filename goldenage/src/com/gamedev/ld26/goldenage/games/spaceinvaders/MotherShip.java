package com.gamedev.ld26.goldenage.games.spaceinvaders;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gamedev.ld26.goldenage.core.Assets;
import com.gamedev.ld26.goldenage.games.GameObject;
import com.gamedev.ld26.goldenage.games.GameState;

public class MotherShip extends GameObject {

	final int _speed = 250;
	private Rectangle _flyBounds;
	
	private Color _shipColor;
	
	public MotherShip(GameState gs) {
		super(gs);
		
		Assets.siMothership.loop(0.6f);
		
		_flyBounds = gs._windowBounds;
		setSize(40,  25);
		setPosition(_flyBounds.x - 40, _flyBounds.y + _flyBounds.height - 40);
		
		_shipColor = new Color(Color.ORANGE);
	}
	
	public void update(float dt) {	
		float x = _rect.x + (_speed * dt);
		setPosition(x, _rect.y);
		if (x > _flyBounds.x + _flyBounds.width - 41) {
			setAlive(false);
		};
	}
	
	public void kill() {
		if (!isAlive()) return; 
		setAlive(false);
		Assets.bossShot.play();
	}
	
	public void setAlive(boolean alive) {
		super.setAlive(alive);
		if (!alive) {
			Assets.siMothership.stop();
		}
	}
	
	private float _time;
	
	public void render(float delta) {
		Assets.shapes.setColor(_shipColor);
		float top = _rect.y + _rect.height;
		float half = _rect.x + _rect.width/2;
		float halfTop = _rect.y + _rect.height/2;
		
		_time += delta;
		float dif = (float)Math.sin(6 * _time) * 5;
		
		_shipColor.b += dif;
		_shipColor.r += 5* dif;
		_shipColor.r -= 3* dif;
		
		Assets.shapes.triangle(_rect.x - dif, halfTop, half, top, half, _rect.y);
		Assets.shapes.triangle(_rect.x + _rect.width + dif, halfTop, half, top, half, _rect.y);
	}
}
