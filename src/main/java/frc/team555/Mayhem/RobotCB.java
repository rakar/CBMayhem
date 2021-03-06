package frc.team555.Mayhem;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.SPI;
import frc.team555.Mayhem.Behaviors.AutoExample1;
import frc.team555.Mayhem.Behaviors.IntakeLiftBehavior;
import frc.team555.Mayhem.Behaviors.MainLiftBehavior;
import frc.team555.Mayhem.Behaviors.OperatorBehavior;
import frc.team555.Mayhem.Data.ControlData;
import frc.team555.Mayhem.Data.RequestData;
import frc.team555.Mayhem.Mappers.OperatorMapper;
import frc.team555.Mayhem.Mappers.SensorMapper;
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

    // this was made class level to allow for access
    // from methods. hardwareAdapter is the main class
    // object, so this will likely be put back in
    // defineDevices, but may be a helpful shortcut
    // ???
    // oh just bite the bullet and do the right thing.
    //CBHardwareAdapter ha;

    //
    // This has been changed to "public static" from "private" to allow
    // for direct access in cases of NON-reusable mappers/behaviors
    // to avoid lengthy init code, that will always be robot specific
    // anyway. Reusable code, should of course use setter functions to
    // attach to devices.
    //
    public static CBDeviceID
    pdb, navx,

    // driver controls
    driveXAxis, driveYAxis, gyroLockButton,

    // operator controls
    operXAxis, operYAxis, shootCubeButton, intakeLiftUpButton, intakeLiftDownButton, mainLiftUpButton, mainLiftDownButton,

    // drivetrain Motors
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
    intakeLeftMotor, intakeRightMotor,

    // dashboard choosers
    fieldPosition, autoSelection
    ;

    public RobotCB() {
    }

    @Override
    public void cyborgInit() {

        // data init
        requestData = new RequestData();
        controlData = new ControlData();
        requestData.driveData = new CBStdDriveRequestData();
        controlData.driveData = new CBStdDriveControlData();

        defineDevices();
        defineMappers();
        defineControllers();
        defineBehaviors();
    }

    @Override
    public void cyborgDisabledInit() {

    }

    @Override
    public void cyborgAutonomousInit() {
        switch(((RequestData)requestData).autoSelection) {
            case "auto1":
                this.addAutonomous(new AutoExample1(this));
                break;
            default:
                // just say no
                break;
        }
    }

    @Override
    public void cyborgTeleopInit() {

    }

    @Override
    public void cyborgTestInit() {

    }

    private void defineDevices() {
        // Configure Hardware Adapter and Devices
        hardwareAdapter = new CBHardwareAdapter(this);
        //ha = hardwareAdapter;

        pdb = hardwareAdapter.add(
                new CBPDB()
        );

        navx = hardwareAdapter.add(
                new CBNavX(SPI.Port.kMXP)
        );

        // setup drivetrain
        dtFrontLeftMotor = hardwareAdapter.add(
                new CBTalonSRX(1)
                        .setDeviceName("DriveTrain", "FrontLeft")
                        .setPowerSource(pdb, 0)
                        .setSpeedControllerFaultCriteria(
                                new CBSpeedControllerFaultCriteria()
                                        .setBasic(.1, 1, 30)
                        )
        );
        dtFrontRightMotor = hardwareAdapter.add(
                new CBTalonSRX(7)
                        .setDeviceName("DriveTrain", "FrontRight")
                        .setPowerSource(pdb, 1)
                        .setSpeedControllerFaultCriteria(
                                new CBSpeedControllerFaultCriteria()
                                        .setBasic(.1, 1, 30)
                        )
        );
        dtBackLeftMotor = hardwareAdapter.add(
                new CBTalonSRX(3)
                        .setDeviceName("DriveTrain", "BackLeft")
                        .setPowerSource(pdb, 2)
                        .setSpeedControllerFaultCriteria(
                                new CBSpeedControllerFaultCriteria()
                                        .setBasic(.1, 1, 30)
                        )
        );
        dtBackRightMotor = hardwareAdapter.add(
                new CBTalonSRX(8)
                        .setDeviceName("DriveTrain", "BackRight")
                        .setPowerSource(pdb, 3)
                        .setSpeedControllerFaultCriteria(
                                new CBSpeedControllerFaultCriteria()
                                        .setBasic(.1, 1, 30)
                        )
        );
        final double inchesPerTick = 96 / 4499;
        dtLeftEncoder = hardwareAdapter.add(
                new CBEncoder(1, 0, CounterBase.EncodingType.k4X, false, inchesPerTick)
        );
        dtRightEncoder = hardwareAdapter.add(
                new CBEncoder(3, 2, CounterBase.EncodingType.k4X, false, inchesPerTick)
        );


        // setup main lift
        mainLiftMotorFront = hardwareAdapter.add(
                new CBTalonSRX(4)
                        .setDeviceName("MainLift", "Front")
                        .setPowerSource(pdb, 4)
                        .setSpeedControllerFaultCriteria(
                                new CBSpeedControllerFaultCriteria()
                                        .setBasic(.1, 1, 20)
                        )
        );
        mainLiftMotorBack = hardwareAdapter.add(
                new CBTalonSRX(2)
                        .setDeviceName("MainLift", "Back")
                        .setPowerSource(pdb, 5)
                        .setSpeedControllerFaultCriteria(
                                new CBSpeedControllerFaultCriteria()
                                        .setBasic(.1, 1, 20)
                        )
        );
        mainLiftEncoder = hardwareAdapter.add(
                new CBEncoder(4, 5, CounterBase.EncodingType.k4X, false, 1)
        );
        mainLiftLimit = hardwareAdapter.add(
                new CBDigitalInput(9)
        );


        // setup intake lift
        intakeLiftMotor = hardwareAdapter.add(
                new CBTalonSRX(9)
                        .setDeviceName("Intake", "LiftMotor")
                        .setPowerSource(pdb, 6)
                        .setSpeedControllerFaultCriteria(
                                new CBSpeedControllerFaultCriteria()
                                        .setBasic(.1, 1, 20)
                        )
        );
        intakeLiftEncoder = hardwareAdapter.add(
                new CBEncoder(intakeLiftMotor, FeedbackDevice.QuadEncoder, false, 1)
        );


        // setup intake motors
        intakeLeftMotor = hardwareAdapter.add(
                new CBTalonSRX(10)
                        .setDeviceName( "Intake", "LeftMotor")
                        .setPowerSource(pdb, 8)
                        .setSpeedControllerFaultCriteria(
                                new CBSpeedControllerFaultCriteria()
                                        .setBasic(.1, 1, 30)
                        )
        );
        intakeRightMotor = hardwareAdapter.add(
                new CBTalonSRX(5)
                        .setDeviceName("Intake", "RightMotor")
                        .setPowerSource(pdb, 9)
                        .setSpeedControllerFaultCriteria(
                                new CBSpeedControllerFaultCriteria()
                                        .setBasic(.1, 1, 30)
                        )
        );

        // driver controls
        driveXAxis = hardwareAdapter.add(
                new CBAxis(driveStickID, 1)
                        .setDeadzone(0.1)
                        .setScale(-1.0)
        );
        driveYAxis = hardwareAdapter.add(
                new CBAxis(driveStickID, 0)
                        .setDeadzone(0.1)
                        .setScale(-1.0)
        );
        gyroLockButton = hardwareAdapter.add(
                new CBButton(driveStickID, 1)
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

        shootCubeButton = hardwareAdapter.add(
                new CBButton(operatorStickID, 1)
        );
        intakeLiftUpButton = hardwareAdapter.add(
                new CBButton(operatorStickID, 3)
        );
        intakeLiftDownButton = hardwareAdapter.add(
                new CBButton(operatorStickID, 2)
        );
        mainLiftUpButton = hardwareAdapter.add(
                new CBButton(operatorStickID, 4)
        );
        mainLiftDownButton = hardwareAdapter.add(
                new CBButton(operatorStickID, 5)
        );

        // dashboard elements
        fieldPosition = hardwareAdapter.add(
                new CBDashboardChooser<Integer>("Field Position")
                        .addChoice("left",1)
                        .addChoice("center", 2)
                        .addChoice("right",3)
        );
        autoSelection = hardwareAdapter.add(
                new CBDashboardChooser<String>("Autonomous")
                        .addDefault("NONE!!!", "none")
                        .addChoice("Auto1", "auto1")

        );
    }

    private void defineMappers() {
        // setup teleop mappers
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
        // because they work the same way...
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
        this.addSensorMapper(
                new SensorMapper(this)
        );

        this.addSensorMapper(
                new CBMotorMonitorMapper(this)
                        .add(dtFrontLeftMotor)
                        .add(dtFrontRightMotor)
                        .add(dtBackLeftMotor)
                        .add(dtBackRightMotor)
                        .add(mainLiftMotorBack)
                        .add(mainLiftMotorFront)
                        .add(intakeLiftMotor)
        );
    }

    private void defineControllers() {
        // setup robot controllers
        this.addRobotController(
                new CBDifferentialDriveController(this)
                        .addLeftDriveModule(
                                new CBDriveModule(
                                        new CB2DVector(-1, 0), 0)
                                        .addSpeedControllerArray(
                                                new CBVictorArrayController()
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
                                        .addSpeedControllerArray(
                                                new CBVictorArrayController()
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
                        .setSpeedControllerArray(
                                new CBVictorArrayController()
                                        .addSpeedController(mainLiftMotorFront)
                                        .setDriveMode(CBEnums.CBDriveMode.Power)
                        )
        );

        // intake lift controller definition
        this.addRobotController(
                // hardware configurations are done here.
                // there are other "soft" configurations done in the mapper
                // that include margins (which trigger slow motion)
                // and in this case an encoder based top limit
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
                        .setSpeedControllerArray(
                                new CBVictorArrayController()
                                        .addSpeedController(intakeLiftMotor)
                                        .setDriveMode(CBEnums.CBDriveMode.Power)
                        )
        );
    }

    private void defineBehaviors() {
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

        this.addBehavior(new OperatorBehavior(this));
    }
}
