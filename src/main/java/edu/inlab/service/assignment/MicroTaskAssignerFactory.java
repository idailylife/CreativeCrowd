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
        if(type.equals("single")){
            return singlePagedMicroTaskAssigner;
        } else if(type.equals("random")){
            return randomMicroTaskAssigner;
        }
        return null;
    }

    public MicroTaskAssigner getAssigner(int code){
        switch (code){
            case 0:
                return singlePagedMicroTaskAssigner;
            case 1:
                return randomMicroTaskAssigner;
            default:
                return null;
        }
    }

    public void setSinglePagedMicroTaskAssigner(SinglePagedMicroTaskAssigner singlePagedMicroTaskAssigner) {
        this.singlePagedMicroTaskAssigner = singlePagedMicroTaskAssigner;
    }

    public void setRandomMicroTaskAssigner(RandomMicroTaskAssigner randomMicroTaskAssigner) {
        this.randomMicroTaskAssigner = randomMicroTaskAssigner;
    }
}
