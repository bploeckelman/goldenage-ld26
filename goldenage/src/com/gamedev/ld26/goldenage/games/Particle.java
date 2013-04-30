package com.gamedev.ld26.goldenage.games;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gamedev.ld26.goldenage.core.Assets;
import com.gamedev.ld26.goldenage.utils.Utils;

public class Particle {

	private Vector2 _pos;
	private Color _color;
	private Vector2 _vel;
	private Vector2 _accel;
	private Vector2 _size;
	private float _ttl;
	private boolean _alive;
	private boolean _default;
	
	public Particle(Vector2 pos)
	{
		this(pos, Vector2.Zero);
	}
	
	public Particle(Vector2 pos, Vector2 vel) {
		this(pos, vel, Utils.getRandomColor());
	}
	
	public Particle(Vector2 pos, Vector2 vel, Color color)
	{
		this(pos, vel, color, Vector2.Zero);
	}
	
	public Particle(Vector2 pos, Vector2 vel, Color color, Vector2 accel)
	{
		_pos = new Vector2(pos);
		_color = color;
		_vel = vel;
		_accel = accel;
		_ttl = 1f;
		_alive = true;
		_default = true;
		_size = new Vector2(2, 2);
	}
	
	public void Update(float dt)
	{
		_ttl -= dt;
		if (_ttl < 0) _alive = false;
		if (!_default) { //gravity
			_accel.y = -300.f;
		}
		_vel.x += _accel.x * dt;
		_vel.y += _accel.y * dt;
		_pos.x += _vel.x * dt;
		_pos.y += _vel.y * dt;
		
	}
	
	private Rectangle tempRect = new Rectangle();
	public void Render()
	{
		if (!_alive) return;
		Assets.shapes.setColor(_color);
		Assets.shapes.rect(_pos.x, _pos.y, _size.x, _size.y);
		
		// TODO : add particle types for specialized rendering
		if (!_default) {
			Assets.shapes.end();

			Assets.shapes.begin(ShapeType.Line);
			Assets.shapes.setColor(1.f - _color.r, 1.f - _color.r, 1.f - _color.r, 1.f);
			Assets.shapes.rect(_pos.x, _pos.y, _size.x, _size.y);
			Assets.shapes.end();
			Assets.shapes.begin(ShapeType.Filled);
		}
	}

	public boolean isAlive()
	{
		return _alive;
	}
	
	public void setSize(float w, float h)
	{
		_size.set(w, h);
		_default = false;
	}
	
	public void setTTL(float ttl) {
		_ttl = ttl;
		_default = false;
	}
}
