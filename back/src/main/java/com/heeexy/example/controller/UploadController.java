package com.heeexy.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author L-YX
 * @version 1.0
 * @description
 * @data 2019-06-21 06:18
 */
@RestController
@RequestMapping("/upload")
public class UploadController {
    @RequestMapping(value = "/imgUpload")
    public Map imgUpload(HttpServletRequest req, MultipartHttpServletRequest multiReq) throws IOException {
        Map<String,Object> map = new HashMap<>();
        MultipartFile file = multiReq.getFile("file");
        String originalFilename = file.getOriginalFilename();
        String desFilePath =
                   "F:" + File.separator+"ideaIU"
                        + File.separator+"workspace"
                        + File.separator+"group-one"
                        + File.separator+"vue"
                        + File.separator+"src"
                        + File.separator+"assets"
                        + File.separator+"upload"
                        + "/" + originalFilename;
        File localFile  = new File(desFilePath);
        String srcUrl = "http://localhost:9520/static/img/"+originalFilename;


        if (!localFile.exists()) {
            localFile.createNewFile(); //判断文件夹是否存在
//            FileOutputStream fos = new FileOutputStream(localFile);
//            FileInputStream fs = (FileInputStream) multiReq.getFile("img").getInputStream();
            file.transferTo(localFile);
//            byte[] buffer = new byte[1024];
//            int len = 0;
//            while ((len = fs.read(buffer)) != -1) {
//                fos.write(buffer, 0, len);
//            }

            map.put("code", 0);
            map.put("msg", "上传成功");
            map.put("data", srcUrl);
//            fos.close();
//            fs.close();
        } else {
        }
        return map;//这里是我执行封装的返回结果，也可以使用map,
    }
}
