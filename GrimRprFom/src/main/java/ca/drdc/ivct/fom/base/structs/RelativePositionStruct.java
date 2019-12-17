package ca.drdc.ivct.fom.base.structs;

/**
 * Relative position in right-handed Cartesian coordinates.
 */
public class RelativePositionStruct {
    /**
     * The distance from the reference location along the X axis (meters).
     */
    private float bodyXPosition;
    /**
     * The distance from the reference location along the Y axis (meters).
     */
    private float bodyYPosition;
    /**
     * The distance from the reference location along the Z axis (meters).
     */
    private float bodyZPosition;

    /**
     * @param bodyXPosition Relative X position
     * @param bodyYPosition Relative Y position
     * @param bodyZPosition Relative Z position
     */
    public RelativePositionStruct(float bodyXPosition, float bodyYPosition, float bodyZPosition) {
        this.bodyXPosition = bodyXPosition;
        this.bodyYPosition = bodyYPosition;
        this.bodyZPosition = bodyZPosition;
    }

    /**
     * Constructor from strings.
     * @param bodyXPosition Relative X position
     * @param bodyYPosition Relative Y position
     * @param bodyZPosition Relative Z position
     */
    public RelativePositionStruct(String bodyXPosition, String bodyYPosition, String bodyZPosition) {
        this.bodyXPosition = Float.parseFloat(bodyXPosition);
        this.bodyYPosition = Float.parseFloat(bodyYPosition);
        this.bodyZPosition = Float.parseFloat(bodyZPosition);
    }

    public float getBodyXPosition() {
        return bodyXPosition;
    }

    public void setBodyXPosition(float bodyXPosition) {
        this.bodyXPosition = bodyXPosition;
    }

    public float getBodyYPosition() {
        return bodyYPosition;
    }

    public void setBodyYPosition(float bodyYPosition) {
        this.bodyYPosition = bodyYPosition;
    }

    public float getBodyZPosition() {
        return bodyZPosition;
    }

    public void setBodyZPosition(float bodyZPosition) {
        this.bodyZPosition = bodyZPosition;
    }

    @Override
    public String toString() {
        return String.format(
            "RelativePosition [x=%s y=%s z=%s]",
            bodyXPosition,
            bodyYPosition,
            bodyZPosition
        );
    }
}
