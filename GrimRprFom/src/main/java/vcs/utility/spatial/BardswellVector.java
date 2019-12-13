package vcs.utility.spatial;

import java.io.Serializable;


public class BardswellVector implements Serializable
{
	private static final long serialVersionUID = 2884952120879337776L;

	public double Lat,
                Long,
                Height,
                Pitch,
                Course,
                Speed;
	
	public BardswellVector(BardswellVector original)
	{
		Lat = original.Lat;
		Long = original.Long;
		Height = original.Height;
		Pitch = original.Pitch;
		Speed = original.Speed;
		Course = original.Course;
	}

	
  public BardswellVector(Latitude la, Longitude lo, double h, double s, Angle p, Angle c)
  {
    Lat = la.get();
    Long = lo.get();
    Height = h;
    Pitch = p.get();
    Speed = s;
    Course = c.get();
  }

  
	public BardswellVector(double la, double lo, double h, double s, double p, double c)
	{
		Lat = la;
		Long = lo;
		Height = h;
		Pitch = p;
		Speed = s;
		Course = c;
	}

	
	public BardswellVector(LatLongHeight LLH, double s, double p, double c)
	{
		Lat = LLH.getLat();
		Long = LLH.getLong();
		Height = LLH.getHeight();
		Pitch = p;
		Speed = s;
		Course = c;
	}

	
	public BardswellVector(KinematicState KS)
	{
	  	try
	  	{
		    LatLongHeight LLH = KS.getPosition().toLatLongHeight(1e-8);
		    
		    Lat    = LLH.getLat();
		    Long   = LLH.getLong();
		    Height = LLH.getHeight();
		    
		    Rotator R_G_TO_E = KS.getOrientation().getRotationMatrix(); 
		
		    Rotator R_LG_TO_G = LLH.getR_LG_TO_G();
		    
		    Rotator R_G_TO_LG = R_LG_TO_G.transpose();
		
		    Rotator R_LG_TO_E = R_G_TO_E.rotateBy(R_LG_TO_G);
		  
		    EulerAngles omega = R_LG_TO_E.getEulerAngles();
		  
		    Pitch = omega.getY();
		    Course = Math.PI / 2.0 - omega.getZ();
		  
		    Velocity vlg = KS.getVelocity().rotateBy(R_G_TO_LG);
		  
		    double cosPitch  = Math.cos(Pitch);
		    double sinPitch  = Math.sin(Pitch);
		    double sinCourse = Math.sin(Course);
		    double cosCourse = Math.cos(Course);
		  
		    if (Math.abs(cosPitch) < 0.5)
		      Speed = -vlg.getZ() / sinPitch;
		    else
		    if (Math.abs(cosCourse) < 0.5)
		      Speed = vlg.getX() / (cosPitch * sinCourse);
		    else
		      Speed = vlg.getY() / (cosPitch * cosCourse);
	  	}
	  	catch (Exception E)
	  	{
	  		System.err.println(E);
	  	}
	  }
	
}

