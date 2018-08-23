package frc.team555.Mayhem.Mappers;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team555.Mayhem.Data.RequestData;
import frc.team555.Mayhem.RobotCB;
import org.montclairrobotics.cyborg.CBGameMode;
import org.montclairrobotics.cyborg.Cyborg;
import org.montclairrobotics.cyborg.devices.CBDashboardChooser;
import org.montclairrobotics.cyborg.devices.CBDigitalInput;
import org.montclairrobotics.cyborg.devices.CBEncoder;
import org.montclairrobotics.cyborg.devices.CBNavX;
import org.montclairrobotics.cyborg.mappers.CBCustomMapper;

import static org.montclairrobotics.cyborg.Cyborg.gameMode;

public class SensorMapper extends CBCustomMapper {

    private RequestData rd;

    //CBNavXYawSource navXYawSource;

    private CBEncoder mainLiftEncoder;
    private CBDigitalInput mainLiftLimit;
    private CBEncoder intakeLiftEncoder;
    private CBEncoder drivetrainLeftEncoder;
    private CBEncoder drivetrainRightEncoder;
    private CBDashboardChooser<Integer> fieldPosition;
    private CBDashboardChooser<String> autoSelection;
    private CBNavX navx;

    @SuppressWarnings("unchecked")
    public SensorMapper(Cyborg robot) {
        super(robot);
        this.robot = robot;
        rd = (RequestData)Cyborg.requestData;

        mainLiftEncoder = Cyborg.hardwareAdapter.getEncoder(RobotCB.mainLiftEncoder);
        mainLiftLimit = Cyborg.hardwareAdapter.getDigitalInput(RobotCB.mainLiftLimit);
        intakeLiftEncoder = Cyborg.hardwareAdapter.getEncoder(RobotCB.intakeLiftEncoder);
        drivetrainLeftEncoder = Cyborg.hardwareAdapter.getEncoder(RobotCB.dtLeftEncoder);
        drivetrainRightEncoder = Cyborg.hardwareAdapter.getEncoder(RobotCB.dtRightEncoder);
        fieldPosition = (CBDashboardChooser<Integer>)Cyborg.hardwareAdapter.getDevice(RobotCB.fieldPosition);
        autoSelection = (CBDashboardChooser<String>)Cyborg.hardwareAdapter.getDevice(RobotCB.autoSelection);
        navx = Cyborg.hardwareAdapter.getNavX(RobotCB.navx);
    }

    @Override
    public void update() {
        //stdDriveRequestData.gyroLockValue = navXYawSource.get();

        rd.mainLiftEncoderValue = mainLiftEncoder.getDistance();
        rd.mainLiftLimitValue = mainLiftLimit.get();

        rd.drivetrainLeftEncoderValue = drivetrainLeftEncoder.getDistance();
        rd.drivetrainRightEncoderValue = drivetrainRightEncoder.getDistance();
        rd.drivetrainAverageEncoderValue = (rd.drivetrainLeftEncoderValue+rd.drivetrainRightEncoderValue)/2.0;

        rd.robotAngle = navx.getYaw();

        // FMS Data, Driver Station (Just to make things interesting lets try this pre-game only)
        if ((gameMode & CBGameMode.preGame)!=0) {
            rd.gameSpecificMessage = DriverStation.getInstance().getGameSpecificMessage();
            rd.fieldPosition = fieldPosition.getSelected();
            rd.autoSelection = autoSelection.getSelected();
        }

        SmartDashboard.putBoolean("mainLiftLimit", rd.mainLiftLimitValue);
        SmartDashboard.putNumber("mainLiftEncoder", rd.mainLiftEncoderValue);
        SmartDashboard.putNumber("intakeLiftEncoder", intakeLiftEncoder.getDistance());
        SmartDashboard.putNumber("drivetrainLeftEncoder", rd.drivetrainLeftEncoderValue);
        SmartDashboard.putNumber("drivetrainRightEncoder", rd.drivetrainRightEncoderValue);
        SmartDashboard.putString("AutoSelectedEcho", rd.autoSelection);
        SmartDashboard.putNumber("FieldPositionEcho", rd.fieldPosition);
    }
}
