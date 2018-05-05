package frc.team555.Mayhem;

import org.montclairrobotics.cyborg.CBHardwareAdapter;
import org.montclairrobotics.cyborg.devices.CBAxis;
import org.montclairrobotics.cyborg.devices.CBDeviceID;

public class DriveControls {

    private final int driveStickID = 0;

    CBDeviceID xAxis, yAxis;
    CBHardwareAdapter hardwareAdapter;

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
    }

    public CBDeviceID getForwardAxis(){
        return xAxis;
    }

    public CBDeviceID getRotationalAxis(){
        return yAxis;
    }
}
