package ca.drdc.ivct.fom.utils;

import ca.drdc.ivct.fom.base.structs.EntityTypeStruct;
import ca.drdc.ivct.fom.base.structs.EventIdentifierStruct;
import ca.drdc.ivct.fom.base.structs.VelocityVectorStruct;
import ca.drdc.ivct.fom.base.structs.WorldLocationStruct;
import ca.drdc.ivct.fom.warfare.WeaponFire;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vcs.utility.spatial.BardswellVector;
import vcs.utility.spatial.KinematicState;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WeaponFireCSVReader {
    private static Logger logger = LoggerFactory.getLogger(WeaponFireCSVReader.class);

    public static List<WeaponFire> loadCSVFileToWeaponFireList(List<URL> csvFiles) {
        List<WeaponFire> fadWeaponFires = new ArrayList<>();
        String line = "";
        String csvSplitBy = ",";

        for (URL csvFile : csvFiles) {
            logger.info("Reading {}", csvFile.getPath());
            try (BufferedReader br = new BufferedReader(new InputStreamReader(csvFile.openStream()))) {
                // skip header
                line = br.readLine();

                // process each event
                while ((line = br.readLine()) != null && !line.isEmpty()) {
                    List<String> csvItems = Arrays.asList(line.split(csvSplitBy));

                    WeaponFire newWeaponFire = new WeaponFire();
                    newWeaponFire.setEventIdentifier(
                        new EventIdentifierStruct(
                            csvItems.get(WeaponFireHeader.EVENT_COUNT.ordinal()),
                            csvItems.get(WeaponFireHeader.EVENT_ID.ordinal())
                        ));
                    newWeaponFire.setFireControlSolutionRange(Float.parseFloat(csvItems.get(WeaponFireHeader.FIRE_CONTROL_SOLUTION_RANGE.ordinal())));
                    newWeaponFire.setFireMissionIndex(Long.parseLong(csvItems.get(WeaponFireHeader.FIRE_MISSION_INDEX.ordinal())));

                    double[] firingLocationLatLongAlt = {
                        Math.toRadians(Double.parseDouble(csvItems.get(WeaponFireHeader.FIRING_LOCATION_LAT.ordinal()))),
                        Math.toRadians(Double.parseDouble(csvItems.get(WeaponFireHeader.FIRING_LOCATION_LONG.ordinal()))),
                        Double.parseDouble(csvItems.get(WeaponFireHeader.FIRING_LOCATION_HEIGHT.ordinal()))
                    };
                    double[] firingLocationXYZ = CoordConverter.geo_to_xyz(firingLocationLatLongAlt);
                    WorldLocationStruct firingLocation = new WorldLocationStruct(
                        firingLocationXYZ[0],
                        firingLocationXYZ[1],
                        firingLocationXYZ[2]
                    );
                    newWeaponFire.setFiringLocation(firingLocation);

                    newWeaponFire.setFiringObjectIdentifier(csvItems.get(WeaponFireHeader.FIRING_OBJECT_IDENTIFIER.ordinal()));
                    newWeaponFire.setFuseType(Integer.parseInt(csvItems.get(WeaponFireHeader.FUSE_TYPE.ordinal())));

                    String[] headingPitchSpeed = csvItems.get(WeaponFireHeader.HEADING_PITCH_SPEED.ordinal()).split(":");
                    double heading = Double.parseDouble(headingPitchSpeed[0]);
                    double pitch = Double.parseDouble(headingPitchSpeed[1]);
                    double speed = Double.parseDouble(headingPitchSpeed[2]);

                    BardswellVector bardswellVector = new BardswellVector(firingLocationLatLongAlt[0], firingLocationLatLongAlt[1], firingLocationLatLongAlt[2], speed, Math.toRadians(pitch), Math.toRadians(heading));
                    KinematicState kinState = new KinematicState(bardswellVector);
                    VelocityVectorStruct initialVelocity = new VelocityVectorStruct((float) kinState.getVelocity().getX(), (float) kinState.getVelocity().getY(), (float) kinState.getVelocity().getZ());
                    newWeaponFire.setInitialVelocityVector(initialVelocity);

                    newWeaponFire.setMunitionObjectIdentifier(csvItems.get(WeaponFireHeader.MUNITION_OBJECT_IDENTIFIER.ordinal()));
                    String[] munitionType = csvItems.get(WeaponFireHeader.MUNITION_TYPE.ordinal()).split("\\.");
                    EntityTypeStruct munitionTypeStruc = new EntityTypeStruct(
                        munitionType[0],
                        munitionType[1],
                        munitionType[2],
                        munitionType[3],
                        munitionType[4],
                        (munitionType.length > 5 ? munitionType[5] : "0"),
                        (munitionType.length > 6 ? munitionType[6] : "0")
                    );
                    newWeaponFire.setMunitionType(munitionTypeStruc);
                    newWeaponFire.setQuantityFired(Integer.parseInt(csvItems.get(WeaponFireHeader.QUANTITY_FIRED.ordinal())));
                    newWeaponFire.setRateOfFire(Integer.parseInt(csvItems.get(WeaponFireHeader.RATE_OF_FIRE.ordinal())));
                    newWeaponFire.setTargetObjectIdentifier(csvItems.get(WeaponFireHeader.TARGET_OBJECT_IDENTIFIER.ordinal()));
                    newWeaponFire.setWarheadType(Integer.parseInt(csvItems.get(WeaponFireHeader.WARHEAD_TYPE.ordinal())));
                    fadWeaponFires.add(newWeaponFire);
                }

            } catch (IOException iOException) {
                logger.error("Error parsing the FADs", iOException);
                return Collections.emptyList();
            }
        }
        return fadWeaponFires;
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
    private enum WeaponFireHeader {
        EVENT_ID("EventId"),
        EVENT_COUNT("EventCount"),
        FIRE_CONTROL_SOLUTION_RANGE("FireControlSolutionRange"),
        FIRE_MISSION_INDEX("FireMissionIndex"),
        FIRING_LOCATION_LAT("FiringLocationLat"),
        FIRING_LOCATION_LONG("FiringLocationLong"),
        FIRING_LOCATION_HEIGHT("FiringLocationHeight"),
        FIRING_OBJECT_IDENTIFIER("FiringObjectIdentifier"),
        FUSE_TYPE("FuseType"),
        HEADING_PITCH_SPEED("HeadingPitchSpeed"),
        MUNITION_OBJECT_IDENTIFIER("MunitionObjectIdentifier"),
        MUNITION_TYPE("MunitionType"),
        QUANTITY_FIRED("QuantityFired"),
        RATE_OF_FIRE("RateOfFire"),
        TARGET_OBJECT_IDENTIFIER("TargetObjectIdentifier"),
        WARHEAD_TYPE("WarheadType");

        private final String value;

        private WeaponFireHeader(String value) {
            this.value = value;
        }
    }
}
