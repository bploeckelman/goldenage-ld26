package com.gamedev.ld26.goldenage.games;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.gamedev.ld26.goldenage.IShooter;

public class BulletFactory {

	private GameState _gameState;
	private Color _bulletColor;
	private float _bulletSize;
	
	public BulletFactory(GameState gameState, Color color, float size) {
		_gameState = gameState;
		_bulletColor = color;
		_bulletSize = size;		
	}
	
	public Bullet GetBullet(IShooter shooter) {
		shooter.play();
		return new Bullet(shooter, _bulletSize, _bulletColor, _gameState);
	}
	
//	public Bullet GetBullet(Vector2 direction, int speed) {
//		return GetBullet(direction, speed, _bulletColor, _bulletSize);
//	}
	
	public Bullet GetBullet(Vector2 pos, int speed, Color bulletColor, Vector2 direction, float bulletSize) {
		return new Bullet(pos, bulletSize, bulletColor, direction, speed, _gameState);
	}
}
