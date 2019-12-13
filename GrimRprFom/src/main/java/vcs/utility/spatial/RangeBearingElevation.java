package vcs.utility.spatial;

public class RangeBearingElevation
{
  public Angle bearing,
               elevation;
  public Double range;
  
  public RangeBearingElevation(Double r, Double b, Double e)
  {
  	range = r;
  	bearing = new Angle(b);
  	elevation = new Angle(e);
  }
  
  public RangeBearingElevation(Double r, Angle b, Angle e)
  {
  	range = r;
  	bearing = b;
  	elevation = e;
  }
  
}
