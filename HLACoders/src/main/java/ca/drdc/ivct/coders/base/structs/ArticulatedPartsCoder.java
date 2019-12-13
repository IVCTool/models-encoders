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


import ca.drdc.ivct.fom.base.structs.ArticulatedPartsStruct;
import hla.rti1516e.encoding.*;

public class ArticulatedPartsCoder {

    private final HLAfixedRecord recCoder;


    private final HLAinteger32BE classTypeEnum32;
    private final HLAinteger32BE typeMetricEnum32;
    private final HLAfloat32BE value;


    public ArticulatedPartsCoder(EncoderFactory encoderFactory) {
        recCoder = encoderFactory.createHLAfixedRecord();

        classTypeEnum32 = encoderFactory.createHLAinteger32BE();
        recCoder.add(classTypeEnum32);

        typeMetricEnum32 = encoderFactory.createHLAinteger32BE();
        recCoder.add(typeMetricEnum32);

        value = encoderFactory.createHLAfloat32BE();
        recCoder.add(value);
    }

    public HLAfixedRecord getHLAFixedRecord() {
        return recCoder;
    }

    public byte[] toByteArray() {
        return recCoder.toByteArray();
    }

    public void decode(byte[] bytes) throws DecoderException {
        recCoder.decode(bytes);
    }

    public ArticulatedPartsStruct getValue() {
        return new ArticulatedPartsStruct(Integer.toUnsignedLong(classTypeEnum32.getValue()), Integer.toUnsignedLong(typeMetricEnum32.getValue()), value.getValue());
    }

    public void setValue(ArticulatedPartsStruct articulatedPartsStruct) {
        if (articulatedPartsStruct == null) {
            throw new IllegalArgumentException();
        }

        classTypeEnum32.setValue((int) articulatedPartsStruct.getArticulatedPartsType());
        typeMetricEnum32.setValue((int) articulatedPartsStruct.getTypeMetric());
        value.setValue(articulatedPartsStruct.getValue());
    }

}
