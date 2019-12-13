package vcs.utility.spatial;

public class Sweep 
{
  private Angle min,
                max;
	

	public Sweep(double minD, double maxD)
	{
		min = new Angle(minD);
		max = new Angle(maxD);
		
		validate();
	}
  
  
  public Sweep(Angle minA, Angle maxA) 
	{
		min = minA;
		max = maxA;

		validate();
	}
  
  private void validate()
  {
		while (min.get() >= Math.PI * 2.0)
		{
			min = min.subtract(Math.PI * 2.0);
			max = max.subtract(Math.PI * 2.0);
		}

		while (max.get() < min.get())
			max = max.add(Math.PI * 2.0);
  }
	
	public Angle getMin()
	{
		return min;
	}
	
	public Angle getMax()
	{
		return max;
	}
	
	public boolean contains(double d)
	{
		while (d > max.get())
			d -= Math.PI * 2.0;

		return d >= min.get() && d <= max.get();
	}

	
	public boolean contains(Angle a)
	{
		return contains(a.get());
		}

}
