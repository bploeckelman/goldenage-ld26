package com.gamedev.ld26.goldenage.games;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gamedev.ld26.goldenage.core.Assets;
import com.gamedev.ld26.goldenage.utils.Utils;

public class GameObject {
	
	protected Rectangle _rect;
	protected Color _color;
	protected GameState _gState;
	protected boolean _alive;
	protected Object collideObject;
	protected boolean _draw;
	protected boolean _transitionObject = true;
	protected float _respawnImmunity;
	public boolean IsTemporary;
	public boolean deferred = false;
	public boolean _dontUpdate = false;
	
	public int ScoreValue = 0;
	
	public GameObject(GameState gs)
	{
		this(Vector2.Zero, Vector2.Zero, Color.WHITE, gs);
	}
	
	public boolean isTransitionObject() {
		return _transitionObject;
	}
	
	public GameObject(Vector2 pos, Vector2 size, Color color, GameState gs) {
		_gState = gs;
		_gState.AddGameObject(this);
		_alive = true;
		
		_rect = new Rectangle(pos.x, pos.y, size.x, size.y);
		_color = color;
		collideObject = _rect;
		_draw = true;
		_respawnImmunity = 0;
	}
	
	public void setColor(Color color) {
		_color = color;
	}
	
	public boolean collides(GameObject other)
	{
		//TODO make this use a real collideable object
		return _rect.overlaps(other.getRect());
	}
	
	public void setAlive(boolean alive)
	{
		_alive = alive || _respawnImmunity > 0;
	}
	
	public void setImmunity(float value)
	{
		_respawnImmunity = value;
	}
	
	public float getImmunity()
	{
		return _respawnImmunity;	
	}
	
	
	public boolean isAlive()
	{
		return _alive;
	}
	
	
	public void setDraw(boolean value){
		_draw = value;
	}
	
	public boolean getDraw(){
		return _draw;
	}
	
	public void setGameState(GameState gs)
	{
		_gState = gs;
	}
	
	public void setPosition(float x, float y)
	{
		Rectangle bounds = _gState._windowBounds;
		_rect.x = Utils.clamp(x, 0, bounds.x + bounds.width - _rect.width);
		_rect.y = Utils.clamp(y, 0, bounds.y + bounds.height - _rect.height);
	}
	
	public void offset(Vector2 position) {
		_rect.x += position.x;
		_rect.y += position.y;
	}
	
	public boolean inBounds(Rectangle r) {
		return !((_rect.x < r.x) || (_rect.y < r.y)  
				|| ((_rect.x + _rect.width) > (r.x + r.width))
				|| ((_rect.y + _rect.height)> (r.y + r.height)));
	}
	
	public void setSize(float width, float height) {
		_rect.width = width;
		_rect.height = height;		
	}
	
	public void render() {
		render(0);
	}
	
	public void render(float delta) {
		if (!_draw) return;
		Assets.shapes.setColor(_color);
		Assets.shapes.rect(_rect.x, _rect.y, _rect.width, _rect.height);
	}
	
	public Vector2 getUpperLeftPoint()
	{
		return new Vector2(_rect.x,  _rect.y + _rect.height);
	}
	
	public Vector2 getLowerLeftPoint()
	{
		return new Vector2(_rect.x,  _rect.y);
	}
	
	public Vector2 getUpperRightPoint()
	{
		return new Vector2(_rect.x + _rect.width,  _rect.y + _rect.height);
	}
	
	public Vector2 getLowerRightPoint()
	{
		return new Vector2(_rect.x + _rect.width,  _rect.y);
	}
	
	public Vector2 getCenterPosition()
	{
		return new Vector2(_rect.x + _rect.width/2, _rect.y + _rect.height/2);
	}
	
	public Rectangle getRect() { return _rect; }
	public Color getColor() { return _color; }


	public void update(float delta) {}
	
	public void dispose() {}
	
	public boolean collides(Circle circle) {
		return Intersector.overlaps(circle, _rect);
	}
}
