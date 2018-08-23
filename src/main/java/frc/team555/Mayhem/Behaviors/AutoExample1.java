package frc.team555.Mayhem.Behaviors;


import frc.team555.Mayhem.Data.RequestData;
import org.montclairrobotics.cyborg.Cyborg;
import org.montclairrobotics.cyborg.behaviors.CBAutonomous;
import org.montclairrobotics.cyborg.data.CBStdDriveRequestData;
import org.montclairrobotics.cyborg.utils.CB2DVector;
import org.montclairrobotics.cyborg.utils.CBStateMachine;
import org.montclairrobotics.cyborg.utils.CBTarget1D;

public class AutoExample1 extends CBAutonomous {
    RequestData rd;
    double drivetrainAverageEncoderValueAtTransition;
    final static int mainDriveDist = 1000; // this is a bad value!
    final static int littleDriveDist = 500; // this is a bad value!
    final static int mainLiftDist = 1000; // this is a bad value!
    CBTarget1D leftTarget = new CBTarget1D().setTarget(90,4);
    CBTarget1D rightTarget = new CBTarget1D().setTarget(-90,4);
    CBStdDriveRequestData drd = (CBStdDriveRequestData)rd.driveData;
    CBStdDriveRequestData intake = (CBStdDriveRequestData)rd.intake;
    AutoStateMachine stateMachine = new AutoStateMachine();

    enum AutoStates {Start, DriveAndLift, TurnLeft, TurnRight, DriveALittle, Eject, Done};

    private class AutoStateMachine extends CBStateMachine<AutoStates> {
        int cycleCheck = 0;

        public AutoStateMachine() {
            super(AutoStates.Start);
            setLoopMode(CBStateMachineLoopMode.Looping);
        }

        @Override
        public void calcNextState() {
            //SmartDashboard.putString("calc Next State:", currentState.name());

            cycleCheck++;

            switch (currentState) {
                case Start:
                    if((rd.fieldPosition==1 && rd.nearSwitchSide=='L') || (rd.fieldPosition==3 && rd.nearSwitchSide=='R')) {
                        nextState = AutoStates.DriveAndLift;
                    } else {
                        nextState = AutoStates.Done;
                    }
                    break;
                case DriveAndLift:
                    if((rd.drivetrainAverageEncoderValue-drivetrainAverageEncoderValueAtTransition)>mainDriveDist
                            && rd.mainLiftEncoderValue>mainLiftDist) {
                        if(rd.fieldPosition==1) {
                            nextState = AutoStates.TurnRight;
                        } else {
                            nextState = AutoStates.TurnLeft;
                        }
                    }
                    break;
                case TurnLeft:
                    if(leftTarget.isOnTarget()) {
                        nextState = AutoStates.DriveALittle;
                    }
                    break;
                case TurnRight:
                    if(rightTarget.isOnTarget()) {
                        nextState = AutoStates.DriveALittle;
                    }
                    break;
                case DriveALittle:
                    if((rd.drivetrainAverageEncoderValue-drivetrainAverageEncoderValueAtTransition)>littleDriveDist) {
                        nextState = AutoStates.Eject;
                    }
                    break;
                case Eject:
                    if(secondsInState>3) {
                        nextState = AutoStates.Done;
                    }
                    break;
                case Done:
                    break;
            }
        }

        @Override
        public void doTransition() {
            drd.active=true;
            drd.direction.setXY(0,0); // half speed forward (assuming power mode)
            drd.gyroLockValue = 0;
            drd.gyroLockActive = false;
            drd.rotation = 0;
            rd.mainLiftUp = false;
            rd.mainLIftDown = false;
            drivetrainAverageEncoderValueAtTransition = rd.drivetrainAverageEncoderValue;
        }

        @Override
        protected void doCurrentState() {
            //SmartDashboard.putString("do Current State:", currentState.name());
            switch (currentState) {

                case Start:
                    drd.active = true;
                    drd.direction = new CB2DVector(0,0);

                    intake.active=true;
                    intake.direction.setXY(0,-.20); // bad value - Draw cube in?
                    intake.rotation = 0;

                    break;
                case DriveAndLift:
                    if((rd.drivetrainAverageEncoderValue-drivetrainAverageEncoderValueAtTransition)>mainDriveDist) {
                        drd.active=true;
                        drd.direction.setXY(0,.5); // bad value - half speed forward (assuming power mode)
                        drd.gyroLockValue = 0;
                        drd.gyroLockActive = true;
                        drd.rotation = 0;
                    }
                    else {
                        drd.active=true;
                        drd.direction.setXY(0,0); // half speed forward (assuming power mode)
                        drd.gyroLockValue = 0;
                        drd.gyroLockActive = false;
                        drd.rotation = 0;
                    }
                    if (rd.mainLiftEncoderValue>mainLiftDist) {
                        rd.mainLiftUp = true;
                        rd.mainLIftDown = false;
                    } else {
                        rd.mainLiftUp = false;
                        rd.mainLIftDown = false;
                    }
                    break;
                case TurnLeft:
                    drd.active=true;
                    drd.direction.setXY(0,0); // half speed forward (assuming power mode)
                    drd.gyroLockValue = 0;
                    drd.gyroLockActive = false;
                    drd.rotation = .3; // bad value
                    break;
                case TurnRight:
                    drd.active=true;
                    drd.direction.setXY(0,0); // half speed forward (assuming power mode)
                    drd.gyroLockValue = 0;
                    drd.gyroLockActive = false;
                    drd.rotation = -.3; // bad value
                    break;
                case DriveALittle:
                    drd.active=true;
                    drd.direction.setXY(0,.25); // bad value - quarter speed forward (assuming power mode)
                    drd.gyroLockValue = 0;
                    drd.gyroLockActive = false;
                    drd.rotation = 0;
                    break;
                case Eject:
                    rd.shootCube = true;
                    break;
                case Done:
                    rd.shootCube = false;
                    break;
            }
        }
    }


    public AutoExample1(Cyborg robot) {
        super(robot);
        rd = ((RequestData)Cyborg.requestData);
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {
        stateMachine.update();
    }
}
