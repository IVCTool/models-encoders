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

import ca.drdc.ivct.fom.base.structs.AccelerationVectorStruct;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAfixedRecord;
import hla.rti1516e.encoding.HLAfloat32BE;

public class AccelerationStructCoder {

    private final HLAfixedRecord _recCoder;

    private final HLAfloat32BE _AxAngularVelCoder;
    private final HLAfloat32BE _AyAngularVelCoder;
    private final HLAfloat32BE _AzAngularVelCoder;

    public AccelerationStructCoder(EncoderFactory encoderFactory) {

        _recCoder = encoderFactory.createHLAfixedRecord();

        _AxAngularVelCoder = encoderFactory.createHLAfloat32BE();
        _AyAngularVelCoder = encoderFactory.createHLAfloat32BE();
        _AzAngularVelCoder = encoderFactory.createHLAfloat32BE();
        _recCoder.add(_AxAngularVelCoder);
        _recCoder.add(_AyAngularVelCoder);
        _recCoder.add(_AzAngularVelCoder);
    }

    public HLAfixedRecord getHLAfixedRecord() {
        return this._recCoder;
    }

    public byte[] encode(AccelerationVectorStruct accelVector) {

        if ((accelVector) == null) {
            return null;
        }

        _AxAngularVelCoder.setValue(accelVector.getxAcceleration());
        _AyAngularVelCoder.setValue(accelVector.getyAcceleration());
        _AzAngularVelCoder.setValue(accelVector.getzAcceleration());
        return _recCoder.toByteArray();
    }

    /**
     * It is necessary for the bytes to be set beforehand through the spatialFPCoder/spatialRVCoder decode function.
     *
     * @return AccelerationVectorStruct
     */
    public AccelerationVectorStruct getAccelerationStructValue() {
        return new AccelerationVectorStruct(_AxAngularVelCoder.getValue(), _AyAngularVelCoder.getValue(), _AzAngularVelCoder.getValue());
    }

    public AccelerationVectorStruct decodeToAccelerationStruct(byte[] accelerationBytes) throws DecoderException {
        _recCoder.decode(accelerationBytes);
        return new AccelerationVectorStruct(_AxAngularVelCoder.getValue(), _AyAngularVelCoder.getValue(), _AzAngularVelCoder.getValue());
    }
}
