package com.gamedev.ld26.goldenage.games;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

public class PlayerTransition extends Transition {
	
	private Rectangle _initialBounds;
	private GameObject _transitionObject;
	
	private float _transition = 0;
	private float _dw, _dh, _initWidth, _initHeight;
	private float _dx, _dy, _initX, _initY;
	private boolean _adjustPos;
	private Color _initColor;
	private float _dr, _db, _dg;
	
	private float _transitionTime;
		
	public PlayerTransition(GameObject transition, GameObject initial, float transitionTime)
	{
		this(transition, initial, transitionTime, true);
	}
	
	public PlayerTransition(GameObject transition, GameObject initial, float transitionTime, boolean position)
	{	
		_transitionTime = transitionTime;
		_transitionObject = transition;
		Rectangle bounds = transition.getRect();
		
		_initialBounds = initial.getRect();
			
		_dw = -(_initialBounds.width - bounds.width)/ transitionTime;
		_dh = -(_initialBounds.height - bounds.height)/transitionTime;

		_dx = -(_initialBounds.x - bounds.x)/ transitionTime;
		_dy = -(_initialBounds.y - bounds.y)/ transitionTime; 
		
		_initWidth = _initialBounds.width;
		_initHeight = _initialBounds.height;
		_initX = _initialBounds.x;
		_initY = _initialBounds.y;
		
		_adjustPos = position;
		
		_initColor = initial._color;
		Color color = transition.getColor();
		_dr = (color.r - _initColor.r) / transitionTime;
		_dg = (color.g - _initColor.g) / transitionTime;
		_db = (color.b - _initColor.b) / transitionTime;
	}
	
	public void Update(float delta) {
		_transitionObject.setDraw(true);
		
		_transition += delta;
		_transitionObject.setSize(_initWidth + (_transition * _dw), _initHeight + (_transition * _dh));
		
		_transitionObject.setColor(new Color(_initColor.r + (_transition * _dr), _initColor.g + (_transition * _dg), _initColor.b + (_transition * _db), _initColor.a));
		
		if (_transitionObject.IsTemporary) {
			if ((_transitionTime - _transition) < 0.001) {
				_transitionObject.setAlive(false);
			}
		}	
		
		if (!_adjustPos) return; 
		_transitionObject.setPosition(_initX + (_transition * _dx), _initY + (_transition * _dy));
	}
	
	public void Render(){
		
	}
}
