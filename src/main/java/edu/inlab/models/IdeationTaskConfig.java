package edu.inlab.models;

import edu.inlab.utils.BlobObjectConv;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by hebowei on 2017/1/20.
 */
public class IdeationTaskConfig {
    final static int LATEST_VERSION = 0;

    public List<ConfigItem> configItems;
    public int version = 0;


    private class ConfigItem implements Comparable{
        public int umtId;
        public double score;
//        public boolean selected;
        public int refCount;

        public ConfigItem(int umtId){
            this.umtId = umtId;
            this.score = 0;
//            this.selected = false;
            this.refCount = 0;
        }

        @Override
        public int compareTo(Object o) {
            if (o instanceof ConfigItem){
                return (score - ((ConfigItem) o).score) > 0 ? 1:-1;
            } else {
                return 0;
            }
        }
    }

//    public List<Integer> getSelectedItems(int refCountLimit){
//        return getSelectedItems(-1, refCountLimit);
//    }
//
//    public List<Integer> getSelectedItems(int sampleSize, int refCountLimit){
//        List<Integer> results = new ArrayList<>();
//        for (ConfigItem item : configItems){
//            if(item.selected && item.refCount < refCountLimit)
//                results.add(item.umtId);
//        }
//        if(sampleSize > 0){
//            Collections.shuffle(results);
//            results = results.subList(0, sampleSize);
//        }
//        return results;
//    }

    /**
     *
     * @param sampleSize 需要选出的参考项个数
     * @param refCountLimit 参考次数限制
     * @return
     */
    public List<Integer> getTopRankedItems(int sampleSize, int refCountLimit){
        List<ConfigItem> results = new ArrayList<>();
        for (ConfigItem item : configItems){
            if(item.refCount < refCountLimit)
                results.add(item);
        }
        Collections.sort(results);

        // Weighted sampling


        List<Integer> retVal = new ArrayList<>();
        for(ConfigItem item : results)
            retVal.add(item.umtId);
        return retVal;
    }

    public static IdeationTaskConfig ReadFromBlob(Blob input){
        IdeationTaskConfig config =  (IdeationTaskConfig) BlobObjectConv.BlobToObject(input);
        if (config != null && config.version != IdeationTaskConfig.LATEST_VERSION){
            System.err.println("IdeationTaskConfig: blob config version is lower than LATEST_VERSION");
        }
        return config;
    }
}
