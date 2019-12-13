/**
 * Copyright 2017, UK (QinetiQ)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ca.drdc.ivct.coders.base.structs;

import hla.rti1516e.encoding.ByteWrapper;
import hla.rti1516e.encoding.DataElement;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.EncoderException;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Decoder for the RTIobjectId type.
 *
 * @author QinetiQ
 */
public class RTIObjectIdCoder implements DataElement {
    /**
     * The string value
     */
    private String value = "";

    /**
     * Default constructor.
     */
    public RTIObjectIdCoder() {

    }

    /**
     * @return The decoded value as a string
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the string value
     *
     * @param value The value to set
     */
    public void setValue(final String value) {
        // Do not allow a null value
        this.value = value == null ? "" : value;
    }

    @Override
    public int getOctetBoundary() {
        return value.length();
    }

    @Override
    public int getEncodedLength() {
        return toByteArray().length;
    }

    @Override
    public void encode(final ByteWrapper byteWrapper) throws EncoderException {
        byte[] bytes = toByteArray();

        if (byteWrapper.remaining() < bytes.length) {
            throw new EncoderException("RTIobjectIdDecoder: insufficient space in ByteWrapper");
        }

        byteWrapper.put(bytes);
    }

    @Override
    public byte[] toByteArray() throws EncoderException {
        byte[] bytes = value.getBytes(StandardCharsets.US_ASCII);

        // Add the null terminator if not already present
        if (bytes.length == 0 || bytes[bytes.length - 1] != 0) {
            bytes = Arrays.copyOf(bytes, bytes.length + 1);
        }

        return bytes;
    }

    @Override
    public void decode(final ByteWrapper byteWrapper) throws DecoderException {
        try {
            List<Byte> bytesList = new ArrayList<>(byteWrapper.remaining());

            // Scan the byte wrapper until either the end of buffer or a null is reached
            SCAN_LOOP:
            while (byteWrapper.remaining() > 0) {
                int next = byteWrapper.get();

                if (next == 0) {
                    break SCAN_LOOP;
                }

                bytesList.add((byte) next);
            }

            byte[] bytes = new byte[bytesList.size()];

            for (int index = 0; index < bytes.length; index++) {
                bytes[index] = bytesList.get(index);
            }

            decode(bytes);
        } catch (Exception e) {
            throw new DecoderException(e.getMessage(), e);
        }
    }

    /**
     * Decode the provided data to produce a RTIobjectId object.
     */
    @Override
    public void decode(final byte[] bytes) {
        // Truncate before any trailing null characters
        value = new String(bytes, StandardCharsets.US_ASCII);
        if (value.contains("\0")) {
            value = value.substring(0, value.indexOf("\0"));
        }
    }

    @Override
    public String toString() {
        return String.join("", "RTIobjectIdDecoder<\"", value, "\">");
    }

}