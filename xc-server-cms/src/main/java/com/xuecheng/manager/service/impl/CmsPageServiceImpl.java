package com.xuecheng.manager.service.impl;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.CustomException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manager.config.RabbitMQConfig;
import com.xuecheng.manager.dao.CmsPageRepository;
import com.xuecheng.manager.dao.CmsTemplateRepository;
import com.xuecheng.manager.service.ICmsPageService;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @Description: service 层实现类
 * @author: YaoGuangXun
 * @date: 2020/6/9 23:13
 * @Version: 1.0
 */
@Service
public class CmsPageServiceImpl implements ICmsPageService {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;

    /**
     * @Description: http 请求接口 是SpringMVC 提供的。
     * @Author: YaoGX
     * @Date: 2020/7/18 15:34
     **/
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public QueryResponseResult findCmsList(int page, int size, QueryPageRequest queryPageRequest) {


        if (page < 0) {
            page = 0;
        }

        if (size < 0) {
            size = 10;
        }

        if (queryPageRequest == null) {
            queryPageRequest = new QueryPageRequest();
        }
        String siteId = queryPageRequest.getSiteId();
        String templateId = queryPageRequest.getTemplateId();
        String pageAliase = queryPageRequest.getPageAliase();

        // 条件值对象
        CmsPage cmsPage = new CmsPage();
        // 设置条件值 站点id
        if (StringUtils.isNotBlank(siteId)) {
            cmsPage.setSiteId(siteId);
        }

        if (StringUtils.isNotBlank(templateId)) {
            cmsPage.setTemplateId(templateId);
        }

        if (StringUtils.isNotBlank(pageAliase)) {
            cmsPage.setPageAliase(pageAliase);
        }

        // 自定义条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<CmsPage> cmsPages = cmsPageRepository.findAll(example, pageable);

        // 优化封装结果
        QueryResult queryResult = new QueryResult();
        queryResult.setList(cmsPages.getContent());
        queryResult.setTotal(cmsPages.getTotalElements());
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }

    @Override
    public CmsPageResult addCms(CmsPage cmsPage) {
        String pageName = cmsPage.getPageName();
        String pageWebPath = cmsPage.getPageWebPath();
        String siteId = cmsPage.getSiteId();
        CmsPage cmsPage_1 = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(pageName, siteId, pageWebPath);
        if (cmsPage_1 != null) {
            return new CmsPageResult(CommonCode.FAIL, "数据已存在！");
        }
        CmsPage cmsPageResult = cmsPageRepository.save(cmsPage);
        return new CmsPageResult(CommonCode.SUCCESS, cmsPageResult);
    }

    @Override
    public CmsPageResult findById(String id) {
        if (StringUtils.isEmpty(id)) {
            return new CmsPageResult(CommonCode.FAIL, "数据不存在！");
        }
        Optional<CmsPage> optionalPage = cmsPageRepository.findById(id);
        if (optionalPage.isPresent()) {
            CmsPage cmsPage = optionalPage.get();
            return new CmsPageResult(CommonCode.SUCCESS, cmsPage);
        }
        return new CmsPageResult(CommonCode.FAIL, "数据不存在!");
    }

    @Override
    public CmsPageResult editCmsPage(CmsPage cmsPage) {
        String pageId = cmsPage.getPageId();
        if (StringUtils.isEmpty(pageId)) {
            return new CmsPageResult(CommonCode.FAIL, "数据不存在!");
        }

        Optional<CmsPage> optionalPage = cmsPageRepository.findById(pageId);
        if (optionalPage.isPresent()) {
            CmsPage cmsPageResult = cmsPageRepository.save(cmsPage);
            return new CmsPageResult(CommonCode.SUCCESS, cmsPageResult);
        }
        return new CmsPageResult(CommonCode.FAIL, "数据不存在!");
    }

    @Override
    public ResponseResult deleteById(String id) {
        Optional<CmsPage> optionalPage = cmsPageRepository.findById(id);
        if (optionalPage.isPresent()) {
            cmsPageRepository.deleteById(id);
            return ResponseResult.SUCCESS();
        }
        return ResponseResult.FAIL();
    }


    /**
     * 把 数据模型和页面模板进行结合获得 HTML
     *
     * @Author: YaoGX
     * @Date: 2020/7/18 22:57
     **/
    @Override
    public String getPageHtml(String pageId) {
        // 获取数据模型
        Map map = getModelByPageId(pageId);
        if (null == map) {
            CustomException.throwException(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }

        // 获取页面模板信息
        String template = getTemplateByPageId(pageId);
        if (StringUtils.isEmpty(template)) {
            CustomException.throwException(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }

        // 执行静态化
        String htmlContext = generateHtml(template, map);
        return htmlContext;
    }

    /**
     * 获取页面模板信息
     *
     * @Author: YaoGX
     * @Date: 2020/7/19 10:40
     **/
    public String generateHtml(String templateContext, Map map) {
        // 创建配置对象
        Configuration configuration = new Configuration(Configuration.getVersion());
        // 创建模板加载器
        StringTemplateLoader stLoader = new StringTemplateLoader();
        stLoader.putTemplate("template", templateContext);
        // 向 configuration 配置模板加载器
        configuration.setTemplateLoader(stLoader);

        // 获取模板
        try {
            Template template = configuration.getTemplate("template");
            // 调用 Api 进行静态化
            String context = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
            return context;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 获取页面模板信息
     *
     * @Author: YaoGX
     * @Date: 2020/7/18 22:35
     **/
    public String getTemplateByPageId(String pageId) {
        CmsPage cmsPage = this.findById(pageId).getCmsPage();
        if (cmsPage == null) {
            CustomException.throwException(CmsCode.CMS_PAGE_NOTEXISTS);
        }

        String templateId = cmsPage.getTemplateId();
        if (StringUtils.isEmpty(templateId)) {
            CustomException.throwException(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }

        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(templateId);
        if (optional.isPresent()) {
            CmsTemplate cmsTemplate = optional.get();
            String templateFileId = cmsTemplate.getTemplateFileId();

            // 从 GridFS 中获取模板文件内容
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));

            // 打开一个下载流对象
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            // 创建 GridFSResource 对象，获取流
            GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
            // 从流中获取数据
            try {
                String content = IOUtils.toString(gridFsResource.getInputStream(), "UFT-8");
                return content;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    /**
     * @Description: 获取数据模型
     * @Author: YaoGX
     * @Date: 2020/7/18 15:39
     **/
    public Map getModelByPageId(String pageId) {
        CmsPage cmsPage = this.findById(pageId).getCmsPage();
        if (cmsPage == null) {
            CustomException.throwException(CmsCode.CMS_PAGE_NOTEXISTS);
        }

        String dataUrl = cmsPage.getDataUrl();
        if (StringUtils.isEmpty(dataUrl)) {
            CustomException.throwException(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }

        // 请求 /cms/config//model/{id}  接口 ，获取模板数据
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = forEntity.getBody();
        return body;
    }

    /**
     *  发布 页面
     * @Author: YaoGX
     * @Date: 2020/8/8 23:33
     **/
    public ResponseResult postPageToMongoDB(String pageId){

        // 执行页面静态化
        String pageHtml = this.getPageHtml(pageId);
        // 将 静态化文件存储到 GridFS 中。
        CmsPage cmsPage = saveHtml(pageId,pageHtml);

        // 向mq 方式消息
        sendPageIdToMQ(pageId);

        return ResponseResult.SUCCESS();
    }

    private void  sendPageIdToMQ(String pageId){
        CmsPage cmsPage = this.findById(pageId).getCmsPage();
        if (cmsPage == null) {
            CustomException.throwException(CmsCode.CMS_PAGE_NOTEXISTS);
        }

        // 封装 消息内容
        Map map = new HashMap();
        map.put("pageId",pageId);
        String jsonStr = JSON.toJSONString(map);

        // 获取站点id，设置为rotingkey
        String siteId = cmsPage.getSiteId();
        // 发送 页面id 到 RabbitMQ 中
        rabbitTemplate.convertAndSend(RabbitMQConfig.CMS_CLIENT_EX,siteId,jsonStr);

    }


    /**
     * 保存HTML 到gridFS中。
     *
     * @Author: YaoGX
     * @Date: \ 22:43
     **/
    private CmsPage saveHtml(String pageId, String htmlContent) {

        CmsPage cmsPage = this.findById(pageId).getCmsPage();
        if (null == cmsPage)
            CustomException.throwException(CommonCode.ILLEGAL_PARAMETER);

        InputStream inputStream = null;
        try {
            inputStream = IOUtils.toInputStream(htmlContent, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 获取HTML 文件 内容保存到 GridFS中
        ObjectId objectId = gridFsTemplate.store(inputStream, cmsPage.getPageName());

        cmsPage.setHtmlFileId(objectId.toHexString());

        cmsPageRepository.save(cmsPage);
        return cmsPage;
    }

}
