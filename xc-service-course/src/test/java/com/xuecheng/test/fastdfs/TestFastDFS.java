package com.xuecheng.test.fastdfs;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.domain.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.xuecheng.course.CourseApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @Description:
 * @author: YaoGuangXun
 * @date: 2020/9/14 23:25
 * @Version: 1.0
 */
@SpringBootTest(classes = CourseApplication.class)
@RunWith(SpringRunner.class)
public class TestFastDFS {

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private ThumbImageConfig thumbImageConfig;

    @Test
    public void testUpload() {
        File file = new File("D:\\ProgramFiles\\personDocument\\个人文件夹\\表情包\\picture/01.jpg");
        try {
            StorePath storePath = storageClient.uploadFile(new FileInputStream(file), file.length(), "jpg", null);
            System.out.println("带分组的路径：" + storePath.getFullPath());
            System.out.println("不带分组的路径：" + storePath.getPath());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
