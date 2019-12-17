package ca.drdc.ivct.coders.warfare;

import ca.drdc.ivct.coders.base.structs.*;
import ca.drdc.ivct.fom.base.structs.*;
import ca.drdc.ivct.fom.warfare.MunitionDetonation;
import hla.rti1516e.encoding.*;

import java.util.ArrayList;
import java.util.List;


public class MunitionDetonationCoder {

    private final HLAfixedRecord recCoder;
    private final HLAvariableArray<HLAfixedRecord> articulatedParameterStructArrayCoder;
    private final WorldLocationStructCoder detonationLocation;
    private final HLAoctet detonationResultCodeEnum8;
    private final EventIdentifierCoder eventIdCoder;
    private final RTIObjectIdCoder firingObjIdCoder;
    private final VelocityStructCoder velVectorStructCoder;
    private final HLAinteger16BE fuseTypeEnum16;
    private final RTIObjectIdCoder munitionObjIdCoder;
    private final EntityTypeStructCoder munitionTypeStructCoder;
    private final HLAinteger16BE quantityFired;
    private final HLAinteger16BE rateOfFire;
    private final RelativePositionStructCoder relativePositionStructCoder;
    private final RTIObjectIdCoder targetObjIdCoder;
    private final HLAinteger16BE warheadTypeEnum16;
    EncoderFactory encoderFactory;


    public MunitionDetonationCoder(EncoderFactory encoderFactory) {
        this.encoderFactory = encoderFactory;
        recCoder = encoderFactory.createHLAfixedRecord();

        DataElementFactory<HLAfixedRecord> fixedRecordFactory = new DataElementFactory<HLAfixedRecord>() {
            @Override
            public HLAfixedRecord createElement(int i) {
                ArticulatedParameterCoder arrayElementRec = new ArticulatedParameterCoder(encoderFactory);
                return arrayElementRec.getHLAFixedRecord();
            }
        };

        articulatedParameterStructArrayCoder = encoderFactory.createHLAvariableArray(fixedRecordFactory);
        recCoder.add(articulatedParameterStructArrayCoder);

        detonationLocation = new WorldLocationStructCoder(encoderFactory);
        recCoder.add(detonationLocation.getHLAfixedRecord());

        detonationResultCodeEnum8 = encoderFactory.createHLAoctet();
        recCoder.add(detonationResultCodeEnum8);

        eventIdCoder = new EventIdentifierCoder(encoderFactory);
        recCoder.add(eventIdCoder.getHLAFixedRecord());

        firingObjIdCoder = new RTIObjectIdCoder();
        recCoder.add(firingObjIdCoder);

        velVectorStructCoder = new VelocityStructCoder(encoderFactory);
        recCoder.add(velVectorStructCoder.getHLAfixedRecord());

        fuseTypeEnum16 = encoderFactory.createHLAinteger16BE();
        recCoder.add(fuseTypeEnum16);

        munitionObjIdCoder = new RTIObjectIdCoder();
        recCoder.add(munitionObjIdCoder);

        munitionTypeStructCoder = new EntityTypeStructCoder(encoderFactory);
        recCoder.add(munitionTypeStructCoder.getHLAfixedRecord());

        quantityFired = encoderFactory.createHLAinteger16BE();
        recCoder.add(quantityFired);

        rateOfFire = encoderFactory.createHLAinteger16BE();
        recCoder.add(rateOfFire);

        relativePositionStructCoder = new RelativePositionStructCoder(encoderFactory);
        recCoder.add(relativePositionStructCoder.getHLAfixedRecord());

        targetObjIdCoder = new RTIObjectIdCoder();
        recCoder.add(targetObjIdCoder);

        warheadTypeEnum16 = encoderFactory.createHLAinteger16BE();
        recCoder.add(warheadTypeEnum16);

        initDefaults();
    }

    public HLAvariableArray getArticulatedPartsDataArray() {
        return articulatedParameterStructArrayCoder;
    }

    public WorldLocationStructCoder getDetonationLocation() {
        return detonationLocation;
    }

    public HLAoctet getDetonationResultCodeEnum8() {
        return detonationResultCodeEnum8;
    }

    public EventIdentifierCoder getEventIdCoder() {
        return eventIdCoder;
    }

    public RTIObjectIdCoder getFiringObjIdCoder() {
        return firingObjIdCoder;
    }

    public VelocityStructCoder getVelVectorStructCoder() {
        return velVectorStructCoder;
    }

    public HLAinteger16BE getFuseTypeEnum16() {
        return fuseTypeEnum16;
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

    public RelativePositionStructCoder getRelativePositionStructCoder() {
        return relativePositionStructCoder;
    }

    public RTIObjectIdCoder getTargetObjIdCoder() {
        return targetObjIdCoder;
    }

    public HLAinteger16BE getWarheadTypeEnum16() {
        return warheadTypeEnum16;
    }

    private void initDefaults() {
        quantityFired.setValue((short) 1);
        rateOfFire.setValue((short) 0);
    }

    public void decode(byte[] bytes) throws DecoderException {
        recCoder.decode(bytes);
    }

    public ArticulatedParameterStruct[] decodeArticulatedParameterStructArray(byte[] bytes) throws DecoderException {

        articulatedParameterStructArrayCoder.decode(bytes);
        ArticulatedParameterStruct[] articulatedPartsArrayData = new ArticulatedParameterStruct[articulatedParameterStructArrayCoder.size()];

        List<ArticulatedParameterStruct> artParamStructList = new ArrayList<>();
        articulatedParameterStructArrayCoder.forEach(part -> {
            ArticulatedParameterCoder partsCoder = new ArticulatedParameterCoder(encoderFactory);

            try {
                partsCoder.decode(part.toByteArray());
                artParamStructList.add(partsCoder.getValue());
            } catch (DecoderException e) {
                e.printStackTrace();
            }

        });

        artParamStructList.toArray(articulatedPartsArrayData);
        return articulatedPartsArrayData;
    }

    public WorldLocationStruct decodeDetonationLocation(byte[] bytes) throws DecoderException {
        return detonationLocation.decodeToWorldLocStruct(bytes);
    }

    public byte decodeDetonationResultCode(byte[] bytes) throws DecoderException {
        detonationResultCodeEnum8.decode(bytes);
        return detonationResultCodeEnum8.getValue();
    }

    public EventIdentifierStruct decodeEventIdentifier(byte[] bytes) throws DecoderException {
        return eventIdCoder.decode(bytes);
    }

    public String decodeFiringObjectId(byte[] bytes) {
        firingObjIdCoder.decode(bytes);
        return firingObjIdCoder.getValue();
    }

    public VelocityVectorStruct decodeFinalVelocity(byte[] bytes) throws DecoderException {
        return velVectorStructCoder.decodeToVelocityVectorStruct(bytes);
    }

    public int decodeFuseType(byte[] bytes) throws DecoderException {
        fuseTypeEnum16.decode(bytes);
        return Short.toUnsignedInt(fuseTypeEnum16.getValue());
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

    public RelativePositionStruct decodeRelativePosition(byte[] bytes) throws DecoderException {
        return relativePositionStructCoder.decodeToRelativePosStruct(bytes);
    }

    public String decodeTargetObjectIdentifier(byte[] bytes) {
        targetObjIdCoder.decode(bytes);
        return targetObjIdCoder.getValue();
    }

    public int decodeWarheadType(byte[] bytes) throws DecoderException {
        warheadTypeEnum16.decode(bytes);
        return Short.toUnsignedInt(warheadTypeEnum16.getValue());
    }


    public void setValues(MunitionDetonation munitionDetonation) {
        if (munitionDetonation == null) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < munitionDetonation.getArticulatedPartData().length; i++) {
            ArticulatedParameterCoder articulatedParamCoder = new ArticulatedParameterCoder(encoderFactory);
            articulatedParamCoder.setValue(munitionDetonation.getArticulatedPartData()[i]);
            articulatedParameterStructArrayCoder.addElement(articulatedParamCoder.getHLAFixedRecord());
        }
        detonationLocation.setValue(munitionDetonation.getDetonationLocation());
        detonationResultCodeEnum8.setValue(munitionDetonation.getDetonationResultCode());
        eventIdCoder.setValue(munitionDetonation.getEventIdentifier());
        firingObjIdCoder.setValue(munitionDetonation.getFiringObjectIdentifier());
        velVectorStructCoder.setValue(munitionDetonation.getFinalVelocityVector());
        fuseTypeEnum16.setValue((short) munitionDetonation.getFuseType());
        munitionObjIdCoder.setValue(munitionDetonation.getMunitionObjectIdentifier());
        munitionTypeStructCoder.setValues(munitionDetonation.getMunitionType());
        quantityFired.setValue((short) munitionDetonation.getQuantityFired());
        rateOfFire.setValue((short) munitionDetonation.getRateOfFire());
        relativePositionStructCoder.setValue(munitionDetonation.getRelativeDetonationLocation());
        targetObjIdCoder.setValue(munitionDetonation.getTargetObjectIdentifier());
        warheadTypeEnum16.setValue((short) munitionDetonation.getWarheadType());

        recCoder.toByteArray();
    }


}
