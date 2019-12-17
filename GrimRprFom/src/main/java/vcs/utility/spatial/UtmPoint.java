/**
 * COPYRIGHT BY HER MAJESTY THE QUEEN AS REPRESENTED BY THE MINISTER OF 
 * NATIONAL DEFENCE, 2006
 */


package vcs.utility.spatial;

import vcs.utility.spatial.Latitude.*;
import vcs.utility.spatial.Longitude.*;
import vcs.utility.spatial.WGSModel.*;

/**
 * This class provides a representation for UTM coordinates.  It provides some
 * utility functions for finding azimuth angles between points, and converting to
 * Lat/Lon representations.
 *
 */
public class UtmPoint implements java.io.Serializable {

	private static final long serialVersionUID = -1134710976309726754L;
	public static final double DEFAULTFALSEEASTING = 500000;       //500,000 meters 
	public static final double DEFAULTFALSENORTHING = 10000000; //10,000,000 meters
	private double FALSEEASTING = DEFAULTFALSEEASTING;       //500,000 meters 
	private double FALSENORTHING = DEFAULTFALSENORTHING; //10,000,000 meters
	protected WGSModel wm;
	protected double eastings;
	protected double northings;
	protected int zoneNumber;
	protected Character zoneLetter = null;


	/**
	 * Constructor that allows you to create a UTM point and specify the world model to use.
	 * 
	 * @param wmv	the world model.
	 * @param e	the easting value in meters
	 * @param n	the northing value in meters
	 * @param zN	the UTM zone number
	 * @param zL	the UTM zone letter
	 * @throws WorldModelUnsupportedException
	 */
	public UtmPoint(WorldModelVersion wmv, double e, double n, int zN, char zL) throws WorldModelUnsupportedException {
		super();
		wm = new WGSModel(wmv);
		eastings = e;
		northings = n;
		zoneNumber = zN;
		zoneLetter = zL;
	}//end 


	/**
	 * Constructor for UTM point from UTM values. The default WGS84 world model will be used.
	 * 
	 * @param e	the easting in meters
	 * @param n	the northing in meters
	 * @param zN	the UTM zone number
	 * @param zL	the UTM zone letter
	 */
	public UtmPoint(double e, double n, int zN, char zL) {
		super();
		wm = new WGSModel();
		eastings = e;
		northings = n;
		zoneNumber = zN;
		zoneLetter = zL;
	}//end 

	/**
	 * Constructor that doesn't take the zone.
	 * 
	 * @param wmv	the world model
	 * @param e	the easting in meters
	 * @param n	the northing in meters
	 * @throws WorldModelUnsupportedException
	 */
	public UtmPoint(WorldModelVersion wmv, double e, double n) throws WorldModelUnsupportedException {
		super();
		wm = new WGSModel(wmv);
		eastings = e;
		northings = n;
	}//end constructor

	/**
	 * Allows a false northing value to be added.
	 * 
	 * http://en.wiktionary.org/wiki/false_northing
	 * 
	 * TODO - should look at how these are actually used; having a setter breaks the idea of
	 * immutability.  Maybe not a real issue with this, but maybe the false northing 
	 * should just be a fixed value anyway as per the article above.
	 * 
	 * @param fn	the value in meters
	 */
	public void setFalseNorthing(double fn) {
		FALSENORTHING = fn;
	}//end setFalseNorthing

	/**
	 * 
	 * @return	the value of the false northing in meters.
	 */
	public double getFalseNorthing() {
		return FALSENORTHING;
	}//end getFalseNotthing

	/**
	 * Allows a false easting to be set.
	 * 
	 * http://support.esri.com/en/knowledgebase/GISDictionary/term/false%20easting
	 * 
	 * @param fe the value of the false easting in meters
	 */
	public void setFalseEasting(double fe) {
		FALSEEASTING = fe;
	}//end 

	/**
	 * Lets you get the false easting value.
	 * 
	 * @return the value of the false easting in meters.
	 */
	public double getFalseEasting() {
		return FALSEEASTING;
	}//end 
	
	
	/**
	 * The most basic constructor, allows you to set the easting and northing.
	 * The default WGS84 world model will be used.
	 * 
	 * @param e the easting in meters
	 * @param n	the northing in meters
	 */
	public UtmPoint(double e, double n) {
		wm = new WGSModel();
		eastings = e;
		northings = n;
	}//end

	/**
	 * Gives a human readable string reprsentation of the point.
	 * ie:  5568732.334 423756.333 20T
	 * 
	 *   @return a string representation of the UTM position.
	 */
	public String toString() {
		return wm.toString() + " " + 
				Double.toString(eastings) + " " + Double.toString(northings) + " " + 
				Integer.toString(zoneNumber) + " " + zoneLetter; 
	}//end toString

	/**
	 *	Determines if this is the same point in space.
	 *	NOTE: there is no tolerance in this function so it may be very difficult
	 *	to get two UTMPoint objects to be equal, the same as it is almost
	 *	impossible for tow double values to be equal.
	 *
	 *	TODO - this will never return true except by wild coincidence.  We need to introduce an accuracy
	 * 
	 *   This function converts coordinates to LatLong before comparison, to eliminate
	 *   any potential differences caused by differing WorldModels.
	 *   
	 *   @return Boolean : whether or not the specified object is logically equivalent to this one.
	 */
	public boolean equals(UtmPoint other) {
		boolean result = false;

		try
		{
			result = toLatLong().equals(other.toLatLong());
		}
		catch (Exception E)
		{
		}

		return result;
	}//end equals  


	/**
	 * @return the easting value in meters.
	 */
	public double getEastings()   { return eastings;  }
	
	/**
	 * @return the northing value in meters 
	 */
	public double getNorthings()  { return northings;  }
	
	/**
	 * @return the UTM zone number
	 */
	public int    getZoneNumber() { return zoneNumber;  }
	
	/**
	 * @return the UTM zone letter
	 */
	public char   getZoneLetter() { return zoneLetter;  }

	/**
	 * 
	 * Calculates the straight-line distance between this UTMPoint and another.
	 * 
	 *  @return the distance between the two points in metres
	 */
	public double calcDistanceTo(UtmPoint other) {
		return Math.sqrt(Math.pow(northings - other.getNorthings(), 2) 
				+ Math.pow(eastings  - other.getEastings(),  2)); //in metres
	}//end calcDistance


	/**
	 * Calculates the azimuth angle from this UTMPoint to another.
	 * 
	 * @return the azimuth angle in radians
	 */
	public double calcAngleTo(UtmPoint other) {
		//First get the lengths of the sides of the triangle.
		double deltaE = Math.abs(other.getEastings() - eastings);
		double deltaN = Math.abs(other.getNorthings() - northings);
		
		//Now get the angle between them
		double a = Math.atan(deltaN/deltaE);

		//Now we need to figure out what quadrant that is and add the appropriate amount
		if (other.getNorthings() >= northings && other.getEastings() >= eastings) {
			//This is the NE quadrant we need to subtract a from 90* to get it
			return Math.PI/2.0 - a;
		}
		if (other.getNorthings() < northings && other.getEastings() >= eastings) {
			//This is the SE quadrant, so we need to add 90* to it
			return a + Math.PI/2.0;
		}
		if (other.getNorthings() < northings && other.getEastings() < eastings) {
			//This is the SW quadrant, so we need to subtract from 270*
			return 3.0*Math.PI/2.0 - a;
		}

		//So, it must be the NW quadrant, so we need to add 270*
		return a + 3.0*Math.PI/2.0;

	}//end calcAngle
	
	/**
	 * calculates the azimuth angle (relative to north) from this point to another in degrees.
	 * 
	 * @param other	the point the angle is measure to.
	 * @return the angle in degrees.
	 */
	public double azimuthAngleDegrees(UtmPoint other) {
		double radians = calcAngleTo(other);
		return Math.toDegrees(radians);
	}//end

	/**
	 *   Converts UTM coords to Lat/Long.  Equations from USGS Bulletin 1532.
	 *   
	 *   East Longitudes are positive, West longitudes are negative.
	 *   North latitudes are positive, South latitudes are negative
	 *   
	 *   @throws LatitudeOutOfBounds
	 *   @return a LatLong object representing the converted UTM position.
	 */
	public LatLong toLatLong() throws LatitudeOutOfBounds, LongitudeOutOfBounds {
		double e2 = wm.getEccentricitySquared();
		double e1 = (1.0 - Math.sqrt(1.0 - e2)) /
				(1.0 + Math.sqrt(1.0 - e2));

		double x = eastings - FALSEEASTING; //always remove the false easting offset
		double y = northings;
		if (zoneLetter != null && zoneLetter - 'N' < 0)        //point is in southern hemisphere
			y -= FALSENORTHING;            //remove false northing offset for the southern hemisphere

		double LongOrigin = (zoneNumber - 1) * 6 - 180 + 3;  //+3 puts origin in middle of zone

		//    double M = y / wm.getFlattening();
		double M = y / 0.9996 ;
		Angle mu = new Angle(M / (wm.getSemiMajorAxis() * (1.0 - e2 / 4.0 
				- 3.0 * e2 * e2 / 64.0 
				- 5.0 * e2 * e2 * e2 / 256.0)));

		double J1 = 3.0 * e1 / 2.0 - 27.0 * e1 * e1 * e1 / 32.0, 
				J2 = 21.0 * e1 * e1 / 16.0 - 55.0 * e1 * e1 * e1 * e1 / 32.0,
				J3 = 151.0 * e1 * e1 * e1 / 96.0,
				J4 = 1097.0 * e1 * e1 * e1 * e1 / 512.0;

		Latitude fp = new Latitude(mu.get() + J1 * Math.sin(2.0 * mu.get()) 
				+ J2 * Math.sin(4.0 * mu.get())
				+ J3 * Math.sin(6.0 * mu.get())
				+ J4 * Math.sin(8.0 * mu.get()));

		double eccPrimeSquared = e2 / (1.0 - e2);
		double C1 = eccPrimeSquared * fp.cos() * fp.cos();
		double T1 = fp.tan() * fp.tan();
		double R1 = wm.getSemiMajorAxis() * (1 - e2) / Math.pow(1 - e2 * fp.sin() * fp.sin(), 1.5);
		double N1 = wm.getSemiMajorAxis() / Math.sqrt(1 - e2 * fp.sin() * fp.sin());
		double D = x / (N1 * wm.getFlattening());

		double Q1 = N1 * fp.tan() / R1,
				Q2 = D * D / 2.0,
				Q3 = (5.0 + 3.0 * T1 + 10.0 * C1 - 4.0 * C1 * C1 - 9.0 * eccPrimeSquared) * Math.pow(D, 4.0) / 24.0,
				Q4 = (61.0 + 90.0 * T1 + 298.0 * C1 + 45.0 * T1 * T1 - 252.0 * eccPrimeSquared - 3.0 * C1 * C1)
				* Math.pow(D, 6.0) / 720.0;

		Latitude latitude = new Latitude(fp.get() - Q1 * (Q2 - Q3 + Q4));

		double Q5 = D,
				Q6 = (1.0 + 2.0 * T1 + C1) * D * D * D / 6.0,
				Q7 = (5.0 - 2.0 * C1 + 28.0 * T1 - 3.0 * C1 * C1 + 8.0 * eccPrimeSquared + 24.0 * T1 * T1)
				* Math.pow(D, 5.0) / 120.0;

		Longitude longitude = new Longitude(Math.toRadians(LongOrigin) + (Q5 - Q6 + Q7) / fp.cos());

		return new LatLong(latitude, longitude);
	}//end toLatLong


	/**
	 * Returns the correct UTM zone number.
	 * 
	 * @return the UTM zone number for the latitude and longitude. 
	 */

	public static int getUtmNumber(double latDegrees, double longDegrees) {
		//handle the special UTM zones
		if (latDegrees >= 56.0 && latDegrees < 64.0 && 
				longDegrees >= 3.0 && longDegrees < 12.0)
			return 32;

		if (latDegrees >= 72.0 && latDegrees < 84.0 && longDegrees >= 0.0) 
		{
			if (longDegrees <  9.0) 
				return 31;
			if (longDegrees < 21.0) 
				return 33;
			if (longDegrees < 33.0) 
				return 35;
			if (longDegrees < 42.0) 
				return 37;
		}//end if

		return (int)((longDegrees + 180) / 6) + 1;
	}//end getUtmNumber


	/**
	 * getUtmLetter
	 * 
	 * Returns the correct UTM zone letter designator.
	 * Returns 'Z' if latitude is outside the UTM limits of 84N to 80S.
	 * 
	 * @return the UTM zone letter for the latitude, or Z if it is out-of-bounds 
	 */
	public static char getUtmLetter(double latDegrees) {
		if (latDegrees <= 84)
		{
			if (latDegrees >=  72) return 'X';
			if (latDegrees >=  64) return 'W';
			if (latDegrees >=  56) return 'V';
			if (latDegrees >=  48) return 'U';
			if (latDegrees >=  40) return 'T';
			if (latDegrees >=  32) return 'S';
			if (latDegrees >=  24) return 'R';
			if (latDegrees >=  16) return 'Q';
			if (latDegrees >=   8) return 'P';
			if (latDegrees >=   0) return 'N';
			if (latDegrees >=  -8) return 'M';
			if (latDegrees >= -16) return 'L';
			if (latDegrees >= -24) return 'K';
			if (latDegrees >= -32) return 'J';
			if (latDegrees >= -40) return 'H';
			if (latDegrees >= -48) return 'G';
			if (latDegrees >= -56) return 'F';
			if (latDegrees >= -64) return 'E';
			if (latDegrees >= -72) return 'D';
			if (latDegrees >= -80) return 'C';
		}

		return 'Z'; //This is here as an error flag to show that the Latitude is outside the UTM limits
	}//end getUtmLetter

}//end 
