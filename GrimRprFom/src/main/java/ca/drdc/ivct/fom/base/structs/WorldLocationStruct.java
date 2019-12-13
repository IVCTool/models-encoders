package ca.drdc.ivct.fom.base.structs;

/**
 * From the HLA firedRecord Position is in meter and are represented by a
 * float64(double)
 *
 * @author mlavallee
 */
public class WorldLocationStruct {
    /**
     * Distance from the origin along the X axis.
     */
    private double xPosition;
    /**
     * Distance from the origin along the Y axis.
     */
    private double yPosition;
    /**
     * Distance from the origin along the Z axis.
     */
    private double zPosition;

    /**
     * @param xPosition X value
     * @param yPosition Y value
     * @param zPosition Z value
     */
    public WorldLocationStruct(double xPosition, double yPosition, double zPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.zPosition = zPosition;
    }

    /**
     * Constructor from Strings
     * @param xPosition X value
     * @param yPosition Y value
     * @param zPosition Z value
     */
    public WorldLocationStruct(
        String xPosition,
        String yPosition,
        String zPosition
    ) {
        this.xPosition = Double.parseDouble(xPosition);
        this.yPosition = Double.parseDouble(yPosition);
        this.zPosition = Double.parseDouble(zPosition);
    }

    public double getxPosition() {
        return xPosition;
    }

    public void setxPosition(double xPosition) {
        this.xPosition = xPosition;
    }

    public double getyPosition() {
        return yPosition;
    }

    public void setyPosition(double yPosition) {
        this.yPosition = yPosition;
    }

    public double getzPosition() {
        return zPosition;
    }

    public void setzPosition(double zPosition) {
        this.zPosition = zPosition;
    }

    @Override
    public String toString() {
        return "WorldLocationStruct [x=" + xPosition + ", y=" + yPosition + ", z=" + zPosition + "]";
    }
}
