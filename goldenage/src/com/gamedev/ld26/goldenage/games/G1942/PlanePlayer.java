package com.gamedev.ld26.goldenage.games.G1942;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gamedev.ld26.goldenage.core.Assets;
import com.gamedev.ld26.goldenage.games.GameState;
import com.gamedev.ld26.goldenage.games.Player;

public class PlanePlayer extends Player {

	private float _animTime;
	private float AnimTime = 1.0f;
	
	private Rectangle _left;
	private Rectangle _right;
	
	private float _dWings;
	
	private Color _flameColor = new Color(Color.ORANGE);
	
	public PlanePlayer(GameState gs) {
		super(new Vector2(20, 30), Color.BLACK, gs);
		ShootSound = Assets.g1942PlayerLaser;
		
		_left = new Rectangle(0, 0, 10, 1);
		_right = new Rectangle(0, 0, 10, 1);
	}
	
	public void setPosition(float x, float y) {
		super.setPosition(x,  y);
	
		_left.x = x - _dWings - _left.width;
		_left.y = _right.y = y;
		_right.x = x + _rect.width + _dWings;
	}
	
	public void render(float delta) {
		if (!_draw) return;
		drawRect(_rect, _color);
		
		if (!_gState.isTransitioning()) {
			animate(delta);
			
			drawRect(_left, Color.BLACK);
			drawRect(_right, Color.BLACK);
			Assets.shapes.end();
			Assets.shapes.begin(ShapeType.Line);
			Assets.shapes.line(_left.x, _left.y, _right.x, _right.y);
			drawRect(_left, Color.WHITE);
			drawRect(_right, Color.WHITE);
			drawRect(_rect, Color.RED);
			Assets.shapes.end();
			Assets.shapes.begin(ShapeType.Filled);
			
			drawFlame();
		}
	}
	
	private void drawFlame() {
		if (_respawnImmunity > 0) return;
		
		float dif = (float)Math.sin(6 * _animTime) * 5;
		
		_flameColor.b += dif;
		_flameColor.r += 5* dif;
		_flameColor.r -= 3* dif;
		
		float half = _rect.x + _rect.width/2;
		Assets.shapes.setColor(_flameColor);
		Assets.shapes.triangle(half - 4, _rect.y, half, _rect.y + 2, half + 4, _rect.y);
		Assets.shapes.triangle(half - 4, _rect.y, half, _rect.y - Math.abs(dif * 7), half + 4, _rect.y);
	}
	
	private void animate(float delta) {
		_animTime += delta;
		
	
		
		if (_animTime > AnimTime) return;
		float dt =(_animTime/AnimTime);	
		
		_dWings = 5 * dt;
		
		_left.height = _right.height = 10 * dt;	
	}
		
	private void drawRect(Rectangle rect, Color color) {
		Assets.shapes.setColor(color);
		Assets.shapes.triangle(rect.x, rect.y, rect.x + rect.width/2, rect.y + rect.height, rect.x+ rect.width, rect.y);	
	}
	
	public boolean collides(Circle circle) {
		return Intersector.overlaps(circle, _rect) || Intersector.overlaps(circle, _left)
				|| Intersector.overlaps(circle, _right);
	}
}
