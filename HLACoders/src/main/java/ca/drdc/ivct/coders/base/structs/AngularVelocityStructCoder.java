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

import ca.drdc.ivct.fom.base.structs.AngularVelocityVectorStruct;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAfixedRecord;
import hla.rti1516e.encoding.HLAfloat32BE;

public class AngularVelocityStructCoder {

    private final HLAfixedRecord _recCoder;

    private final HLAfloat32BE _XAngularVelCoder;
    private final HLAfloat32BE _YAngularVelCoder;
    private final HLAfloat32BE _ZAngularVelCoder;

    public AngularVelocityStructCoder(EncoderFactory encoderFactory) {

        _recCoder = encoderFactory.createHLAfixedRecord();

        _XAngularVelCoder = encoderFactory.createHLAfloat32BE();
        _YAngularVelCoder = encoderFactory.createHLAfloat32BE();
        _ZAngularVelCoder = encoderFactory.createHLAfloat32BE();
        _recCoder.add(_XAngularVelCoder);
        _recCoder.add(_YAngularVelCoder);
        _recCoder.add(_ZAngularVelCoder);
    }

    public HLAfixedRecord getHLAfixedRecord() {
        return this._recCoder;
    }

    public byte[] encode(AngularVelocityVectorStruct angularVel) {

        if ((angularVel) == null) {
            return null;
        }

        _XAngularVelCoder.setValue(angularVel.getxAngularVelocity());
        _YAngularVelCoder.setValue(angularVel.getyAngularVelocity());
        _ZAngularVelCoder.setValue(angularVel.getzAngularVelocity());
        return _recCoder.toByteArray();
    }

    /**
     * It is necessary for the bytes to be set beforehand through the spatialFPCoder/spatialRVCoder decode function.
     *
     * @return AngularVelocityVectorStruct
     */
    public AngularVelocityVectorStruct getAngularVelocityVectorStructValue() {
        return new AngularVelocityVectorStruct(_XAngularVelCoder.getValue(), _YAngularVelCoder.getValue(),
            _ZAngularVelCoder.getValue());
    }

    public AngularVelocityVectorStruct decodeToAngularVelocityStruct(byte[] angularVelocitybytes) throws DecoderException {
        _recCoder.decode(angularVelocitybytes);
        return new AngularVelocityVectorStruct(_XAngularVelCoder.getValue(), _YAngularVelCoder.getValue(), _ZAngularVelCoder.getValue());
    }
}
