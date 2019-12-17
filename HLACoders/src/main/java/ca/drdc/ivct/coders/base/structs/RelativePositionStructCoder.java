package ca.drdc.ivct.coders.base.structs;

import ca.drdc.ivct.fom.base.structs.RelativePositionStruct;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAfixedRecord;
import hla.rti1516e.encoding.HLAfloat32BE;

public class RelativePositionStructCoder {


    private final HLAfixedRecord _recCoder;

    private final HLAfloat32BE _XCoder;
    private final HLAfloat32BE _YCoder;
    private final HLAfloat32BE _ZCoder;

    public RelativePositionStructCoder(EncoderFactory encoderFactory) {

        _recCoder = encoderFactory.createHLAfixedRecord();

        _XCoder = encoderFactory.createHLAfloat32BE();
        _YCoder = encoderFactory.createHLAfloat32BE();
        _ZCoder = encoderFactory.createHLAfloat32BE();

        _recCoder.add(_XCoder);
        _recCoder.add(_YCoder);
        _recCoder.add(_ZCoder);

    }

    public HLAfixedRecord getHLAfixedRecord() {
        return this._recCoder;
    }

    public void setValue(RelativePositionStruct relativePosition) {

        if ((relativePosition) == null) {
            return;
        }

        _XCoder.setValue(relativePosition.getBodyXPosition());
        _YCoder.setValue(relativePosition.getBodyYPosition());
        _ZCoder.setValue(relativePosition.getBodyZPosition());
    }

    public byte[] toByteArray() {
        return _recCoder.toByteArray();
    }

    /**
     * @return RelativePositionStruct the relative location.
     */
    public RelativePositionStruct getRelativePositionValue() {
        return new RelativePositionStruct(_XCoder.getValue(), _YCoder.getValue(), _ZCoder.getValue());
    }

    public RelativePositionStruct decodeToRelativePosStruct(byte[] relativePositionBytes) throws DecoderException {
        _recCoder.decode(relativePositionBytes);
        return new RelativePositionStruct(_XCoder.getValue(), _YCoder.getValue(), _ZCoder.getValue());
    }
}
