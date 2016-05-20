package edu.inlab.web;

import com.sun.tools.internal.jxc.ap.Const;
import edu.inlab.models.FileBucket;
import edu.inlab.models.TempFile;
import edu.inlab.models.json.AjaxResponseBody;
import edu.inlab.service.FileValidator;
import edu.inlab.service.TempFileService;
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

    @InitBinder("fileBucket")
    protected void initBinderFileBucket(WebDataBinder binder){
        binder.setValidator(fileValidator);
    }

    /**
     * 会检查userMicrotaskId的Session避免恶意传文件
     * 文件上传成功后删除Session
     * @param fileBucket
     * @param bindingResult
     * @return JSONObject: {state: STATE; content: TEMP_FILE_ID}
     */
    @ResponseBody
    @RequestMapping(value = "/upload", method = RequestMethod.POST,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public AjaxResponseBody singleFileUpload(@Valid FileBucket fileBucket,
                                             BindingResult bindingResult,
                                             HttpServletRequest request) throws IOException{
        Integer umtId = (Integer) request.getSession().getAttribute(Constants.KEY_FILE_UPLOAD);
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
            FileCopyUtils.copy(fileBucket.getFile().getBytes(),
                    new File(Constants.UPLOAD_FILE_STORE_LOCATION + umtId + multipartFile.getOriginalFilename()));
            String fileName = umtId + multipartFile.getOriginalFilename();
            TempFile tempFile = new TempFile();
            tempFile.setFilename(fileName);
            tempFile.setUsermicrotaskId(umtId);
            tempFileService.save(tempFile);
            ajaxResponseBody.setState(200);
            ajaxResponseBody.setContent(fileName);
            request.getSession().removeAttribute(Constants.KEY_FILE_UPLOAD);
        }
        return ajaxResponseBody;
    }

}
