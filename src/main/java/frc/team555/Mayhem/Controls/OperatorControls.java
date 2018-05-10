package frc.team555.Mayhem.Controls;

import org.montclairrobotics.cyborg.CBHardwareAdapter;
import org.montclairrobotics.cyborg.devices.CBAxis;
import org.montclairrobotics.cyborg.devices.CBButton;
import org.montclairrobotics.cyborg.devices.CBDevice;
import org.montclairrobotics.cyborg.devices.CBDeviceID;

public class OperatorControls {

    // joystick port
    private final int operatorStickID = 0;

    // axis declarations
    private CBDeviceID xAxis, yAxis;

    // hardware adapter for CB
    CBHardwareAdapter hardwareAdapter;

    // button IDs
    private final int shootCubeButtonID      = 1;
    private final int intakeLiftUpButtonID   = 2;
    private final int intakeLiftDownButtonID = 3;
    private final int mainLiftUpButtonID     = 4;
    private final int mainLiftDownButtonID   = 5;

    // button declarations
    private CBDeviceID shootCubeButton;
    private CBDeviceID intakeLiftUpButton;
    private CBDeviceID intakeLiftDownButton;
    private CBDeviceID mainLiftUpButton;
    private CBDeviceID mainLiftDownButton;

    public OperatorControls(CBHardwareAdapter hardwareAdapter){
        this.hardwareAdapter = hardwareAdapter;
    }

    public void setup(){
        xAxis = hardwareAdapter.add(new CBAxis(operatorStickID, 1)
                .setDeadzone(0.1)
                .setScale(-1.0));

        yAxis = hardwareAdapter.add(new CBAxis(operatorStickID, 0)
                .setDeadzone(0.1)
                .setScale(-1.0));

        shootCubeButton      = hardwareAdapter.add(new CBButton(operatorStickID, shootCubeButtonID));
        intakeLiftUpButton   = hardwareAdapter.add(new CBButton(operatorStickID, intakeLiftUpButtonID));
        intakeLiftDownButton = hardwareAdapter.add(new CBButton(operatorStickID, intakeLiftDownButtonID));
        mainLiftUpButton     = hardwareAdapter.add(new CBButton(operatorStickID, mainLiftUpButtonID));
        mainLiftDownButton   = hardwareAdapter.add(new CBButton(operatorStickID, mainLiftDownButtonID));
    }

    public CBDeviceID getForwardAxis(){
        return xAxis;
    }

    public CBDeviceID getRotationalAxis(){
        return yAxis;
    }

    public CBDeviceID getShootCubeButton(){
        return shootCubeButton;
    }

    public CBDeviceID getIntakeLiftUpButton(){
        return intakeLiftUpButton;
    }

    public CBDeviceID getIntakeLiftDownButton(){
        return intakeLiftDownButton;
    }

    public CBDeviceID getMainLiftUpButton(){
        return mainLiftUpButton;
    }

    public CBDeviceID getMainLiftDownButton(){
        return mainLiftDownButton;
    }
}
