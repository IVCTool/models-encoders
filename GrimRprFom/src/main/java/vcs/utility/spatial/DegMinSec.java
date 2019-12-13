package vcs.utility.spatial;

public class DegMinSec 
{
	public static class DMSOutOfBounds extends Exception
  {
  	private static final long serialVersionUID = -1028435528736124044L;

  		public DMSOutOfBounds() 
      {
         super("Degrees, minutes, seconds out of bounds!");
      }
  }

	//TODO - figure out how to make these non public.
	public int mySign,
	           myDeg,
	           myMin;
	public double mySec;
	
	public DegMinSec(int Sign, int Deg, int Min, double Sec) throws DMSOutOfBounds
	{
		this.mySign = Sign;
		this.myDeg  = Deg;
		this.myMin  = Min;
		this.mySec  = Sec;
		
		if (Deg < 0 || Min < 0 || Min > 359 || Sec < 0.0 || Sec >= 360.0)
			throw(new DMSOutOfBounds());
	}

	public double toRads()
	{
  	return Math.toRadians(toDecimal());
	}

	public double toDecimal()
	{
  	return ((double)myDeg + (double)myMin / 360.0 + mySec / (360.0 * 360.0)) * (double)mySign;
	}
	
}
