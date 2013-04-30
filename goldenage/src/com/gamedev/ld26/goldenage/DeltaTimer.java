package com.gamedev.ld26.goldenage;

public class DeltaTimer {
	private TimerListener _listener;
	private float _triggerTime;
	private float _currentTime;
	
	public DeltaTimer(TimerListener listener, float time) {
		_listener = listener;
		_triggerTime = time;
	}
	
	public void Update(float delta) {
		_currentTime +=delta;
		if (_currentTime >= _triggerTime) {
		    _currentTime -= _triggerTime;
			_listener.OnTimer();
		}
	}
}
