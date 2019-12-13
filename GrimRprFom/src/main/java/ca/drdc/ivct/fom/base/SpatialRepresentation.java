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
package ca.drdc.ivct.fom.base;

import ca.drdc.ivct.fom.base.structs.OrientationStruct;
import ca.drdc.ivct.fom.base.structs.VelocityVectorStruct;
import ca.drdc.ivct.fom.base.structs.WorldLocationStruct;
import ca.drdc.ivct.fom.der.DeadReckoningAlgorithm;

public interface SpatialRepresentation {

    /**
     * Location of the object.
     *
     * @return Represent the location of an object.
     */
    WorldLocationStruct getWorldLocation();

    /**
     * Whether the object is frozen or not.
     *
     * @return true is the object is frozen in place.
     */
    boolean isFrozen();

    /**
     * The angles of rotation around the coordinate axes between the object's
     * attitude and the reference coordinate system axes (calculated as the
     * Tait-Bryan Euler angles specifying the successive rotations needed to
     * transform from the world coordinate system to the entity coordinate
     * system).
     *
     * @return OrientationStruct represent the orientation of the objects.
     */
    OrientationStruct getOrientation();

    /**
     * @return DeadReckoningAlgorithm The deadReckoningAlgorithm use to encode the data
     */
    DeadReckoningAlgorithm getDeadReckoningAlgorithm();

    /**
     * @return VelocityVectorStruct The velocity Vector of the Entity
     */
    VelocityVectorStruct getVelocityVector();
}
