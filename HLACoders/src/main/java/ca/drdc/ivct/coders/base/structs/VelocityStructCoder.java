package ca.drdc.ivct.coders.base.structs;

import ca.drdc.ivct.fom.base.structs.VelocityVectorStruct;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAfixedRecord;
import hla.rti1516e.encoding.HLAfloat32BE;

public class VelocityStructCoder {

    private final HLAfixedRecord _recCoder;

    private final HLAfloat32BE _VxCoder;
    private final HLAfloat32BE _VyCoder;
    private final HLAfloat32BE _VzCoder;

    public VelocityStructCoder(EncoderFactory encoderFactory) {

        _recCoder = encoderFactory.createHLAfixedRecord();

        _VxCoder = encoderFactory.createHLAfloat32BE();
        _VyCoder = encoderFactory.createHLAfloat32BE();
        _VzCoder = encoderFactory.createHLAfloat32BE();

        _recCoder.add(_VxCoder);
        _recCoder.add(_VyCoder);
        _recCoder.add(_VzCoder);
    }

    public HLAfixedRecord getHLAfixedRecord() {
        return this._recCoder;
    }

    public void setValue(VelocityVectorStruct velocity) {

        if ((velocity) == null) {
            return;
        }

        _VxCoder.setValue(velocity.getxVelocity());
        _VyCoder.setValue(velocity.getyVelocity());
        _VzCoder.setValue(velocity.getzVelocity());
    }

    public byte[] toByteArray() {
        return _recCoder.toByteArray();
    }

    /**
     * It is necessary for the bytes to be set beforehand through the
     * spatialFPCoder/spatialRVCoder decode function.
     *
     * @return VelocityVectorStruct
     */
    public VelocityVectorStruct getVelocityVectorStruct() {
        return new VelocityVectorStruct(_VxCoder.getValue(), _VyCoder.getValue(), _VzCoder.getValue());
    }

    public VelocityVectorStruct decodeToVelocityVectorStruct(byte[] velocityBytes) throws DecoderException {
        _recCoder.decode(velocityBytes);
        return new VelocityVectorStruct(_VxCoder.getValue(), _VyCoder.getValue(), _VzCoder.getValue());
    }
}
