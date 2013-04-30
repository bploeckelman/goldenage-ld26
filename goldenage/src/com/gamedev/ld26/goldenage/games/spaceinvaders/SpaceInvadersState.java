package com.gamedev.ld26.goldenage.games.spaceinvaders;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gamedev.ld26.goldenage.DeltaTimer;
import com.gamedev.ld26.goldenage.Globals;
import com.gamedev.ld26.goldenage.GoldenAgeGame;
import com.gamedev.ld26.goldenage.TimerListener;
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

public class SpaceInvadersState extends GameState implements TimerListener {

	private final int Right = 0;
	private final int RightDown = 1;
	private final int Left = 2;
	private final int LeftDown = 3;
	
	private final int Rows = 5;
	private final int Columns = 15;
	
	private final int WinCount = 20;
	private final int MaxSpeed = 350;
	
	private Bullet _bullet;
	
	private ArrayList<BaseInvader> _aliens = new ArrayList<BaseInvader>(); 
	private int _alienSpeed;
	private int _downHeight = -30;
	private int _dy;
	private BulletFactory _bulletFactory;
	private Rectangle _alienBounds;
	
	private DeltaTimer _timer;
	private int _movement = Right;
	
	public SpaceInvadersState(GoldenAgeGame game, GameState previous) {
		super(game, previous);
		setupScreen();
	    setupTransition(previous);
	    _stageMusic= Assets.spaceInvMusic;
	}
	
	protected Player createPlayer() {
		return new SpacePlayer(this);
	}
	
	private void killMotherShip() {
		if (_motherShip == null) return;
		_motherShip.setAlive(false);
	}
	
	public void dispose() {
		killMotherShip();
	}
	
	private void setupScreen() {
		_bulletFactory = new BulletFactory(this, Color.GRAY, 6);
		_timer = new DeltaTimer(this, 0.5f);
		
		Color[] alienColor = new Color[] { Color.CYAN, Utils.blend(Color.CYAN, Color.MAGENTA),
				Color.MAGENTA, Utils.blend(Color.MAGENTA, Color.YELLOW), Color.YELLOW };
		
		float playerHeight = _player.getRect().height;
		
		_alienBounds = new Rectangle(_windowBounds.x + 10, playerHeight, _windowBounds.width - 20, _windowBounds.height - playerHeight);
				
		for (int y = 0; y < Rows; y++) {
			int rowValue = Rows - y;
			int rotation = rowValue + 1;
			int score =  rowValue*rowValue;
			for (int x = 0; x < Columns; x++) {
				BaseInvader invader = new BaseInvader(this, 35, alienColor[y]);
				invader.ScoreValue = score;
				invader.Rotation = rotation;
				_aliens.add(invader);
			}
		}
		
		resetScreen();
	}

	private float _shipTime;
	private MotherShip _motherShip;
	
	@Override
	protected void updateScreen(float delta) {	
		if (_game.input.isButtonDown(0) && !bulletExists()) {
			_bullet = _bulletFactory.GetBullet(_player);
		}		
		
		if (_motherShip == null) {
			_shipTime += delta;
			if (_shipTime > 7 && Assets.random.nextInt(100) < 5) {
				_shipTime = 0;
				_motherShip = new MotherShip(this);
			}
		} else {
			if (_bullet != null && _bullet.collides(_motherShip)) {
				_motherShip.kill();
				Score.AddToScore(1000);
			}
			
			if (!_motherShip.isAlive()) {
				_motherShip = null;
			}
		}
		
		
		
		_timer.Update(delta);
		
		Vector2 offset = GetOffset(delta);
		
		boolean changeDirections = false;
		boolean onBottom = false;
		
		int aliens = 0;
		
		for (BaseInvader alien : _aliens) {
			if (alien.isAlive()) {
				if (checkBullet(alien)) continue;
				
				alien.offset(offset);
				aliens++;
				
				if (!changeDirections) {
					changeDirections |= checkBounds(alien);
				}
								
				onBottom |= (alien.getRect().y < _alienBounds.y);

				if (shouldFire()){
					Bullet bullet = _bulletFactory.GetBullet(alien);
					bullet.setTarget(_player);
				}
			}
		}
		
		_alienSpeed = (int)(MaxSpeed * ((float)WinCount / aliens));
		
		if (onBottom) {
			_player.setAlive(false);
			return;
		}
		
		if (aliens <= WinCount) {
			_gameWon = true;
			return;
		}
		
		changeDirection(changeDirections || (_dy < _downHeight));
	}
	
	private boolean checkBounds(GameObject object) {
		switch (_movement){
			case Left:
				return (object.getLowerLeftPoint().x <= _alienBounds.x);
			case Right:
				return (object.getLowerRightPoint().x >= (_alienBounds.x + _alienBounds.width));		
		}
		return false;
	}
	
	protected void resetScreen() {
		_movement = Right;
		
		int alienWidth = 950;
		 
		float hgap = alienWidth / (Columns + 1);
		float vgap = 300 / (Rows + 1);
		
		float xPos = _windowBounds.x + ((_windowBounds.width - alienWidth)/2) + hgap;
		float initX = xPos;
		float yPos = _windowBounds.height - vgap - 50;
				
		int index = 0;
		for (int y = 0; y < Rows; y++) {
			for (int x = 0; x < Columns; x++) {
				_aliens.get(index++).setPosition(xPos, yPos);
				xPos += hgap;
			}
			xPos = initX;
			yPos -= vgap;
		}
		
		super.resetScreen();
	}
	
	private boolean bulletExists()
	{
		return (_bullet != null) && (_bullet.isAlive());
	}
	
	private boolean checkBullet(BaseInvader alien) {
		if (bulletExists()) {
			if (_bullet.collides(alien)) {
				alien.setAlive(false);
				Score.AddToScore(alien.ScoreValue);
				_bullet.setAlive(false);
				return true;
			}
		}
		return false;
	}
	
	private boolean shouldFire() {
		if (_alienFire) {
			if (Assets.random.nextInt(100) < 5) {
				_alienFire = false;
				return true;
			}
		}
		return false;
	}
	
	private void changeDirection(boolean change) {
		if (!change) return;
		
		if (++_movement == 4) {
			_movement = 0;
		}
	}
	
	private Vector2 GetOffset(float delta) {
		float dy = 0;
		float dx = 0;
		
		if (_movement == LeftDown || _movement == RightDown) {
			float tempDy = delta*_alienSpeed;
			_dy -= tempDy;
			if (_dy < _downHeight) {
				dy = _dy - _downHeight;
			} else {
				dy = -tempDy;
			}
		} else {
			_dy = 0;
			dx = delta*_alienSpeed;
			if (_movement == Left) {
				dx = -dx;
			}			
		}
		
		return new Vector2(dx, dy);
	}
	
	private void setupTransition(GameState previousScreen) {
		if (previousScreen == null) return;
		_player.setDraw(false);
		
		int index = 0;
		
		for (GameObject obj : previousScreen.getGameObjects()) {
			
			if (obj.isAlive()) {
				if ((obj.isTransitionObject()) && (index < _aliens.size())) {
					addTransition(new PlayerTransition(_aliens.get(index++), obj, _transitionTime));
				}
			}
		}
		
		addTransition(new PlayerTransition(_player,  previousScreen.getPlayer(), _transitionTime, false));
		String textString = "Welcome to 1978";
		addTransition(new TextTransition(textString,
				new Vector2(Config.window_half_width - (textString.length() * 30 /2.0f), Config.window_half_height), Color.RED, _transitionTime));
		String textString2 = "Color?!  You've got to be kidding me!";
		addTransition(new TextTransition(textString2,
				new Vector2(Config.window_half_width - (textString2.length() * 30 /2.0f), Config.window_half_height + 40), Color.GRAY, 20.f));
	}
	
	protected void handleReset(float delta) {
		for (GameObject bullet : getGameObjects()) {
			if (bullet.getClass() == Bullet.class) {
				bullet.setAlive(false);
			}
		}
		
		killMotherShip();
	}

	@Override
	protected void renderScreen(float delta) {
		if (_motherShip == null) {
			Utils.drawText(Score.getScoreString(4), 10, Config.window_height - 40, 20, 20, new Color(1f,1f,1f,1f), STRING_JUSTIFICATION.LEFT);
			Utils.drawText(Score.getLivesString(), Config.window_width - 10, Config.window_height - 40, 20, 20, new Color(1f,1f,1f,1f), STRING_JUSTIFICATION.RIGHT);
		}
	}

	private boolean _alienFire = false;
	@Override
	public void OnTimer() {
		_alienFire = true;
	}
	
	public Globals.Games nextScreen() {
		return Globals.Games.g1942;
	}
}
