/**
 * COPYRIGHT BY HER MAJESTY THE QUEEN AS REPRESENTED BY THE MINISTER OF 
 * NATIONAL DEFENCE, 2006
 */

package vcs.utility.spatial;

public class KinematicState implements java.io.Serializable 
{
	private static final long serialVersionUID = -8253614550139207253L;

	private WGSPosition  position;
	private Velocity     velocity;
	private Acceleration acceleration;
	private EulerAngles  orientation;
	private EulerDerivs  orientationRate;

	private double myTime;

	/**
	 * Copy Constructor
	 */
	public KinematicState(KinematicState Other)
	{
		position        = Other.getPosition();
		velocity        = Other.getVelocity();
		acceleration    = Other.getAcceleration();
		orientation     = Other.getOrientation();
		orientationRate = Other.getOrientationRate();
		
		myTime = Other.getTime();
	}
	
	/**
	 * Constructor
	 */
	public KinematicState(double time, 
			                  WGSPosition pos, Velocity vel, Acceleration acc, 
			                  EulerAngles or, EulerDerivs orr)
	{
		position        = pos;
		velocity        = vel;
		acceleration    = acc;
		orientation     = or;
		orientationRate = orr;
		
		this.myTime = time;
	}

	
	public KinematicState(BardswellVector BV)
	{
		double roll = 0.0;
		
    try
    {
      LatLongHeight LLH = new LatLongHeight(BV.Lat, BV.Long, BV.Height);
      position = LLH.toWGSPosition();
      
      Rotator R_LG_TO_G = LLH.getR_LG_TO_G();

	    double cosPitch  = Math.cos(BV.Pitch);
	    double sinPitch  = Math.sin(BV.Pitch);
	    double cosCourse = Math.cos(BV.Course);
	    double sinCourse = Math.sin(BV.Course);
	    
	    // Get the rotation matrices from LG to G and G to LG
	
	    Rotator R_G_TO_LG = R_LG_TO_G.transpose();
	
	    // Calculate the velocity in the LG frame
	
	    velocity = new Velocity(BV.Speed * cosPitch * sinCourse,
	                        		BV.Speed * cosPitch * cosCourse,
	                           -BV.Speed * sinPitch);
	    
	    // Calculate the velocity in the G frame
	
	    velocity = velocity.rotateBy(R_LG_TO_G);
	
	    orientation = new EulerAngles(BV.Course, BV.Pitch, roll);
	    Rotator R_LG_TO_E = orientation.getR_LG_TO_E();
	    Rotator R_G_TO_E = R_LG_TO_E.rotateBy(R_G_TO_LG);
	    orientation = R_G_TO_E.getEulerAngles();
    }
    catch (Exception E)
    {
    	E.printStackTrace();
    }
	}
	
	
	/**
   * toString
   * 
   *   @return A String representation of the object.
   */
	public String toString()
	{
		return position.toString() + " " + 
		       velocity.toString() + " " +   
		       acceleration.toString() + " " +
		       orientation.toString() + " " +
		       orientationRate.toString();
	}
	
	
  /**
   * equals
   * 
   *   @return Boolean : whether or not the specified object is logically equivalent to this one.
   */
	
  public boolean equals(KinematicState other)
	{
  	return position.equals(other.getPosition()) &&
  	       velocity.equals(other.getVelocity()) &&
  	       acceleration.equals(other.getAcceleration()) &&
  	       orientation.equals(other.getOrientation()) &&
  	       orientationRate.equals(other.getOrientationRate());
	}    
	
	public WGSPosition  getPosition()         {	return position;	      }
	public Velocity     getVelocity()   	    {	return velocity;	      }
	public Acceleration getAcceleration()	    {	return acceleration;	  }
	public EulerAngles  getOrientation()      {	return orientation;   	}
	public EulerDerivs  getOrientationRate()	{	return orientationRate;	}

	public double getTime()	{	return myTime;	}


	public void setPosition(WGSPosition p)          {	position = p;	      }
	public void setVelocity(Velocity v)   	        {	velocity = v;	      }
	public void setAcceleration(Acceleration a)	    {	acceleration = a;	  }
	public void setOrientation(EulerAngles e)       {	orientation = e;   	}
	public void setOrientationRate(EulerDerivs ed)	{	orientationRate = ed;	}

	public void setTime(double t)	{	myTime = t;	}
}
