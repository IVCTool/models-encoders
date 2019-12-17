/**
 * COPYRIGHT BY HER MAJESTY THE QUEEN AS REPRESENTED BY THE MINISTER OF 
 * NATIONAL DEFENCE, 2006
 */

package vcs.utility.spatial;

public class XYZVector implements java.io.Serializable
{
	private static final long serialVersionUID = 2431165801716485689L;

	protected double myX,
	                 myY,
	                 myZ;

	
  public XYZVector()
  {
    this.myX = 0.0;
    this.myY = 0.0;
    this.myZ = 0.0;
  }

	public XYZVector(double x, double y, double z)
	{
		this.myX = x;
		this.myY = y;
		this.myZ = z;
	}

	public XYZVector(double[] xyz)
	{
    myX = xyz[0];
    myY = xyz[1];
    myZ = xyz[2];
	}

	public XYZVector(XYZVector other)
	{
    myX = other.getX();
    myY = other.getY();
    myZ = other.getZ();
	}
	
	
  /**
   * toString
   * 
   *   @return A String representation of the object.
   */
	public String toString()
	{
	  return Double.toString(myX) + " " + Double.toString(myY) + " " + Double.toString(myZ); 
	}

  /**
   * equals
   * 
   *   @return Boolean : whether or not the specified object is logically equivalent to this one.
   */
	public boolean equals(XYZVector other)
	{
  	return myX == other.getX() &&
  	       myY == other.getY() &&
  	       myZ == other.getZ();
	}    


	public double getX()
	{
	  return myX;
	}
	
	public double getY()
	{
	  return myY;
	}
	
	public double getZ()
	{
	  return myZ;
	}
	
  public XYZVector getXYZ()
  {
    return new XYZVector(this.myX, this.myY, this.myZ);
  }
  
  public void setX(double x)
  {
    this.myX = x;
  }
  
  public void setY(double y)
  {
    this.myY = y;
  }
  
  public void setZ(double z)
  {
    this.myZ = z;
  }
  
  public void setXYZ(XYZVector xyz)
  {
    this.myX = xyz.getX();
    this.myZ = xyz.getY();
    this.myZ = xyz.getZ();
  }
 
	public XYZVector add(XYZVector Other)
	{
		return new XYZVector(myX + Other.getX(),
		                     myY + Other.getY(),
		                     myZ + Other.getZ());
	}

	public XYZVector subtract(XYZVector Other)
	{
		return new XYZVector(myX - Other.getX(),
		                     myY - Other.getY(),
		                     myZ - Other.getZ());
	}
	
	public XYZVector multiplyBy(XYZMatrix m)
	{
		return new XYZVector(m.get(0,0) * myX + m.get(0,1) * myY + m.get(0,2) * myZ,
		                     m.get(1,0) * myX + m.get(1,1) * myY + m.get(1,2) * myZ,
                  		   m.get(2,0) * myX + m.get(2,1) * myY + m.get(2,2) * myZ);
	}

	
	public XYZVector scaleBy(double scalar)
	{
		return new XYZVector(myX * scalar,
		                     myY * scalar,
                  		   myZ * scalar);
	}


	public double magnitude()
	{
		return Math.sqrt(myX * myX + myY * myY + myZ * myZ);
	}
	
	public boolean differsFrom(XYZVector other, double tolerance)
	{
	  return Math.abs(myX - other.getX()) > tolerance ||
	         Math.abs(myY - other.getY()) > tolerance ||
	         Math.abs(myZ - other.getZ()) > tolerance;
	}
	
}