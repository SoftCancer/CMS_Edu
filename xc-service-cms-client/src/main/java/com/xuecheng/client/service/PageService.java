package com.xuecheng.client.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.client.dao.CmsPageRepository;
import com.xuecheng.client.dao.CmsSiteRepository;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Optional;

/**
 * @Description:
 * @author: YaoGuangXun
 * @date: 2020/7/30 22:31
 * @Version: 1.0
 */
@Service
public class PageService {

    private static final Logger logger = LoggerFactory.getLogger(PageService.class);

    @Autowired
    CmsPageRepository cmsPageRepository;
    @Autowired
    CmsSiteRepository cmsSiteRepository;

    @Autowired
    private GridFSBucket gridFSBucket;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    public void savePageToServerPath(String pageId) {

        CmsPage cmsPage = this.getCmsPageById(pageId);
        if (null == cmsPage) {

        }
        // 根据 pageId 查询 html 的文件 id。
        String htmlFileId = cmsPage.getHtmlFileId();
        // 根据 pageId 从GridFS 中获取 HTML 文件。
        InputStream inputStream = this.getFileById(htmlFileId);
        if (inputStream == null) {
            return;
        }

        String siteId = cmsPage.getSiteId();
        // 根据站点id 获取站点信息
        CmsSite cmsSite = this.getCmsSiteById(siteId);
        // 获取站点的物理路径
        String sitePhysicalPath = cmsSite.getSitePhysicalPath();

        // 拼接页面物理路径
        String pagePath = sitePhysicalPath + cmsPage.getPagePhysicalPath() + cmsPage.getPageName();
        OutputStream outputStream = null;
        try {
            // 获取输出流
            outputStream = new FileOutputStream(new File(pagePath));
            // 将HTML 文件保存到服务器的物理路径
            IOUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * @Description: 获取输入流
     * @Author: YaoGX
     * @Date: 2020/7/30 23:20
     **/
    public InputStream getFileById(String htmlFileId) {
        // 获取文件对象
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(htmlFileId)));
        // 打开下载流
        GridFSDownloadStream gFSDS = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        // 定义 GridFsResource
        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gFSDS);

        try {
            return gridFsResource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取 CMSPage
     *
     * @Author: YaoGX
     * @Date: 2020/7/30 23:06
     **/
    public CmsPage getCmsPageById(String pageId) {
        Optional<CmsPage> cmsPageOptional = cmsPageRepository.findById(pageId);
        if (cmsPageOptional.isPresent()) {
            CmsPage cmsPage = cmsPageOptional.get();
            return cmsPage;
        }
        return null;
    }

    /**
     * 获取 CmsSite
     *
     * @Author: YaoGX
     * @Date: 2020/7/30 23:06
     **/
    public CmsSite getCmsSiteById(String siteId) {
        Optional<CmsSite> cmsPageOptional = cmsSiteRepository.findById(siteId);
        if (cmsPageOptional.isPresent()) {
            CmsSite cmsSite = cmsPageOptional.get();
            return cmsSite;
        }
        return null;
    }
}
