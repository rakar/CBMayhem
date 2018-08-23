package frc.team555.Mayhem.Behaviors;

import frc.team555.Mayhem.Data.ControlData;
import frc.team555.Mayhem.Data.RequestData;
import org.montclairrobotics.cyborg.Cyborg;
import org.montclairrobotics.cyborg.behaviors.CBBehavior;


public class MainLiftBehavior extends CBBehavior {
    private RequestData rd;
    private ControlData cd;

    public MainLiftBehavior(Cyborg robot) {
        super(robot);

        rd = (RequestData)Cyborg.requestData;
        cd = (ControlData)Cyborg.controlData;

        // These are essentially static configurations of the lift
        // they can be set once and are generally never changed
        cd.mainLift.normDown = -0.5;
        cd.mainLift.slowDown = -0.2;
        cd.mainLift.normUp = 1.0;
        cd.mainLift.slowUp = 0.5;

        cd.mainLift.target.setActive(false);
        cd.mainLift.bottomMargin.setTarget(1000,10);
        cd.mainLift.bottomMargin.setActive(true);
        cd.mainLift.topMargin.setTarget(4000,10);
        cd.mainLift.topMargin.setActive(true);
        cd.mainLift.topEncoderLimit.setTarget(5000,0);
        cd.mainLift.topEncoderLimit.setActive(true);
    }

    @Override
    public void update() {
        super.update();

        cd.mainLift.requestUp = rd.mainLiftUp;
        cd.mainLift.requestDown = rd.mainLIftDown;
    }
}
