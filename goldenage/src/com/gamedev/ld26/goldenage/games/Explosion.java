package com.gamedev.ld26.goldenage.games;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.gamedev.ld26.goldenage.core.Assets;
import com.gamedev.ld26.goldenage.utils.Utils;

public class Explosion extends GameObject {

	ArrayList<Particle> _particles = new ArrayList<Particle>();
	
	public Explosion(Vector2 pos, Color color, Vector2 vel, GameState gs) {
		this(pos, color, vel, gs, 50);
	}
	
	public Explosion(Vector2 pos, Color color, Vector2 vel, GameState gs, int numParticles) {
		super(pos, new Vector2(), color, gs);
		
		float angle = 0f;
		int count = Math.max(1, numParticles);
		float angleDelta = 360.0f / count;
		
		for (int i =0; i < count; i++)
		{
			angle += angleDelta + Assets.random.nextFloat() * 3f;
			
			float x = (float)Math.cos(angle / 180.0 * Math.PI) * Assets.random.nextFloat() * 100;
			float y = -(float)Math.sin(angle / 180.0 * Math.PI) * Assets.random.nextFloat() * 100;
			
			_particles.add(new Particle(pos, new Vector2(vel.x + x, vel.y + y), color, new Vector2(-vel.x * .3f ,(-vel.y * .3f) + Assets.random.nextFloat() * -50)));
		}
	}
	
	public void update(float dt)
	{
		boolean stillRunning = false;
		for (Particle particle : _particles)
		{
			particle.Update(dt);
			stillRunning = stillRunning || particle.isAlive();
		}
		_alive = stillRunning;
	}
	
	public void render(float dt){
		
		for (Particle particle : _particles)
		{
			particle.Render();
		}
	}

	public void setParticleSize(float w, float h) {
		for (Particle particle : _particles) {
			particle.setSize(w, h);
		}
	}
	
	public void setParticleTTL(float ttl) {
		for (Particle particle : _particles) {
			particle.setTTL(ttl);
		}
	}
}
