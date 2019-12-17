package ca.drdc.ivct.fom.base.structs;

/**
 * Entity type of a base entity.
 */
public class EntityTypeStruct {
    /**
     * Kind of entity.
     */
    private short entityKind;
    /**
     * Domain in which the entity operates.
     */
    private short domain;
    /**
     * Country to which the design of the entity is attributed.
     */
    private int countryCode;
    /**
     * Main category that describes the entity.
     */
    private short category;
    /**
     * Subcategory to which an entity belongs based on the Category field.
     */
    private short subcategory;
    /**
     * Specific information about an entity based on the Subcategory field.
     */
    private short specific;
    /**
     * Extra information required to describe a particular entity.
     */
    private short extra;

    /**
     * Constructor from Strings.
     * @param entityKind Entity kind as string.
     * @param domain Domain as string.
     * @param countryCode Country code as string.
     * @param category Category as string.
     * @param subcategory Subcategory as string.
     * @param specific Specific as string.
     * @param extra Extra as string.
     */
    public EntityTypeStruct(
        String entityKind,
        String domain,
        String countryCode,
        String category,
        String subcategory,
        String specific,
        String extra
    ) {
        this.entityKind = Byte.parseByte(entityKind);
        this.domain = Byte.parseByte(domain);
        this.countryCode = Short.parseShort(countryCode);
        this.category = Byte.parseByte(category);
        this.subcategory = Byte.parseByte(subcategory);
        this.specific = Byte.parseByte(specific);
        this.extra = Byte.parseByte(extra);
    }

    /**
     * Constructor from Strings.
     * @param entityKind Entity kind .
     * @param domain Domain as string.
     * @param countryCode Country code
     * @param category Category
     * @param subcategory Subcategory
     * @param specific Specific
     * @param extra Extra
     */
    public EntityTypeStruct(
        short entityKind,
        short domain,
        int countryCode,
        short category,
        short subcategory,
        short specific,
        short extra
    ) {
        super();
        this.entityKind = entityKind;
        this.domain = domain;
        this.countryCode = countryCode;
        this.category = category;
        this.subcategory = subcategory;
        this.specific = specific;
        this.extra = extra;
    }

    public short getEntityKind() {
        return entityKind;
    }

    public void setEntityKind(short entityKind) {
        this.entityKind = entityKind;
    }

    public short getDomain() {
        return domain;
    }

    public void setDomain(short domain) {
        this.domain = domain;
    }

    public int getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(int countryCode) {
        this.countryCode = countryCode;
    }

    public short getCategory() {
        return category;
    }

    public void setCategory(short category) {
        this.category = category;
    }

    public short getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(short subcategory) {
        this.subcategory = subcategory;
    }

    public short getSpecific() {
        return specific;
    }

    public void setSpecific(short specific) {
        this.specific = specific;
    }

    public short getExtra() {
        return extra;
    }

    public void setExtra(short extra) {
        this.extra = extra;
    }

    @Override
    public String toString() {
        return String.format(
            "Entity [Kind=%s Domain=%s CountryCode=%s Category=%s Subcategory=%s Specific=%s Extra=%s]",
            entityKind,
            domain,
            countryCode,
            category,
            subcategory,
            specific,
            extra
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntityTypeStruct that = (EntityTypeStruct) o;

        if (entityKind != that.entityKind) return false;
        if (domain != that.domain) return false;
        if (countryCode != that.countryCode) return false;
        if (category != that.category) return false;
        if (subcategory != that.subcategory) return false;
        if (specific != that.specific) return false;
        return extra == that.extra;
    }

    @Override
    public int hashCode() {
        int result = entityKind;
        result = 31 * result + (int) domain;
        result = 31 * result + countryCode;
        result = 31 * result + (int) category;
        result = 31 * result + (int) subcategory;
        result = 31 * result + (int) specific;
        result = 31 * result + (int) extra;
        return result;
    }
}
