package com.gamedev.ld26.goldenage.games.spaceinvaders;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.gamedev.ld26.goldenage.DirectionVector;
import com.gamedev.ld26.goldenage.IShooter;
import com.gamedev.ld26.goldenage.core.Assets;
import com.gamedev.ld26.goldenage.games.Explosion;
import com.gamedev.ld26.goldenage.games.GameObject;
import com.gamedev.ld26.goldenage.games.GameState;
import com.gamedev.ld26.goldenage.utils.Utils;

public class BaseInvader extends GameObject implements IShooter {

	public Sound ShootSound = Assets.siAlienLaser;
	
	public float Rotation = 8;
	public BaseInvader(GameState gs, float size, Color color) {
		super(Vector2.Zero, new Vector2(size, size), color, gs);
		}
		
	public void setAlive(boolean value)
	{
		super.setAlive(value);
		if (!value)
		{
			Explosion explosion = new Explosion(getLowerLeftPoint(), _color, Vector2.Zero, _gState );
			explosion.deferred = true;
		}
	}
	
	private float _dr;
	
	public void offset(Vector2 position) {
		super.offset(position);
		float sign = position.x > 0 ? -1 : 1;
		Rotation = Math.abs(Rotation) * sign;
	}
	
	public void render(float delta)
	{
		_dr += Rotation;
		
		Assets.shapes.setColor(_color);
		Vector2 center = Utils.rectCenter(_rect);
		
		float halfWidth = _rect.width/2;
		float halfHeight = _rect.height/2;
		
		Assets.shapes.translate(center.x, center.y, 0);
		Assets.shapes.rotate(0, 0, 1, _dr);
		Assets.shapes.rect(-halfWidth, -halfHeight, _rect.width, _rect.height);
		Assets.shapes.end();
		
		Assets.shapes.begin(ShapeType.Line);
		Assets.shapes.setColor(Color.GRAY);
		Assets.shapes.rect(-halfWidth, -halfHeight, _rect.width, _rect.height);
		Assets.shapes.end();
		
		Assets.shapes.begin(ShapeType.Filled);
		
		Assets.shapes.identity();
	}

	@Override
	public Vector2 GetTip() {
		return new Vector2(_rect.x + (_rect.width/2), _rect.y);
	}

	@Override
	public int getShootSpeed() {
		return 400 + Assets.random.nextInt(50);
	}

	@Override
	public Vector2 getShootDirection() {
		return new Vector2(DirectionVector.Down);
	}
	
	public void play(){
		if (ShootSound != null) {
			ShootSound.play();
		}
	}
}
