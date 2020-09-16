package com.xuecheng.course.common;

import com.github.tobato.fastdfs.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.exception.FdfsUnsupportStorePathException;
import com.github.tobato.fastdfs.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 参考博客地址：https://blog.csdn.net/W_Meng_H/article/details/85402879
 * @Description:
 * @author: YaoGuangXun
 * @date: 2020/9/15 22:16
 * @Version: 1.0
 */
@Component
public class FastDFSClient {

    @Autowired
    private FastFileStorageClient storageClient;

    /** 用于获取文件服务器的地址 */
    @Autowired
    private FdfsWebServer fdfsWebServer;


    /**
     * 上传文件
     * @param file 文件对象
     * @return 文件访问地址
     * @Author: YaoGX
     * @Date: 2020/9/15 22:18
     **/
    public String uploadFile(MultipartFile file) {
        StorePath storePath = null;
        try {
            storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(),FilenameUtils.getExtension(file.getOriginalFilename()), null);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return getResAccessUrl(storePath);
    }


    /**
     * 上传文件
     * @param file 文件对象
     * @return 文件访问地址
     * @Author: YaoGX
     * @Date: 2020/9/15 22:27
     **/
    public String uploadFile(File file){
        StorePath storePath = null;
        try {
            storePath = storageClient.uploadFile(new FileInputStream(file), file.length(),FilenameUtils.getExtension(file.getName()), null);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return getResAccessUrl(storePath);
    }

    /**
     * 将一段字符串生成一个文件上传
     * @param content 文件内容
     * @param fileExtension
     * @return
     */
    public String uploadFile(String content,String fileExtension){
       byte[] buff = content.getBytes(Charset.forName("UTF-8"));
        ByteArrayInputStream stream = new ByteArrayInputStream(buff);
        StorePath storePath = storageClient.uploadFile(stream,buff.length,fileExtension,null);
        return getResAccessUrl(storePath);
    }

    // 封装图片完整URL地址
    private String getResAccessUrl(StorePath storePath) {
        String fileUrl = fdfsWebServer.getWebServerUrl() + storePath.getFullPath();
        return fileUrl;
    }

    /**
     * 下载文件
     * @param fileUrl 文件url
     * @return
     */
    public byte[]  download(String fileUrl) {
        String group = fileUrl.substring(0, fileUrl.indexOf("/"));
        String path = fileUrl.substring(fileUrl.indexOf("/") + 1);
        byte[] bytes = storageClient.downloadFile(group, path, new DownloadByteArray());
        return bytes;
    }

    /**
     * 删除文件
     * @param fileUrl 文件访问地址
     * @return
     */
    public void deleteFile(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)) {
            return;
        }
        try {
            StorePath storePath = StorePath.praseFromUrl(fileUrl);
            storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
        } catch (FdfsUnsupportStorePathException e) {
            e.getMessage();
        }
    }

}
