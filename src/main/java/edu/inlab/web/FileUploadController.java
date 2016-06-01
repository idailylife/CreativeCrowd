package edu.inlab.web;


import edu.inlab.models.FileBucket;
import edu.inlab.models.TempFile;
import edu.inlab.models.UserTask;
import edu.inlab.models.json.AjaxResponseBody;
import edu.inlab.service.FileValidator;
import edu.inlab.service.TempFileService;
import edu.inlab.service.UserTaskService;
import edu.inlab.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;

/**
 * Created by inlab-dell on 2016/5/20.
 */
@Controller
@RequestMapping(value = "/file")
public class FileUploadController {

    @Autowired
    FileValidator fileValidator;

    @Autowired
    TempFileService tempFileService;

    @Autowired
    UserTaskService userTaskService;

    @InitBinder("fileBucket")
    protected void initBinderFileBucket(WebDataBinder binder){
        binder.setValidator(fileValidator);
    }


    @ResponseBody
    @RequestMapping(value = "/upload", method = RequestMethod.POST,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public AjaxResponseBody singleFileUpload(@Valid FileBucket fileBucket,
                                             BindingResult bindingResult,
                                             HttpServletRequest request) throws IOException{
        //Integer umtId = (Integer) request.getSession().getAttribute(Constants.KEY_FILE_UPLOAD);
        Integer taskId = fileBucket.getTaskId();
        Integer uid = (Integer) request.getSession().getAttribute(Constants.KEY_USER_UID);
        UserTask userTask = null;
        Integer umtId = null;
        if(taskId != null && uid != null){
            if(uid > 0){
                userTask = userTaskService.getUnfinishedByUserIdAndTaskId(uid, taskId);
            } else if(uid == Constants.VAL_USER_UID_MTURK) {
                String mturkId = (String) request.getSession().getAttribute(Constants.KEY_MTURK_ID);
                userTask = userTaskService.getUnfinishedByMTurkIdAndTaskId(mturkId, taskId);
            }
            umtId = userTask.getCurrUserMicrotaskId();
        }
        if(Constants.DEBUG)
            umtId = -1;
        AjaxResponseBody ajaxResponseBody = new AjaxResponseBody();
        if(umtId == null){
            ajaxResponseBody.setState(401);
            ajaxResponseBody.setMessage("Auth check failure.");
        } else if(bindingResult.hasErrors()){
            ajaxResponseBody.setState(402);
            ajaxResponseBody.setMessage("Error in request body");
        } else {
            MultipartFile multipartFile = fileBucket.getFile();
            String fileName = umtId + "__" + multipartFile.getOriginalFilename();

            //If the user have already uploaded something for this usermicrotask
            //Remove the old file record and copy the new one, then change the database record
            TempFile tempFile = tempFileService.getByUsermicrotaskId(umtId);
            if(tempFile == null){
                FileCopyUtils.copy(fileBucket.getFile().getBytes(),
                        new File(Constants.UPLOAD_FILE_STORE_LOCATION + fileName));
                tempFile = new TempFile();
                tempFile.setFilename(fileName);
                tempFile.setUsermicrotaskId(umtId);
                tempFileService.save(tempFile);
            } else {
                File oldFile = new File(Constants.UPLOAD_FILE_STORE_LOCATION + tempFile.getFilename());
                if(oldFile.exists()){
                    if(!oldFile.delete()) {
                        throw new IOException("Cannot delete origin file " + tempFile.getFilename());
                    }
                }
                FileCopyUtils.copy(fileBucket.getFile().getBytes(),
                        new File(Constants.UPLOAD_FILE_STORE_LOCATION + fileName));
                tempFile.setFilename(fileName);
                tempFileService.update(tempFile);
            }


            ajaxResponseBody.setState(200);
            ajaxResponseBody.setContent(fileName);
            //request.getSession().removeAttribute(Constants.KEY_FILE_UPLOAD);
        }
        return ajaxResponseBody;
    }

}
