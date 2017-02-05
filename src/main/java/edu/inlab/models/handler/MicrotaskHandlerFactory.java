package edu.inlab.models.handler;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by hebowei on 16/5/14.
 */
@Service("microtaskHandlerFactory")
public class MicrotaskHandlerFactory {
    public final static String RENDERER_SIMPLE = "simple";
    public final static String RENDERER_GRID_CHOICE = "grid_choice";
    public final static String RENDERER_SIMPLE_WITH_REF = "simple_ref";

    @Autowired
    private MicrotaskPageRenderer simpleWithRefRenderer;

    @Autowired
    private MicrotaskPageRenderer simplePageRenderer;

    @Autowired
    private MicrotaskPageRenderer gridSimilarityChoicePageRender;

    public void setSimpleWithRefRenderer(SimpleWithRefRenderer simpleWithRefRenderer){
        this.simpleWithRefRenderer = simpleWithRefRenderer;
    }

    public void setSimplePageRenderer(SimplePageRenderer simplePageRenderer){
        this.simplePageRenderer = simplePageRenderer;
    }

    public void setGridSimilarityChoicePageRender(GridSimilarityChoicePageRender gridSimilarityChoicePageRender){
        this.gridSimilarityChoicePageRender = gridSimilarityChoicePageRender;
    }


    public MicrotaskPageRenderer getRenderer(String name){
        if(name.equals(RENDERER_SIMPLE)){
            //return new SimplePageRenderer();
            return this.simplePageRenderer;
        } else if (name.equals(RENDERER_GRID_CHOICE)){
            //return new GridSimilarityChoicePageRender();
            return this.gridSimilarityChoicePageRender;
        } else if (name.equals(RENDERER_SIMPLE_WITH_REF)) {
            return this.simpleWithRefRenderer;
        } else {
            return null;
        }
    }
}
