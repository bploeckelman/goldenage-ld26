package com.gamedev.ld26.goldenage.games.G1942;


public class PlaneSpawnInfo {

	private int _Count;
	private float _SpawnTime;
	private float _SpawnDelay;
	private float _InitialSpawnTime;
	private float _InitialSpawnDelay;
	private int _initialCount;
	
	public PlaneSpawnInfo(int count, float spawnDelay, float spawnTime) {
		_initialCount = count;
		_InitialSpawnDelay = spawnDelay;
		_InitialSpawnTime = spawnTime;
		_SpawnTime = spawnTime;
	}
	
	public void SetSpawnTime(float time)
	{
		_SpawnTime = time;
	}
	
	public boolean ShouldSpawn(float dt)
	{
		_SpawnTime -= dt;
		if (_SpawnTime < 0)
		{
			_Count = _initialCount;
			_SpawnTime = _InitialSpawnTime;
		}
		if (_Count > 0)
		{
			_SpawnDelay -= dt;
			if (_SpawnDelay < 0)
			{
				_Count--;
				_SpawnDelay = _InitialSpawnDelay;
				return true;
			}
		}
		return false;
	}

}
