package ca.drdc.ivct.coders.base.structs;

import ca.drdc.ivct.fom.base.structs.EntityTypeStruct;
import hla.rti1516e.encoding.*;

public class EntityTypeStructCoder {
    private HLAfixedRecord fixdRec;

    private HLAoctet entityKind;
    private HLAoctet domain;
    private HLAinteger16BE countryCode;
    private HLAoctet category;
    private HLAoctet subCategory;
    private HLAoctet specific;
    private HLAoctet extra;

    public EntityTypeStructCoder(EncoderFactory encoderFactory) {
        entityKind = encoderFactory.createHLAoctet();
        domain = encoderFactory.createHLAoctet();
        countryCode = encoderFactory.createHLAinteger16BE();
        category = encoderFactory.createHLAoctet();
        subCategory = encoderFactory.createHLAoctet();
        specific = encoderFactory.createHLAoctet();
        extra = encoderFactory.createHLAoctet();

        fixdRec = encoderFactory.createHLAfixedRecord();
        fixdRec.add(entityKind);
        fixdRec.add(domain);
        fixdRec.add(countryCode);
        fixdRec.add(category);
        fixdRec.add(subCategory);
        fixdRec.add(specific);
        fixdRec.add(extra);
    }

    private void initializeAttributes() {
        Byte unusedVal = (byte) 0;

        entityKind.setValue(unusedVal);
        domain.setValue(unusedVal);
        countryCode.setValue((short) 0);
        category.setValue(unusedVal);
        subCategory.setValue(unusedVal);
        specific.setValue(unusedVal);
        extra.setValue(unusedVal);
    }

    /**
     * Sets Byte value from a String on an HLAoctet object. The only reason this
     * is used is to handle passed null values by not throwing an error and not
     * replacing the previous value.
     *
     * @param de    The attribute (which extends HLAoctet) to set the value on.
     * @param value The value to set.
     */
    private <T extends HLAoctet> void setByte(T de, String value) {
        if (value == null || value.isEmpty())
            return;

        de.setValue(Byte.parseByte(value));
    }

    /**
     * Sets Short value String on an HLAinteger16BE object. The only reason this
     * is used is to handle passed null values by not throwing an error
     *
     * @param s     The attribute (which extends HLAinteger16BE) to set the value
     *              on.
     * @param value The value to set.
     */
    private <T extends HLAinteger16BE> void setShort(T s, String value) {
        if (value == null || value.isEmpty()) {
            return;
        }

        s.setValue(Short.parseShort(value));
    }

    public byte[] encode(EntityTypeStruct entType) {
        if (entType == null) {
            throw new IllegalArgumentException();
        }

        initializeAttributes();

        setByte(entityKind, String.valueOf(entType.getEntityKind()));
        setByte(domain, String.valueOf(entType.getDomain()));
        setShort(countryCode, String.valueOf(entType.getCountryCode()));
        setByte(category, String.valueOf(entType.getCategory()));
        setByte(subCategory, String.valueOf(entType.getSubcategory()));
        setByte(specific, String.valueOf(entType.getSpecific()));
        setByte(extra, String.valueOf(entType.getExtra()));

        return fixdRec.toByteArray();
    }

    public byte[] toByteArray() {
        return fixdRec.toByteArray();
    }

    public EntityTypeStruct getEntityType() {
        return new EntityTypeStruct(entityKind.getValue(), domain.getValue(), Short.toUnsignedInt(countryCode.getValue()), category.getValue(), subCategory.getValue(), specific.getValue(), extra.getValue());
    }

    public EntityTypeStruct decodeToType(byte[] bytes) throws DecoderException {
        fixdRec.decode(bytes);
        return new EntityTypeStruct(entityKind.getValue(), domain.getValue(), Short.toUnsignedInt(countryCode.getValue()), category.getValue(), subCategory.getValue(), specific.getValue(), extra.getValue());
    }


    public void setValues(EntityTypeStruct entType) {
        if (entType == null) {
            throw new IllegalArgumentException();
        }

        entityKind.setValue((byte) entType.getEntityKind());
        domain.setValue((byte) entType.getDomain());
        countryCode.setValue((short) entType.getCountryCode());
        category.setValue((byte) entType.getCategory());
        subCategory.setValue((byte) entType.getSubcategory());
        specific.setValue((byte) entType.getSpecific());
        extra.setValue((byte) entType.getExtra());
    }

    public HLAfixedRecord getHLAfixedRecord() {
        return this.fixdRec;
    }
}
