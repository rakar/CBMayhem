package frc.team555.Mayhem.StateMachines;

import frc.team555.Mayhem.Data.ControlData;
import frc.team555.Mayhem.Data.RequestData;
import frc.team555.Mayhem.States.IntakeStates;
import org.montclairrobotics.cyborg.utils.CBStateMachine;

//TODO: Complete after Control Data has been added
public class IntakeStateMachine extends CBStateMachine<IntakeStates> {

    private RequestData requestData;
    private ControlData controlData;

    protected IntakeStateMachine(IntakeStates start) {
        super(start);
    }

    @Override
    protected void calcNextState() {

        boolean buttonPressed = requestData.shootCube;
        if(buttonPressed){
            nextState = IntakeStates.OUTTAKE;
        } else {
            nextState = IntakeStates.OUTTAKE;
        }
    }

    @Override
    protected void doCurrentState() {
        switch(currentState){

            case INTAKE:
                break;

            case OUTTAKE:
                break;
        }

    }

    @Override
    protected void doTransition() {

    }
}
