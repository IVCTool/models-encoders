package ca.drdc.ivct.fom.utils;

import ca.drdc.ivct.fom.base.structs.EntityTypeStruct;
import ca.drdc.ivct.fom.base.structs.VelocityVectorStruct;
import ca.drdc.ivct.fom.base.structs.WorldLocationStruct;
import ca.drdc.ivct.fom.warfare.WeaponFire;

import java.util.Map;
import java.util.Objects;

public class WeaponFireEqualUtils {
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
    public WeaponFireEqualUtils(Double worldLocationThreshold, Double orientationThreshold, Double velocityThreshold,
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
    public WeaponFireEqualUtils(Map<String, Double> thresholds) {

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
    public WeaponFireEqualUtils() {
    }

    public boolean baseEntityTypeEqual(EntityTypeStruct firstEntityType, EntityTypeStruct secondEntityType) {
        return firstEntityType.equals(secondEntityType);
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

    public boolean areWeaponFiresParametersEquals(WeaponFire firstWeaponFire, WeaponFire secondWeaponFire) {
        if (firstWeaponFire == secondWeaponFire) return true;

        if (Math.abs(firstWeaponFire.getFireControlSolutionRange() - secondWeaponFire.getFireControlSolutionRange()) > worldLocationThreshold)
            return false;
        if (firstWeaponFire.getFireMissionIndex() != secondWeaponFire.getFireMissionIndex()) return false;
        if (firstWeaponFire.getFuseType() != secondWeaponFire.getFuseType()) return false;
        if (firstWeaponFire.getQuantityFired() != secondWeaponFire.getQuantityFired()) return false;
        if (firstWeaponFire.getRateOfFire() != secondWeaponFire.getRateOfFire()) return false;
        if (firstWeaponFire.getWarheadType() != secondWeaponFire.getWarheadType()) return false;
        if (!Objects.equals(firstWeaponFire.getEventIdentifier(), secondWeaponFire.getEventIdentifier()))
            return false;
        if (!Objects.equals(firstWeaponFire.getFiringObjectIdentifier(), secondWeaponFire.getFiringObjectIdentifier()))
            return false;
        if (!Objects.equals(firstWeaponFire.getMunitionObjectIdentifier(), secondWeaponFire.getMunitionObjectIdentifier()))
            return false;
        if (!Objects.equals(firstWeaponFire.getMunitionType(), secondWeaponFire.getMunitionType())) return false;
        return Objects.equals(firstWeaponFire.getTargetObjectIdentifier(), secondWeaponFire.getTargetObjectIdentifier());
    }
}
