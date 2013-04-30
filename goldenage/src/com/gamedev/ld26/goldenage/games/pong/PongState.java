package com.gamedev.ld26.goldenage.games.pong;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gamedev.ld26.goldenage.Globals;
import com.gamedev.ld26.goldenage.GoldenAgeGame;
import com.gamedev.ld26.goldenage.core.Assets;
import com.gamedev.ld26.goldenage.core.Score;
import com.gamedev.ld26.goldenage.games.Ball;
import com.gamedev.ld26.goldenage.games.GameState;
import com.gamedev.ld26.goldenage.games.Player;
import com.gamedev.ld26.goldenage.utils.Config;
import com.gamedev.ld26.goldenage.utils.Utils;
import com.gamedev.ld26.goldenage.utils.Utils.STRING_JUSTIFICATION;

public class PongState extends GameState {

	private static final float div_width  = 16;
	private static final float div_height = 16;	
	private static final float respawn_speed = 300.f;
	private static final int win_score = 3;
	private static final Vector2 size = new Vector2(Config.pong_paddle_size_x, Config.pong_paddle_size_y);
	
	private Rectangle _divider;
	private Rectangle _edgeLeft;
	private Rectangle _edgeRight;
	
	private Player _cpu;
	private Ball _ball;
	
	private int _playerScore;
	private int _cpuScore;
	
	private boolean _countdown;
	private float _counter;
	
	
	public PongState(GoldenAgeGame game, GameState previous) {
		super(game, previous);
		_windowBounds = Config.pong_window_bounds;
		_divider = new Rectangle(Config.pong_window_bounds.x + div_width / 2, Config.window_half_height - div_height / 2, div_width, div_height);
		_edgeLeft = new Rectangle(Config.pong_window_bounds.x - 20, 0, 20, Config.window_height);
		_edgeRight = new Rectangle(Config.pong_window_bounds.x + Config.pong_window_bounds.width, 0, 20, Config.window_height);
		
		_cpu = new Player(size, Color.WHITE, this);
		_cpu.setPosition(Config.window_half_width, Config.window_height - size.y);
		
		_ball = new Ball(new Vector2(Config.window_half_width, Config.window_half_height), 10, Color.WHITE, this);
		_ball.setSquare(true);
		_ball._dontUpdate = true; // wait to update till countdown is over
		
		_playerScore = 0;
		_cpuScore = 0;
		_stageMusic = Assets.pongMusic;
		_transitionTime =0;
		Score.ResetScore();
		
		_countdown = true;
		_counter = 3.f;
		Assets.countdown321.play();
	}

	@Override
	protected void updateScreen(float delta) {
		if (_countdown) {
			_counter -= delta;
			if (_counter <= 0.f) {
				_countdown = false;
				_ball._dontUpdate = false;
			} else {
				Utils.constrainToRect(_player, Config.pong_window_bounds);
				return;
			}
		}
		//_ball.update(Gdx.graphics.getDeltaTime());
		checkForScore();		
		
		updateCpu(delta);
		
		Utils.constrainToRect(_player, Config.pong_window_bounds);
		Utils.constrainToRect(_cpu, Config.pong_window_bounds);
		Utils.constrainToRect(_ball, Config.pong_window_bounds);
		handleCollisions();
	}

	@Override
	protected void renderScreen(float delta) {
		_cpu.render(delta);
		_ball.render(delta);
		drawExtras();
		drawScores();
		if (_countdown) {
			if (_counter > 2.f) {
				Utils.drawText("3", Config.window_half_width, Config.window_half_height + 100, 64, 100, Color.WHITE, Utils.STRING_JUSTIFICATION.CENTER);
			} else if (_counter > 1.f) {
				Utils.drawText("2", Config.window_half_width, Config.window_half_height + 100, 64, 100, Color.WHITE, Utils.STRING_JUSTIFICATION.CENTER);
			} else {
				Utils.drawText("1", Config.window_half_width, Config.window_half_height + 100, 64, 100, Color.WHITE, Utils.STRING_JUSTIFICATION.CENTER);
			}
		}
	}	

	private void checkForScore() {
		if (_ball.getCircle().y <= 0) {
			_ball.setAlive(false);
			_cpuScore++;
			Assets.pongCpuScoreSound.play();
			Score.loseLife();
		}
		
		if (_ball.getCircle().y >= Config.window_height){
			_ball.setAlive(false);
			_playerScore++;
			Assets.pongPlayerScoreSound.play();
			Score.AddToScore(1);
		}
		
		if (_playerScore == win_score) {
			_gameWon = true;
			_ball.setAlive(true);
			return;
		}
		
		if (!_ball.isAlive()) {
			_ball = new Ball(new Vector2(Config.window_half_width, Config.window_half_height), 10, Color.WHITE, this);
			_ball.setSquare(true);
			_ball.setSpeed(respawn_speed);
		}
	}
	
	private void updateCpu(float delta) {
		Circle ball = _ball.getCircle();
		Rectangle rect = _cpu.getRect();
		Vector2 center = new Vector2(rect.x + rect.width / 2, rect.y + rect.height / 2);
		
		// Move cpu paddle towards ball with variable speed when ball is coming towards paddle
		if (_ball.getDir().y > 0) {
			float diff = ball.x - center.x;
			float dir = (float) Math.signum(diff);
			float scale = (float) Math.min(1.f, Assets.random.nextFloat() + 0.3f);
			if (Math.abs(diff) < rect.width / 2) {
				scale = 0;
			}
			rect.x += (dir * delta * _ball.getSpeed() * scale);			
		}
	}
	
	private void handleCollisions() {
		final Rectangle playerRect = _player.getRect();
		final Rectangle cpuRect = _cpu.getRect();
		Circle ballCircle = _ball.getCircle();
		Vector2 ballDir = _ball.getDir();
		
		// Intersection tests - ball/paddle
		if (Intersector.overlaps(ballCircle, playerRect)) {
			ballCircle.y = playerRect.y + playerRect.height + ballCircle.radius;
			ballDir.y *= -1;
			float hitPos = (ballCircle.x - playerRect.x) / playerRect.width;
			ballDir.x += hitPos - .5f;
			_ball.setSpeed(_ball.getSpeed() + 50.f);
			Assets.beep.play();
		}
		
		if (Intersector.overlaps(_ball.getCircle(), cpuRect)) {
			ballCircle.y = cpuRect.y - ballCircle.radius;
			ballDir.y *= -1;
			float hitPos = (ballCircle.x - cpuRect.x) / cpuRect.width;
			ballDir.x += hitPos - .5f;
			_ball.setSpeed(_ball.getSpeed() + 50.f);
			Assets.beep.play();
		}
	}

	private void drawExtras() {
		Assets.shapes.setColor(Color.WHITE);
		float w = _divider.width * 2;
		int num = (int) (Config.pong_window_bounds.width / w);
		for(int i = 0; i < num; ++i) {
			float x = _divider.x + i * w;
			Assets.shapes.rect(x, _divider.y, _divider.width, _divider.height);
		}
		Assets.shapes.rect(_edgeLeft.x, _edgeLeft.y, _edgeLeft.width, _edgeLeft.height);
		Assets.shapes.rect(_edgeRight.x, _edgeRight.y, _edgeRight.width, _edgeRight.height);
	}
	
	private void drawScores() {
		String s1 = "" + _cpuScore;
		String s2 = "" + _playerScore;
		int cw = 40;
		int ch = 64;
		float x1 = Config.window_half_width / 4;
		float y1 = Config.window_height - ch - 32;
		float x2 = Config.window_width - x1 - cw;
		float y2 = ch;
		Utils.drawText(s1, x1, y1, cw, ch, Color.WHITE, STRING_JUSTIFICATION.LEFT);
		Utils.drawText(s2, x2, y2, cw, ch, Color.WHITE, STRING_JUSTIFICATION.LEFT);
	}

	public Globals.Games nextScreen() {
		return Globals.Games.breakout;
	}
}
