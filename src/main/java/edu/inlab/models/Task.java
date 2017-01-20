package edu.inlab.models;

import edu.inlab.repo.usertype.JSONObjectUserType;
import edu.inlab.service.assignment.MicroTaskAssigner;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONObject;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.sql.Blob;
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
public class Task extends CaptchaCapableModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    @Length(min = 10, max = 128)
    private String title;

    @Column(name = "quota", nullable = false)
    private Integer quota;

    @Column(name = "claimed_count")
    @Min(value = 0)
    private Integer claimedCount;

    @Column(name = "finished_count")
    @Min(value = 0)
    private Integer finishedCount;

    /**
     * Task description
     * {
     *     desc: 任务简略描述,
     *     desc_detail: 任务细节描述,
     *     est_time: 预估完成时间,
     *     info: {
     *         附加信息字段: 附加信息值,
     *         ...
     *     }
     * }
     * `info` is optional.
     */
    @Column(name = "desc_json")
    @Type(type = "customJsonObject")
    private JSONObject descJson;

    /**
     * This defines the way that microtasks being assigned to one worker
     * Models and assigners should be defined in edu.inlab.service.assignment.MicroTaskAssigner
     * When assigning microtasks, MicroTaskAssignFactory will get instances of specified assigners to
     *   dispatch(assign) microtasks
     * For further implementation, params of assigner can be defined in `params` column
     *   as JSON key:value pairs
     */
    @Column(name = "mode", nullable = false)
    private Integer mode;   //Normal, Random Assign, Sequenced Assign

    @Column(name = "start_time")
    private Long startTime;

    @Column(name = "end_time")
    private Long endTime;

    @Column(name = "owner_id", nullable = false)
    private Integer ownerId;

    @Column(name = "image")
    private String image;

    @Column(name = "tag")
    private String tag;

    /**
     * This defines if a task can be claimed more than once by one unique worker
     */
    @Column(name = "repeatable", nullable = false)
    @Max(1)
    private Integer repeatable;

    @Column(name = "type", nullable = false)
    private Integer type;

    public final static int TYPE_NORMAL = 0;
    public final static int TYPE_MTURK = 1;

    @Column(name = "params")
    private String params;  //Assigner parameters string

    @Column(name = "time_limit")
    private Integer timeLimit; // Time limit for one task (in minute)

    /**
     * The wageType is to define the type of wage to be paid to workers
     * See following definitions for details.
     */
    @Column(name = "wage_type")
    private Integer wageType;
    public static int WAGE_TYPE_PER_TASK = 0;   //One-time computed wage, the `wage` column is a double numeric value
    public static int WAGE_TYPE_PER_MICROTASK = 1;  //Defined as an JSON string in the `wage` column, to be implemented

    @Column(name = "wage", nullable = false)
    private String wage;

    @Column(name = "config_blob")
    private Blob configBlob;

    @Column(name = "optlock")
    @Version
    private Integer version;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "task_id")
    private List<Microtask> relatedMictorasks;

    public Task() {
        this.claimedCount = 0;
        this.finishedCount = 0;
        this.wageType = WAGE_TYPE_PER_TASK;
    }

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

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
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

    public String getParams() {
//        if(mode== MicroTaskAssigner.TASK_ASSIGN_SINGLE_RANDOM && params.contains("\'")){
//            params = params.replace('\'', '`');
//        }
        return params;
    }

    public void setParams(String params) {
        if(!params.isEmpty() && params.charAt(0) == ','){
            //Fix weird bug when uploading params via FormData: the string will contain a prefix `,`
            params = params.substring(1);
        }
        this.params = params;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getWageType() {
        return wageType;
    }

    public void setWageType(Integer wageType) {
        this.wageType = wageType;
    }

    public String getWage() {
        return wage;
    }

    public void setWage(String wage) {
        this.wage = wage;
    }

    public Blob getConfigBlob() {
        return configBlob;
    }

    public void setConfigBlob(Blob configBlob) {
        this.configBlob = configBlob;
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

    public String getTypeName(){
        switch (type){
            case TYPE_MTURK:
                return "MTurk";
            case TYPE_NORMAL:
                return "普通";
            default:
                return "未定义";
        }
    }

    public boolean isExpired(){
        if(this.endTime == null){
            return false;
        }
        Date currDate = new Date();
        Date endDate = new Date(this.endTime*1000);
        return currDate.after(endDate);
    }

    public boolean isFull(){
        return claimedCount >= quota;
    }

    public boolean getTaskInvalid(){
        return isExpired() || isFull();
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

    private String parseMMDDStr(Long time, int year){
        String retStr;
        Date date = new Date(time*1000);
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
