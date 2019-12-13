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

import ca.drdc.ivct.fom.base.structs.OrientationStruct;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAfixedRecord;
import hla.rti1516e.encoding.HLAfloat32BE;

public class OrientationStructCoder {

    private final HLAfixedRecord _recCoder;

    private final HLAfloat32BE _PsiCoder;
    private final HLAfloat32BE _ThetaCoder;
    private final HLAfloat32BE _PhiCoder;

    public OrientationStructCoder(EncoderFactory encoderFactory) {

        _recCoder = encoderFactory.createHLAfixedRecord();

        _PsiCoder = encoderFactory.createHLAfloat32BE();
        _ThetaCoder = encoderFactory.createHLAfloat32BE();
        _PhiCoder = encoderFactory.createHLAfloat32BE();

        _recCoder.add(_PsiCoder);
        _recCoder.add(_ThetaCoder);
        _recCoder.add(_PhiCoder);

    }

    public HLAfixedRecord getHLAfixedRecord() {
        return this._recCoder;
    }

    public byte[] encode(OrientationStruct orientation) {

        if ((orientation) == null) {
            return new byte[0];
        }

        _PsiCoder.setValue(orientation.getPsi());
        _ThetaCoder.setValue(orientation.getTheta());
        _PhiCoder.setValue(orientation.getPhi());
        return _recCoder.toByteArray();
    }

    /**
     * It is necessary for the bytes to be set beforehand through the
     * spatialFPCoder/spatialRVCoder decode function.
     *
     * @return byte[]
     */
    public OrientationStruct getOrientationStructValue() {
        return new OrientationStruct(_PsiCoder.getValue(), _ThetaCoder.getValue(), _PhiCoder.getValue());
    }

    public OrientationStruct decodeToOrientationStruct(byte[] orientationBytes) throws DecoderException {
        _recCoder.decode(orientationBytes);
        return new OrientationStruct(_PsiCoder.getValue(), _ThetaCoder.getValue(), _PhiCoder.getValue());
    }
}
