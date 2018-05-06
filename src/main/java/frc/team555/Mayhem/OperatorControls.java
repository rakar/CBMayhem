package frc.team555.Mayhem;

import org.montclairrobotics.cyborg.CBHardwareAdapter;
import org.montclairrobotics.cyborg.devices.CBAxis;
import org.montclairrobotics.cyborg.devices.CBDeviceID;

public class OperatorControls {

    // joystick port
    public final int operatorStickID = 0;

    // button IDs
    public final int shootCube      = 1;
    public final int intakeLiftUp   = 2;
    public final int intakeLiftDown = 3;
    public final int mainLiftUp     = 4;
    public final int mainLiftDown   = 5;

    CBDeviceID xAxis, yAxis;
    CBHardwareAdapter hardwareAdapter;

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
    }

    public CBDeviceID getForwardAxis(){
        return xAxis;
    }

    public CBDeviceID getRotationalAxis(){
        return yAxis;
    }
}
