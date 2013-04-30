package com.gamedev.ld26.goldenage.games.end;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.gamedev.ld26.goldenage.Globals;
import com.gamedev.ld26.goldenage.GoldenAgeGame;
import com.gamedev.ld26.goldenage.core.Assets;
import com.gamedev.ld26.goldenage.core.Score;
import com.gamedev.ld26.goldenage.games.GameState;
import com.gamedev.ld26.goldenage.utils.Utils;
import com.gamedev.ld26.goldenage.utils.Utils.STRING_JUSTIFICATION;

public class EndScreen extends GameState {
	
	public class TextInfo {
		public String Text;
		public String Text2;
		public int Size;
		public Color Color;
	}
	
	private ArrayList<TextInfo> _text = new ArrayList<TextInfo>();
	
	public EndScreen(GoldenAgeGame game, GameState previous) {
		super(game, previous);
		 _stageMusic= Assets.endScreenMusic;
		
		int score = Score.GetScore();
		int s1 = score + 2 + Assets.random.nextInt(100000);
		int s2 = s1 + 1 + Assets.random.nextInt(900000);
		
		add("VANITY BOARD", 50, Color.WHITE);
		
		add("High Scores", 40, Color.ORANGE);
		add("1) "+ getRandomDev(), "" + s2, 30, Color.WHITE);
		add("2) "+ getRandomDev(), "" + s1, 30, Color.WHITE);
		add("3) "+ getRandom(), "" + (score + 1), 30, Color.WHITE);
		add("Your score " + score, 30, Color.RED);
		
		int time = (int)Score.getTime();
		// they need at least one second
		time = Math.max(time,  1);
			
		int t1 = time - 2 - Assets.random.nextInt(50);
		int t2 = t1 - Assets.random.nextInt(100);
		
		t1 = Math.max(t1, 0);
		t2 = Math.max(t2, 0);
	
		add("Shortest Times", 40, Color.ORANGE);
		add("1) "+ getRandomDev(), t2 + " seconds", 30, Color.WHITE);
		add("2) "+ getRandomDev(), t1 + " seconds", 30, Color.WHITE);
		add("3) "+ getRandom(), (time - 1) + " seconds", 30, Color.WHITE);
		add("Your time " + time + " seconds", 30, Color.RED);
		
		int lives = Score.getLostLives();
		int l1 = lives - 1;
		int l2 = l1 - Assets.random.nextInt(2);
		int l3 = l2 - Assets.random.nextInt(4);
		// if l3 > 0, set to 0, if less than 0, leave - cause it's fucking funny
		l3 = Math.min(l3,  0);		
		
		add("Lives Lost", 40, Color.ORANGE);
		add("1) "+ getRandomDev(), l3 + " lives", 30, Color.WHITE);
		add("2) "+ getRandomDev(), l2 + " lives", 30, Color.WHITE);
		add("3) "+ getRandom(), l1 + " lives", 30, Color.WHITE);
		add("You lost " + lives + " lives", 30, Color.RED);
		
		add("You almost made it! Better luck next time", 20, Color.WHITE);
	}
		
	private void add(String text, int size, Color color) {
		TextInfo textInfo = new TextInfo();
		textInfo.Text = text;
		textInfo.Size = size;
		textInfo.Color = color;
		
		_text.add(textInfo);
	}
	
	private void add(String t1, String t2, int size, Color color) {
		TextInfo textInfo = new TextInfo();
		textInfo.Text = t1;
		textInfo.Text2 = t2;
		textInfo.Size = size;
		textInfo.Color = color;
		
		_text.add(textInfo);
	}
	
	private static final String[] DevList = new String[] { "BAR", "DSG", "BRP" };

	private String getRandomDev() {
		return getRandom(DevList);
	}
	
	private static final String[] RandoList = new String[] 
		 { "BAR", "DSG", "BRP", "KE ", "ZJR", "CCR" };
	
	private String getRandom() {
		return getRandom(RandoList);
	}
	
	private String getRandom(String[] nameList){
		return nameList[Assets.random.nextInt(nameList.length)];
	}

	@Override
	protected void updateScreen(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void renderScreen(float delta) {
		// TODO Auto-generated method stub
		
	}
	
	public void update(float delta) {
		
	}
	
	public void render(float delta) {
		float y = (_windowBounds.y + _windowBounds.height - 60);
		float centerx = _windowBounds.x + _windowBounds.width / 2;
		float leftx = centerx - 375;
		float rightx = centerx + 375;
		for (TextInfo ti : _text) {
			int size = ti.Size;
			
			if (size > 30) {
				y -= 10;
			}
			
			if (ti.Text2 != null) {
				Utils.drawText(ti.Text, leftx, y, size, size, ti.Color, STRING_JUSTIFICATION.LEFT);
				Utils.drawText(ti.Text2, rightx, y, size, size, ti.Color, STRING_JUSTIFICATION.RIGHT);
			} else {
				Utils.drawText(ti.Text, centerx, y, size, size, ti.Color, STRING_JUSTIFICATION.CENTER);
			}
			y -= (size + 5);
		}
	}
	
	public Globals.Games nextScreen() {
		return Globals.Games.pong;
	}
}
