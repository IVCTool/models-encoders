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
package ca.drdc.ivct.fom.der;

public enum DeadReckoningAlgorithm {
    OTHER((byte) 0),
    STATIC((byte) 1),
    DRM_FPW((byte) 2),
    DRM_RPW((byte) 3),
    DRM_RVW((byte) 4),
    DRM_FVW((byte) 5),
    DRM_FPB((byte) 6),
    DRM_RPB((byte) 7),
    DRM_RVB((byte) 8),
    DRM_FVB((byte) 9);

    private final byte discriminantByte;

    DeadReckoningAlgorithm(byte discriminantByte) {
        this.discriminantByte = discriminantByte;
    }

    public static DeadReckoningAlgorithm valueOf(byte b) {
        for (DeadReckoningAlgorithm deadReckoningAlgorithm : DeadReckoningAlgorithm.values()) {
            if (deadReckoningAlgorithm.discriminantByte == b) {
                return deadReckoningAlgorithm;
            }
        }
        throw new IllegalArgumentException("Valeur incorrect : " + b);
    }

    public byte discriminantByte() {
        return discriminantByte;
    }

    public short value() {
        return (short) discriminantByte;
    }

}
