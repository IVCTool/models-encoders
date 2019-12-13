package ca.drdc.ivct.fom.utils;

import ca.drdc.ivct.fom.base.ParameterValue;
import ca.drdc.ivct.fom.base.structs.*;
import ca.drdc.ivct.fom.warfare.MunitionDetonation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vcs.utility.spatial.BardswellVector;
import vcs.utility.spatial.KinematicState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class MunitionDetonationCSVReader {
    private static Logger logger = LoggerFactory.getLogger(MunitionDetonationCSVReader.class);

    public static List<MunitionDetonation> loadCSVFileToMunitionDetonationList(List<URL> csvFiles) {
        List<MunitionDetonation> fadMunitionDetonations = new ArrayList<>();
        String line;
        String csvSplitBy = ",";

        for (URL csvFile : csvFiles) {
            logger.info("Reading {}", csvFile.getPath());
            try (BufferedReader br = new BufferedReader(new InputStreamReader(csvFile.openStream()))) {
                // skip header
                line = br.readLine();

                if (line.split(csvSplitBy).length != MunitionDetonationHeader.values().length) {
                    continue;
                }

                // process each event
                while ((line = br.readLine()) != null && !line.isEmpty()) {
                    List<String> csvItems = Arrays.asList(line.split(csvSplitBy));

                    MunitionDetonation newMunitionDetonation = new MunitionDetonation();

                    double[] worldLocationLatLongAlt = {
                        Math.toRadians(Double.parseDouble(csvItems.get(MunitionDetonationHeader.WORLD_LOCATION_LAT.ordinal()))),
                        Math.toRadians(Double.parseDouble(csvItems.get(MunitionDetonationHeader.WORLD_LOCATION_LONG.ordinal()))),
                        Double.parseDouble(csvItems.get(MunitionDetonationHeader.WORLD_LOCATION_HEIGHT.ordinal()))
                    };
                    double[] worldLocationXYZ = CoordConverter.geo_to_xyz(worldLocationLatLongAlt);
                    WorldLocationStruct worldLocation = new WorldLocationStruct(
                        worldLocationXYZ[0], worldLocationXYZ[1], worldLocationXYZ[2]
                    );
                    newMunitionDetonation.setDetonationLocation(worldLocation);

                    newMunitionDetonation.setDetonationResultCode(Byte.parseByte(csvItems.get(MunitionDetonationHeader.DETONATION_RESULT_CODE.ordinal())));

                    newMunitionDetonation.setEventIdentifier(
                        new EventIdentifierStruct(
                            csvItems.get(MunitionDetonationHeader.EVENT_COUNT.ordinal()),
                            csvItems.get(MunitionDetonationHeader.EVENT_ID.ordinal())
                        ));
                    newMunitionDetonation.setFiringObjectIdentifier(csvItems.get(MunitionDetonationHeader.FIRING_OBJECT_ID.ordinal()));

                    String[] headingPitchSpeed = csvItems.get(MunitionDetonationHeader.HEADING_PITCH_SPEED.ordinal()).split(":");
                    double heading = Double.parseDouble(headingPitchSpeed[0]);
                    double pitch = Double.parseDouble(headingPitchSpeed[1]);
                    double speed = Double.parseDouble(headingPitchSpeed[2]);

                    BardswellVector bardswellVector = new BardswellVector(worldLocationLatLongAlt[0], worldLocationLatLongAlt[1], worldLocationLatLongAlt[2], speed, Math.toRadians(pitch), Math.toRadians(heading));
                    KinematicState kinState = new KinematicState(bardswellVector);
                    VelocityVectorStruct finalVelocity = new VelocityVectorStruct((float) kinState.getVelocity().getX(), (float) kinState.getVelocity().getY(), (float) kinState.getVelocity().getZ());
                    newMunitionDetonation.setFinalVelocityVector(finalVelocity);

                    newMunitionDetonation.setFuseType(Integer.parseInt(csvItems.get(MunitionDetonationHeader.FUSE_TYPE.ordinal())));

                    newMunitionDetonation.setMunitionObjectIdentifier(csvItems.get(MunitionDetonationHeader.MUNITION_OBJECT_ID.ordinal()));

                    String[] munitionType = csvItems.get(MunitionDetonationHeader.MUNITION_TYPE.ordinal()).split("\\.");
                    EntityTypeStruct munitionTypeStruct = new EntityTypeStruct(
                        munitionType[0],
                        munitionType[1],
                        munitionType[2],
                        munitionType[3],
                        munitionType[4],
                        (munitionType.length > 5 ? munitionType[5] : "0"),
                        (munitionType.length > 6 ? munitionType[6] : "0")
                    );
                    newMunitionDetonation.setMunitionType(munitionTypeStruct);

                    newMunitionDetonation.setQuantityFired(Integer.parseInt(csvItems.get(MunitionDetonationHeader.QUANTITY_FIRED.ordinal())));

                    newMunitionDetonation.setRateOfFire(Integer.parseInt(csvItems.get(MunitionDetonationHeader.RATE_OF_FIRE.ordinal())));

                    float[] relativePositionXYZ = {
                        Float.parseFloat(csvItems.get(MunitionDetonationHeader.RELATIVE_POSITION_X.ordinal())),
                        Float.parseFloat(csvItems.get(MunitionDetonationHeader.RELATIVE_POSITION_Y.ordinal())),
                        Float.parseFloat(csvItems.get(MunitionDetonationHeader.RELATIVE_POSITION_Z.ordinal()))
                    };
                    newMunitionDetonation.setRelativeDetonationLocation(new RelativePositionStruct(relativePositionXYZ[0], relativePositionXYZ[1], relativePositionXYZ[2]));

                    newMunitionDetonation.setTargetObjectIdentifier(csvItems.get(MunitionDetonationHeader.TARGET_OBJECT_ID.ordinal()));

                    newMunitionDetonation.setWarheadType(Integer.parseInt(csvItems.get(MunitionDetonationHeader.WARHEAD_TYPE.ordinal())));

                    if (csvItems.size() > MunitionDetonationHeader.ARTICULATED_PART_DATAFILE.ordinal()) {
                        String partsDataFileName = csvItems.get(MunitionDetonationHeader.ARTICULATED_PART_DATAFILE.ordinal());
                        newMunitionDetonation.setArticulatedPartData(loadArticulatedPartFromCSV(
                            csvFiles
                                .stream()
                                .filter(
                                    url -> url != null && url.getFile().contains(partsDataFileName)
                                )
                                .findFirst()
                            )
                        );
                    } else {
                        newMunitionDetonation.setArticulatedPartData(new ArticulatedParameterStruct[0]);
                    }

                    fadMunitionDetonations.add(newMunitionDetonation);
                }
            } catch (IOException iOException) {
                logger.error("Error parsing the FADs", iOException);
                return Collections.emptyList();
            }
        }

        return fadMunitionDetonations;
    }

    private static ArticulatedParameterStruct[] loadArticulatedPartFromCSV(Optional<URL> partsData) throws IOException {
        if (!partsData.isPresent()) {
            return new ArticulatedParameterStruct[0];
        }

        String line;
        String csvSplitBy = ",";
        List<ArticulatedParameterStruct> articulatedParameters = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(partsData.get().openStream()))) {
            // skip header
            line = br.readLine();

            while ((line = br.readLine()) != null && !line.isEmpty()) {
                List<String> csvItems = Arrays.asList(line.split(csvSplitBy));

                ArticulatedParameterStruct parameter = new ArticulatedParameterStruct();
                parameter.setArticulatedParameterChange(Byte.parseByte(csvItems.get(MunitionDetonationPartHeader.ARTICULATED_PARAMETER_CHANGE.ordinal())));
                parameter.setPartAttachedTo(Integer.parseInt(csvItems.get(MunitionDetonationPartHeader.PART_ATTACHED_TO.ordinal())));

                int articulatedParameterType = Integer.parseInt(csvItems.get(MunitionDetonationPartHeader.ARTICULATED_PARAMETER_TYPE.ordinal()));
                ParameterValue parameterValue;

                if (articulatedParameterType == 0) {
                    ArticulatedPartsStruct articulatedParts = new ArticulatedPartsStruct();
                    articulatedParts.setArticulatedPartsType(Long.parseLong(csvItems.get(MunitionDetonationPartHeader.ARTICULATED_PARTS_TYPE.ordinal())));
                    articulatedParts.setValue(Float.parseFloat(csvItems.get(MunitionDetonationPartHeader.ARTICULATED_PARTS_VALUE.ordinal())));
                    articulatedParts.setTypeMetric(Long.parseLong(csvItems.get(MunitionDetonationPartHeader.ARTICULATED_PARTS_TYPE_METRIC.ordinal())));
                    parameterValue = articulatedParts;
                } else {
                    AttachedPartsStruct attachedParts = new AttachedPartsStruct();
                    attachedParts.setStation(Long.parseLong(csvItems.get(MunitionDetonationPartHeader.ATTACHED_PARTS_STATION.ordinal())));
                    String[] storeType = csvItems.get(MunitionDetonationPartHeader.ATTACHED_PARTS_STORE_TYPE.ordinal()).split("\\.");
                    EntityTypeStruct storeTypeStruct = new EntityTypeStruct(
                        storeType[0],
                        storeType[1],
                        storeType[2],
                        storeType[3],
                        storeType[4],
                        (storeType.length > 5 ? storeType[5] : "0"),
                        (storeType.length > 6 ? storeType[6] : "0")
                    );
                    attachedParts.setStoreType(storeTypeStruct);
                    parameterValue = attachedParts;
                }

                parameterValue.setArticulatedParameterType(articulatedParameterType);
                parameter.setParameterValue(parameterValue);
                articulatedParameters.add(parameter);
            }
        }

        return articulatedParameters.toArray(new ArticulatedParameterStruct[0]);
    }

    private enum MunitionDetonationHeader {
        WORLD_LOCATION_LAT("WorldLocationLat"),
        WORLD_LOCATION_LONG("WorldLocationLong"),
        WORLD_LOCATION_HEIGHT("WorldLocationHeight"),
        DETONATION_RESULT_CODE("DetonationResultCode"),
        EVENT_ID("EventId"),
        EVENT_COUNT("EventCount"),
        FIRING_OBJECT_ID("FiringObjectId"),
        HEADING_PITCH_SPEED("HeadingPitchSpeed"),
        FUSE_TYPE("FuseType"),
        MUNITION_OBJECT_ID("MunitionObjectId"),
        MUNITION_TYPE("MunitionType"),
        QUANTITY_FIRED("QuantityFired"),
        RATE_OF_FIRE("RateOfFire"),
        RELATIVE_POSITION_X("RelativePositionX"),
        RELATIVE_POSITION_Y("RelativePositionY"),
        RELATIVE_POSITION_Z("RelativePositionZ"),
        TARGET_OBJECT_ID("TargetObjectId"),
        WARHEAD_TYPE("WarheadType"),
        ARTICULATED_PART_DATAFILE("ArticulatedPartDataFile");

        private final String value;

        MunitionDetonationHeader(String value) {
            this.value = value;
        }
    }

    private enum MunitionDetonationPartHeader {
        ARTICULATED_PARAMETER_CHANGE("ArticulatedParameterChange"),
        PART_ATTACHED_TO("PartAttachedTo"),
        ARTICULATED_PARAMETER_TYPE("ArticulatedParameterType"),
        ARTICULATED_PARTS_TYPE("ArticulatedPartsType"),
        ARTICULATED_PARTS_TYPE_METRIC("ArticulatedPartsTypeMetric"),
        ARTICULATED_PARTS_VALUE("ArticulatedPartsValue"),
        ATTACHED_PARTS_STATION("AttachedPartsStation"),
        ATTACHED_PARTS_STORE_TYPE("AttachedPartsStoreType");

        private final String value;

        MunitionDetonationPartHeader(String value) {
            this.value = value;
        }
    }
}
