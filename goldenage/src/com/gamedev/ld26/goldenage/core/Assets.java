package com.gamedev.ld26.goldenage.core;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Assets {
	
	public static Random random;
	public static SpriteBatch batch;
	public static ShapeRenderer shapes;
	
	public static Texture sheet;
	public static Texture background;
	public static Texture potato;
	public static Texture baked_potato;
	public static Texture glitchTexture;
	
	public static Sound wat;
	public static Sound beep;
	public static Sound lifeLostSound;
	public static Sound laserSound;
	public static Sound laser1Sound;
	public static Sound bricksSound;
	public static Sound buzzSound;
	public static Sound wobbleSound;
	public static Sound missileSound;
	public static Sound glitchSound;
	public static Sound coinReturnSound;
	public static Sound coinHitSound;
	public static Sound countdown321;
	
	public static Sound pongCpuScoreSound;
	public static Sound pongPlayerScoreSound;
	public static Sound pongWallBounceSound;
	
	public static Sound siPlayerLaser;
	public static Sound siAlienLaser;
	public static Sound siMothership;
	
	public static Sound planeShot;
	public static Sound bossShot;
	public static Sound g1942PlayerLaser;
	
	public static Music pongMusic;
	public static Music breakoutMusic;
	public static Music spaceInvMusic;
	public static Music g1942Music;
	public static Music endScreenMusic;
	public static Music titleMusic;
	
	public static TextureRegion[][] letters;
	public static TextureRegion[][] digits;
	public static TextureRegion[][] symbols;
	
	public static void load() {
		random = new Random();
		batch  = new SpriteBatch();
		shapes = new ShapeRenderer();
		
		sheet  = new Texture(Gdx.files.internal("data/spritesheet.png"));
		sheet.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		background = new Texture(Gdx.files.internal("data/background.png"));
		background.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);	
		
		potato = new Texture(Gdx.files.internal("data/potato.png"));
		potato.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		baked_potato = new Texture(Gdx.files.internal("data/baked_potato.png"));
		baked_potato.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		glitchTexture = new Texture(Gdx.files.internal("data/glitch.png"));
		glitchTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		wat = Gdx.audio.newSound(Gdx.files.internal("data/audio/wat.wav"));
		beep = Gdx.audio.newSound(Gdx.files.internal("data/audio/beep.wav"));
		bricksSound = Gdx.audio.newSound(Gdx.files.internal("data/audio/brickhit.wav"));
		buzzSound = Gdx.audio.newSound(Gdx.files.internal("data/audio/buzz.wav"));
		laserSound = Gdx.audio.newSound(Gdx.files.internal("data/audio/laser2.wav"));
		laser1Sound = Gdx.audio.newSound(Gdx.files.internal("data/audio/laser1.wav"));
		lifeLostSound = Gdx.audio.newSound(Gdx.files.internal("data/audio/life_lost.wav"));
		wobbleSound = Gdx.audio.newSound(Gdx.files.internal("data/audio/wobbledown.wav"));
		missileSound = Gdx.audio.newSound(Gdx.files.internal("data/audio/missile.wav"));
		glitchSound = Gdx.audio.newSound(Gdx.files.internal("data/audio/glitch.wav"));
		coinHitSound = Gdx.audio.newSound(Gdx.files.internal("data/audio/coinhit.wav"));
		coinReturnSound = Gdx.audio.newSound(Gdx.files.internal("data/audio/coin_return.wav"));
		countdown321 = Gdx.audio.newSound(Gdx.files.internal("data/audio/countdown-321.mp3"));
		
		pongCpuScoreSound = Gdx.audio.newSound(Gdx.files.internal("data/audio/pong-cpu-score.wav"));
		pongPlayerScoreSound = Gdx.audio.newSound(Gdx.files.internal("data/audio/pong-player-score.wav"));
		pongWallBounceSound = Gdx.audio.newSound(Gdx.files.internal("data/audio/pong-wall-bounce.wav"));
//		pongWallBounceSound = Gdx.audio.newSound(Gdx.files.internal("data/audio/pong-wall-bounce-lol.wav"));
		
		siPlayerLaser = laserSound;
		siAlienLaser = laser1Sound;
		siMothership = beep;
		
		planeShot = laserSound;
		bossShot = missileSound;
		g1942PlayerLaser = laser1Sound;
		
		titleMusic = Gdx.audio.newMusic(Gdx.files.internal("data/audio/title.mp3"));
		pongMusic = Gdx.audio.newMusic(Gdx.files.internal("data/audio/aces_high.mp3"));
		breakoutMusic = Gdx.audio.newMusic(Gdx.files.internal("data/audio/breakout.mp3"));
		spaceInvMusic = Gdx.audio.newMusic(Gdx.files.internal("data/audio/space_invaders.mp3"));
		g1942Music = Gdx.audio.newMusic(Gdx.files.internal("data/audio/g1942.mp3"));
		endScreenMusic = Gdx.audio.newMusic(Gdx.files.internal("data/audio/endscreen.mp3"));
		
		letters = splitAndGet(sheet, 8, 8, 0, 30, 26, 1);
		digits  = splitAndGet(sheet, 8, 8, 0, 31, 10, 1);
		symbols = splitAndGet(sheet, 8, 8, 10, 31, 18, 1);
	}
	
	public static void dispose() {
		background.dispose();
		sheet.dispose();
		batch.dispose();
	}
	
	private static TextureRegion[][] splitAndGet(Texture texture, int width, int height, int col, int row, int xTiles, int yTiles) {
		TextureRegion[][] allRegions = TextureRegion.split(texture, width, height);
		TextureRegion[][] regions = new TextureRegion[yTiles][xTiles];
		for (int y = 0; y < yTiles; ++y) {
			for (int x = 0; x < xTiles; ++x) {
				regions[y][x] = allRegions[row + y][col + x];
			}
		}
		return regions;
	}
	
}
