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

import ca.drdc.ivct.fom.base.structs.SpatialRVStruct;
import ca.drdc.ivct.fom.der.DeadReckoningAlgorithm;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAboolean;
import hla.rti1516e.encoding.HLAfixedRecord;

public class SpatialRVStructCoder {

    private final HLAfixedRecord _recCoder;

    private final WorldLocationStructCoder _locationCoder;
    private final VelocityStructCoder _velocityCoder;
    private final HLAboolean _isFrozenCoder;// Could Be A HLAoctet possibly
    private final OrientationStructCoder _orientationCoder;
    private final AccelerationStructCoder _accelCoder;
    private final AngularVelocityStructCoder _angVelocityCoder;

    public SpatialRVStructCoder(EncoderFactory encoderFactory) {
        _recCoder = encoderFactory.createHLAfixedRecord();

        _locationCoder = new WorldLocationStructCoder(encoderFactory);
        _isFrozenCoder = encoderFactory.createHLAboolean();
        _orientationCoder = new OrientationStructCoder(encoderFactory);
        _velocityCoder = new VelocityStructCoder(encoderFactory);
        _accelCoder = new AccelerationStructCoder(encoderFactory);
        _angVelocityCoder = new AngularVelocityStructCoder(encoderFactory);

        HLAfixedRecord locationCoder = _locationCoder.getHLAfixedRecord();
        HLAfixedRecord velocityCoder = _velocityCoder.getHLAfixedRecord();
        HLAfixedRecord orientationCoder = _orientationCoder.getHLAfixedRecord();
        HLAfixedRecord accelCoder = _accelCoder.getHLAfixedRecord();
        HLAfixedRecord angVelocityCoder = _angVelocityCoder.getHLAfixedRecord();

        _recCoder.add(locationCoder);
        _recCoder.add(_isFrozenCoder);
        _recCoder.add(orientationCoder);
        _recCoder.add(velocityCoder);
        _recCoder.add(accelCoder);
        _recCoder.add(angVelocityCoder);
    }

    public HLAfixedRecord getFixedRecord() {
        return _recCoder;
    }

    public byte[] encode(SpatialRVStruct spatialRVStruct) {

        _locationCoder.setValue(spatialRVStruct.getWorldLocation());
        _isFrozenCoder.setValue(spatialRVStruct.isFrozen());
        _orientationCoder.encode(spatialRVStruct.getOrientation());
        _velocityCoder.setValue(spatialRVStruct.getVelocityVector());
        _accelCoder.encode(spatialRVStruct.getAccelerationVector());
        _angVelocityCoder.encode(spatialRVStruct.getAngularVelocityVector());

        return _recCoder.toByteArray();
    }

    public SpatialRVStruct decodeToSpatial(byte[] bytes) throws DecoderException {
        _recCoder.decode(bytes);

        return new SpatialRVStruct(DeadReckoningAlgorithm.DRM_RVW,
            _locationCoder.getWorldLocationStructValue(),
            _isFrozenCoder.getValue(),
            _orientationCoder.getOrientationStructValue(),
            _velocityCoder.getVelocityVectorStruct(),
            _accelCoder.getAccelerationStructValue(),
            _angVelocityCoder.getAngularVelocityVectorStructValue());
    }

}
