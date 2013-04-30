package com.gamedev.ld26.goldenage.games;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gamedev.ld26.goldenage.core.Assets;
import com.gamedev.ld26.goldenage.games.pong.PongState;
import com.gamedev.ld26.goldenage.utils.Config;
import com.gamedev.ld26.goldenage.utils.Utils;

public class Ball extends GameObject {

	private static final float max_speed = 750.f;
	
	private Circle _circle;
	private Vector2 _dir;
	private float _speed;
	
	private boolean _isSquare;
	
	public Ball(Vector2 pos, float radius, Color color, GameState gs) {
		super(pos, Vector2.Zero, color, gs);
		_transitionObject = false;
		
		_circle = new Circle(pos.x, pos.y, radius);
		float x = Assets.random.nextFloat() * 2.f - 1.f;
		float y = Assets.random.nextBoolean() ? -1.f : 1.f;
		_dir = new Vector2(x, y).nor();
		_speed = 500.f;
	}
	
	public void setSquare(boolean square) {
		_isSquare = square;
	}
	
	public void setPosition(float x, float y)
	{
		float size = _circle.radius * 2;
		_circle.x = Utils.clamp(x, 0, Config.window_width - size);
		_circle.y = Utils.clamp(y, 0, Config.window_height - size);
	}
	
	public void setVelocity(Vector2 newVel)
	{
		_dir = newVel;
	}
	
	public void multiplyDir(Vector2 newVal)
	{
		_dir.scl(newVal);
	}
	
	public void setCollisionDirection(Vector2 bounce)
	{
		if (bounce.x != 0)
		{
			_dir.x = Math.abs(_dir.x);
			_dir.x *= bounce.x;
			_dir.y += (Assets.random.nextFloat() -.5f) * .1f;
		}
		if (bounce.y != 0)
		{
			_dir.y = Math.abs(_dir.y);
			_dir.y *= bounce.y;
			_dir.x += (Assets.random.nextFloat() -.5f) * .1f;
		}
		//_dir.nor();
	}
	
	public boolean collides(GameObject other)
	{
		return (other != null) && other.collides(_circle);
	}
	
	public void update(float delta) {
		if (!_alive) return;
		if (_dontUpdate) return;
		_circle.x += _dir.x * _speed * delta;
		_circle.y += _dir.y * _speed * delta;
		
		keepInsideRect(_gState._windowBounds);
		clampSpeed();
	}

	public void clampSpeed() {
		_speed = Utils.clamp(_speed, -max_speed, max_speed);
	}

	public void render(float delta) {
		Assets.shapes.setColor(_color);
		if (_isSquare) {		
			float rad = _circle.radius;
			float size = rad*2;
			Assets.shapes.rect(_circle.x - rad, _circle.y - rad, size, size);
		} else {
			Assets.shapes.circle(_circle.x, _circle.y, _circle.radius);
		}
	}
	

	public boolean inBounds(Rectangle r) {
		return !(((_circle.x - _circle.radius) < r.x)
			|| ((_circle.x + _circle.radius) > (r.x + r.width))
			|| ((_circle.y - _circle.radius) < r.y) 
			|| ((_circle.y + _circle.radius) > (r.y + r.height)));
	}
	
	public Circle getCircle() { return _circle; }
	public Vector2 getDir() { return _dir; }
	public float getSpeed() { return _speed; }
	public void setSpeed(float s) { _speed = s; clampSpeed(); }
	public void setSize(float x, float y) { _circle.radius = x;}

	public Vector2 getPos()
	{
		return new Vector2(_circle.x, _circle.y);
	}
	
	protected void keepInsideRect(Rectangle bounds) {
		boolean pong = _gState instanceof PongState;
		if ((_circle.x - _circle.radius) < bounds.x) {
			_circle.x = bounds.x + _circle.radius;
			_dir.x = -_dir.x;
			if (pong) { // hackity hack hack hack
				Assets.pongWallBounceSound.play();
			}
		}
		if ((_circle.x + _circle.radius) > (bounds.x + bounds.width)) {
			_circle.x = (bounds.x + bounds.width) - _circle.radius;
			_dir.x = -_dir.x;
			if (pong) { // hackity hack hack hack
				Assets.pongWallBounceSound.play();
			}
		}
		if ((_circle.y - _circle.radius) < bounds.y) {
			_circle.y = bounds.y + _circle.radius;
			_dir.y = -_dir.y;
		}
		if ((_circle.y + _circle.radius) > (bounds.y + bounds.height)) {
			_circle.y = (bounds.y + bounds.height) - _circle.radius;
			_dir.y = -_dir.y;
		}
	}
}
