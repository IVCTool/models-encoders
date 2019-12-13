package ca.drdc.ivct.fom.base.structs;

import ca.drdc.ivct.fom.base.ParameterValue;

import java.util.Objects;

public class ArticulatedParameterStruct {
    /**
     * Indicator of a change to the part. This field shall be set to zero
     * for each exercise and sequentially incremented by one for each change
     * in articulation parameters. In the case where all possible values are
     * exhausted, the numbers shall be reused beginning at zero.
     */
    private byte articulatedParameterChange;
    /**
     * Identification of the articulated part to which this articulation parameter
     * is attached. This field shall contain the value zero if the articulated
     * part is attached directly to the entity.
     */
    private int partAttachedTo;
    /**
     * Details of the parameter: whether it is an articulated or an attached part, and its type and value.
     */
    private ParameterValue parameterValue;

    public ArticulatedParameterStruct() {
    }

    public ArticulatedParameterStruct(byte articulatedParameterChange, int partAttachedTo, ParameterValue parameterValue) {
        this.articulatedParameterChange = articulatedParameterChange;
        this.partAttachedTo = partAttachedTo;
        this.parameterValue = parameterValue;
    }

    /**
     * Constructor from strings.
     * @param articulatedParameterChange Articulated parameter change as string.
     * @param partAttachedTo Part attached to as string.
     * @param parameterValue Parameter value as string.
     */
    public ArticulatedParameterStruct(String articulatedParameterChange, String partAttachedTo, ParameterValue parameterValue) {
        this.articulatedParameterChange = Byte.parseByte(articulatedParameterChange);
        this.partAttachedTo = Integer.parseInt(partAttachedTo);
        this.parameterValue = parameterValue;
    }

    public byte getArticulatedParameterChange() {
        return articulatedParameterChange;
    }

    public void setArticulatedParameterChange(byte articulatedParameterChange) {
        this.articulatedParameterChange = articulatedParameterChange;
    }

    public int getPartAttachedTo() {
        return partAttachedTo;
    }

    public void setPartAttachedTo(int partAttachedTo) {
        this.partAttachedTo = partAttachedTo;
    }

    public ParameterValue getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(ParameterValue parameterValue) {
        this.parameterValue = parameterValue;
    }

    @Override
    public String toString() {
        return String.format(
            "ArticulatedParameter [change=%s attachedTo=%s value=%s]",
            articulatedParameterChange,
            partAttachedTo,
            parameterValue
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArticulatedParameterStruct that = (ArticulatedParameterStruct) o;

        if (articulatedParameterChange != that.articulatedParameterChange) return false;
        if (partAttachedTo != that.partAttachedTo) return false;
        return Objects.equals(parameterValue, that.parameterValue);
    }

    @Override
    public int hashCode() {
        int result = articulatedParameterChange;
        result = 31 * result + partAttachedTo;
        result = 31 * result + (parameterValue != null ? parameterValue.hashCode() : 0);
        return result;
    }
}
