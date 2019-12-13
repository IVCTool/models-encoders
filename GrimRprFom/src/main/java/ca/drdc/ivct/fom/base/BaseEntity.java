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

import ca.drdc.ivct.fom.base.structs.EntityIdentifierStruct;
import ca.drdc.ivct.fom.base.structs.EntityTypeStruct;

/**
 * BaseEntity according to the GRIM RPR FOM
 */
public class BaseEntity {
    private EntityIdentifierStruct entityIdentifier;
    private EntityTypeStruct entityType;
    private SpatialRepresentation spatial;

    /**
     * @return the type value
     */
    public EntityTypeStruct getEntityType() {
        return this.entityType;
    }

    public void setEntityType(EntityTypeStruct entityType) {
        this.entityType = entityType;
    }

    /**
     * @return the identifier value
     */
    public EntityIdentifierStruct getEntityIdentifier() {
        return this.entityIdentifier;
    }

    public void setEntityIdentifier(EntityIdentifierStruct entityIdentifier) {
        this.entityIdentifier = entityIdentifier;
    }

    public void setSpatial(SpatialRepresentation spatial) {
        this.spatial = spatial;
    }

    public SpatialRepresentation getSpatialRepresentation() {
        return spatial;
    }

    public void setSpatialRepresentation(final SpatialRepresentation spatialRepresentation) {
        this.spatial = spatialRepresentation;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((entityIdentifier == null) ? 0 : entityIdentifier.hashCode());
        result = prime * result + ((entityType == null) ? 0 : entityType.hashCode());
        result = prime * result + ((spatial == null) ? 0 : spatial.hashCode());
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
        BaseEntity other = (BaseEntity) obj;
        if (entityIdentifier == null) {
            if (other.entityIdentifier != null)
                return false;
        } else if (!entityIdentifier.equals(other.entityIdentifier))
            return false;
        if (entityType == null) {
            if (other.entityType != null)
                return false;
        } else if (!entityType.equals(other.entityType))
            return false;
        if (spatial == null) {
            if (other.spatial != null)
                return false;
        } else if (!spatial.equals(other.spatial))
            return false;
        return true;
    }


    @Override
    public String toString() {
        return "Entity [" + this.entityIdentifier + " " + this.getEntityType().toString() + " " + this.getSpatialRepresentation() + "]";
    }
}
