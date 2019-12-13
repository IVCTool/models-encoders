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


import ca.drdc.ivct.fom.base.structs.AttachedPartsStruct;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAfixedRecord;
import hla.rti1516e.encoding.HLAinteger32BE;

public class AttachedPartsCoder {

    private final HLAfixedRecord recCoder;


    private final HLAinteger32BE stationEnum;
    private final EntityTypeStructCoder storeTypeCoder;

    public AttachedPartsCoder(EncoderFactory encoderFactory) {
        recCoder = encoderFactory.createHLAfixedRecord();

        stationEnum = encoderFactory.createHLAinteger32BE();
        recCoder.add(stationEnum);

        storeTypeCoder = new EntityTypeStructCoder(encoderFactory);
        recCoder.add(storeTypeCoder.getHLAfixedRecord());
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

    public AttachedPartsStruct getValue() {
        return new AttachedPartsStruct(Integer.toUnsignedLong(stationEnum.getValue()), storeTypeCoder.getEntityType());
    }

    public void setValue(AttachedPartsStruct attachedPartsStruct) {
        if (attachedPartsStruct == null) {
            throw new IllegalArgumentException();
        }

        stationEnum.setValue((int) attachedPartsStruct.getStation());
        storeTypeCoder.setValues(attachedPartsStruct.getStoreType());
    }

}
