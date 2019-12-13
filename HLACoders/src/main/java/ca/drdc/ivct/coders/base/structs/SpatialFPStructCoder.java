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

import ca.drdc.ivct.fom.base.structs.SpatialFPStruct;
import ca.drdc.ivct.fom.der.DeadReckoningAlgorithm;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAboolean;
import hla.rti1516e.encoding.HLAfixedRecord;

public class SpatialFPStructCoder {

    private final HLAfixedRecord _recCoder;

    private final WorldLocationStructCoder _locationCoder;
    private final HLAboolean _isFrozenCoder;
    private final OrientationStructCoder _orientationCoder;
    private final VelocityStructCoder _velocityCoder;

    public SpatialFPStructCoder(EncoderFactory encoderFactory) {
        _recCoder = encoderFactory.createHLAfixedRecord();

        _locationCoder = new WorldLocationStructCoder(encoderFactory);
        _isFrozenCoder = encoderFactory.createHLAboolean();
        _orientationCoder = new OrientationStructCoder(encoderFactory);
        _velocityCoder = new VelocityStructCoder(encoderFactory);

        HLAfixedRecord locationCoder = _locationCoder.getHLAfixedRecord();
        HLAfixedRecord orientationCoder = _orientationCoder.getHLAfixedRecord();
        HLAfixedRecord velocityCoder = _velocityCoder.getHLAfixedRecord();

        _recCoder.add(locationCoder);
        _recCoder.add(_isFrozenCoder);
        _recCoder.add(orientationCoder);
        _recCoder.add(velocityCoder);
    }

    public HLAfixedRecord getFixedRecord() {
        return _recCoder;
    }

    public byte[] encode(SpatialFPStruct spatialFPStruct) {
        _locationCoder.setValue(spatialFPStruct.getWorldLocation());
        _isFrozenCoder.setValue(spatialFPStruct.isFrozen());
        _orientationCoder.encode(spatialFPStruct.getOrientation());
        _velocityCoder.setValue(spatialFPStruct.getVelocityVector());

        return _recCoder.toByteArray();
    }

    public SpatialFPStruct decodeToSpatial(byte[] bytes) throws DecoderException {
        _recCoder.decode(bytes);

        return new SpatialFPStruct(DeadReckoningAlgorithm.DRM_FPW,
            _locationCoder.getWorldLocationStructValue(),
            _isFrozenCoder.getValue(),
            _orientationCoder.getOrientationStructValue(),
            _velocityCoder.getVelocityVectorStruct());
    }

}
