package ca.drdc.ivct.coders.base.structs;

import ca.drdc.ivct.fom.base.ParameterValue;
import ca.drdc.ivct.fom.base.structs.ArticulatedParameterStruct;
import ca.drdc.ivct.fom.base.structs.ArticulatedPartsStruct;
import ca.drdc.ivct.fom.base.structs.AttachedPartsStruct;
import hla.rti1516e.encoding.*;

public class ArticulatedParameterCoder {


    private final HLAfixedRecord recCoder;
    private final HLAoctet articulatedParameterChange;
    private final HLAinteger16BE partAttachedTo;

    private final HLAvariantRecord<HLAinteger32BE> parameterValueVariantStruct;
    private final ArticulatedPartsCoder articulatedPartsCoder;
    private final AttachedPartsCoder attachedPartsCoder;

    private EncoderFactory encoderFactory;

    public ArticulatedParameterCoder(EncoderFactory encoderFactory) {
        this.encoderFactory = encoderFactory;
        recCoder = encoderFactory.createHLAfixedRecord();

        articulatedParameterChange = encoderFactory.createHLAoctet();
        recCoder.add(articulatedParameterChange);

        partAttachedTo = encoderFactory.createHLAinteger16BE();
        recCoder.add(partAttachedTo);


        parameterValueVariantStruct = encoderFactory.createHLAvariantRecord(encoderFactory.createHLAinteger32BE());

        articulatedPartsCoder = new ArticulatedPartsCoder(encoderFactory);
        parameterValueVariantStruct.setVariant(encoderFactory.createHLAinteger32BE(0), articulatedPartsCoder.getHLAFixedRecord());

        attachedPartsCoder = new AttachedPartsCoder(encoderFactory);
        parameterValueVariantStruct.setVariant(encoderFactory.createHLAinteger32BE(1), attachedPartsCoder.getHLAFixedRecord());

        recCoder.add(parameterValueVariantStruct);
    }

    public HLAfixedRecord getHLAFixedRecord() {
        return recCoder;
    }

    public byte[] toByteArray() {
        return recCoder.toByteArray();
    }

    public void decode(byte[] bytes) throws DecoderException {
        recCoder.decode(bytes);
    }

    public ArticulatedParameterStruct getValue() {
        ParameterValue parameterValue;
        switch (parameterValueVariantStruct.getDiscriminant().getValue()) {
            case 0:
                parameterValue = articulatedPartsCoder.getValue();
                break;
            case 1:
                parameterValue = attachedPartsCoder.getValue();
                break;
            default:
                return null;
        }

        parameterValue.setArticulatedParameterType(parameterValueVariantStruct.getDiscriminant().getValue());
        return new ArticulatedParameterStruct(articulatedParameterChange.getValue(), partAttachedTo.getValue(), parameterValue);
    }

    public void setValue(ArticulatedParameterStruct articulatedParameter) {
        articulatedParameterChange.setValue(articulatedParameter.getArticulatedParameterChange());
        partAttachedTo.setValue((short) articulatedParameter.getPartAttachedTo());

        ParameterValue parameterValue = articulatedParameter.getParameterValue();

        if (parameterValue == null) {
            throw new IllegalArgumentException();
        }

        switch ((int) parameterValue.getArticulatedParameterType()) {
            case 0:
                articulatedPartsCoder.setValue((ArticulatedPartsStruct) parameterValue);
                parameterValueVariantStruct.setDiscriminant(encoderFactory.createHLAinteger32BE(0));
                break;
            case 1:
                attachedPartsCoder.setValue((AttachedPartsStruct) parameterValue);
                parameterValueVariantStruct.setDiscriminant(encoderFactory.createHLAinteger32BE(1));
                break;
            default:
                break;
        }
    }
}
