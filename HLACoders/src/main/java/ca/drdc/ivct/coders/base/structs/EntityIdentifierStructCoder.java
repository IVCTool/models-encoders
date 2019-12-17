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

import ca.drdc.ivct.fom.base.structs.EntityIdentifierStruct;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAfixedRecord;
import hla.rti1516e.encoding.HLAinteger16BE;

import java.util.StringTokenizer;

public class EntityIdentifierStructCoder {

    private final HLAfixedRecord recCoder;
    private final HLAinteger16BE entNumberCoder;
    private final FederateIdentifierStructCoder federateIdentifierStructCoder;

    public EntityIdentifierStructCoder(EncoderFactory encoderFactory) {
        recCoder = encoderFactory.createHLAfixedRecord();
        federateIdentifierStructCoder = new FederateIdentifierStructCoder(encoderFactory);
        HLAfixedRecord federateStructCoder = federateIdentifierStructCoder.getHLAfixedRecord();
        recCoder.add(federateStructCoder);
        entNumberCoder = encoderFactory.createHLAinteger16BE();
        recCoder.add(entNumberCoder);
    }

    public byte[] encode(String entIdDotStr) {
        String[] entIdFieldStrArr = new String[3];
        if (entIdDotStr == null || entIdDotStr.indexOf('.') < 0) {
            return new byte[0];
        }
        StringTokenizer stk = new StringTokenizer(entIdDotStr, ".");
        if (stk.hasMoreTokens())
            entIdFieldStrArr[0] = stk.nextToken();
        if (stk.hasMoreTokens())
            entIdFieldStrArr[1] = stk.nextToken();
        if (stk.hasMoreTokens())
            entIdFieldStrArr[2] = stk.nextToken();

        federateIdentifierStructCoder.setValues(entIdDotStr);
        entNumberCoder.setValue((short) (Integer.parseInt(entIdFieldStrArr[2])));

        return recCoder.toByteArray();
    }

    public byte[] encode(EntityIdentifierStruct entIdentifier) {
        if (entIdentifier == null) {
            throw new IllegalArgumentException();
        }

        initializeAttributes();

        StringTokenizer stk = new StringTokenizer(entIdentifier.toBinaryString(), ".");
        String[] splitString = new String[3];

        int idx = 0;
        while (stk.hasMoreElements()) {
            splitString[idx] = stk.nextToken();
            idx++;
        }

        federateIdentifierStructCoder.setSiteIdCoderValue(Integer.parseInt(splitString[0]));
        federateIdentifierStructCoder.setAppIdCoderValue(Integer.parseInt(splitString[1]));
        entNumberCoder.setValue(Short.parseShort(splitString[2]));

        return recCoder.toByteArray();
    }

    private void initializeAttributes() {
        byte unusedVal = (byte) 0;
        federateIdentifierStructCoder.initializeAttributes();
        entNumberCoder.setValue(unusedVal);

    }

    public String decodeToIdDotString(byte[] bytes) throws DecoderException {
        recCoder.decode(bytes);

        String resultDotStr = federateIdentifierStructCoder.decodeFederateIdToDotString();
        resultDotStr += ("." + Short.toUnsignedInt(entNumberCoder.getValue()));

        return resultDotStr;
    }

    public EntityIdentifierStruct decodeToType(byte[] bytes) throws DecoderException {
        recCoder.decode(bytes);
        return new EntityIdentifierStruct(
            Short.toUnsignedInt(federateIdentifierStructCoder.getSiteIdCoderValue()),
            Short.toUnsignedInt(federateIdentifierStructCoder.getAppIdCoderValue()),
            Short.toUnsignedInt(entNumberCoder.getValue()));
    }
}
