/*******************************************************************************
 * Copyright (C) Her Majesty the Queen in Right of Canada, 
 * as represented by the Minister of National Defence, 2018
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package ca.drdc.ivct.fom.utils;

import ca.drdc.ivct.fom.base.BaseEntity;
import ca.drdc.ivct.fom.base.structs.*;
import ca.drdc.ivct.fom.der.DeadReckoningAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vcs.utility.spatial.BardswellVector;
import vcs.utility.spatial.KinematicState;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * CSV reader for base Entity. The CSV must contain the following item in this
 * order: entityId,entityType,description
 * <p>
 * The CSV must contain a header.
 *
 * @author laurenceo, mlavallee
 */
public class BaseEntityCSVReader {
    private static Logger logger = LoggerFactory.getLogger(BaseEntityCSVReader.class);

    public static List<BaseEntity> loadCSVFileToBaseEntityList(List<URL> csvFiles) throws IOException, ParseException {

        List<BaseEntity> fadEntities = new ArrayList<>();
        String line = "";
        String csvSplitBy = ",";

        for (int i = 0; i < csvFiles.size(); i++) {
            logger.info("Reading {}", csvFiles.get(i).getPath());
            try (BufferedReader br = new BufferedReader(new InputStreamReader(csvFiles.get(i).openStream()))) {
                // skip header
                line = br.readLine();

                // process each entity
                while ((line = br.readLine()) != null && !line.isEmpty()) {
                    List<String> csvItems = Arrays.asList(line.split(csvSplitBy));

                    BaseEntity newEntity = new BaseEntity();
                    newEntity.setEntityIdentifier(new EntityIdentifierStruct(csvItems.get(EntityHeader.ENTITY_ID.ordinal())));
                    String[] entityLine = csvItems.get(EntityHeader.ENTITY_TYPE.ordinal()).split("\\.");
                    newEntity.setEntityType(
                        new EntityTypeStruct(
                            entityLine[0],
                            entityLine[1],
                            entityLine[2],
                            entityLine[3],
                            entityLine[4],
                            entityLine[5],
                            entityLine[6]
                        )
                    );

                    double[] worldLocationLatLongAlt = {
                        Math.toRadians(Double.parseDouble(csvItems.get(EntityHeader.WORLD_LOCATION_LAT.ordinal()))),
                            Math.toRadians(Double.parseDouble(csvItems.get(EntityHeader.WORLD_LOCATION_LONG.ordinal()))),
                            Double.parseDouble(csvItems.get(EntityHeader.WORLD_LOCATION_ALT.ordinal()))
                    };

                    double[] worldLocationXYZ = CoordConverter.geo_to_xyz(worldLocationLatLongAlt);

                    WorldLocationStruct worldLocation = new WorldLocationStruct(
                            worldLocationXYZ[0], worldLocationXYZ[1], worldLocationXYZ[2]
                    );
                    boolean isFrozen = Boolean.parseBoolean(csvItems.get(EntityHeader.IS_FROZEN.ordinal()));

                    OrientationStruct orientationLocalCoordinates = new OrientationStruct(csvItems.get(EntityHeader.ORIENTATION.ordinal()));
                    BardswellVector pseudoBardswellVector = new BardswellVector(worldLocationLatLongAlt[0], worldLocationLatLongAlt[1], worldLocationLatLongAlt[2], 1., Math.toRadians(orientationLocalCoordinates.getPsi()), Math.toRadians(orientationLocalCoordinates.getTheta()) );
                    KinematicState pseudoKinematic = new KinematicState(pseudoBardswellVector);

                    float roll  = (float) (orientationLocalCoordinates.getPhi() + (worldLocationLatLongAlt[0] - Math.PI/2));
                    OrientationStruct orientationInGlobalCoordinates = new OrientationStruct((float) pseudoKinematic.getOrientation().getX(), (float)pseudoKinematic.getOrientation().getY(), roll);


                    String[] velocityLine = csvItems.get(EntityHeader.HEADING_PITCH_SPEED.ordinal()).split(";");
                    double heading = Double.parseDouble(velocityLine[0]);
                    double pitch = Double.parseDouble(velocityLine[1]);
                    double speed = Double.parseDouble(velocityLine[2]);
                    BardswellVector bardswellVector = new BardswellVector(worldLocationLatLongAlt[0], worldLocationLatLongAlt[1], worldLocationLatLongAlt[2], speed, Math.toRadians(pitch), Math.toRadians(heading) );
                    KinematicState kinState = new KinematicState(bardswellVector);
                    VelocityVectorStruct velocityVector = new VelocityVectorStruct((float) kinState.getVelocity().getX(), (float) kinState.getVelocity().getY(), (float) kinState.getVelocity().getZ());



                    DeadReckoningAlgorithm itemDeadReckoningAlgorithm = DeadReckoningAlgorithm.valueOf(csvItems.get(EntityHeader.DEAD_RECKONING_ALGORITHM.ordinal()));

                    switch (itemDeadReckoningAlgorithm) {
                        case DRM_FPW:
                        case DRM_FPB:
                            newEntity.setSpatialRepresentation(
                                new SpatialFPStruct(itemDeadReckoningAlgorithm, worldLocation, isFrozen, orientationInGlobalCoordinates, velocityVector));
                            break;

                        case DRM_RVW:
                        case DRM_RVB:
                            AngularVelocityVectorStruct angularVelocityVector =
                                new AngularVelocityVectorStruct(csvItems.get(EntityHeader.ANGULAR_VELOCITY.ordinal()));
                            AccelerationVectorStruct accelerationVector =
                                new AccelerationVectorStruct(csvItems.get(EntityHeader.ACCELERATION_VECTOR.ordinal()));

                            newEntity.setSpatialRepresentation(
                                new SpatialRVStruct(itemDeadReckoningAlgorithm, worldLocation,
                                    isFrozen, orientationInGlobalCoordinates, velocityVector, accelerationVector, angularVelocityVector));
                            break;
                        default:
                            logger.warn("The algorithm {} is not yet implemented, the entity won't have any spatial reprentation.", itemDeadReckoningAlgorithm);
                            break;
                    }

                    fadEntities.add(newEntity);
                }

            } catch (IOException iOException) {
                logger.error("Error parsing the FADs", iOException);
                throw iOException;
            }
        }
        return fadEntities;
    }

    /**
     * Print all files in the specified absolute path for debugging purpose.
     *
     * @param dir the absolute path of the directory.
     */
    public static void printAllFilesInDir(String dir) {
        File currentDir = new File(dir + File.separator);
        if (currentDir.isDirectory()) {
            File[] files = currentDir.listFiles();
            for (File file : files) {
                logger.info("{}\n{}", file.getAbsolutePath(), file.getName());
            }
        }
    }


    /**
     * Order of field in the CSV
     */
    private enum EntityHeader {
        ENTITY_ID("entityId"),
        ENTITY_TYPE("entityType"),
        DESCRIPTION("description"),
        DEAD_RECKONING_ALGORITHM("deadReckoningAlgorithm"),
        WORLD_LOCATION_LAT("worldLocationLat"),
        WORLD_LOCATION_LONG("worldLocationLong"),
        WORLD_LOCATION_ALT("worldLocationHeight"),
        IS_FROZEN("isFrozen"),
        ORIENTATION("orientation"),
        HEADING_PITCH_SPEED("headingPitchSpeed"),
        ACCELERATION_VECTOR("accelerationVector"),
        ANGULAR_VELOCITY("angularVelocity");

        private final String value;

        private EntityHeader(String value) {
            this.value = value;
        }
    }
}
