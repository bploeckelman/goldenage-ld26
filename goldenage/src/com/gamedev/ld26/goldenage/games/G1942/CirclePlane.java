package com.gamedev.ld26.goldenage.games.G1942;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.gamedev.ld26.goldenage.core.Assets;
import com.gamedev.ld26.goldenage.games.BulletFactory;
import com.gamedev.ld26.goldenage.games.GameState;
import com.gamedev.ld26.goldenage.utils.Config;

public class CirclePlane extends Plane {

	private boolean turning;
	private boolean hasTurned;
	private int initialDir;
	
	public CirclePlane(Vector2 pos, Vector2 size, Color color, GameState gs,
			BulletFactory bf) {
		super(pos, size, color, gs, bf);
		if (pos.x ==0)
		{
			_dir = 0;
		}
		else {
			_dir = 180;
		}
		turning = false;
		hasTurned = false;
		initialDir = _dir;
		speed = 300;
		ScoreValue = 35;
	}

	public void update(float dt){
		float x = (float)Math.cos(_dir / 180.0 * Math.PI);
		float y = -(float)Math.sin(_dir / 180.0 * Math.PI);
		
		_rect.x += x * speed * dt;
		_rect.y += y * speed * dt;
		
		if (((initialDir == 0 && _rect.x > 300) || (initialDir == 180 && _rect.x < Config.window_width - 300) )&& !hasTurned)
		{
			turning = true;
			hasTurned = true;
		}
		if (turning)
		{
			_dir += 2;
			if (_dir >= 360)
			{
				_dir -= 360;
			}
			if (_dir == initialDir)
			{
				turning = false;
			}
			if (Assets.random.nextDouble() > .98)
			{
				fireBullet();
			}
		}
		
		

		_alive = _alive && _gState._windowBounds.overlaps(_rect);
	}
}
