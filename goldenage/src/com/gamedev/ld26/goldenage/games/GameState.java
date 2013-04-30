package com.gamedev.ld26.goldenage.games;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gamedev.ld26.goldenage.Globals;
import com.gamedev.ld26.goldenage.GoldenAgeGame;
import com.gamedev.ld26.goldenage.core.Assets;
import com.gamedev.ld26.goldenage.core.Input;
import com.gamedev.ld26.goldenage.utils.Config;
import com.gamedev.ld26.goldenage.utils.Utils;


public abstract class GameState {

	protected final GoldenAgeGame _game;	
	private GameState _previousGame;
	
	protected Player _player;
	protected ArrayList<GameObject> _gameObjects = new ArrayList<GameObject>();

	public Rectangle _windowBounds = new Rectangle(0,0, Config.window_width, Config.window_height);
	protected boolean _gameWon = false;
	protected Music _stageMusic;
	
	protected GameState(GoldenAgeGame game, GameState previous) {
		_game = game;
		_previousGame = previous;
		setupPlayer(previous);
	}
	
	public Rectangle getBounds() {
		return _windowBounds;
	}
	
	protected void setupPlayer(GameState previous) {
		_player = createPlayer();
		
		float x = Config.window_width / 2;
		float y = 0;
		if (previous != null) 
		{		
			Rectangle playerPosition = previous.getPlayer().getRect();
			x = playerPosition.x;
			y = playerPosition.y;
		}
	
		_player.setPosition(x, y);
	}
	
	protected Player createPlayer() {
		return new Player(new Vector2(100, 20), Color.WHITE, this);
	}
	
	public void AddGameObject(GameObject obj)
	{
		_newObjects.add(obj);
	}
	
	
	public ArrayList<GameObject> getGameObjects()
	{
		return _gameObjects;
	}
	
	public ArrayList<GameObject> _newObjects = new ArrayList<GameObject>();
	public ArrayList<GameObject> _deferredObjects = new ArrayList<GameObject>();
	
	public void update(float delta) {
		if (!_player.isAlive()) {
			if (updateReset(delta)) {
				return;
			}
		}
		
		updatePlayer(delta);
		
		if (_previousGame != null) {
			if (!transitionScreen(delta)) {
				_previousGame.dispose();
				_previousGame = null;
			}
		} else {		
			updateScreen(delta);
		}
		
		for (GameObject obj: _gameObjects)
		{
			obj.update(delta);
		}
		
		//TODO make this a reusable list

		try {
		for (GameObject obj: _gameObjects)
		{
			if (obj._alive){
				_newObjects.add(obj);
			}
			if (obj.deferred) {
				_deferredObjects.add(obj);
			}
		}
		} catch (Exception e){ } 
		_gameObjects = _newObjects;
		_newObjects = new ArrayList<GameObject>();
	}
	
	public Player getPlayer()
	{
		return _player;
	}
	
	public boolean isTransitioning() {
		return (_previousGame != null);
	}
	
	public void dispose() {		
	}
	
	protected void updatePlayer(float delta) {
		_player.setPosition(_game.input.getCurrMouse().x - _player.getRect().width/2, 0);
	}
	
	private float _resetTime = 0;
	
	// override to handle update image
	protected boolean updateReset(float delta) {
		_resetTime += delta;
		
		for (GameObject object : _gameObjects)
		{
			if (object instanceof Explosion)
			{
				object.update(delta);
			}
		}
		
		if (_resetTime > 3f) {
			_resetTime = 0;
			resetScreen();
			return false;
		}
		
		handleReset(delta);
		return true;
	}
	
	protected void handleReset(float delta) { }
	
	protected void resetScreen() { 
		_player.setAlive(true);
	}
	
	protected abstract void updateScreen(float delta);
	
	public void render(float delta) {	
		Assets.shapes.begin(ShapeType.Filled);

		renderScreen(delta);
		render(_player, delta);
		//try {
			for (GameObject object : _gameObjects)
			{
				if (object.deferred) {
					continue;
				}
				render(object, delta);
			}
			
			for (GameObject object : _deferredObjects) {
				render(object, delta);
			}
			_deferredObjects = new ArrayList<GameObject>();
		//} catch (Exception e) { 

		//	System.out.println(e.getMessage());
	
		//} 
		Assets.shapes.end();
		if(isTransitioning())
		{
			for (Transition pt : _objectTransitions) {
				pt.Render();
			}
		}
	}
	
	protected void render(GameObject gameObject, float delta) {
		if (gameObject != null && gameObject.isAlive()) {
			gameObject.render(delta);
		}
		

	}
	
	protected abstract void renderScreen(float delta);

	public boolean getGameWon() {
		return _gameWon;
	}
	
	public Music getMusic()
	{
		return _stageMusic;
	}
	
	public Input getInput() {
		return _game.input;
	}
	
	private float _transition = 0;	
	protected float _transitionTime = 1.5f;
	private ArrayList<Transition> _objectTransitions = new ArrayList<Transition>();
	
	protected void addTransition(Transition trans) {
		_objectTransitions.add(trans);
	}
	
	protected void clearTransitions(){
		_objectTransitions.clear();
	}
		
	protected boolean transitionScreen(float delta) {
		_transition += delta;
		
		for (Transition pt : _objectTransitions) {
			pt.Update(delta);
		}
		
		return (_transition < _transitionTime);
	}
	
	protected GameObject createScaleTransition() {
		Vector2 pos = getTransPosition();
		Vector2 size = getTransSize();		
		Color color = Utils.getRandomColor();
		GameObject object = new GameObject(pos, size, color, this);		
		object.IsTemporary = true; 
		return object;
	}
	
	protected Vector2 getTransPosition()	{
		Random rand = Assets.random;
		float x, y;
		
		switch (rand.nextInt(3)) {
			case 1:
				x = _windowBounds.x - rand.nextInt(100);
				y = getSide(100);
				break;
			case 2:
				x = _windowBounds.x + _windowBounds.height + rand.nextInt(100);
				y = getSide(100);
				break;
			default:
				x = getTop(0);
				y = (rand.nextBoolean()) ? _windowBounds.y - rand.nextInt(100) 
						: _windowBounds.y + _windowBounds.height + rand.nextInt(100); 
				break;
		}
				
		return new Vector2(x, y);
	}
	
	protected Vector2 getTransSize() {
		float size = 20 + Assets.random.nextInt(20);
		return new Vector2(size, size);
	}
	
	private float getTop(float extra) {
		return -extra + ((_windowBounds.width + (extra*2)) * Assets.random.nextFloat());
	}
	
	private float getSide(float extra) {
		return -extra + ((_windowBounds.height + (extra*2)) * Assets.random.nextFloat());
	}

	// For debugging to skip to next game
	public void setGameWon() { _gameWon = true; }
	
	public abstract Globals.Games nextScreen();
}
