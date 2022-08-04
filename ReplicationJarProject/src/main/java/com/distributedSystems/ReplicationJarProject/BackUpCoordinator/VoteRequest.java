package com.distributedSystems.ReplicationJarProject.BackUpCoordinator;

import java.io.Serializable;

public class VoteRequest implements Serializable {

    int commit;
    int abort;

    public int getCommit() {
        return commit;
    }

    public void setCommit(int commit) {
        this.commit = commit;
    }

    public int getAbort() {
        return abort;
    }

    public void setAbort(int abort) {
        this.abort = abort;
    }

    public VoteRequest() {
        this.commit = 0;
        this.abort = 0;

    }

    @Override
    public String toString() {
        return "{ commit: "+commit+", \nabort: "+abort+" }";
    }
}
