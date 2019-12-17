/**
 * COPYRIGHT BY HER MAJESTY THE QUEEN AS REPRESENTED BY THE MINISTER OF 
 * NATIONAL DEFENCE, 2006
 */

package vcs.utility.spatial;

import vcs.utility.spatial.WGSModel.*;
import vcs.utility.spatial.Latitude.*;
import vcs.utility.spatial.Longitude.*;

public class LatLong implements java.io.Serializable
{
	private static final long serialVersionUID = -3284350485977908824L;

	/** The latitude object. */
	protected Latitude Lat;
	
	/** The longitude object. */
	protected Longitude Long;

	protected void init(double latitude, double longitude)  throws LatitudeOutOfBounds, LongitudeOutOfBounds 
	{
		Lat  = new Latitude(latitude);
		Long = new Longitude(longitude);
	}//end init
	 
	/**
	 * Default constructor.  Does nothing.
	 *
	 */
	public LatLong() {
		
	}
	
	/**
	 * Constructor
	 * 
	 * @param latitude latitude in radians
	 * @param longitude longitude in radians 
	 * @throws LatitudeOutOfBounds
	 * @throws LongitudeOutOfBounds
	 */
	public LatLong(double latitude, double longitude) throws LatitudeOutOfBounds, LongitudeOutOfBounds {
			init(latitude, longitude);
	}//end constructor

		/**
		 * Constructor
		 * 
		 *
		 * @throws LatitudeOutOfBounds
		 */
		public LatLong(Angle latitude, Angle longitude) throws LatitudeOutOfBounds, LongitudeOutOfBounds
		{
			init(latitude.get(), longitude.get());
		}

		public LatLong(Latitude latitude, Longitude longitude) 
		{
			Lat = latitude;
			Long = longitude;
		}


		/**
	   * toString
	   * 
	   *   @return A String representation of the object.
	   */
		
		public String toString()
		{
		  return Lat.toString() + " " + Long.toString(); 
		}
		

		/**
	   * equals
	   * 
	   *   @return Boolean : whether or not the specified object is logically equivalent to this one.
	   */
	  public boolean equals(LatLong other)
		{
	  	return Lat.equals(other.getLat()) &&
	  	       Long.equals(other.getLong());
		}    
		
	  /**
	   * Returns the latitude in radians, southern values will be negative.  This is deprecated
	   * and shouldn't be used anymore.  use getLatRadians instead.
	   * 
	   * @return the latitude as radians, south negative.
	   */
	  @Deprecated
	  public double getLat()         { return getLatRadians(); }
	  
	  /**
	   * Returns the longitude in radians, western values will be negative.  This is deprecated
	   * and shouldn't be used anymore.  use getLongtRadians instead.
	   * 
	   * @return the longitude as radians, west negative.
	   */
	  @Deprecated
	  public double getLong()        { return getLongRadians(); }
	  
		public Latitude getLatAngle()    { return Lat; }
	  public Longitude getLongAngle()   { return Long; }

	  public double getLatRadians()  { return Lat.get(); }
	  public double getLongRadians() { return Long.get(); }
		public double getLatDegrees()  { return Lat.getDecimalDegrees(); }
	  public double getLongDegrees() { return Long.getDecimalDegrees(); }

	  /**
	   * toUtm
	   * 
	   *   Converts lat/long to UTM coords.  Equations from USGS Bulletin 1532
	   *   East Longitudes are positive, West longitudes are negative.
	   *   North latitudes are positive, South latitudes are negative
	   *   
	   *   @return : UtmPoint
	   */
	  public UtmPoint toUTM()
	  {
	  	UtmPoint UP = null;
	  	
	  	try
	  	{
	      UP = toUTM(new WGSModel());
	  	}
	  	catch (WorldModelUnsupportedException E)
	  	{
	  	}
	  	
	  	return UP;
	  }
    
	  /**
	   * toUtm
	   *   Converts lat/long to UTM coords .  Equations from USGS Bulletin 1532
	   *   East Longitudes are positive, West longitudes are negative.
	   *   North latitudes are positive, South latitudes are negative
	   *   
	   *   @param  wmv : the WorldModel version to use in the conversion
	   *   @return : UtmPoint
	   */
	  public UtmPoint toUTM(WorldModelVersion wmv) throws WorldModelUnsupportedException
	  {
	    return toUTM(new WGSModel(wmv));
	  }
    
	  /**
	   * toUtm
	   *   Converts lat/long to UTM coords.  
	   *   Equations from USGS Bulletin 1532
	   *   East Longitudes are positive, West longitudes are negative.
	   *   North latitudes are positive, South latitudes are negative
	   *   
	   *   @param  wm : the WorldModel to use in the conversion
	   *   @return : UtmPoint
	   */
	  public UtmPoint toUTM(WGSModel wm) throws WorldModelUnsupportedException 
	  { 
	    //calculate the UTM zone
	    char zoneLetter = UtmPoint.getUtmLetter(Lat.getDecimalDegrees());
	    int zoneNumber  = UtmPoint.getUtmNumber(Lat.getDecimalDegrees(), Long.getDecimalDegrees());

	    //calculate the middle of the longitude zone
	    double LongOrigin = (zoneNumber - 1)*6 - 180 + 3;  //+3 puts origin in middle
	    double LongOriginRad = Math.toRadians(LongOrigin);

	    //calculate the arc of the Meridian
	    double N = (wm.getRadius() - wm.getEcc()) / (wm.getRadius() + wm.getEcc()); // n
	    double T = Math.pow((Lat.sin() / Lat.cos()), 2);
	    double Meridian = ((Lat.get() * (1 + N + 1.25 * (Math.pow(N, 2) + Math.pow(N, 3)))) 
	    		- (Lat.sin() * Lat.cos() * (3 * (N + Math.pow(N, 2) + 0.875 * Math.pow(N, 3)))) 
	    		+ (Math.sin(2*Lat.get()) * Math.cos(2*Lat.get()) * (1.875 * (Math.pow(N, 2) + Math.pow(N, 3))))
	    		- (Math.sin(3*Lat.get()) * Math.cos(3*Lat.get()) * 35 / 24 * Math.pow(N, 3))) * wm.getEcc();
	    
	    //VRH
	    double C = 1 - wm.getEccentricitySquared() * Lat.sin() * Lat.sin();
	    double V = wm.getRadius() / Math.sqrt(C);
	    double R = V * (1 - wm.getEccentricitySquared()) / C;
	    double H = V / R - 1.0;
	    double LongDif = Long.get() - LongOriginRad; 

	    double northings = Meridian + Math.pow(LongDif, 2) * (V / 2 * Lat.sin() * Lat.cos()) 
	                       + Math.pow(Math.pow(LongDif, 2),2) * (V / 24 * Lat.sin() 
	                       * (Math.pow(Lat.cos(), 3)) * (5 - (T) + 9 * H)) 
	                       + Math.pow(Math.pow(LongDif, 2),2) * Math.pow(LongDif, 2) 
	                       * (V / 720 * Lat.sin() * (Math.pow(Lat.cos(), 3)) 
	                       * (Math.pow(Lat.cos(), 2)) * (61 - 58 * (T) + T * T));
	    if (Lat.get() < 0)
	      northings += UtmPoint.DEFAULTFALSENORTHING; 

	    double eastings = LongDif * (V * Lat.cos()) + Math.pow(LongDif, 2) 
	                      * LongDif * (V / 6 * (Math.pow(Lat.cos(), 3)) * (V / R - T)) 
	                      + Math.pow(Math.pow(LongDif, 2),2) * LongDif 
	                      * ((V / 120 * (Math.pow(Lat.cos(), 3)) 
	                      * (Math.pow(Lat.cos(), 2))) 
	                      * (5 - 18 * T + T * T + 14 * H - 58 * T * H));
	    
	    eastings += UtmPoint.DEFAULTFALSEEASTING; //always add the false Easting offset 
	    
	    return new UtmPoint(wm.getVersion(), eastings, northings, zoneNumber, zoneLetter);
	  }

		/**
		 * calcMappingParams
		 * 
		 * Calculates distance and azimuth values given the latitude and longitude values of a reference point.  
		 * The default world model is used.  Note that the azimuth angle returned is given with respect to North,
		 * and positive azimuths are westward.
		 * 
		 *
		 * @param OtherLL The reference point latitude and longitude in radians.
		 *
		 * @return A MappingParameters object.
		 */
		public MappingParameters calcMappingParams(LatLong OtherLL)
		{
		   return calcMappingParams(new WGSModel(), OtherLL);
		}

	  
		/**
		 *
		 * Calculates distance and azimuth values given the latitude and longitude values of a reference point.  
		 * The default world model is used.  Note that the azimuth angle returned is given with respect to North,
		 * and positive azimuths are westward.
		 *
		 * @param wmv     The WorldModelVersion to use in the calculation.
		 * @param OtherLL The reference point latitude and longitude in radians.
		 *
		 * @return A MappingParameters object.
		 */
		public MappingParameters calcMappingParams(WorldModelVersion wmv, LatLong OtherLL) throws WorldModelUnsupportedException
		{
		   return calcMappingParams(new WGSModel(wmv), OtherLL);
		}

	  
	  /**
		 * calcMappingParams
		 * 
		 * Calculates distance and azimuth values given the latitude and longitude values of a reference point.  
		 * The default world model is used.  Note that the azimuth angle returned is given with respect to North,
		 * and positive azimuths are westward.
		 *
		 * @param wm      The WorldModel to use in the calculation.
		 * @param OtherLL The reference point latitude and longitude in radians.
		 *
		 * @return A MappingParameters object.
		 */
		public MappingParameters calcMappingParams(WGSModel wm, LatLong OtherLL)
		{
			// Define and calculate values for variables necessary in the conversion
			Angle U1 = new Angle(Math.atan((1 - wm.getFlattening()) * Lat.tan()));
			Angle U2 = new Angle(Math.atan((1 - wm.getFlattening()) * OtherLL.Lat.tan()));

			// Define and initialise lambda, omega and lambda2 with approximate values
			Angle lambda = new Angle(OtherLL.getLong() - Long.get());
			Angle omega = new Angle(lambda.get());
			Angle lambda2 = new Angle(0.0);

			// Define and initialise variables required to calculate lambda accurately
			double sinp2Sigma = 0.0;
			double sinSigma   = 0.0;
			double cosSigma   = 0.0;
			Angle  sigma      = new Angle(0.0);
			double sinAlpha   = 0.0;
			Angle  alpha      = new Angle(0.0);
			double cos2SigmaM = 0.0;
			double C          = 0.0;

			double cosU1 = U1.cos();
			double sinU1 = U1.sin();
			double cosU2 = U2.cos();
			double sinU2 = U2.sin();
			double coslambda = lambda.cos();
			double sinlambda = lambda.sin();
			double cosalpha = alpha.cos();

			// Iterate until the change in lambda is not significant
			do 
			{
				lambda2 = lambda;
				sinp2Sigma = Math.pow(cosU2 * sinlambda, 2) + Math.pow(cosU1 * sinU2 - sinU1 * cosU2 * coslambda, 2);
				cosSigma = sinU1 * sinU2 + cosU1 * cosU2 * coslambda;
				sinSigma = Math.sqrt(sinp2Sigma);
				sigma = new Angle(Math.atan2(sinSigma, cosSigma));
				sinAlpha = (cosU1 * cosU2 * sinlambda) / sinSigma;
				alpha = new Angle(Math.asin(sinAlpha));
				cosalpha = alpha.cos();
				cos2SigmaM = cosSigma - ((2 * sinU1 * sinU2) / (cosalpha * cosalpha));
				C = (wm.getFlattening() / 16) * cosalpha * cosalpha * (4 + wm.getFlattening() * (4 - 3 * cosalpha * cosalpha));
				lambda = new Angle(omega.get() + (1 - C) * wm.getFlattening() * sinAlpha * (sigma.get() + C * sinSigma * (cos2SigmaM + C * cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM))));
				coslambda = lambda.cos();
				sinlambda = lambda.sin();
			} 
			while (Math.abs(lambda2.difference(lambda).get()) > 1E-9);

			// Calculate the variables necessary to calculate the ellipsoidal distance
			double up2 = (cosalpha * cosalpha * (wm.getSemiMajorAxis() * wm.getSemiMajorAxis() - wm.getSemiMinorAxis() * wm.getSemiMinorAxis())) / (wm.getSemiMinorAxis() * wm.getSemiMinorAxis());
			double A = 1 + ((up2 / 16384) * (4096 + up2 * (-768 + up2 * (320 - 175 * up2))));
			double B = (up2 / 1024) * (256 + up2 * (-128 + up2 * (74 - 47 * up2)));
			double deltaSigma = B * sinSigma * (cos2SigmaM + (B / 4) * (cosSigma * (-1 + 2 * (cos2SigmaM * cos2SigmaM))
										- (B/6) * cos2SigmaM * (-3 + 4 * sinp2Sigma) * (-3 + 4 * cos2SigmaM * cos2SigmaM)));

			double distance = wm.getSemiMinorAxis() * A * (sigma.get() - deltaSigma);

			Angle  azimuth   = new Angle(Math.atan2(cosU2 * sinlambda,  cosU1 * sinU2 - sinU1 * cosU2 * coslambda));
			
			double height = Math.atan2(cosU1 * sinlambda, -sinU1 * cosU2 + cosU1 * sinU2 * coslambda);
			
			return new MappingParameters(distance, azimuth, height);
		}

		/**
		 * calcLatLongAzimuth 
		 * 
		 * Calculates the latitude, longitude and
		 * reverse azimuth of a point given the latitude and longitude values of a
		 * reference point, the distance between it and the required point and the
		 * forward azimuth for the two points.
		 *
		 * @param lat1       : The latitude of the reference point.
		 * @param long1      : The longitude of the reference point.
		 * @param distance   : The ellipsoidal distance between two points.
		 * @param alpha12    : The forward azimuth between two points in radians.
		 *
		 * @return an array containing the lat, long, and reverse azimuth of the target point
		 */
		public static double[] calculateLatLongAzimuth(double lat1, double long1, double distance, double alpha12)
		{
			double a = 6378137.0;   //The semi major axis of the ellipsoid
			double f = 1.0 / 298.257223563;   //The flattening of the ellipsoid
			double b = a * ( 1.0 - f );  //The semi minor axis of the ellipsoid
			double e2 = f * ( 2.0 - f ); //The square of the eccentricity
			double e = Math.sqrt( e2 );  //The eccentricity of the ellipsoid
			double cosalpha12 = Math.cos( alpha12 );
			double sinalpha12 = Math.sin( alpha12 );

			// Define and initialise the return byte
			double returnArray[] = { 0.0, 0.0, 0.0 };

			// Define and calculate values for variables necessary in the conversion
			double U1 = Math.atan( ( 1 - f ) * Math.tan( lat1 ) );

			double cosU1 = Math.cos( U1 );
			double sinU1 = Math.sin( U1 );

			double sigma1 = Math.atan2( Math.tan( U1 ), cosalpha12 );
			double alpha = Math.asin( cosU1 * sinalpha12 );

			double cosalpha = Math.cos( alpha );
			double sinalpha = Math.sin( alpha );

			double up2 = ( cosalpha * cosalpha * ( a * a - b * b ) ) / ( b * b );
			double A = 1 + ( ( up2 / 16384 ) * ( 4096 + up2 * ( -768 + up2 * ( 320 - 175 * up2 ) ) ) );
			double B = ( up2 / 1024 ) * ( 256 + up2 * ( -128 + up2 * ( 74 - 47 * up2 ) ) );
			double sigma = distance / ( b * A );

			// Define and initialise variables required to calculate lambda accurately
			double val2SigmaM = 0.0;
			double sigmaLast = 0.0;
			double deltaSigma = 0.0;

			double cossigma = Math.cos( sigma );
			double sinsigma = Math.sin( sigma );
			double cosval2SigmaM = Math.cos( val2SigmaM );
			double sinval2SigmaM = Math.sin( val2SigmaM );

			// Iterate until the change in lambda is not significant
			do {
				sigmaLast = sigma;
				val2SigmaM = 2 * sigma1 + sigma;
				cosval2SigmaM = Math.cos( val2SigmaM );
				sinval2SigmaM = Math.sin( val2SigmaM );

				deltaSigma = B * sinsigma * ( Math.cos( val2SigmaM + (B/4) * ( cossigma * ( -1 + 2 * ( cosval2SigmaM * cosval2SigmaM ) )
									- (B/6) * cosval2SigmaM * ( -3 + 4 * sinsigma * sinsigma ) * ( -3 + 4 * cosval2SigmaM * cosval2SigmaM ) ) ) );

				sigma = deltaSigma + ( distance / ( b * A ) );
				cossigma = Math.cos( sigma );
				sinsigma = Math.sin( sigma );
			} while( Math.abs( sigmaLast - sigma ) > 1E-9);

			// Calculate and store latitude
			double lat2 = Math.atan2( sinU1 * cossigma + cosU1 * sinsigma * cosalpha12, ( 1 - f ) * 
									Math.sqrt( sinalpha * sinalpha + Math.pow( sinU1 * sinsigma - cosU1 * cossigma * cosalpha12, 2 ) ) );
			returnArray[0] = lat2;

			// Calculate variables necessary for conversion
			double lambda = Math.atan2( sinsigma * sinalpha12, cosU1 * cossigma - sinU1 * sinsigma * cosalpha12 );
			double C = (f/16) * cosalpha * cosalpha * ( 4 + f * ( 4 - 3 * cosalpha * cosalpha ) );
			double omega = lambda - ( 1 - C ) * f * sinalpha * ( sigma + C * sinsigma * ( cosval2SigmaM + C * cossigma * 
									( -1 + 2 * cosval2SigmaM * cosval2SigmaM ) ) );

			// Calculate and store latitude
			double long2 = long1 + omega;
			returnArray[1] = long2;

			// Calculate and store reverse azimuth
			//double alpha21 = Math.atan( sinalpha / ( -sinU1 * sinsigma + cosU1 * cossigma * cosalpha12 ) );
			double alpha21 = Math.atan2( sinalpha, -sinU1 * sinsigma + cosU1 * cossigma * cosalpha12 );
			//if (lat2 > lat1) {
			//	alpha21 = alpha21 + Math.PI;
			//}
			returnArray[2] = alpha21;

			// Return the label
			return returnArray;
		}
		
	  public Rotator getR_LG_TO_G()
	  {
	    double sinlat  = Lat.sin();
	    double coslat  = Lat.cos();
	    double sinlong = Long.sin();
	    double coslong = Long.cos();

	  	return new Rotator
	  	 (-sinlong,  -sinlat * coslong,  coslat * coslong,
	       coslong,  -sinlat * sinlong,  coslat * sinlong,
	           0.0,             coslat,            sinlat);
	  }

	  
	  public DerivRotator getDerivR_LG_TO_G(LatLong other)
	  {
	    double sinlat  = Lat.sin();
	    double coslat  = Lat.cos();
	    double sinlong = Long.sin();
	    double coslong = Long.cos();

	  	DerivRotator temp1 = new DerivRotator
	  	  (-coslong,   sinlat * sinlong,  -coslat * sinlong,
	       -sinlong,  -sinlat * coslong,   coslat * coslong,
	            0.0,                0.0,                0.0);

	  	DerivRotator temp2 = new DerivRotator
	  	   (0.0,  -coslat * coslong,  -sinlat * coslong,
	        0.0,  -coslat * sinlong,  -sinlat * sinlong,
	        0.0,            -sinlat,             coslat);

	    DerivRotator t1 = temp1.scaleBy(other.getLong());
	    DerivRotator t2 = temp2.scaleBy(other.getLat());

	    return t1.addTo(t2);
	  }

		
}