package ca.drdc.ivct.coders.warfare;

import ca.drdc.ivct.coders.base.structs.*;
import ca.drdc.ivct.fom.base.structs.EntityTypeStruct;
import ca.drdc.ivct.fom.base.structs.EventIdentifierStruct;
import ca.drdc.ivct.fom.base.structs.VelocityVectorStruct;
import ca.drdc.ivct.fom.base.structs.WorldLocationStruct;
import ca.drdc.ivct.fom.warfare.WeaponFire;
import hla.rti1516e.encoding.*;

public class WeaponFireCoder {

    private final HLAfixedRecord recCoder;
    private final EventIdentifierCoder eventIdCoder;
    private final HLAfloat32BE fireControlSolutionRange;
    private final HLAinteger32BE fireMissionIndex;
    private final WorldLocationStructCoder firingLocStructCoder;
    private final RTIObjectIdCoder firingObjIdCoder;
    private final HLAinteger16BE fuseTypeEnum16;
    private final VelocityStructCoder velVectorStructCoder;
    private final RTIObjectIdCoder munitionObjIdCoder;
    private final EntityTypeStructCoder munitionTypeStructCoder;
    private final HLAinteger16BE quantityFired;
    private final HLAinteger16BE rateOfFire;
    private final RTIObjectIdCoder targetObjIdCoder;
    private final HLAinteger16BE warheadTypeEnum16;
    EncoderFactory encoderFactory;

    public WeaponFireCoder(EncoderFactory encoderFactory) {
        this.encoderFactory = encoderFactory;
        recCoder = encoderFactory.createHLAfixedRecord();


        eventIdCoder = new EventIdentifierCoder(encoderFactory);
        recCoder.add(eventIdCoder.getHLAFixedRecord());

        fireControlSolutionRange = encoderFactory.createHLAfloat32BE();
        recCoder.add(fireControlSolutionRange);

        fireMissionIndex = encoderFactory.createHLAinteger32BE();
        recCoder.add(fireMissionIndex);

        firingLocStructCoder = new WorldLocationStructCoder(encoderFactory);
        recCoder.add(firingLocStructCoder.getHLAfixedRecord());

        firingObjIdCoder = new RTIObjectIdCoder();
        recCoder.add(firingObjIdCoder);

        fuseTypeEnum16 = encoderFactory.createHLAinteger16BE();
        recCoder.add(fuseTypeEnum16);

        velVectorStructCoder = new VelocityStructCoder(encoderFactory);
        recCoder.add(velVectorStructCoder.getHLAfixedRecord());

        munitionObjIdCoder = new RTIObjectIdCoder();
        recCoder.add(munitionObjIdCoder);

        munitionTypeStructCoder = new EntityTypeStructCoder(encoderFactory);
        recCoder.add(munitionTypeStructCoder.getHLAfixedRecord());

        quantityFired = encoderFactory.createHLAinteger16BE();
        recCoder.add(quantityFired);

        rateOfFire = encoderFactory.createHLAinteger16BE();
        recCoder.add(rateOfFire);

        targetObjIdCoder = new RTIObjectIdCoder();
        recCoder.add(targetObjIdCoder);

        warheadTypeEnum16 = encoderFactory.createHLAinteger16BE();
        recCoder.add(warheadTypeEnum16);

        initDefaults();
    }

    public EventIdentifierCoder getEventIdCoder() {
        return eventIdCoder;
    }

    public HLAfloat32BE getFireControlSolutionRange() {
        return fireControlSolutionRange;
    }

    public HLAinteger32BE getFireMissionIndex() {
        return fireMissionIndex;
    }

    public WorldLocationStructCoder getFiringLocStructCoder() {
        return firingLocStructCoder;
    }

    public RTIObjectIdCoder getFiringObjIdCoder() {
        return firingObjIdCoder;
    }

    public HLAinteger16BE getFuseTypeEnum16() {
        return fuseTypeEnum16;
    }

    public VelocityStructCoder getVelVectorStructCoder() {
        return velVectorStructCoder;
    }

    public RTIObjectIdCoder getMunitionObjIdCoder() {
        return munitionObjIdCoder;
    }

    public EntityTypeStructCoder getMunitionTypeStructCoder() {
        return munitionTypeStructCoder;
    }

    public HLAinteger16BE getQuantityFired() {
        return quantityFired;
    }

    public HLAinteger16BE getRateOfFire() {
        return rateOfFire;
    }

    public RTIObjectIdCoder getTargetObjIdCoder() {
        return targetObjIdCoder;
    }

    public HLAinteger16BE getWarheadTypeEnum16() {
        return warheadTypeEnum16;
    }

    private void initDefaults() {
        fireControlSolutionRange.setValue((float) 0.0);
        fireMissionIndex.setValue(0);
        quantityFired.setValue((short) 1);
        rateOfFire.setValue((short) 0);
    }

    public void decode(byte[] bytes) throws DecoderException {
        recCoder.decode(bytes);
    }

    public EventIdentifierStruct decodeEventIdentifier(byte[] bytes) throws DecoderException {
        return eventIdCoder.decode(bytes);
    }

    public float decodeFireControlSolRange(byte[] bytes) throws DecoderException {
        fireControlSolutionRange.decode(bytes);
        return fireControlSolutionRange.getValue();
    }


    public long decodeFireMissionIndex(byte[] bytes) throws DecoderException {
        fireMissionIndex.decode(bytes);
        return Integer.toUnsignedLong(fireMissionIndex.getValue());
    }

    public WorldLocationStruct decodeFiringLocation(byte[] bytes) throws DecoderException {
        return firingLocStructCoder.decodeToWorldLocStruct(bytes);
    }

    public String decodeFiringObjectId(byte[] bytes) {
        firingObjIdCoder.decode(bytes);
        return firingObjIdCoder.getValue();
    }

    public int decodeFuseType(byte[] bytes) throws DecoderException {
        fuseTypeEnum16.decode(bytes);
        return Short.toUnsignedInt(fuseTypeEnum16.getValue());
    }

    public VelocityVectorStruct decodeInitialVelocity(byte[] bytes) throws DecoderException {
        return velVectorStructCoder.decodeToVelocityVectorStruct(bytes);
    }

    public String decodeMunitionObjectIdentifier(byte[] bytes) {
        munitionObjIdCoder.decode(bytes);
        return munitionObjIdCoder.getValue();
    }

    public EntityTypeStruct decodeMunitionType(byte[] bytes) throws DecoderException {
        return munitionTypeStructCoder.decodeToType(bytes);
    }

    public int decodeQuantityFired(byte[] bytes) throws DecoderException {
        quantityFired.decode(bytes);
        return Short.toUnsignedInt(quantityFired.getValue());
    }

    public int decodeRateOfFire(byte[] bytes) throws DecoderException {
        rateOfFire.decode(bytes);
        return Short.toUnsignedInt(rateOfFire.getValue());
    }

    public String decodeTargetObjectIdentifier(byte[] bytes) {
        targetObjIdCoder.decode(bytes);
        return targetObjIdCoder.getValue();
    }

    public int decodeWarheadType(byte[] bytes) throws DecoderException {
        warheadTypeEnum16.decode(bytes);
        return Short.toUnsignedInt(warheadTypeEnum16.getValue());
    }

    public void setValues(WeaponFire weaponFire) {
        if (weaponFire == null) {
            throw new IllegalArgumentException();
        }

        eventIdCoder.setValue(weaponFire.getEventIdentifier());
        fireControlSolutionRange.setValue(weaponFire.getFireControlSolutionRange());
        fireMissionIndex.setValue((int) weaponFire.getFireMissionIndex());
        firingLocStructCoder.setValue(weaponFire.getFiringLocation());
        firingObjIdCoder.setValue(weaponFire.getFiringObjectIdentifier());
        fuseTypeEnum16.setValue((short) weaponFire.getFuseType());
        velVectorStructCoder.setValue(weaponFire.getInitialVelocityVector());
        munitionObjIdCoder.setValue(weaponFire.getMunitionObjectIdentifier());
        munitionTypeStructCoder.setValues(weaponFire.getMunitionType());
        quantityFired.setValue((short) weaponFire.getQuantityFired());
        rateOfFire.setValue((short) weaponFire.getRateOfFire());
        targetObjIdCoder.setValue(weaponFire.getTargetObjectIdentifier());
        warheadTypeEnum16.setValue((short) weaponFire.getWarheadType());


        recCoder.toByteArray();
    }


}
