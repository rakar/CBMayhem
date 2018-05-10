package frc.team555.Mayhem.Controls;

import org.montclairrobotics.cyborg.CBHardwareAdapter;
import org.montclairrobotics.cyborg.devices.CBAxis;
import org.montclairrobotics.cyborg.devices.CBButton;
import org.montclairrobotics.cyborg.devices.CBDeviceID;

public class DriveControls {

    // joystick port
    private final int driveStickID = 0;

    // Axis declarations
    private CBDeviceID xAxis, yAxis;

    // button IDs
    private final int gyroLockButtonID = 1;

    // button declarations
    private CBDeviceID gyroLockButton;

    // hardware adapter for CB
    private CBHardwareAdapter hardwareAdapter;

    public DriveControls(CBHardwareAdapter hardwareAdapter){
        this.hardwareAdapter = hardwareAdapter;
    }

    public void setup(){
        xAxis = hardwareAdapter.add(new CBAxis(driveStickID, 1)
                .setDeadzone(0.1)
                .setScale(-1.0));

        yAxis = hardwareAdapter.add(new CBAxis(driveStickID, 0)
                .setDeadzone(0.1)
                .setScale(-1.0));

        gyroLockButton = hardwareAdapter.add(new CBButton(driveStickID, gyroLockButtonID));

    }

    public CBDeviceID getForwardAxis(){
        return xAxis;
    }

    public CBDeviceID getRotationalAxis(){
        return yAxis;
    }

    public CBDeviceID getGyroLockButton(){
        return gyroLockButton;
    }
}
