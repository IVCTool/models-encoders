/*******************************************************************************
 * Copyright (C) Her Majesty the Queen in Right of Canada, 
 * as represented by the Minister of National Defence, 2018
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package ca.drdc.ivct.fom.base.structs;

import ca.drdc.ivct.fom.base.SpatialRepresentation;
import ca.drdc.ivct.fom.der.DeadReckoningAlgorithm;

/**
 * Spatial structure for Dead Reckoning Algorithm FPW (2) and FPB (6).
 *
 * @author mlavallee
 */
public class SpatialFPStruct implements SpatialRepresentation {

    /**
     * Dead reckoning algorithm
     */
    private DeadReckoningAlgorithm deadReckoningAlgorithm;

    /**
     * Location of the object.
     */
    private WorldLocationStruct worldLocation;

    /**
     * Whether the object is frozen or not.
     */
    private boolean frozen;

    /**
     * The rate at which an object's position is changing over time.
     */
    private VelocityVectorStruct velocityVector;

    /**
     * The angles of rotation around the coordinate axes between the object's
     * attitude and the reference coordinate system axes (calculated as the
     * Tait-Bryan Euler angles specifying the successive rotations needed to
     * transform from the world coordinate system to the entity coordinate
     * system).
     */
    private OrientationStruct orientation;

    public SpatialFPStruct(DeadReckoningAlgorithm deadReckoningAlgorithm, WorldLocationStruct worldLocation, boolean isFrozen, OrientationStruct orientation,
        VelocityVectorStruct velocityVector) {
        super();
        this.deadReckoningAlgorithm = deadReckoningAlgorithm;
        this.worldLocation = worldLocation;
        this.frozen = isFrozen;
        this.orientation = orientation;
        this.velocityVector = velocityVector;
    }

    public WorldLocationStruct getWorldLocation() {
        return worldLocation;
    }

    public void setWorldLocation(WorldLocationStruct worldLocation) {
        this.worldLocation = worldLocation;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean isFrozen) {
        this.frozen = isFrozen;
    }

    public OrientationStruct getOrientation() {
        return orientation;
    }

    public void setOrientation(OrientationStruct orientation) {
        this.orientation = orientation;
    }

    @Override
    public VelocityVectorStruct getVelocityVector() {
        return velocityVector;
    }

    public void setVelocityVector(VelocityVectorStruct velocityVector) {
        this.velocityVector = velocityVector;
    }

    public DeadReckoningAlgorithm getDeadReckoningAlgorithm() {
        return deadReckoningAlgorithm;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((deadReckoningAlgorithm == null) ? 0 : deadReckoningAlgorithm.hashCode());
        result = prime * result + (frozen ? 1231 : 1237);
        result = prime * result + ((orientation == null) ? 0 : orientation.hashCode());
        result = prime * result + ((velocityVector == null) ? 0 : velocityVector.hashCode());
        result = prime * result + ((worldLocation == null) ? 0 : worldLocation.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SpatialFPStruct other = (SpatialFPStruct) obj;
        if (deadReckoningAlgorithm != other.deadReckoningAlgorithm)
            return false;
        if (frozen != other.frozen)
            return false;
        if (orientation == null) {
            if (other.orientation != null)
                return false;
        } else if (!orientation.equals(other.orientation))
            return false;
        if (velocityVector == null) {
            if (other.velocityVector != null)
                return false;
        } else if (!velocityVector.equals(other.velocityVector))
            return false;
        if (worldLocation == null) {
            if (other.worldLocation != null)
                return false;
        } else if (!worldLocation.equals(other.worldLocation))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "SpatialFPStruct [deadReckoningAlgo=" + deadReckoningAlgorithm + ", " + worldLocation
            + ", frozen=" + frozen + ", " + velocityVector + ", " + orientation + "]";
    }

}
