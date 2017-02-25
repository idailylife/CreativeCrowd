package edu.inlab.utils;

/**
 * Created by inlab-dell on 2016/5/9.
 *
 * 文件上传位置说明(TASK_ID是任务ID):
 * - Task描述图片:               ../upload/task/TASK_ID/        图片文件名前缀是DESCIMG
 * - Task中Microtask相关图片     ../upload/task/TASK_ID/
 * - 用户做任务时上传的图片       ../upload/user_gen/TASK_ID/
 */
public class Constants {
    public static final boolean DEBUG = false;
    public static final boolean ALLOW_REGISTER = false; // 允许用户注册

    //图片上传目录
    public static final String UPLOAD_FILE_STORE_LOCATION = "D:/Code/Java/CreativeCrowd/src/main/webapp/static/img/upload/";
    //"/Users/hebowei/IdeaProjects/CreativeCrowd/src/main/webapp/static/img/upload/";


    public static final long MAX_FILE_SIZE = 5242880;  //5MB
    public static final long MAX_REQUEST_SIZE = 20971520;  //20MB
    public static final int FILE_SIZE_THRESHOLD = 0; // Size threshold after which files will be written to disk

    public static final String KEY_USER_UID = "_uid";
    public static final int VAL_USER_UID_MTURK = 0;
    public static final String KEY_MTURK_ID = "_mturkId";
    public static final String KEY_USER_TOKEN = "_utoken";
    public static final String KEY_FILE_UPLOAD = "_fupload";

    public static final String KEY_TASK_DESC = "desc";
    //public static final String KEY_TASK_IMG_URL = "img";
    public static final String KEY_TASK_DESC_DETAIL = "desc_detail";
    public static final String KEY_TASK_EST_TIME = "est_time";
    public static final String KEY_TASK_REL_INFO = "info";
    public static final String KEY_CAPTCHA_SESSION ="_captcha";

    //public static final String KEY_UPLOAD_TOKEN = "_upd_token";
    public static final String KEY_UPLOAD_TOKEN_MAP = "_upd_map";

    public static final int USER_LIST_TASK_LENGTH = 20; //用户中心：每一页显示多少个Task
}
