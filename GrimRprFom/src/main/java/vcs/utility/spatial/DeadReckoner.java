package vcs.utility.spatial;

public class DeadReckoner  
{
  //EnumeratedDataType "DRAlgorithmEnum"
  public static int DR_ALGORITHIM_STEADY = 1;
  public static int DR_ALGORITHIM_FPW = 2;
  public static int DR_ALGORITHIM_RPW = 3;
  public static int DR_ALGORITHIM_RVW = 4;
  public static int DR_ALGORITHIM_FVW = 5;
  public static int DR_ALGORITHIM_FPB = 6;
  public static int DR_ALGORITHIM_RPB = 7;
  public static int DR_ALGORITHIM_RVB = 8;
  public static int DR_ALGORITHIM_FVB = 9;
  public static int DR_ALGORITHIM_REPW = 10;
  public static int DR_ALGORITHIM_REVW = 11;

	private KinematicState myTruth;
	private	int myAlgorithm = 1;

	public DeadReckoner(int algorithm, KinematicState truth)
	{
		this.myTruth     = new KinematicState(truth);
	  this.myAlgorithm = algorithm;
	}

	public DeadReckoner(int algorithm, double startTime,
			                WGSPosition position, Velocity velocity, Acceleration acceleration, 
			                EulerAngles orientation, EulerDerivs angularVelocity)
	{
		myTruth = new KinematicState(startTime, position, velocity, acceleration, orientation, angularVelocity);
		this.myAlgorithm = algorithm;
	}
	
  /**
   * toString
   * 
   *   @return A String representation of the object.
   */
public String toString()
	{
		return myTruth.toString() + " " + myAlgorithm;
	}
	
/**
 * equals
 *
 *   @return Boolean : whether or not the specified object is logically equivalent to this one.
 */
public boolean equals(DeadReckoner other)
{
	 return myAlgorithm == other.getDRAlgorithm() &&
	        myTruth.equals(other.myTruth);
}
	
	/**
	 * projectTo
	 *
	 *   @param newTime : future time to project to.
	 *
	 *   @return A KinematicState object that represents the projected state 
	 */
	public KinematicState projectTo(double newTime)
	{
		return projectBy(new Duration(newTime - getTime()));
	}
	
	/**
	 * projectBy
	 *
	 *   @param dur : time duration by which to project.
	 *
	 *   @return A KinematicState object that represents the projected state 
	 */
	public KinematicState projectBy(Duration dur)
	{
		WGSPosition  newPosition        = myTruth.getPosition();
		Velocity     newVelocity        = myTruth.getVelocity();
		Acceleration newAcceleration    = myTruth.getAcceleration();
		EulerAngles  newOrientation     = myTruth.getOrientation();
	  EulerDerivs  newOrientationRate = myTruth.getOrientationRate();
		
	  try
	  {
		switch (myAlgorithm)
		{
		  case 1:  // No calculation
				break;

		  case 2:
				newPosition = getPosition().add(getVelocity().distanceOverTime(dur));
				break;

		  case 3:
		  {
		  	newPosition = getPosition().add(getVelocity().distanceOverTime(dur));

		  	Rotator R_G_TO_Et1       = new Rotator(getOrientation());
		  	Rotator R_Et_TO_Et1      = getOrientationRate().getR_E_TO_E(dur);
				Rotator R_G_TO_Et        = R_Et_TO_Et1.rotateBy(R_G_TO_Et1);
				newOrientation           = R_G_TO_Et.getEulerAngles();

				DerivRotator W               = getOrientationRate().getWMatrix();
				DerivRotator minusW          = W.scaleBy(-1);
				DerivRotator temp            = minusW.rotateBy(R_Et_TO_Et1);
				DerivRotator R_Deriv_G_TO_Et = temp.rotateBy(R_G_TO_Et1);
				newOrientationRate           = newOrientation.calcDerivatives(R_Deriv_G_TO_Et);
				break;
		  }

			case 4:
			{
				newPosition = getPosition().add(getVelocity().    distanceOverTime(dur)).add(getAcceleration().distanceOverTime(dur));

				newVelocity = getVelocity().add(getAcceleration().velocityOverTime(dur));

				Rotator R_G_TO_Et1  = new Rotator(getOrientation());
				Rotator R_Et_TO_Et1 = getOrientationRate().getR_E_TO_E(dur);
				Rotator R_G_TO_Et   = R_Et_TO_Et1.rotateBy(R_G_TO_Et1);
				newOrientation      = R_G_TO_Et.getEulerAngles();

				DerivRotator W               = getOrientationRate().getWMatrix();
				DerivRotator minusW          = W.scaleBy(-1);
				DerivRotator temp            = minusW.rotateBy(R_Et_TO_Et1);
				DerivRotator R_Deriv_G_TO_Et = temp.rotateBy(R_G_TO_Et1);
				newOrientationRate           = newOrientation.calcDerivatives(R_Deriv_G_TO_Et);
				break;
			}

			case 5:
				newPosition = getPosition().add(getVelocity()    .distanceOverTime(dur)).add(getAcceleration().distanceOverTime(dur));

				newVelocity = getVelocity().add(getAcceleration().velocityOverTime(dur));
				break;

			case 6:
			{
				Rotator R_G_TO_Et1     = new Rotator(getOrientation());
				Rotator R_G_TO_Et1_INV = R_G_TO_Et1.transpose();

				IntegRotator R1   = getOrientationRate().getR1Matrix(dur);
				IntegRotator temp = R_G_TO_Et1_INV.rotateBy(R1);
				Distance posTerm2 = getVelocity().rotateBy(temp);
				newPosition       = getPosition().add(posTerm2);

				Rotator R_E_TO_E     = getOrientationRate().getR_E_TO_E(dur);
				Rotator R_E_TO_E_INV = R_E_TO_E.transpose();
				Rotator velMatrix    = R_G_TO_Et1_INV.rotateBy(R_E_TO_E_INV);
				newVelocity          = getVelocity().rotateBy(velMatrix);

				DerivRotator W         = getOrientationRate().getWMatrix();
				DerivRotator accMatrix = velMatrix.rotateBy(W);
				newAcceleration        = getVelocity().rotateBy(accMatrix);

				DerivRotator minusW          = W.scaleBy(-1);
				DerivRotator temp2           = minusW.rotateBy(R_E_TO_E);
				DerivRotator R_Deriv_G_TO_Et = temp2.rotateBy(R_G_TO_Et1);
				newOrientationRate           = newOrientation.calcDerivatives(R_Deriv_G_TO_Et);
				break;
			}

			case 7:
			{
				Rotator R_G_TO_Et1     = new Rotator(getOrientation());
				Rotator R_G_TO_Et1_INV = R_G_TO_Et1.transpose();

				IntegRotator R1   = getOrientationRate().getR1Matrix(dur);
				IntegRotator temp = R_G_TO_Et1_INV.rotateBy(R1);
				Distance posTerm2 = getVelocity().rotateBy(temp);
				newPosition       = getPosition().add(posTerm2);

				Rotator R_E_TO_E     = getOrientationRate().getR_E_TO_E(dur);
				Rotator R_E_TO_E_INV = R_E_TO_E.transpose();
				Rotator velMatrix    = R_G_TO_Et1_INV.rotateBy(R_E_TO_E_INV);
				newVelocity          = getVelocity().rotateBy(velMatrix);

				DerivRotator W         = getOrientationRate().getWMatrix();
				DerivRotator accMatrix = velMatrix.rotateBy(W);
				newAcceleration        = getVelocity().rotateBy(accMatrix);

				Rotator R_G_TO_Et = R_E_TO_E.rotateBy(R_G_TO_Et1);
				newOrientation    = R_G_TO_Et.getEulerAngles();

				DerivRotator minusW          = W.scaleBy(-1);
				DerivRotator temp2           = minusW.rotateBy(R_E_TO_E);
				DerivRotator R_Deriv_G_TO_Et = temp2.rotateBy(R_G_TO_Et1);
				newOrientationRate           = newOrientation.calcDerivatives(R_Deriv_G_TO_Et);
				break;
			}

			case 8:
			{
				Rotator R_G_TO_Et1           = new Rotator(getOrientation());
				Rotator R_G_TO_Et1_INV       = R_G_TO_Et1.transpose();

				IntegRotator R1              = getOrientationRate().getR1Matrix(dur);
				IntegIntegRotator R2         = getOrientationRate().getR2Matrix(dur);
				Distance R1xVel              = getVelocity().rotateBy(R1);
				Distance R2xAcc              = getAcceleration().rotateBy(R2);
				Distance R1xVel_plus_R2xAcc  = R1xVel.add(R2xAcc);
				Distance posTerm2            = R1xVel_plus_R2xAcc.rotateBy(R_G_TO_Et1_INV);
				newPosition                  = getPosition().add(posTerm2);

				Velocity  Vel_plus_Acc_x_dur = getVelocity().add(getAcceleration().velocityOverTime(dur));
				Rotator R_E_TO_E             = getOrientationRate().getR_E_TO_E(dur);
				Rotator R_E_TO_E_INV         = R_E_TO_E.transpose();
				Rotator temp                 = R_G_TO_Et1_INV.rotateBy(R_E_TO_E_INV);
				newVelocity                  = Vel_plus_Acc_x_dur.rotateBy(temp);

				DerivRotator W               = getOrientationRate().getWMatrix();
				Acceleration W_x_VpAxDur     = Vel_plus_Acc_x_dur.rotateBy(W);
				Acceleration WxVpAxDur_p_Acc = W_x_VpAxDur.add(getAcceleration());
				newAcceleration              = WxVpAxDur_p_Acc.rotateBy(temp);

				Rotator R_G_TO_Et            = R_E_TO_E.rotateBy(R_G_TO_Et1);
				newOrientation               = R_G_TO_Et.getEulerAngles();

				DerivRotator minusW          = W.scaleBy(-1);
				DerivRotator temp2           = minusW.rotateBy(R_E_TO_E);
				DerivRotator R_Deriv_G_TO_Et = temp2.rotateBy(R_G_TO_Et1);
				newOrientationRate           = newOrientation.calcDerivatives(R_Deriv_G_TO_Et);
				break;
		  }

			case 9:
			{
				Rotator R_G_TO_Et1     = new Rotator(getOrientation());
				Rotator R_G_TO_Et1_INV = R_G_TO_Et1.transpose();

				IntegRotator R1             = getOrientationRate().getR1Matrix(dur);
				IntegIntegRotator R2        = getOrientationRate().getR2Matrix(dur);
			  Distance R1xVel             = getVelocity().rotateBy(R1);
			  Distance R2xAcc             = getAcceleration().rotateBy(R2);
			  Distance R1xVel_plus_R2xAcc = R1xVel.add(R2xAcc);
				Distance posTerm2           = R1xVel_plus_R2xAcc.rotateBy(R_G_TO_Et1_INV);
				newPosition                 = getPosition().add(posTerm2);

				Velocity Vel_plus_Acc_x_dur = getVelocity().add(getAcceleration().velocityOverTime(dur));
				Rotator R_E_TO_E            = getOrientationRate().getR_E_TO_E(dur);
				Rotator R_E_TO_E_INV        = R_E_TO_E.transpose();
				Rotator temp                = R_G_TO_Et1_INV.rotateBy(R_E_TO_E_INV);
				newVelocity                 = Vel_plus_Acc_x_dur.rotateBy(temp);

				DerivRotator W               = getOrientationRate().getWMatrix();
				Acceleration W_x_VpAxDur     = Vel_plus_Acc_x_dur.rotateBy(W);
				Acceleration WxVpAxDur_p_Acc = W_x_VpAxDur.add(getAcceleration());
				newAcceleration              = WxVpAxDur_p_Acc.rotateBy(temp);

				DerivRotator minusW          = W.scaleBy(-1);
				DerivRotator temp2           = minusW.rotateBy(R_E_TO_E);
				DerivRotator R_Deriv_G_TO_Et = temp2.rotateBy(R_G_TO_Et1);
				newOrientationRate           = newOrientation.calcDerivatives(R_Deriv_G_TO_Et);
				break;
			}

			case 10:
				newPosition    = getPosition()   .add(getVelocity()       .distanceOverTime(dur));
				
				newOrientation = getOrientation().addTo(getOrientationRate().orientationOverTime(dur));
				break;

			case 11:
				newPosition    = getPosition()   .add(getVelocity()       .distanceOverTime(dur)).add(getAcceleration().distanceOverTime(dur));
				
				newVelocity    = getVelocity()   .add(getAcceleration()   .velocityOverTime(dur));
				
				newOrientation = getOrientation().addTo(getOrientationRate().orientationOverTime(dur));
				break;
				
			default:
				throw new Exception("Bad DR algorithm: " + myAlgorithm);
		} // switch
	  }
	  catch (Exception E)
	  {
	  	E.printStackTrace();
	  }

	  return new KinematicState(myTruth.getTime() + dur.get(),
				                      newPosition,
				                      newVelocity,
				                      newAcceleration,
				                      newOrientation,
				                      newOrientationRate);
	}
		
	private double getTime()	                  {	return myTruth.getTime();	           }
	public void setTime(double t)	              {	myTruth.setTime(t);	           }
	public int getDRAlgorithm()                 { return myAlgorithm;                  }

	public WGSPosition   getPosition()         {	return myTruth.getPosition();	       }
	public Velocity      getVelocity()   	    {	return myTruth.getVelocity();	       }
	public Acceleration  getAcceleration()	    {	return myTruth.getAcceleration();	   }
	public EulerAngles   getOrientation()	    {	return myTruth.getOrientation();   	 }
	public EulerDerivs   getOrientationRate()	{	return myTruth.getOrientationRate(); }

	public KinematicState getKinematicState()	{	return myTruth; }
	
	
	public void setPosition(WGSPosition p)         {	myTruth.setPosition(p);	       }
	public void setVelocity(Velocity v)   	       {	myTruth.setVelocity(v);	       }
	public void setAcceleration(Acceleration a)	   {	myTruth.setAcceleration(a);	       }
	public void setOrientation(EulerAngles e)	     {  myTruth.setOrientation(e);	       }
	public void setOrientationRate(EulerDerivs ed) {	myTruth.setOrientationRate(ed);	       }

}
