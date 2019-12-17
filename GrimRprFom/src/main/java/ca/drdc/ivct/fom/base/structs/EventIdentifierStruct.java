package ca.drdc.ivct.fom.base.structs;

import java.util.Objects;

/**
 * Struct identifying a singular event.
 */
public class EventIdentifierStruct {
    /**
     * The event number. Uniquely assigned by the simulation application (federate)
     * that initiates the sequence of events. It shall be set to one for each exercise
     * and incremented by one for each event. In the case where all possible values are
     * exhausted, the numbers may be reused beginning again at one.
     */
    private int eventCount;
    /**
     * Identification of the object issuing the event.
     */
    private String issuingObjectIdentifier;

    /**
     * @param eventCount The increasing counter of weapon fires/detonations in the simulation
     * @param issuingObjectIdentifier The ID of the entity who is launching the weapon fire/detonation
     */
    public EventIdentifierStruct(
        String eventCount,
        String issuingObjectIdentifier
    ) {
        this.eventCount = Integer.parseInt(eventCount);
        this.issuingObjectIdentifier = issuingObjectIdentifier;
    }

    /**
     * @param eventCount The increasing counter of weapon fires/detonations in the simulation
     * @param issuingObjectIdentifier The ID of the entity who is launching the weapon fire/detonation
     */
    public EventIdentifierStruct(int eventCount, String issuingObjectIdentifier) {
        this.eventCount = eventCount;
        this.issuingObjectIdentifier = issuingObjectIdentifier;
    }

    public int getEventCount() {
        return eventCount;
    }

    public void setEventCount(int eventCount) {
        this.eventCount = eventCount;
    }

    public String getIssuingObjectIdentifier() {
        return issuingObjectIdentifier;
    }

    public void setIssuingObjectIdentifier(String issuingObjectIdentifier) {
        this.issuingObjectIdentifier = issuingObjectIdentifier;
    }

    @Override
    public String toString() {
        return String.format(
            "Event [Id=%s EventCount=%s]",
            issuingObjectIdentifier,
            eventCount
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventIdentifierStruct that = (EventIdentifierStruct) o;

        if (eventCount != that.eventCount) return false;
        return Objects.equals(issuingObjectIdentifier, that.issuingObjectIdentifier);
    }

    @Override
    public int hashCode() {
        int result = eventCount;
        result = 31 * result + (issuingObjectIdentifier != null ? issuingObjectIdentifier.hashCode() : 0);
        return result;
    }
}
