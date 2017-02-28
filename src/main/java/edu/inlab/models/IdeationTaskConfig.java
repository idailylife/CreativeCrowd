package edu.inlab.models;

import edu.inlab.utils.BlobObjectConv;
import edu.inlab.utils.RandomCollection;

import java.sql.Blob;
import java.util.*;

/**
 * Created by hebowei on 2017/1/20.
 */
public class IdeationTaskConfig {
    final static int LATEST_VERSION = 1;

    public List<ConfigItem> configItems;    //TODO: 修改为 Map<umtId, ConfigItem>
    public int version = 1;


    private class ConfigItem implements Comparable{
        public int umtId;
        public double score;        //实时分数
        public double initScore;    //初始分数（人工设定值)

        public ConfigItem(int umtId){
            this.umtId = umtId;
            this.score = 0;
            this.initScore = 0;
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


    /**
     *
     * @param sampleSize 需要选出的参考项个数
     * @return
     */
    public Set<Integer> getTopRankedItems(int sampleSize){
        // Weighted sampling
        RandomCollection<Integer> randomCollection = new RandomCollection<>();
        for(ConfigItem item : configItems){
            randomCollection.add(item.score, item.umtId);
        }
        return randomCollection.nextSet(sampleSize);
    }

    public static IdeationTaskConfig ReadFromBlob(Blob input){
        IdeationTaskConfig config =  (IdeationTaskConfig) BlobObjectConv.BlobToObject(input);
        if (config != null && config.version != IdeationTaskConfig.LATEST_VERSION){
            System.err.println("IdeationTaskConfig: blob config version is lower than LATEST_VERSION");
        }
        return config;
    }

    public Map<Integer, Double> getScoreMap(){
        Map<Integer, Double> scoreMap = new HashMap<>();
        for(ConfigItem configItem : configItems){
            scoreMap.put(configItem.umtId, configItem.score);
        }
        return scoreMap;
    }

    /**
     * Set score if ConfigItem object
     * if the umtId does not exist in
     * WARNING: the time complexity is O(n), but considering this action is not frequently used, it's acceptable
     * @param umtId
     * @param score
     */
    public void setItem(int umtId, double score){
        boolean itemFound = false;
        for(int i=0; i<configItems.size(); i++){
            ConfigItem item = configItems.get(i);
            if(item.umtId == umtId){
                itemFound = true;
                item.score = score;
                configItems.set(i, item);
            }
        }
        if(!itemFound){
            ConfigItem newItem = new ConfigItem(umtId);
            newItem.score = score;
            configItems.add(newItem);
        }
    }
}
