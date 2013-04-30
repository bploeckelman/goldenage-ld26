package com.gamedev.ld26.goldenage.games.G1942;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.gamedev.ld26.goldenage.core.Assets;
import com.gamedev.ld26.goldenage.games.Bullet;
import com.gamedev.ld26.goldenage.games.BulletFactory;
import com.gamedev.ld26.goldenage.games.GameState;
import com.gamedev.ld26.goldenage.utils.Config;
import com.gamedev.ld26.goldenage.utils.Utils;

public class BossPlane extends Plane {

	private Vector2 _targetPosition;
	private static final Vector2 _size = new Vector2(100,100);
	private boolean dying;
	private final int TotalHitPoints = 50;
	
	public BossPlane(Color color, GameState gs,	BulletFactory bf) {
		super(new Vector2(Config.window_half_width - _size.x/2, 0), _size, color, gs, bf);
		_targetPosition = new Vector2(_rect.x, Config.window_half_height);
		_hitPoints = TotalHitPoints;
		ShootSound = null;
		dying = false;
		ScoreValue = 1000;
	}

	public void update(float dt){
		if (dying)
		{
			_targetPosition = new Vector2(_rect.x, -20);
			if (_rect.y < 0)
				_alive = false;
		}
		Vector2 tempAngle = new Vector2(_targetPosition.x -_rect.x, _targetPosition.y - _rect.y);

		float distanceSq = _targetPosition.dst2(_rect.x, _rect.y);
		if (distanceSq <= (speed * dt) * (speed * dt))
		{
			_rect.x = _targetPosition.x;
			_rect.y = _targetPosition.y;
			fireArc(TotalHitPoints - _hitPoints + 10);
			while ( _targetPosition.dst2(_rect.x, _rect.y) < 500*500)
			{
			_targetPosition = new Vector2(Assets.random.nextFloat() * (Config.window_width- 100) + 50,
										  Assets.random.nextFloat() * (Config.window_height - 100) + 50);
			}
		}
		else {
			tempAngle.nor();
			_rect.x += tempAngle.x * speed * dt;
			_rect.y += tempAngle.y * speed * dt;
		}
		
		if (!dying && Assets.random.nextFloat() > .98f)
		{
			Vector2 playerAngle = new Vector2(_gState.getPlayer().getRect().x -_rect.x, _gState.getPlayer().getRect().y - _rect.y);
			playerAngle.nor();
			Bullet bullet = bFactory.GetBullet(Utils.rectCenter(_rect), 300, Color.RED, new Vector2(playerAngle.x, playerAngle.y), 5); 
			bullet.setTarget(_gState.getPlayer());
			Assets.laser1Sound.play();
		}
	}
	
	public void fireArc(int bullets)
	{
		float angleDelta = 360.0f / bullets;
		float angle = 0;
		
		for (int i =0; i < bullets; i++)
		{
			angle += angleDelta;
			float x = (float)Math.cos(angle / 180.0 * Math.PI);
			float y = -(float)Math.sin(angle / 180.0 * Math.PI);
			
			Bullet bullet = bFactory.GetBullet(Utils.rectCenter(_rect), 100, _color, new Vector2(x, y), 5); 
			bullet.setTarget(_gState.getPlayer());
		}
		
		if (_hitPoints < TotalHitPoints/2)
		{
			angle +=(angleDelta /2.0f);
			for (int i =0; i < bullets/2; i++)
			{
				angle += angleDelta* 2.0f;
				float x = (float)Math.cos(angle / 180.0 * Math.PI);
				float y = -(float)Math.sin(angle / 180.0 * Math.PI);
				
				Bullet bullet = bFactory.GetBullet(Utils.rectCenter(_rect), 200, _color, new Vector2(x, y), 5); 
				bullet.setTarget(_gState.getPlayer());
			}
		}
		Assets.bossShot.play();
		
	}
	
	public boolean gotHit() {
		_hitPoints--;
		if (!dying && _hitPoints <= 0)
		{
			dying = true;
			return true;
		}
		return false;
	}
	
	public void render(float delta)
	{
		Assets.shapes.end();
		Assets.batch.begin();
		
		Assets.batch.setColor(Color.WHITE);
		Assets.batch.draw(dying ? Assets.baked_potato : Assets.potato, _rect.x, _rect.y, _rect.width, _rect.height);
		Assets.batch.end();
		Assets.shapes.begin(ShapeType.Filled);
	}
	
}
