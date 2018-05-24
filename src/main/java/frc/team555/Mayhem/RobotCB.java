package frc.team555.Mayhem;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.SPI;

import frc.team555.Mayhem.Behaviors.IntakeLiftBehavior;
import frc.team555.Mayhem.Behaviors.MainLiftBehavior;
import frc.team555.Mayhem.Data.ControlData;
import frc.team555.Mayhem.Data.RequestData;
import frc.team555.Mayhem.Mappers.OperatorMapper;
import frc.team555.Mayhem.Mappers.SensorMapper;

import org.montclairrobotics.cyborg.CBHardwareAdapter;
import org.montclairrobotics.cyborg.Cyborg;
import org.montclairrobotics.cyborg.assemblies.CBDriveModule;
import org.montclairrobotics.cyborg.assemblies.CBVictorArrayController;
import org.montclairrobotics.cyborg.behaviors.CBStdDriveBehavior;
import org.montclairrobotics.cyborg.controllers.CBDifferentialDriveController;
import org.montclairrobotics.cyborg.controllers.CBLiftController;
import org.montclairrobotics.cyborg.data.CBStdDriveControlData;
import org.montclairrobotics.cyborg.data.CBStdDriveRequestData;
import org.montclairrobotics.cyborg.devices.*;
import org.montclairrobotics.cyborg.mappers.CBArcadeDriveMapper;
import org.montclairrobotics.cyborg.mappers.CBMotorMonitorMapper;
import org.montclairrobotics.cyborg.utils.CB2DVector;
import org.montclairrobotics.cyborg.utils.CBEnums;
import org.montclairrobotics.cyborg.utils.CBPIDErrorCorrection;

public class RobotCB extends Cyborg {

    // constants
    // joystick ports
    private final int driveStickID = 0;
    private final int operatorStickID = 1;

    // button IDs
    private final int gyroLockButtonID = 1;

    private final int shootCubeButtonID = 1;
    private final int intakeLiftUpButtonID = 3;
    private final int intakeLiftDownButtonID = 2;
    private final int mainLiftUpButtonID = 4;
    private final int mainLiftDownButtonID = 5;


    private CBDeviceID
            // power dist board
            pdb,

    // driver controls
    driveXAxis, driveYAxis, gyroLockButton,

    // operator controls
    operXAxis, operYAxis, shootCubeButton, intakeLiftUpButton, intakeLiftDownButton, mainLiftUpButton, mainLiftDownButton,

    //navx
    navx,

    // dt Motors
    dtFrontLeftMotor, dtFrontRightMotor, dtBackLeftMotor, dtBackRightMotor,

    // dt Encoders
    dtLeftEncoder, dtRightEncoder,

    //lift Motors
    mainLiftMotorFront, mainLiftMotorBack, intakeLiftMotor,

    // lift encoders
    mainLiftEncoder, intakeLiftEncoder,

    // lift limit switches
    mainLiftLimit,

    // intake motors
    intakeLeftMotor, intakeRightMotor;


    @Override
    public void cyborgInit() {

        // data init
        requestData = new RequestData();
        controlData = new ControlData();
        requestData.driveData = new CBStdDriveRequestData();
        controlData.driveData = new CBStdDriveControlData();


        // Configure Hardware Adapter and Devices
        CBHardwareAdapter ha = new CBHardwareAdapter(this);
        hardwareAdapter = ha;

        pdb = ha.add(
                new CBPDB()
        );

        navx = ha.add(
                new CBNavX(SPI.Port.kMXP)
        );

        // setup drivetrain
        dtFrontLeftMotor = ha.add(
                new CBTalonSRX(1)
                        .setPowerSource(pdb, 0)
                        .setSpeedControllerFaultCriteria(
                                new CBSpeedControllerFaultCriteria()
                                        .setBasic(.1, 1, 30)
                        )
        );
        dtFrontRightMotor = ha.add(
                new CBTalonSRX(7)
                        .setPowerSource(pdb, 1)
                        .setSpeedControllerFaultCriteria(
                                new CBSpeedControllerFaultCriteria()
                                        .setBasic(.1, 1, 30)
                        )

        );
        dtBackLeftMotor = ha.add(
                new CBTalonSRX(3)
                        .setPowerSource(pdb, 2)
                        .setSpeedControllerFaultCriteria(
                                new CBSpeedControllerFaultCriteria()
                                        .setBasic(.1, 1, 30)
                        )
        );
        dtBackRightMotor = ha.add(
                new CBTalonSRX(8)
                        .setPowerSource(pdb, 3)
                        .setSpeedControllerFaultCriteria(
                                new CBSpeedControllerFaultCriteria()
                                        .setBasic(.1, 1, 30)
                        )

        );
        final double inchesPerTick = 96 / 4499;
        dtLeftEncoder = ha.add(
                new CBEncoder(1, 0, CounterBase.EncodingType.k4X, false, inchesPerTick)
        );
        dtRightEncoder = ha.add(
                new CBEncoder(3, 2, CounterBase.EncodingType.k4X, false, inchesPerTick)
        );


        // setup main lift
        mainLiftMotorFront = ha.add(
                new CBTalonSRX(4)
                        .setPowerSource(pdb, 4)
                        .setSpeedControllerFaultCriteria(
                                new CBSpeedControllerFaultCriteria()
                                        .setBasic(.1, 1, 20)
                        )
        );
        mainLiftMotorBack = ha.add(
                new CBTalonSRX(2)
                        .setPowerSource(pdb, 5)
                        .setSpeedControllerFaultCriteria(
                                new CBSpeedControllerFaultCriteria()
                                        .setBasic(.1, 1, 20)
                        )
        );
        mainLiftEncoder = ha.add(
                new CBEncoder(4, 5, CounterBase.EncodingType.k4X, false, 1)
        );
        mainLiftLimit = ha.add(
                new CBDigitalInput(9)
        );


        // setup intake lift
        intakeLiftMotor = ha.add(
                new CBTalonSRX(9)
                        .setPowerSource(pdb, 6)
                        .setSpeedControllerFaultCriteria(
                                new CBSpeedControllerFaultCriteria()
                                        .setBasic(.1, 1, 20)
                        )

        );
        intakeLiftEncoder = ha.add(
                new CBEncoder(intakeLiftMotor, FeedbackDevice.QuadEncoder, false, 1)
        );


        // setup intake motors
        intakeLeftMotor = ha.add(
                new CBTalonSRX(10)
                        .setPowerSource(pdb, 8)
                        .setSpeedControllerFaultCriteria(
                                new CBSpeedControllerFaultCriteria()
                                        .setBasic(.1, 1, 30)
                        )
        );
        intakeRightMotor = ha.add(
                new CBTalonSRX(5)
                        .setPowerSource(pdb, 9)
                        .setSpeedControllerFaultCriteria(
                                new CBSpeedControllerFaultCriteria()
                                        .setBasic(.1, 1, 30)
                        )
        );

        // driver controls
        driveXAxis = ha.add(
                new CBAxis(driveStickID, 1)
                        .setDeadzone(0.1)
                        .setScale(-1.0)
        );
        driveYAxis = ha.add(
                new CBAxis(driveStickID, 0)
                        .setDeadzone(0.1)
                        .setScale(-1.0)
        );
        gyroLockButton = hardwareAdapter.add(
                new CBButton(driveStickID, gyroLockButtonID)
        );

        // operator controls
        operXAxis = hardwareAdapter.add(
                new CBAxis(operatorStickID, 1)
                        .setDeadzone(0.1)
                        .setScale(-1.0)
        );

        operYAxis = hardwareAdapter.add(
                new CBAxis(operatorStickID, 0)
                        .setDeadzone(0.1)
                        .setScale(-1.0)
        );

        shootCubeButton = ha.add(
                new CBButton(operatorStickID, shootCubeButtonID)
        );
        intakeLiftUpButton = ha.add(
                new CBButton(operatorStickID, intakeLiftUpButtonID)
        );
        intakeLiftDownButton = ha.add(
                new CBButton(operatorStickID, intakeLiftDownButtonID)
        );
        mainLiftUpButton = ha.add(
                new CBButton(operatorStickID, mainLiftUpButtonID)
        );
        mainLiftDownButton = ha.add(
                new CBButton(operatorStickID, mainLiftDownButtonID)
        );

        // setup teleop mapper
        this.addTeleOpMapper(
                new CBArcadeDriveMapper(this)
                        .setAxes(driveYAxis, null, driveXAxis)
                        // TODO: the following line commented because the mode above was changed to Power (from speed)
                        // TODO: Tune Axis Scales
                        //.setAxisScales(0, 40, 90) // no strafe, 40 inches/second, 90 degrees/second
                        .setGyroLockButton(gyroLockButton)
        );

        // Here is a hack:
        // create a second "drivetrain" to operate the intake
        // because the work the same way...
        this.addTeleOpMapper(
                new CBArcadeDriveMapper(this)
                        .setAxes(operYAxis, null, operXAxis)
                        .setRequestData(((RequestData) requestData).intake)
        );

        this.addTeleOpMapper(
                new OperatorMapper(this)
                        .setIntakeLiftDownButton(intakeLiftDownButton)
                        .setIntakeLiftUpButton(intakeLiftUpButton)
                        .setMainLiftDownButton(mainLiftDownButton)
                        .setMainLiftUpButton(mainLiftUpButton)
                        .setShootCubeButton(shootCubeButton)
        );

        // setup sensor mapper(s)
        this.addCustomMapper(
                new SensorMapper(this)
                        .setMainLiftLimits(mainLiftEncoder, mainLiftLimit, intakeLiftEncoder)
        );

        this.addCustomMapper(
                new CBMotorMonitorMapper(this)
                        .add(dtFrontLeftMotor)
                        .add(dtFrontRightMotor)
                        .add(dtBackLeftMotor)
                        .add(dtBackRightMotor)
                        .add(mainLiftMotorBack)
                        .add(mainLiftMotorFront)
                        .add(intakeLiftEncoder)
                        .add(intakeLeftMotor)
                        .add(intakeRightMotor)
        );

        // setup robot controllers
        this.addRobotController(
                new CBDifferentialDriveController(this)
                        .addLeftDriveModule(
                                new CBDriveModule(
                                        new CB2DVector(-1, 0), 0)
                                        .addSpeedControllerArray(new CBVictorArrayController()
                                                .setDriveMode(CBEnums.CBDriveMode.Power)
                                                .addSpeedController(dtFrontLeftMotor)
                                                .addSpeedController(dtBackLeftMotor)
                                                .setEncoder(dtLeftEncoder)
                                                .setErrorCorrection(
                                                        new CBPIDErrorCorrection()
                                                                .setConstants(new double[]{1.5, 0, 0.0015}
                                                                )
                                                )
                                        )
                        )
                        .addRightDriveModule(
                                new CBDriveModule(new CB2DVector(1, 0), 180)
                                        .addSpeedControllerArray(new CBVictorArrayController()
                                                .setDriveMode(CBEnums.CBDriveMode.Power)
                                                .addSpeedController(dtFrontRightMotor)
                                                .addSpeedController(dtBackRightMotor)
                                                .setEncoder(dtRightEncoder)
                                                .setErrorCorrection(
                                                        new CBPIDErrorCorrection()
                                                                .setConstants(new double[]{1.5, 0, 0.0015}
                                                                )
                                                )
                                        )
                        )
        );

        // yup again with the second drivetrain for the intake
        this.addRobotController(
                new CBDifferentialDriveController(this)
                        .addLeftDriveModule(
                                new CBDriveModule(new CB2DVector(-6, 0), 0)
                                        .addSpeedControllerArray(
                                                new CBVictorArrayController()
                                                        .setDriveMode(CBEnums.CBDriveMode.Power)
                                                        .addSpeedController(intakeLeftMotor)
                                        )
                        )
                        .addRightDriveModule(
                                new CBDriveModule(new CB2DVector(6, 0), -180)
                                        .addSpeedControllerArray(
                                                new CBVictorArrayController()
                                                        .setDriveMode(CBEnums.CBDriveMode.Power)
                                                        .addSpeedController(intakeRightMotor)
                                        )
                        )
                        .setControlData(((ControlData) controlData).intake)
        );

        // main lift controller definition
        this.addRobotController(
                // hardware configurations are done here.
                // there are other "soft" configurations done in the mapper
                // that include margins (which trigger slow motion)
                // and in this case a encoder based top limit
                new CBLiftController(this)
                        // setData allows you to pick a CBLinearControllerData variable
                        // in controlData to use for this lift. There might be several
                        // lift controllers and each one would be controlled by a different
                        // CBLinearControllerData object in controlData.
                        .setData(((ControlData) controlData).mainLift)
                        // set a lower limit switch this is a hard limit
                        .setBottomLimit(mainLiftLimit)
                        // set the encoder for the lift
                        .setEncoder(mainLiftEncoder)
                        // attach a speed controller array to drive the lift
                        .setSpeedControllerArray(new CBVictorArrayController()
                                .addSpeedController(mainLiftMotorFront)
                                .setDriveMode(CBEnums.CBDriveMode.Power)
                        )
        );

        // intake lift controller definition
        this.addRobotController(
                // hardware configurations are done here.
                // there are other "soft" configurations done in the mapper
                // that include margins (which trigger slow motion)
                // and in this case a encoder based top limit
                new CBLiftController(this)
                        // setData allows you to pick a CBLinearControllerData variable
                        // in controlData to use for this lift. There might be several
                        // lift controllers and each one would be controlled by a different
                        // CBLinearControllerData object in controlData.
                        .setData(((ControlData) controlData).intakeLift)
                        // set a lower limit switch this is a hard limit
                        //.setBottomLimit(mainLiftLimit)
                        // set the encoder for the lift
                        .setEncoder(intakeLiftEncoder)
                        // attach a speed controller array to drive the lift
                        .setSpeedControllerArray(new CBVictorArrayController()
                                .addSpeedController(intakeLiftMotor)
                                .setDriveMode(CBEnums.CBDriveMode.Power)
                        )
        );


        // setup behaviors
        this.addBehavior(new CBStdDriveBehavior(this));
        this.addBehavior(new MainLiftBehavior(this));
        this.addBehavior(new IntakeLiftBehavior(this));

        // while this looks like a drivetrain, its an intake.
        this.addBehavior(
                new CBStdDriveBehavior(this)
                        .setRequestData(((RequestData) requestData).intake)
                        .setControlData(((ControlData) controlData).intake)
        );

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
