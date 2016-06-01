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


    public MicroTaskAssigner getAssigner(String type){
        MicroTaskAssigner assigner = null;
        if(type.equals("single")){
            assigner = singlePagedMicroTaskAssigner;
        } else if(type.equals("random")){
            assigner = randomMicroTaskAssigner;
        }
//        if(assigner != null){
//            assigner.parseParamsString(paramStr);
//        }
        return assigner;
    }

    public MicroTaskAssigner getAssigner(int code){
        MicroTaskAssigner assigner = null;
        switch (code){
            case 0:
                assigner = singlePagedMicroTaskAssigner;
                break;
            case 1:
                assigner = randomMicroTaskAssigner;
                break;
        }
//        if(assigner != null){
//            assigner.parseParamsString(paramStr);
//        }
        return assigner;
    }

    public void setSinglePagedMicroTaskAssigner(SinglePagedMicroTaskAssigner singlePagedMicroTaskAssigner) {
        this.singlePagedMicroTaskAssigner = singlePagedMicroTaskAssigner;
    }

    public void setRandomMicroTaskAssigner(RandomMicroTaskAssigner randomMicroTaskAssigner) {
        this.randomMicroTaskAssigner = randomMicroTaskAssigner;
    }
}
