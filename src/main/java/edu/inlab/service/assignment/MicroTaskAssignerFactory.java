package edu.inlab.service.assignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by inlab-dell on 2016/5/31.
 */

@Service("microTaskAssignFactory")
public class MicroTaskAssignerFactory {
    @Autowired
    private MicroTaskAssigner singlePagedMicroTaskAssigner;

    @Autowired
    private MicroTaskAssigner randomMicroTaskAssigner;

    @Autowired
    private MicroTaskAssigner sequenceMicroTaskAssigner;

    @Autowired
    private MicroTaskAssigner singlePagedRandomTaskAssigner;


    public MicroTaskAssigner getAssigner(int code){
        MicroTaskAssigner assigner = null;
        switch (code){
            case MicroTaskAssigner.TASK_ASSIGN_SINGLE:
                assigner = singlePagedMicroTaskAssigner;
                break;
            case MicroTaskAssigner.TASK_ASSIGN_RANDOM:
                assigner = randomMicroTaskAssigner;
                break;
            case MicroTaskAssigner.TASK_ASSIGN_SEQUENCE:
                assigner = sequenceMicroTaskAssigner;
                break;
            case MicroTaskAssigner.TASK_ASSIGN_SINGLE_RANDOM:
                assigner = singlePagedRandomTaskAssigner;
                break;
        }

        return assigner;
    }

    public void setSinglePagedMicroTaskAssigner(SinglePagedMicroTaskAssigner singlePagedMicroTaskAssigner) {
        this.singlePagedMicroTaskAssigner = singlePagedMicroTaskAssigner;
    }

    public void setRandomMicroTaskAssigner(RandomMicroTaskAssigner randomMicroTaskAssigner) {
        this.randomMicroTaskAssigner = randomMicroTaskAssigner;
    }

    public void setSequenceMicroTaskAssigner(MicroTaskAssigner sequenceMicroTaskAssigner) {
        this.sequenceMicroTaskAssigner = sequenceMicroTaskAssigner;
    }

    public void setSinglePagedRandomTaskAssigner(MicroTaskAssigner singlePagedRandomTaskAssigner) {
        this.singlePagedRandomTaskAssigner = singlePagedRandomTaskAssigner;
    }
}
