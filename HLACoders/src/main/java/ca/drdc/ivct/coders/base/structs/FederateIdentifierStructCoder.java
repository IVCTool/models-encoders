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

import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAfixedRecord;
import hla.rti1516e.encoding.HLAinteger16BE;

import java.util.StringTokenizer;

public class FederateIdentifierStructCoder {

    private final HLAfixedRecord recCoder;

    private final HLAinteger16BE siteIdCoder;
    private final HLAinteger16BE appIdCoder;

    public FederateIdentifierStructCoder(EncoderFactory encoderFactory) {
        recCoder = encoderFactory.createHLAfixedRecord();

        siteIdCoder = encoderFactory.createHLAinteger16BE();
        recCoder.add(siteIdCoder);
        appIdCoder = encoderFactory.createHLAinteger16BE();
        recCoder.add(appIdCoder);
    }

    public String decodeFederateIdToDotString() {
        return Short.toUnsignedInt(siteIdCoder.getValue())
            + ("." + Short.toUnsignedInt(appIdCoder.getValue()));
    }

    public HLAfixedRecord getHLAfixedRecord() {
        return this.recCoder;
    }

    byte[] encode(String federateIdDotStr) {
        setValues(federateIdDotStr);
        return recCoder.toByteArray();
    }


    public short getSiteIdCoderValue() {
        return siteIdCoder.getValue();
    }

    public void setSiteIdCoderValue(int siteId) {
        siteIdCoder.setValue((short) siteId);
    }

    public short getAppIdCoderValue() {
        return appIdCoder.getValue();
    }

    public void setAppIdCoderValue(int appID) {
        appIdCoder.setValue((short) appID);
    }


    public void setValues(String federateIdDotStr) {
        String[] federateIdFieldStrArr = new String[3];
        if (federateIdDotStr == null || federateIdDotStr.indexOf('.') < 0) {
            return;
        }

        StringTokenizer stk = new StringTokenizer(federateIdDotStr, ".");
        if (stk.hasMoreTokens())
            federateIdFieldStrArr[0] = stk.nextToken();
        if (stk.hasMoreTokens())
            federateIdFieldStrArr[1] = stk.nextToken();

        siteIdCoder.setValue((short) (Integer.parseInt(federateIdFieldStrArr[0])));
        appIdCoder.setValue((short) (Integer.parseInt(federateIdFieldStrArr[1])));
    }

    public void initializeAttributes() {
        Byte unusedVal = (byte) 0;
        siteIdCoder.setValue(unusedVal);
        appIdCoder.setValue(unusedVal);
    }
}
