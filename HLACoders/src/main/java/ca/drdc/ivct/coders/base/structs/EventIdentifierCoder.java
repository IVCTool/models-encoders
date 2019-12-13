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

import ca.drdc.ivct.fom.base.structs.EventIdentifierStruct;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAfixedRecord;
import hla.rti1516e.encoding.HLAinteger16BE;

public class EventIdentifierCoder {

    private final HLAfixedRecord recCoder;


    private final HLAinteger16BE eventCountCoder;
    private final RTIObjectIdCoder issuingObjectIdCoder;


    public EventIdentifierCoder(EncoderFactory encoderFactory) {
        recCoder = encoderFactory.createHLAfixedRecord();

        eventCountCoder = encoderFactory.createHLAinteger16BE();
        recCoder.add(eventCountCoder);

        issuingObjectIdCoder = new RTIObjectIdCoder();
        recCoder.add(issuingObjectIdCoder);

    }

    public HLAfixedRecord getHLAFixedRecord() {
        return recCoder;
    }

    public byte[] toByteArray() {
        return recCoder.toByteArray();
    }

    public EventIdentifierStruct decode(byte[] bytes) throws DecoderException {
        recCoder.decode(bytes);

        return new EventIdentifierStruct(Short.toUnsignedInt(eventCountCoder.getValue()), issuingObjectIdCoder.getValue());
    }

    public EventIdentifierStruct getValue() {
        return new EventIdentifierStruct(Short.toUnsignedInt(eventCountCoder.getValue()), issuingObjectIdCoder.getValue());
    }

    public void setValue(EventIdentifierStruct eventIdentifierStruct) {
        if (eventIdentifierStruct == null) {
            throw new IllegalArgumentException();
        }

        eventCountCoder.setValue((short) eventIdentifierStruct.getEventCount());
        issuingObjectIdCoder.setValue(eventIdentifierStruct.getIssuingObjectIdentifier());

    }

}
