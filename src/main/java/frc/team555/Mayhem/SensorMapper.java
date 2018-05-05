package frc.team555.Mayhem;

import org.montclairrobotics.cyborg.Cyborg;
import org.montclairrobotics.cyborg.data.CBDifferentialDriveControlData;
import org.montclairrobotics.cyborg.data.CBStdDriveControlData;
import org.montclairrobotics.cyborg.data.CBStdDriveRequestData;
import org.montclairrobotics.cyborg.devices.CBEncoder;
import org.montclairrobotics.cyborg.devices.CBNavXYawSource;
import org.montclairrobotics.cyborg.mappers.CBCustomMapper;

public class SensorMapper extends CBCustomMapper {

    Cyborg robot;
    CBStdDriveRequestData stdDriveRequestData = (CBStdDriveRequestData) Cyborg.requestData.driveData;

    CBNavXYawSource navXYawSource;

    public SensorMapper(Cyborg robot) {
        super(robot);
        this.robot = robot;
    }

    @Override
    public void update() {
        stdDriveRequestData.gyroLockValue = navXYawSource.get();
    }
}
