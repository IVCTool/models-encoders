package ca.drdc.ivct.coders.base.structs;

import ca.drdc.ivct.fom.base.structs.WorldLocationStruct;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAfixedRecord;
import hla.rti1516e.encoding.HLAfloat64BE;

public class WorldLocationStructCoder {

    private final HLAfixedRecord _recCoder;

    private final HLAfloat64BE _XCoder;
    private final HLAfloat64BE _YCoder;
    private final HLAfloat64BE _ZCoder;

    public WorldLocationStructCoder(EncoderFactory encoderFactory) {

        _recCoder = encoderFactory.createHLAfixedRecord();

        _XCoder = encoderFactory.createHLAfloat64BE();
        _YCoder = encoderFactory.createHLAfloat64BE();
        _ZCoder = encoderFactory.createHLAfloat64BE();

        _recCoder.add(_XCoder);
        _recCoder.add(_YCoder);
        _recCoder.add(_ZCoder);

    }

    public HLAfixedRecord getHLAfixedRecord() {
        return this._recCoder;
    }

    public void setValue(WorldLocationStruct worldLocation) {

        if ((worldLocation) == null) {
            return;
        }

        _XCoder.setValue(worldLocation.getxPosition());
        _YCoder.setValue(worldLocation.getyPosition());
        _ZCoder.setValue(worldLocation.getzPosition());
    }

    public byte[] toByteArray() {
        return _recCoder.toByteArray();
    }

    /**
     * It is necessary for the bytes to be set beforehand through the
     * spatialFPCoder/spatialRVCoder decode function.
     *
     * @return WorldLocationStruct the world location.
     */
    public WorldLocationStruct getWorldLocationStructValue() {
        return new WorldLocationStruct(_XCoder.getValue(), _YCoder.getValue(), _ZCoder.getValue());
    }

    public WorldLocationStruct decodeToWorldLocStruct(byte[] worldLocBytes) throws DecoderException {
        _recCoder.decode(worldLocBytes);
        return new WorldLocationStruct(_XCoder.getValue(), _YCoder.getValue(), _ZCoder.getValue());
    }

}
