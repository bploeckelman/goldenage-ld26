package com.gamedev.ld26.goldenage.games;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gamedev.ld26.goldenage.IShooter;

public class Bullet extends Ball {
	
	private Rectangle _bounds;
	private GameObject _target;
	
	public void setBounds(Rectangle bounds) {
		_bounds = bounds;
	}
	
	public Bullet(IShooter player, float radius, Color color, GameState gameState) {
		this(player.GetTip(), radius, color, player.getShootDirection(), player.getShootSpeed(), gameState);
	}
		
	public Bullet(Vector2 position, float radius, Color color, Vector2 direction, int speed, GameState gameState) {
		super(position,  radius, color, gameState);
		setSpeed(speed);
		setVelocity(direction);
		_bounds = gameState.getBounds();
	}
	
	protected void keepInsideRect(Rectangle bounds) {
	}
	
	public void update(float delta) {
		if (!_alive) return; 
		
		super.update(delta);
		
		if (_bounds != null && !inBounds(_bounds)) {
			setAlive(false);
		} else if (_target != null && _target.isAlive() && collides(_target)) {
			_target.setAlive(false);
			setAlive(false);
			
		}
	}
	
	public void setTarget(GameObject target) {
		_target = target;
	}
	
	public GameObject getTarget()
	{
		return _target;
	}
}
