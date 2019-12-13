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
package ca.drdc.ivct.coders.base.structs;

import ca.drdc.ivct.fom.base.SpatialRepresentation;
import ca.drdc.ivct.fom.base.structs.SpatialFPStruct;
import ca.drdc.ivct.fom.base.structs.SpatialRVStruct;
import ca.drdc.ivct.fom.der.DeadReckoningAlgorithm;
import hla.rti1516e.encoding.*;

public class SpatialCoder {

    private final HLAvariantRecord<HLAoctet> _variantCoder;
    private final SpatialFPStructCoder fpStructCoder;
    private final SpatialRVStructCoder rvStructCoder;
    private EncoderFactory encoderFactory;

    public SpatialCoder(EncoderFactory encoderFactory) {
        this.encoderFactory = encoderFactory;
        fpStructCoder = new SpatialFPStructCoder(encoderFactory);
        HLAfixedRecord fpStructCoderFixedRecord = fpStructCoder.getFixedRecord();

        rvStructCoder = new SpatialRVStructCoder(encoderFactory);
        HLAfixedRecord rvStructCoderFixedRecord = rvStructCoder.getFixedRecord();

        _variantCoder = encoderFactory.createHLAvariantRecord(encoderFactory.createHLAoctet());

        _variantCoder.setVariant(encoderFactory.createHLAoctet(
            DeadReckoningAlgorithm.DRM_FPW.discriminantByte()), fpStructCoderFixedRecord);
        _variantCoder.setVariant(encoderFactory.createHLAoctet(
            DeadReckoningAlgorithm.DRM_RVW.discriminantByte()), rvStructCoderFixedRecord);
    }

    public byte[] encode(SpatialRepresentation spatialStruct) {

        byte[] ret = new byte[0];
        if (spatialStruct == null) {
            return ret;
        }
        switch (spatialStruct.getDeadReckoningAlgorithm()) {
            case DRM_FPW:
            case DRM_FPB:
                ret = encode((SpatialFPStruct) spatialStruct);
                break;
            case DRM_RVW:
            case DRM_RVB:
                ret = encode((SpatialRVStruct) spatialStruct);
                break;
            default:
                break;
        }
        return ret;
    }

    public byte[] encode(SpatialFPStruct spatialfpStruct) {
        fpStructCoder.encode(spatialfpStruct);
        _variantCoder.setDiscriminant(encoderFactory.createHLAoctet(spatialfpStruct.getDeadReckoningAlgorithm().discriminantByte()));// FPW
        return _variantCoder.toByteArray();
    }

    public byte[] encode(SpatialRVStruct spatialrvStruct) {
        rvStructCoder.encode(spatialrvStruct);
        _variantCoder.setDiscriminant(encoderFactory.createHLAoctet(spatialrvStruct.getDeadReckoningAlgorithm().discriminantByte()));// RVW
        return _variantCoder.toByteArray();
    }

    public SpatialRepresentation decode(byte[] bytes) throws DecoderException {
        _variantCoder.decode(bytes);
        DeadReckoningAlgorithm deadReckoningAlgorithm = DeadReckoningAlgorithm.valueOf(_variantCoder.getDiscriminant().getValue());

        switch (deadReckoningAlgorithm) {
            case DRM_FPW:
            case DRM_FPB:
                return fpStructCoder.decodeToSpatial(_variantCoder.getValue().toByteArray());
            case DRM_RVW:
            case DRM_RVB:
                return rvStructCoder.decodeToSpatial(_variantCoder.getValue().toByteArray());
            default:
                return null;// Other algo are not implemented
        }
    }
}
