package com.gamedev.ld26.goldenage.games.G1942;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.gamedev.ld26.goldenage.Globals;
import com.gamedev.ld26.goldenage.GoldenAgeGame;
import com.gamedev.ld26.goldenage.core.Assets;
import com.gamedev.ld26.goldenage.core.Score;
import com.gamedev.ld26.goldenage.games.Bullet;
import com.gamedev.ld26.goldenage.games.BulletFactory;
import com.gamedev.ld26.goldenage.games.GameObject;
import com.gamedev.ld26.goldenage.games.GameState;
import com.gamedev.ld26.goldenage.games.Player;
import com.gamedev.ld26.goldenage.games.PlayerTransition;
import com.gamedev.ld26.goldenage.games.TextTransition;
import com.gamedev.ld26.goldenage.utils.Config;
import com.gamedev.ld26.goldenage.utils.Utils;
import com.gamedev.ld26.goldenage.utils.Utils.STRING_JUSTIFICATION;

public class G1942State extends GameState {

	private final float GUN_INTERVAL = .15f; 
	private final int SHIPS_TO_KILL = 40;
	
	static final Vector2 size = new Vector2(80, 80);
	private BulletFactory _bulletFactory;
	private float _respawnTime = 0;
	private int _shipsKilled;
	private PlaneSpawnInfo _leftCircleSpawner;
	private PlaneSpawnInfo _rightCircleSpawner;
	private boolean _bossSpawned;
	private BossPlane _boss;
	private float _gunDelay;
	private MovingBackground back1;
	private MovingBackground back2;
	private final Color _backGroundColor1 = new Color(0,0f,.3f,1f);
	private final Color _backGroundColor2 = new Color(0,.3f,0,1f);
	
	public G1942State(GoldenAgeGame game, GameState previous) {
		super(game, previous);
		// TODO Auto-generated constructor stub
		_bulletFactory = new BulletFactory(this, Color.GRAY, 3);
		_windowBounds = Config.g1942_window_bounds;
		_shipsKilled = 0;
		_gunDelay = 0;
		_leftCircleSpawner = new PlaneSpawnInfo(15, .15f, 10f);
		_rightCircleSpawner = new PlaneSpawnInfo(15, .15f, 12f);
		_rightCircleSpawner.SetSpawnTime(15f);
		back1 = new MovingBackground(_windowBounds, _backGroundColor1, _backGroundColor2);
		back2 = new MovingBackground(_windowBounds, _backGroundColor2, _backGroundColor1);
		back2.SetYOffset(_windowBounds.height);
		_player.setImmunity(0);
		
		setupTransition(previous);
		_bossSpawned = false;
		_stageMusic = Assets.g1942Music;
		
	}

	@Override
	protected void updateScreen(float delta) {
		if (_boss != null && !_boss.isAlive())
		{
			_gameWon = true;
		}
		
		if (_respawnTime > 0)
		{
			respawnPlayer(delta);
		}
		if (_gunDelay > 0) _gunDelay -= delta;
		if (_player.getImmunity() > 0) 
		{
			_player.setImmunity(_player.getImmunity() - delta);
			_player.setDraw((int)(_player.getImmunity() * 5) % 2 == 0);
		}
		else {
			_player.setDraw(true);
		}
		
		if (_game.input.isButtonDown(0) && _player.getImmunity() <=0 &&  _gunDelay <= 0 && _player.isAlive()) {
			_bulletFactory.GetBullet(_player);
			_gunDelay = GUN_INTERVAL;
		}	
		
		if (!_bossSpawned)
		{
			if (Assets.random.nextDouble() > .98)
			{
				Plane plane = new Plane(new Vector2(Assets.random.nextFloat() * Config.window_width,Config.window_height), new Vector2(20,20), Color.WHITE, this, _bulletFactory);
				//AddGameObject(plane);
			}
			spawnCircleShips(delta);
		}
		if (_shipsKilled > SHIPS_TO_KILL && !_bossSpawned)
		{
			_bossSpawned = true;
			_boss = new BossPlane(Color.WHITE, this, _bulletFactory);
		}
		
		checkCollisions();
		back1.Update(delta);
		back2.Update(delta);
		
		// Fuck it a catch all
		if (!_player.isAlive() && _respawnTime <= 0)
		{
			_respawnTime = 2f;
		}
	}
	
	protected Player createPlayer() {
		return new PlanePlayer(this);
	}
	
	private void spawnCircleShips(float dt)
	{	
		if (_leftCircleSpawner.ShouldSpawn(dt))
		{
			CirclePlane cirPlane = new CirclePlane(new Vector2(0,500), new Vector2(20,20), Color.RED, this, _bulletFactory);
		}
		if (_rightCircleSpawner.ShouldSpawn(dt))
		{
			CirclePlane cirPlane = new CirclePlane(new Vector2(Config.window_width,400), new Vector2(20,20), Color.BLUE, this, _bulletFactory);
		}
	}
	
	private void checkCollisions()
	{
		for (GameObject obj: _gameObjects)
		{
			if (obj.getClass() == Bullet.class)
			{
				if (!obj.isAlive()) continue;
				Bullet bullet = (Bullet)obj;
				if (bullet.getTarget() == _player)
				{
//					if (bullet.collides(_player))
//					{
//						bullet.setAlive(false);
//						killPlayer();
//					}
				}
				else { // Must be from player
					for (GameObject tar : _gameObjects)
					{
						if (tar instanceof Plane && obj.collides(tar))
						{
							
							obj.setAlive(false);
							if (((Plane)tar).gotHit())
							{
								_shipsKilled++;
								Score.AddToScore(tar.ScoreValue);
							}
						}
					}
				}
			}
			else if (obj instanceof Plane)
			{
				if (_player.isAlive() && _player.getImmunity() <= 0 && obj.collides(_player))
				{
					killPlayer();
					if (((Plane)obj).gotHit())
						_shipsKilled++;
				}
			}
		}
	}

	private void killPlayer()
	{
		_respawnTime = 3f;
		_player.setAlive(false);
	}
	
	private void respawnPlayer(float dt){
		_respawnTime -= dt;
		if (_respawnTime <= 0)
		{
			_player.setImmunity(2f);
			_player.setAlive(true);
			AddGameObject(_player);
		}
	}

	protected boolean updateReset(float delta) {
		return false;
	}
	
	
	protected void updatePlayer(float delta) {
		_player.setPosition(_game.input.getCurrMouse().x - _player.getRect().width/2, Config.window_height - _game.input.getCurrMouse().y);
	}
	
	@Override
	protected void renderScreen(float delta) {
		back1.render();
		back2.render();
		Assets.shapes.end();
		Utils.drawText(Score.getScoreString(5), 10, Config.window_height - 40, 20, 20, new Color(1f,1f,1f,1f), STRING_JUSTIFICATION.LEFT);
		Utils.drawText(Score.getLivesString(), Config.window_width - 10, Config.window_height - 40, 20, 20, new Color(1f,1f,1f,1f), STRING_JUSTIFICATION.RIGHT);

		Assets.shapes.begin(ShapeType.Filled);
	}
		
	private void setupTransition(GameState previousScreen) {
		if (previousScreen == null) return;
		_transitionTime = 3.0f;
		_player.setDraw(false);
				
		for (GameObject obj : previousScreen.getGameObjects()) {
			
			if (obj.isAlive()) {
				if ((obj.isTransitionObject())) {
					addTransition(new PlayerTransition(createScaleTransition(), obj, _transitionTime/2));
				}
			}
		}
		
		addTransition(new PlayerTransition(_player,  previousScreen.getPlayer(), _transitionTime, false));
		String textString = "Welcome to 1984";
		addTransition(new TextTransition(textString,
				new Vector2(Config.window_half_width - (textString.length() * 30 /2.0f), Config.window_half_height), Color.RED, _transitionTime));
		String textString2 = "Gradients?! Witchcraft!!!";
		addTransition(new TextTransition(textString2,
				new Vector2(Config.window_half_width - (textString2.length() * 30 /2.0f), Config.window_half_height + 40), Color.GRAY, 20.f));
	}
	
	public Globals.Games nextScreen() {
		return Globals.Games.glitch;
	}
}
