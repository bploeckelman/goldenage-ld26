package com.gamedev.ld26.goldenage.games.breakout;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gamedev.ld26.goldenage.core.Assets;
import com.gamedev.ld26.goldenage.games.GameObject;
import com.gamedev.ld26.goldenage.games.GameState;
import com.gamedev.ld26.goldenage.utils.Config;
import com.gamedev.ld26.goldenage.utils.Utils;

public class Block extends GameObject {
	public static final int BLOCKS_WIDE = 15;
	public static final int BLOCKS_TALL = 12;
	public static final float BLOCK_AREA = .5f;
	public static final int TOP_GAP = 4;

	private final float BLOCK_WIDTH = Config.window_width / BLOCKS_WIDE;
	private final float BLOCK_HEIGHT = (Config.window_height * BLOCK_AREA) / (BLOCKS_TALL + TOP_GAP);
	private final Vector2 _index;
	private Vector2 _pos;
	private float _width;
	private float _height; 
	private final Vector2 _origin;
	
	public Block(Vector2 pos, Color color, Vector2 origin, GameState gs)
	{
		super(gs);
		_index = pos;
		_origin = origin;
		setSize(0);
		setColor(color);
	}
	
	public void setSize(float size)
	{
		_width = BLOCK_WIDTH * size;
		_height = BLOCK_HEIGHT * size;
		_pos = Utils.lerpVector2(_origin, new Vector2(_index.x * BLOCK_WIDTH,(Config.window_height - _height) - (_index.y * BLOCK_HEIGHT)), size);
		_rect = new Rectangle(_pos.x, _pos.y, _width, _height);
	}
	

	public void update(float deltaTime)
	{
		
	}
	
	public void render(float delta)
	{
		Assets.shapes.setColor(this.getColor());
		Assets.shapes.rect(_pos.x, (_pos.y), _width, _height);
		Assets.shapes.end();
		
		Assets.shapes.begin(ShapeType.Line);
		Assets.shapes.setColor(Color.GRAY);
		Assets.shapes.rect(_pos.x, (_pos.y), _width, _height);
		Assets.shapes.end();
		
		Assets.shapes.begin(ShapeType.Filled);
	}

}
