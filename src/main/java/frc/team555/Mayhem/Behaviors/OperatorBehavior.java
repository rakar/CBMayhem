package frc.team555.Mayhem.Behaviors;

import frc.team555.Mayhem.Data.ControlData;
import frc.team555.Mayhem.Data.RequestData;
import org.montclairrobotics.cyborg.Cyborg;
import org.montclairrobotics.cyborg.behaviors.CBBehavior;
import org.montclairrobotics.cyborg.data.CBStdDriveControlData;

public class OperatorBehavior  extends CBBehavior {

    private RequestData rd = (RequestData) Cyborg.requestData;
    private ControlData cd = (ControlData) Cyborg.controlData;
    private CBStdDriveControlData intake = cd.intake;

    public OperatorBehavior(Cyborg robot) {
        super(robot);
    }

    @Override
    public void update() {
        super.update();

        if(rd.shootCube) {
            intake.active=true;
            intake.direction.setXY(0,1); // bad value - shoot cube?
            intake.rotation = 0;
        } else {
            if(!intake.active) {
                intake.active = true;
                intake.direction.setXY(0, -.20); // bad value - Draw cube in?
                intake.rotation = 0;
            }
        }
    }
}
