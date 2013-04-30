package com.gamedev.ld26.goldenage.games.G1942;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.gamedev.ld26.goldenage.core.Assets;

public class MovingBackground {

	private float _speed;
	private Color _topColor;
	private Color _bottomColor;
	private Rectangle _rect;
	
	public MovingBackground(Rectangle rect, Color top, Color bottom) {
		_rect = new Rectangle(rect);
		_topColor = top;
		_bottomColor = bottom;
		_speed = 200;
	}
	
	public void SetYOffset(float y)
	{
		_rect.y += _rect.height;
	}
	
	public void Update(float dt)
	{
		_rect.y -= dt* _speed;
		if (_rect.y < -_rect.height)
		{
			_rect.y += (2*_rect.height);
		}
	}
	
	public void render()
	{
		Assets.shapes.rect(_rect.x, _rect.y, _rect.width, _rect.height, _bottomColor, _bottomColor, _topColor, _topColor);
	}

}
