package frc.team555.Mayhem.Behaviors;

import frc.team555.Mayhem.Data.ControlData;
import frc.team555.Mayhem.Data.RequestData;
import org.montclairrobotics.cyborg.Cyborg;
import org.montclairrobotics.cyborg.behaviors.CBBehavior;


public class IntakeLiftBehavior extends CBBehavior {
    private RequestData rd = (RequestData)Cyborg.requestData;
    private ControlData cd = (ControlData)Cyborg.controlData;

    public IntakeLiftBehavior(Cyborg robot) {
        super(robot);

        // These are essentially static configurations of the lift
        // they can be set once and are generally never changed
        cd.intakeLift.normDown = -0.5;
        cd.intakeLift.slowDown = -0.1;
        cd.intakeLift.normUp = 0.5;
        cd.intakeLift.slowUp = 0.25;

        cd.intakeLift.target.setActive(false);
        cd.intakeLift.bottomMargin.setTarget(20000,10);
        cd.intakeLift.bottomMargin.setActive(true);
        cd.intakeLift.topMargin.setTarget(120000,10);
        cd.intakeLift.topMargin.setActive(true);
        cd.intakeLift.topEncoderLimit.setTarget(140000,0);
        cd.intakeLift.topEncoderLimit.setActive(true);
    }

    @Override
    public void update() {
        super.update();

        cd.intakeLift.requestUp = rd.intakeLiftUp;
        cd.intakeLift.requestDown = rd.intakeLiftDown;
    }
}
