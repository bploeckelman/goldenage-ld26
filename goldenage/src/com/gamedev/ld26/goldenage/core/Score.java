package com.gamedev.ld26.goldenage.core;

public class Score {
	
	private static int _score;
	private static int _livesLost;
	private static float _time;
	
	public static void ResetScore()
	{
		_score = 0;
		_livesLost = 0;
		_time = 0;
	}
	
	public static int GetScore()
	{
		return _score;
	}
	
	public static void AddToScore(int amount)
	{
		_score += amount;
	}
	
	public static void loseLife() {
		_livesLost++;
	}
	
	public static int getLostLives() {
		return _livesLost;
	}
	
	public static void setTime(float time) {
		_time = time;
	}
	
	public static float getTime() {
		return _time;
	}
	
	public static String getScoreString(int padding){
		
		 String tempString = "" + _score;
		 int numbersToPad = padding - tempString.length();
		 for (int i =0;i < numbersToPad; i++)
		 {
			 tempString = "0"+ tempString;
		 }
		 if (tempString.length() > padding)
		 {
			 tempString = "";
			 for(int i =0;i < padding; i++)
			 {
				 tempString += "9";
			 }
		 }
		 tempString = "Score: " + tempString;
		return tempString;
	}
	
	public static String getLivesString()
	{
		return "Lives lost: " + _livesLost;
	}
}
