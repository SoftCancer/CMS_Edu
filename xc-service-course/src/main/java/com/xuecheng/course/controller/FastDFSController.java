package com.xuecheng.course.controller;

import com.xuecheng.course.common.FastDFSClient;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @author: YaoGuangXun
 * @date: 2020/9/16 21:10
 * @Version: 1.0
 */
@RestController
@RequestMapping("/fdfs")
public class FastDFSController {

    @Autowired
    private FastDFSClient fastDFSClient;

    /**
     * @Description: 文件上传
     * nullgroup1/M00/00/00/wKgBaF9iJnyAKRQsAAB1wETVYm0795.jpg
     * @Author: YaoGX
     * @Date: 2020/9/16 21:23
     **/
    @RequestMapping("/upload/file")
    public Map<String, Object> upload(@RequestParam("file") MultipartFile file) {
        Map<String, Object> resultMap = new HashMap<>();
        String filename = file.getOriginalFilename();
        String path = fastDFSClient.uploadFile(file);
        if (null == path || "".equals(path)) {
            resultMap.put("code", 500);
            resultMap.put("msg", "上传错误！");
            return resultMap;
        }

        resultMap.put("code", 200);
        resultMap.put("path", path);
        resultMap.put("msg", "上传成功！");
        System.out.println(path);
        return resultMap;
    }

    /**
     * @Description: group1/M00/00/00/wKgIZVzZEF2ATP08ABC9j8AnNSs744.jpg
     * @Author: YaoGX
     * @Date: 2020/9/16 21:43
     **/
    @RequestMapping(value = "/download")
    public void download(HttpServletRequest request, HttpServletResponse response) {
        String fileUrl = request.getParameter("filrUrl");
        int index = fileUrl.lastIndexOf("/");
        // 获取文件名称
        String fileName = fileUrl.substring(index + 1);
        byte[] data = fastDFSClient.download(fileUrl);

        try {
            // 设置响应头
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            // 获取绑定了客户端的流
            ServletOutputStream outputStream = response.getOutputStream();
            // 把 字节 中的数据写入到输出流中
            IOUtils.write(data, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件
     * @Description:
     * @Author: YaoGX
     * @Date: 2020/9/16 22:08
     **/
    @RequestMapping("/deleteFile")
    public String delFile(String filePath , HttpServletRequest request ,HttpServletResponse response){
        fastDFSClient.deleteFile(filePath);
        request.setAttribute("msg", "成功删除，'" + filePath);
        return "index";
    }

}
