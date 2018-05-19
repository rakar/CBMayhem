package frc.team555.Mayhem.Mappers;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team555.Mayhem.Data.RequestData;
import org.montclairrobotics.cyborg.Cyborg;
import org.montclairrobotics.cyborg.data.CBDifferentialDriveControlData;
import org.montclairrobotics.cyborg.data.CBStdDriveControlData;
import org.montclairrobotics.cyborg.data.CBStdDriveRequestData;
import org.montclairrobotics.cyborg.devices.CBDeviceID;
import org.montclairrobotics.cyborg.devices.CBDigitalInput;
import org.montclairrobotics.cyborg.devices.CBEncoder;
import org.montclairrobotics.cyborg.devices.CBNavXYawSource;
import org.montclairrobotics.cyborg.mappers.CBCustomMapper;

public class SensorMapper extends CBCustomMapper {

    //Cyborg robot;
    //CBStdDriveRequestData stdDriveRequestData = (CBStdDriveRequestData) Cyborg.requestData.driveData;
    RequestData rd;

    //CBNavXYawSource navXYawSource;

    CBEncoder mainLiftEncoder;
    CBDigitalInput mainLiftLimit;

    public SensorMapper(Cyborg robot) {
        super(robot);
        this.robot = robot;
        rd = (RequestData)Cyborg.requestData;
    }

    @Override
    public void update() {
        //stdDriveRequestData.gyroLockValue = navXYawSource.get();

        // These values are mapped for debugging purposes only
        rd.mainLiftEncoderValue = mainLiftEncoder.getDistance();
        rd.mainLiftLimitValue = mainLiftLimit.get();
        SmartDashboard.putBoolean("mainLiftLimit", rd.mainLiftLimitValue);
        SmartDashboard.putNumber("mainLiftEncoder", rd.mainLiftEncoderValue);
        //
    }

    public CBCustomMapper setMainLiftLimits(CBDeviceID encoder, CBDeviceID bLimit) {
        mainLiftEncoder = Cyborg.hardwareAdapter.getEncoder(encoder);
        mainLiftLimit = Cyborg.hardwareAdapter.getDigitalInput(bLimit);
        return this;
    }
}
