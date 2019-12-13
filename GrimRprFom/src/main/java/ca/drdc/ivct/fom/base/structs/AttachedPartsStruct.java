package ca.drdc.ivct.fom.base.structs;

import ca.drdc.ivct.fom.base.ParameterValue;

import java.util.Objects;

/**
 * Structure to represent removable parts that may be attached to an entity.
 */
public class AttachedPartsStruct extends ParameterValue {
    /**
     * Identification of the location (or station) to which the part is attached.
     */
    private long station;
    /**
     * The entity type of the attached part.
     */
    private EntityTypeStruct storeType;

    public AttachedPartsStruct(long station, EntityTypeStruct storeType) {
        this.station = station;
        this.storeType = storeType;
    }

    /**
     * Constructor from strings.
     * @param station Station as string.
     * @param storeType Store type as string.
     */
    public AttachedPartsStruct(String station, EntityTypeStruct storeType) {
        this.station = Long.parseLong(station);
        this.storeType = storeType;
    }

    public AttachedPartsStruct() {
    }

    public long getStation() {
        return station;
    }

    public void setStation(long station) {
        this.station = station;
    }

    public EntityTypeStruct getStoreType() {
        return storeType;
    }

    public void setStoreType(EntityTypeStruct storeType) {
        this.storeType = storeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        AttachedPartsStruct that = (AttachedPartsStruct) o;

        if (station != that.station) return false;
        return Objects.equals(storeType, that.storeType);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (station ^ (station >>> 32));
        result = 31 * result + (storeType != null ? storeType.hashCode() : 0);
        return result;
    }
}
