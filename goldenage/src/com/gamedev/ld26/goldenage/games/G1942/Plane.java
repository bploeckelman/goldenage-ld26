package com.gamedev.ld26.goldenage.games.G1942;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.gamedev.ld26.goldenage.IShooter;
import com.gamedev.ld26.goldenage.core.Assets;
import com.gamedev.ld26.goldenage.games.Bullet;
import com.gamedev.ld26.goldenage.games.BulletFactory;
import com.gamedev.ld26.goldenage.games.Explosion;
import com.gamedev.ld26.goldenage.games.GameObject;
import com.gamedev.ld26.goldenage.games.GameState;
import com.gamedev.ld26.goldenage.utils.Config;
import com.gamedev.ld26.goldenage.utils.Utils;

public class Plane extends GameObject implements IShooter {

	public Sound ShootSound = Assets.planeShot;
	
	protected float distance;
	protected int _dir;
	protected float speed;
	protected BulletFactory bFactory;
	private boolean hasFired;
	protected int _hitPoints;
	
	public Plane(Vector2 pos, Vector2 size, Color color, GameState gs, BulletFactory bf) {
		super(pos, size, color, gs);
		distance = Assets.random.nextFloat() * .4f + .3f;
		distance *= Config.window_height;
		_dir = 60 + Assets.random.nextInt(60);
		speed = 200;
		bFactory = bf;
		hasFired = false;
		_hitPoints = 1;
		ScoreValue = 20;
	}

	public void update(float dt){
		float x = (float)Math.cos(_dir / 180.0 * Math.PI);
		float y = -(float)Math.sin(_dir / 180.0 * Math.PI);
		
		_rect.x += x * speed * dt;
		_rect.y += y * speed * dt;
		
		if (_rect.y < distance && !hasFired)
		{
			hasFired = true;
			hitDistance();
		}
		
		_alive = _alive && _gState._windowBounds.overlaps(_rect);
	}

	protected void hitDistance(){
		fireBullet();
		_dir += 180;
	}
	
	protected void fireBullet(){
		Bullet bul = bFactory.GetBullet(this);
		bul.setTarget(_gState.getPlayer());
		bul.setColor(_color);
		bul.setSize(4, 4);
	}
	
	public boolean gotHit() {
		_hitPoints--;
		if (_hitPoints <= 0)
		{
			float x = (float)Math.cos(_dir / 180.0 * Math.PI);
			float y = -(float)Math.sin(_dir / 180.0 * Math.PI);
			Explosion explosion = new Explosion(getCenterPosition(), _color, new Vector2(x*speed, y * speed), _gState);
			explosion.deferred = true;
			_alive = false;
			return true;
		}
		return false;
	}
	
	@Override
	public Vector2 GetTip() {
		return new Vector2(_rect.x + (_rect.width/2), _rect.y);
	}

	@Override
	public int getShootSpeed() {
		return 1000 + Assets.random.nextInt(500);
	}

	@Override
	public Vector2 getShootDirection() {
		float x = (float)Math.cos(_dir / 180.0 * Math.PI);
		float y = -(float)Math.sin(_dir / 180.0 * Math.PI);
		return new Vector2(x, y);
	}
	
	public void render(float delta)
	{
		Assets.shapes.setColor(_color);
		Vector2 center = Utils.rectCenter(_rect);
		
		Assets.shapes.translate(center.x, center.y, 0);
		Assets.shapes.rotate(0, 0, -1, _dir);
		Assets.shapes.scale(1, .7f, 1);
		Assets.shapes.translate(-center.x, -center.y, 0);
		
		Assets.shapes.triangle(_rect.x, _rect.y, _rect.x, _rect.y + _rect.height, _rect.x+ _rect.width, _rect.y + _rect.height/2);
		Assets.shapes.identity();
		//Assets.shapes.rect(_rect.x, _rect.y, _rect.width, _rect.height);
	}
	
	public void play(){
		if (ShootSound != null) {
			ShootSound.play();
		}
	}
}
