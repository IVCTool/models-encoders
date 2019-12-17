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

import java.text.ParseException;

/**
 * EntityIdentifier of a base entity according to the GRIM RPR fom
 */
public class EntityIdentifierStruct {

    private int site;
    private int application;
    private int entity;

    /**
     * @param entityIdentifier The dot separated entityIdentifier containing sit, app and entity IDs.
     * @throws ParseException Happens if the entityID string is broken.
     */
    public EntityIdentifierStruct(String entityIdentifier) throws ParseException {
        super();
        String[] entityIdentifierStrArray = entityIdentifier.split("\\.");

        if (entityIdentifierStrArray.length != 3) {
            throw new ParseException("Wrong number of arguments 3 required, found " + entityIdentifierStrArray.length + " arguments.", entityIdentifierStrArray.length);
        }

        this.site = Integer.parseInt(entityIdentifierStrArray[0]);
        this.application = Integer.parseInt(entityIdentifierStrArray[1]);
        this.entity = Integer.parseInt(entityIdentifierStrArray[2]);
    }

    /**
     * @param site The site ID
     * @param application The app ID
     * @param entity The entity ID
     */
    public EntityIdentifierStruct(int site, int application, int entity) {
        super();
        this.site = site;
        this.application = application;
        this.entity = entity;
    }

    public int getSite() {
        return site;
    }

    public void setSite(int site) {
        this.site = site;
    }

    public int getApplication() {
        return application;
    }

    public void setApplication(int application) {
        this.application = application;
    }

    public int getEntity() {
        return entity;
    }

    public void setEntity(int entity) {
        this.entity = entity;
    }


    public String toBinaryString() {
        return site + "." + application + "." + entity;
    }

    @Override
    public String toString() {
        return "EntityIdentifier [ site=" + site + ", application=" + application + ", entity=" + entity + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + application;
        result = prime * result + entity;
        result = prime * result + site;
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
        EntityIdentifierStruct other = (EntityIdentifierStruct) obj;
        if (application != other.application)
            return false;
        if (entity != other.entity)
            return false;
        if (site != other.site)
            return false;
        return true;
    }


}
