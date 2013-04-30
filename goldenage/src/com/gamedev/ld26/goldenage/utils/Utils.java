package com.gamedev.ld26.goldenage.utils;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gamedev.ld26.goldenage.core.Assets;
import com.gamedev.ld26.goldenage.games.GameObject;

public class Utils {

	private static final String symbols = ",.!?'\"-+=/\\%()<>:;";
	private static Music currentMusic;
	public static enum STRING_JUSTIFICATION { LEFT, CENTER, RIGHT };
	
	public static void drawText(String text, float x, float y, int w, int h, Color color, STRING_JUSTIFICATION justify) {
		text = text.toUpperCase();
		int s = 2; // spacing between characters (pixels)
		com.gamedev.ld26.goldenage.core.Assets.batch.begin();
		Assets.batch.setColor(color);
		float totalWidth = text.length() * w;
		switch (justify)
		{
		case LEFT:
			break;
		case CENTER:
			
			x -= totalWidth/2;
			break;
		case RIGHT:
			 x-= totalWidth;
			break;
		}
		for(int i = 0; i < text.length(); ++i) {
			char ch = text.charAt(i);
			float xPos = x + i * w + s;
			float yPos = y;
			
			if (ch >= 'A' && ch <= 'Z') {
				Assets.batch.draw(Assets.letters[0][ch - 'A'], xPos, yPos, w, h);
			} else if (ch >= '0' && ch <= '9') {
				Assets.batch.draw(Assets.digits[0][ch - '0'], xPos, yPos, w, h);
			} else {
				int index = symbols.indexOf(ch);
				if (index != -1) {
					Assets.batch.draw(Assets.symbols[0][index], xPos, yPos, w, h);
				}
			}
		}
		Assets.batch.setColor(Color.WHITE);
		Assets.batch.end();
	}
	
	public static void PlayMusic(Music music)
	{
		if (currentMusic != null)
		{
			currentMusic.stop();
		}
		
		currentMusic = music;
		
		if (music != null)
		{
			currentMusic.setLooping(true);
			currentMusic.play();
			currentMusic.setVolume(.3f);
		}
	}
	
	public static float clamp(float value, float min, float max)
	{
		return Math.max(Math.min(value, max), min);
	}
	
	public static void constrainToRect(GameObject object, Rectangle bounds) {
		Rectangle r = object.getRect();
		r.x = clamp(r.x, bounds.x, bounds.x + bounds.width - r.width);
		r.y = clamp(r.y, bounds.y, bounds.y + bounds.height - r.height);
	}
	
	public static Vector2 lerpVector2(Vector2 initial, Vector2 dest, float amount)
	{
		float dx = dest.x - initial.x;
		float dy = dest.y - initial.y;
		dx *= amount;
		dy *= amount;
		return new Vector2(initial.x + dx, initial.y + dy);
	}
	
	public static float toRadians(float degrees)
	{
		return degrees /180.0f * (float)Math.PI;
	}
	
	public static float lerp(float initial, float dest, float amount) {
		return initial + amount * (dest - initial);
	}
	
	public static Vector2 rectCenter(Rectangle rect)
	{
		return new Vector2(rect.x + ( rect.width/2), rect.y + (rect.height/2));
	}
	
	public static void print(Exception e) {
		System.out.println(e.getMessage());
		System.out.println(e.getStackTrace());
	}
	
	public static Color getRandomColor() {
		return getRandomColor(1f);
	}
	
	public static Color getRandomColor(float alpha) {
		return new Color(Assets.random.nextFloat(), Assets.random.nextFloat(), Assets.random.nextFloat(), alpha);
	}

	
	public static Color blend(Color color1, Color color2) {
		Color returnColor = new Color(color1);
		returnColor.lerp(color2,  0.5f);
		return returnColor;
	}
}
