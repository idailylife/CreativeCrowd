package edu.inlab.models;

import edu.inlab.repo.usertype.JSONObjectUserType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.json.JSONObject;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by inlab-dell on 2016/5/4.
 */
@Entity
@Table(name = "task")
@TypeDef(name = "customJsonObject", typeClass = JSONObjectUserType.class)
public class Task {
    public static int TYPE_NORMAL = 0;
    public static int TYPE_MTURK = 1;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "quota", nullable = false)
    @Min(value = 0)
    private Integer quota;

    @Column(name = "claimed_count", nullable = false)
    @Min(value = 0)
    private Integer claimedCount;

    @Column(name = "finished_count", nullable = false)
    @Min(value = 0)
    private Integer finishedCount;

    @Column(name = "desc_json")
    @Type(type = "customJsonObject")
    private JSONObject descJson;

    @Column(name = "mode", nullable = false)
    private Integer mode;   //Normal, Random Assign, Assign with Policy

    @Column(name = "start_time")
    private Integer startTime;

    @Column(name = "end_time")
    private Integer endTime;

    @Column(name = "owner_id", nullable = false)
    private Integer ownerId;

    @Column(name = "image")
    private String image;

    @Column(name = "tag")
    private String tag;

    @Column(name = "repeatable", nullable = false)
    @Max(1)
    private Integer repeatable;

    @Column(name = "type", nullable = false)
    private Integer type;

    @Column(name = "time_limit")
    private Integer timeLimit; // Time limit for one task (in minute)

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "task_id")
    private List<Microtask> relatedMictorasks;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuota() {
        return quota;
    }

    public void setQuota(Integer quota) {
        this.quota = quota;
    }

    public Integer getFinishedCount() {
        return finishedCount;
    }

    public void setFinishedCount(Integer finishedCount) {
        this.finishedCount = finishedCount;
    }

    public JSONObject getDescJson() {
        return descJson;
    }

    public void setDescJson(JSONObject descJson) {
        this.descJson = descJson;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getClaimedCount() {
        return claimedCount;
    }

    public void setClaimedCount(Integer claimedCount) {
        this.claimedCount = claimedCount;
    }

    public List<Microtask> getRelatedMictorasks() {
        return relatedMictorasks;
    }

    public void setRelatedMictorasks(List<Microtask> relatedMictorasks) {
        this.relatedMictorasks = relatedMictorasks;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getRepeatable() {
        return repeatable;
    }

    public void setRepeatable(Integer repeatable) {
        this.repeatable = repeatable;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    @Override
    public int hashCode() {
        if(this.id != null){
            return this.id;
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(this.id != null && ((Task)obj).getId() != null){
            return this.id.equals(((Task) obj).getId());
        }
        return super.equals(obj);
    }

    public boolean isExpired(){
        if(this.endTime == null){
            return false;
        }
        Date currDate = new Date();
        Date endDate = new Date((long)this.endTime*1000);
        return currDate.after(endDate);
    }

    public boolean isFull(){
        return claimedCount >= quota;
    }

    public boolean getTaskInvalid(){
        boolean state = isExpired() || isFull();
        return state;
    }

    /**
     * 转换任务起止时间戳信息到文字
     * @return
     */
    public String getDurationStr(){
        if(null == startTime && null == endTime){
            if(type == TYPE_NORMAL)
                return "不限";
            else if(type == TYPE_MTURK)
                return "Unlimited";
        }
        String retStr = "";

        Calendar calendar = Calendar.getInstance();
        Date currDate = new Date();
        calendar.setTime(currDate);
        int year = calendar.get(Calendar.YEAR);


        if(null != startTime){
            retStr += parseMMDDStr(startTime, year);
        }
        retStr += " - ";
        if(null != endTime){
            retStr += parseMMDDStr(endTime, year);
        }
        return retStr;
    }

    private String parseMMDDStr(Integer time, int year){
        String retStr;
        Date date = new Date((long)time*1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int endYear = calendar.get(Calendar.YEAR);

        if(endYear == year){
            retStr = new SimpleDateFormat("MM/dd").format(date);
        } else {
            retStr = dateFormat.format(date);
        }
        return retStr;
    }
}
