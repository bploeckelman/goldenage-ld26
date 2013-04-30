package com.gamedev.ld26.goldenage;

import com.badlogic.gdx.math.Vector2;

public interface IShooter {
	Vector2 GetTip();
	int getShootSpeed();
	Vector2 getShootDirection();
	void play();
}
