package frc.team555.Mayhem;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.SPI;
import org.montclairrobotics.cyborg.CBHardwareAdapter;
import org.montclairrobotics.cyborg.Cyborg;
import org.montclairrobotics.cyborg.assemblies.CBDriveModule;
import org.montclairrobotics.cyborg.assemblies.CBSrxArrayController;
import org.montclairrobotics.cyborg.controllers.CBDifferentialDriveController;
import org.montclairrobotics.cyborg.controllers.CBDriveController;
import org.montclairrobotics.cyborg.devices.*;
import org.montclairrobotics.cyborg.mappers.CBArcadeDriveMapper;
import org.montclairrobotics.cyborg.utils.CB2DVector;
import org.montclairrobotics.cyborg.utils.CBEnums;

public class RobotCB extends Cyborg {

    private CBDeviceID


        // dt Motors
        dtFrontLeftMotor,
        dtFrontRightMotor,
        dtBackLeftMotor,
        dtBackRightMotor,

        // dt Encoders
        dtLeftEncoder,
        dtRightEncoder;

    @Override
    public void cyborgInit() {

        // Configure Hardware Adapter
        Cyborg.hardwareAdapter = new CBHardwareAdapter(this).setJoystickCount(3);
        CBHardwareAdapter hardwareAdapter = Cyborg.hardwareAdapter;

        // setup dt Motors
        dtFrontLeftMotor  = hardwareAdapter.add(new CBTalonSRX(1));
        dtFrontRightMotor = hardwareAdapter.add(new CBTalonSRX(7));
        dtBackLeftMotor   = hardwareAdapter.add(new CBTalonSRX(3));
        dtBackRightMotor  = hardwareAdapter.add(new CBTalonSRX(8));

        // setup dt encoders
        final double inchesPerTick = 1/((2280.0+2219.0)/2.0/4.0/12.0);
        dtLeftEncoder  = hardwareAdapter.add(new CBEncoder(1, 0, CounterBase.EncodingType.k4X, false, inchesPerTick));
        dtRightEncoder = hardwareAdapter.add(new CBEncoder(3, 2, CounterBase.EncodingType.k4X, false, inchesPerTick));

        // setup drive modules //TODO: Test Vector and Orientation
        CBDriveModule dtLeftModule  = new CBDriveModule(new CB2DVector(-1, 0), 0)
                .addSpeedControllerArray(new CBSrxArrayController()
                        .setDriveMode(CBEnums.CBDriveMode.Speed)
                        .addSpeedController(dtFrontLeftMotor)
                        .addSpeedController(dtBackLeftMotor)
                        .setEncoder(dtLeftEncoder));

        CBDriveModule dtRightModule  = new CBDriveModule(new CB2DVector(1, 0), 0)
                .addSpeedControllerArray(new CBSrxArrayController()
                        .setDriveMode(CBEnums.CBDriveMode.Speed)
                        .addSpeedController(dtFrontRightMotor)
                        .addSpeedController(dtBackRightMotor)
                        .setEncoder(dtRightEncoder));

        // setup dt controller
        CBDifferentialDriveController dtController =
                new CBDifferentialDriveController(this)
                    .addLeftDriveModule(dtLeftModule)
                    .addDriveModule(dtRightModule);

        // setup drive controller
        DriveControls driveControls = new DriveControls(hardwareAdapter);
        driveControls.setup();

        // setup teleop mapper //TODO: Tune Axis Scales
        this.addTeleOpMapper(new CBArcadeDriveMapper(this)
                .setAxes(driveControls.getForwardAxis(), null, driveControls.getRotationalAxis())
                .setAxisScales(0, 40, 90) // no strafe, 40 inches/second, 90 degrees/second //TODO: Tune Scales
        );

        // setup robot controller
        this.addRobotController(dtController);


    }

    @Override
    public void cyborgTeleopInit() {

    }

    @Override
    public void cyborgTestInit() {

    }

    @Override
    public void cyborgTestPeriodic() {

    }
}
