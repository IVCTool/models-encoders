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

import java.util.StringTokenizer;


/**
 * The magnitude of the change in linear velocity of an object over time.
 *
 * @author laurenceo
 */
public class AccelerationVectorStruct {

    /**
     * Acceleration component along the X axis. m/s²
     */
    private float xAcceleration;

    /**
     * Acceleration component along the Y axis. m/s²
     */
    private float yAcceleration;

    /**
     * Acceleration component along the Z axis. m/s²
     */
    private float zAcceleration;

    public AccelerationVectorStruct(float xAcceleration, float yAcceleration, float zAcceleration) {
        this.xAcceleration = xAcceleration;
        this.yAcceleration = yAcceleration;
        this.zAcceleration = zAcceleration;
    }

    public AccelerationVectorStruct(String accelStr) {
        StringTokenizer token = new StringTokenizer(accelStr, ";");

        if (token.hasMoreTokens()) {
            this.xAcceleration = Float.parseFloat(token.nextToken());
        }

        if (token.hasMoreTokens()) {
            this.yAcceleration = Float.parseFloat(token.nextToken());
        }

        if (token.hasMoreTokens()) {
            this.zAcceleration = Float.parseFloat(token.nextToken());
        }
    }

    public float getxAcceleration() {
        return xAcceleration;
    }

    public void setxAcceleration(float xAcceleration) {
        this.xAcceleration = xAcceleration;
    }

    public float getyAcceleration() {
        return yAcceleration;
    }

    public void setyAcceleration(float yAcceleration) {
        this.yAcceleration = yAcceleration;
    }

    public float getzAcceleration() {
        return zAcceleration;
    }

    public void setzAcceleration(float zAcceleration) {
        this.zAcceleration = zAcceleration;
    }

    @Override
    public String toString() {
        return "AccelerationVectorStruct [xAcceleration=" + xAcceleration + ", yAcceleration=" + yAcceleration
            + ", zAcceleration=" + zAcceleration + "]";
    }

}
