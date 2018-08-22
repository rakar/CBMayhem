package frc.team555.Mayhem.Data;

import org.montclairrobotics.cyborg.data.CBControlData;
import org.montclairrobotics.cyborg.data.CBLiftControllerData;
import org.montclairrobotics.cyborg.data.CBStdDriveControlData;

public class ControlData extends CBControlData {

    //public CBTriState intakeStatus;
    //public CBTriState intakeLiftStatus;
    //public CBTriState mainLiftStatus;

    public CBLiftControllerData mainLift = new CBLiftControllerData();
    public CBLiftControllerData intakeLift = new CBLiftControllerData();

    // intake, yeah intake
    public CBStdDriveControlData intake = new CBStdDriveControlData();
}
