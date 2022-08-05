package com.distributedSystems.ReplicationJarProject.BackUpCoordinator;

import javax.swing.plaf.nimbus.State;
import java.io.Serializable;

public class GlobalRequest implements Serializable {

    private boolean isCommit; // Commit or abort
    private StateRegister stateRegister;

    public boolean isCommit() {
        return isCommit;
    }

    public StateRegister getStateRegister() {
        return stateRegister;
    }

    public void setStateRegister(StateRegister stateRegister) {
        this.stateRegister = stateRegister;
    }

    public void setCommit(boolean commit) {
        isCommit = commit;

    }



    public GlobalRequest (boolean isCommit){
        this.isCommit = isCommit;
        stateRegister = null;
    }

    public GlobalRequest (boolean isCommit, StateRegister lastState){
        this.isCommit = isCommit;
        stateRegister = lastState;
    }


    @Override
    public String toString() {
        return "{ Commit: "+isCommit+"}";
    }

}
