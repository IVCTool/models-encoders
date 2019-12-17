package ca.drdc.ivct.fom.base;

/**
 * Variant record specifying the type of articulation parameter (articulated or attached part), and its type and value.
 */
public abstract class ParameterValue {
    /**
     * Discriminator for the variant record.
     */
    private long articulatedParameterType;

    public long getArticulatedParameterType() {
        return articulatedParameterType;
    }

    public void setArticulatedParameterType(long articulatedParameterType) {
        this.articulatedParameterType = articulatedParameterType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParameterValue that = (ParameterValue) o;

        return articulatedParameterType == that.articulatedParameterType;
    }

    @Override
    public int hashCode() {
        return (int) (articulatedParameterType ^ (articulatedParameterType >>> 32));
    }
}
