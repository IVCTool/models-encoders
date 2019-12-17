package ca.drdc.ivct.fom.base.structs;

import ca.drdc.ivct.fom.base.ParameterValue;

/**
 * Structure to represent the state of a movable part of an entity.
 */
public class ArticulatedPartsStruct extends ParameterValue {
    /**
     * The type class uniquely identifies a particular articulated
     * part on a given entity type. Guidance for uniquely assigning
     * type classes to an entity's articulated parts is given in
     * section 4.8 of the enumeration document (SISO-REF-010).
     */
    private long articulatedPartsType;
    /**
     * The type metric uniquely identifies the transformation to be applied to the articulated part.
     */
    private long typeMetric;
    /**
     * Value of the transformation to be applied to the articulated part.
     */
    private float value;

    public ArticulatedPartsStruct(long articulatedPartsType, long typeMetric, float value) {
        this.articulatedPartsType = articulatedPartsType;
        this.typeMetric = typeMetric;
        this.value = value;
    }

    /**
     * Constructor from strings.
     * @param articulatedPartsType Articulated type as string.
     * @param typeMetric Type metric as string.
     * @param value Value as string.
     */
    public ArticulatedPartsStruct(String articulatedPartsType, String typeMetric, String value) {
        this.articulatedPartsType = Long.parseLong(articulatedPartsType);
        this.typeMetric = Long.parseLong(typeMetric);
        this.value = Float.parseFloat(value);
    }

    public ArticulatedPartsStruct() {
    }

    public long getArticulatedPartsType() {
        return articulatedPartsType;
    }

    public void setArticulatedPartsType(long articulatedPartsType) {
        this.articulatedPartsType = articulatedPartsType;
    }

    public long getTypeMetric() {
        return typeMetric;
    }

    public void setTypeMetric(long typeMetric) {
        this.typeMetric = typeMetric;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format(
            "ArticulatedPart [type=%s typeMetric=%s value=%s]",
            articulatedPartsType,
            typeMetric,
            value
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ArticulatedPartsStruct that = (ArticulatedPartsStruct) o;

        if (articulatedPartsType != that.articulatedPartsType) return false;
        if (typeMetric != that.typeMetric) return false;
        return Float.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (articulatedPartsType ^ (articulatedPartsType >>> 32));
        result = 31 * result + (int) (typeMetric ^ (typeMetric >>> 32));
        result = 31 * result + (value != +0.0f ? Float.floatToIntBits(value) : 0);
        return result;
    }
}
