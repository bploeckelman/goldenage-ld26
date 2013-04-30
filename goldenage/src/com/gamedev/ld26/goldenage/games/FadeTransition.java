package com.gamedev.ld26.goldenage.games;

import com.badlogic.gdx.graphics.Color;

public class FadeTransition extends Transition {

	protected float _da;
	protected float _alpha;
	private GameObject _object;
	
	public FadeTransition(GameObject obj, boolean fade, float time) {
		_object = obj;
		
		_alpha = (fade) ? 1f : 0;
		
		_da = 1 / time;
		if (fade) {
			_da = -_da;
		}
	}
	
	@Override
	public void Update(float delta) {
		_alpha += (_da * delta);
		
		Color color = new Color(_object.getColor());
		color.a = _alpha;
		_object.setColor(color);	
	}
	
	public void Render()
	{
	
	}
}
