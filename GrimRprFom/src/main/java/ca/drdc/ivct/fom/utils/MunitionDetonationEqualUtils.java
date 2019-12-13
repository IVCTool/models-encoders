package ca.drdc.ivct.fom.utils;

import ca.drdc.ivct.fom.base.structs.RelativePositionStruct;
import ca.drdc.ivct.fom.base.structs.VelocityVectorStruct;
import ca.drdc.ivct.fom.base.structs.WorldLocationStruct;
import ca.drdc.ivct.fom.warfare.MunitionDetonation;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class MunitionDetonationEqualUtils {
    // in meter
    private double worldLocationThreshold = 0.001;
    // in radian
    private double orientationThreshold = 0.0001;
    // in meter/second
    private double velocityThreshold = 0.001;
    // in meter/second^2
    private double accelerationThreshold = 0.001;
    //in rad/second
    private double angularVelocityThreshold = 0.0001;

    /**
     * Set the threshold value for the spatial comparison, if the value is null, set the default value
     *
     * @param worldLocationThreshold   default value is 0.001 meter
     * @param orientationThreshold     default value is 0.0001 radian
     * @param velocityThreshold        default value is 0.001 meter/second
     * @param accelerationThreshold    default value is 0.001 meter/second^2
     * @param angularVelocityThreshold default value is 0.0001 rad/second
     */
    public MunitionDetonationEqualUtils(Double worldLocationThreshold, Double orientationThreshold, Double velocityThreshold,
        Double accelerationThreshold, Double angularVelocityThreshold) {

        // set the threshold value, if the value is null, set the default value
        this.worldLocationThreshold = (worldLocationThreshold != null) ? worldLocationThreshold : this.worldLocationThreshold;
        this.orientationThreshold = (orientationThreshold != null) ? orientationThreshold : this.orientationThreshold;
        this.velocityThreshold = (velocityThreshold != null) ? velocityThreshold : this.velocityThreshold;
        this.accelerationThreshold = (accelerationThreshold != null) ? accelerationThreshold : this.accelerationThreshold;
        this.angularVelocityThreshold = (angularVelocityThreshold != null) ? angularVelocityThreshold : this.angularVelocityThreshold;
    }

    /**
     * Set the threshold value for the spatial comparison, if the value is null, set the default value
     *
     * @param thresholds containing value for:
     *                   worldLocation default value is 0.001 meter
     *                   orientation default value is 0.0001 radian
     *                   velocity default value is 0.001 meter/second
     *                   acceleration default value is 0.001 meter/second^2
     *                   angularVelocity default value is 0.0001 rad/second
     */
    public MunitionDetonationEqualUtils(Map<String, Double> thresholds) {

        // set the threshold value, if the value is null, set the default value
        this.worldLocationThreshold = (thresholds.get("worldLocation") != null) ? thresholds.get("worldLocation") : this.worldLocationThreshold;
        this.orientationThreshold = (thresholds.get("orientation") != null) ? thresholds.get("orientation") : this.orientationThreshold;
        this.velocityThreshold = (thresholds.get("velocity") != null) ? thresholds.get("velocity") : this.velocityThreshold;
        this.accelerationThreshold = (thresholds.get("acceleration") != null) ? thresholds.get("acceleration") : this.accelerationThreshold;
        this.angularVelocityThreshold = (thresholds.get("angularVelocity") != null) ? thresholds.get("angularVelocity") : this.angularVelocityThreshold;
    }

    /**
     * Use the default Threshold
     */
    public MunitionDetonationEqualUtils() {
    }

    public boolean worldLocationEquals(WorldLocationStruct firstWorldLocation, WorldLocationStruct secondWorldLocation) {
        return Math.abs(firstWorldLocation.getxPosition() - secondWorldLocation.getxPosition()) <= worldLocationThreshold &&
            Math.abs(firstWorldLocation.getyPosition() - secondWorldLocation.getyPosition()) <= worldLocationThreshold &&
            Math.abs(firstWorldLocation.getzPosition() - secondWorldLocation.getzPosition()) <= worldLocationThreshold;
    }

    public boolean velocityEquals(VelocityVectorStruct firstVelocityVectorStruct, VelocityVectorStruct secondVelocityVectorStruct) {
        return Math.abs(firstVelocityVectorStruct.getxVelocity() - secondVelocityVectorStruct.getxVelocity()) <= velocityThreshold &&
            Math.abs(firstVelocityVectorStruct.getyVelocity() - secondVelocityVectorStruct.getyVelocity()) <= velocityThreshold &&
            Math.abs(firstVelocityVectorStruct.getzVelocity() - secondVelocityVectorStruct.getzVelocity()) <= velocityThreshold;
    }

    public boolean relativePositionEquals(RelativePositionStruct firstRelativePositionStruct, RelativePositionStruct secondRelativePositionStruct) {
        return Math.abs(firstRelativePositionStruct.getBodyXPosition() - secondRelativePositionStruct.getBodyXPosition()) <= worldLocationThreshold &&
            Math.abs(firstRelativePositionStruct.getBodyYPosition() - secondRelativePositionStruct.getBodyYPosition()) <= worldLocationThreshold &&
            Math.abs(firstRelativePositionStruct.getBodyZPosition() - secondRelativePositionStruct.getBodyZPosition()) <= worldLocationThreshold;
    }

    public boolean areMunitionDetonationParametersEquals(MunitionDetonation firstMunitionDetonation, MunitionDetonation secondMunitionDetonation) {
        if (firstMunitionDetonation == secondMunitionDetonation) return true;
        if (firstMunitionDetonation.getDetonationResultCode() != secondMunitionDetonation.getDetonationResultCode())
            return false;
        if (firstMunitionDetonation.getFuseType() != secondMunitionDetonation.getFuseType()) return false;
        if (firstMunitionDetonation.getQuantityFired() != secondMunitionDetonation.getQuantityFired()) return false;
        if (firstMunitionDetonation.getRateOfFire() != secondMunitionDetonation.getRateOfFire()) return false;
        if (firstMunitionDetonation.getWarheadType() != secondMunitionDetonation.getWarheadType()) return false;
        if (!Arrays.equals(firstMunitionDetonation.getArticulatedPartData(), secondMunitionDetonation.getArticulatedPartData()))
            return false;
        if (!Objects.equals(firstMunitionDetonation.getEventIdentifier(), secondMunitionDetonation.getEventIdentifier()))
            return false;
        if (!Objects.equals(firstMunitionDetonation.getFiringObjectIdentifier(), secondMunitionDetonation.getFiringObjectIdentifier()))
            return false;
        if (!Objects.equals(firstMunitionDetonation.getMunitionObjectIdentifier(), secondMunitionDetonation.getMunitionObjectIdentifier()))
            return false;
        if (!Objects.equals(firstMunitionDetonation.getMunitionType(), secondMunitionDetonation.getMunitionType()))
            return false;
        return Objects.equals(firstMunitionDetonation.getTargetObjectIdentifier(), secondMunitionDetonation.getTargetObjectIdentifier());
    }
}
