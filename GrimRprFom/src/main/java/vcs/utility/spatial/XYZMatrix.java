/**
 * COPYRIGHT BY HER MAJESTY THE QUEEN AS REPRESENTED BY THE MINISTER OF 
 * NATIONAL DEFENCE, 2006
 */

package vcs.utility.spatial;

public class XYZMatrix implements java.io.Serializable
{
	private static final long serialVersionUID = 2088222941776311393L;

	protected double[][] elements = {{0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}};

  public enum MatrixType
  {
  	IDENTITY;
  }
  
  /**
   * equals
   * 
   *   @return Boolean : whether or not the specified object is logically equivalent to this one.
   */
  public boolean equals(XYZMatrix other)
	{
  	for (int i = 0; i < 3; i++)
    	for (int j = 0; j < 3; j++)
    		if (elements[i][j] != other.get(i, j))
    			return false;
  		
  	return true;
	}    
  
   
  /**
   * toString
   * 
   *   @return A String representation of the object.
   */
  
  public String toString()
  {
  	String result = "";
  	
    for (int i = 0; i < 3; i++)
	    for (int j = 0; j < 3; j++)
	    	result += elements[i][j] + " ";
  	
  	return result;
  }
  
  
  public XYZMatrix(MatrixType type)
  {
  	if (type == MatrixType.IDENTITY)
  	{
  		elements[0][0] = 1.0;
  		elements[1][1] = 1.0;
  		elements[2][2] = 1.0;
    } 
 }
  
  public XYZMatrix()
  {
  }

  public XYZMatrix(XYZMatrix Original)
  {
    for (int i = 0; i < 3; i++)
	    for (int j = 0; j < 3; j++)
	      elements[i][j] = Original.get(i, j);
  }
  

  public XYZMatrix(double e00, double e01, double e02, 
  		                   double e10, double e11, double e12,
  		                   double e20, double e21, double e22)
  {
    elements[0][0] = e00;    elements[0][1] = e01;    elements[0][2] = e02;
    elements[1][0] = e10;    elements[1][1] = e11;    elements[1][2] = e12;
    elements[2][0] = e20;    elements[2][1] = e21;    elements[2][2] = e22;
  }
  
  
  public double get(int i, int j)
  {
  	return elements[i][j];
  }
  
  public void set(int i, int j, double Val)
  {
  	elements[i][j] = Val;
  }
  
  public XYZMatrix multiplyBy(XYZMatrix other)
  {
  	XYZMatrix newR = new XYZMatrix();

    for (int i = 0; i < 3; i++)
      for (int j = 0; j < 3; j++)
	      for (int k = 0; k < 3; k++)
	      	newR.set(i, j, newR.get(i, j) + elements[i][k] * other.get(k, j));

    return newR;
  }

	
  public XYZMatrix scaleBy(double scalar)
  {
  	XYZMatrix newR = new XYZMatrix();

    for( int i = 0; i < 3; i++ )
    	for( int j = 0; j < 3; j++ )
	      newR.set(i, j, elements[i][j] * scalar);
    return newR;
  }


  public XYZMatrix addTo(XYZMatrix other)
  {
  	XYZMatrix newR = new XYZMatrix();

    for( int i = 0; i < 3; i++ )
    	for( int j = 0; j < 3; j++ )
	      newR.set(i, j, elements[i][j] + other.get(i, j));
    
    return newR;
  }


  public XYZMatrix transpose()
  {
  	XYZMatrix newR = new XYZMatrix();

    for( int i = 0; i < 3; i++ )
	    for( int j = 0; j < 3; j++ )
	      newR.set(i, j, elements[j][i]);

    return newR;
  }

 
}