package ca.drdc.ivct.fom.base.structs;

/**
 * The rate at which an object's position is changing over time.
 */
public class VelocityVectorStruct {
    /**
     * Velocity component along the X axis. (Meter per second)
     */
    private float xVelocity;
    /**
     * Velocity component along the Y axis. (Meter per second)
     */
    private float yVelocity;
    /**
     * Velocity component along the Z axis. (Meter per second)
     */
    private float zVelocity;

    /**
     * @param xVelocity Speed in X
     * @param yVelocity Speed in Y
     * @param zVelocity Speed in Z
     */
    public VelocityVectorStruct(float xVelocity, float yVelocity, float zVelocity) {
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        this.zVelocity = zVelocity;
    }

    /**
     * Constructor from Strings.
     * @param xVelocity Speed in X
     * @param yVelocity Speed in Y
     * @param zVelocity Speed in Z
     */
    public VelocityVectorStruct(String xVelocity, String yVelocity, String zVelocity) {
        this.xVelocity = Float.parseFloat(xVelocity);
        this.yVelocity = Float.parseFloat(yVelocity);
        this.zVelocity = Float.parseFloat(zVelocity);
    }

    public float getxVelocity() {
        return xVelocity;
    }

    public void setxVelocity(float xVelocity) {
        this.xVelocity = xVelocity;
    }

    public float getyVelocity() {
        return yVelocity;
    }

    public void setyVelocity(float yVelocity) {
        this.yVelocity = yVelocity;
    }

    public float getzVelocity() {
        return zVelocity;
    }

    public void setzVelocity(float zVelocity) {
        this.zVelocity = zVelocity;
    }

    @Override
    public String toString() {
        return "VelocityVectorStruct [x=" + xVelocity + ", y=" + yVelocity + ", z=" + zVelocity + "]";
    }
}
