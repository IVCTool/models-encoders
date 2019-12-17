package vcs.utility.spatial;

public class Duration  
{
	private double myDur;
	
	public Duration(double dur)
	{
		this.myDur = dur;
	}
	
	public boolean isShorterThan(Duration other)
	{
		return myDur < other.myDur;
	}
	
	public boolean isLongerThan(Duration other)
	{
		return myDur > other.myDur;
	}

	public String toString()
	{
		return myDur + " sec";
	}
	
	public Duration add(Duration other)
	{
		return new Duration(myDur + other.myDur);
	}
	
	public Duration subtract(Duration other)
	{
		return new Duration(myDur - other.myDur);
	}

	
	public double get()
	{
		return myDur;
	}
}
