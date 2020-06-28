// 导入封装后的请求方式
import http from './../../../base/api/public'
import querystring from 'querystring'

let sysConfig = require('@/../config/sysConfig')
let apiUrl = sysConfig.xcApiUrlPre;


// 导出 请求服务端接口函数
export const pageList = (page, size, params) => {
  // 把json 转化为 key：value 格式。
  let qs = querystring.stringify(params);  // 不起作用
  // apiUrl  :  /api
  return http.requestQuickGet(apiUrl + '/cms/page/list/' + page + '/' + size + '?' + qs);
}

// 新增页面
export const pageAdd = params => {
  // apiUrl  :  /api
  return http.requestPost(apiUrl + '/cms/page/add/', params);
}

// 获取cmsPage
export const getCms = pageId => {
  return http.requestQuickGet(apiUrl + '/cms/page/get/' + pageId);
}

// 修改功能
export const pageEdit = params => {
  return http.requestPut(apiUrl + '/cms/page/edit/', params);
}

// 删除功能
export const pageDel = pageId => {
  return http.requestDelete(apiUrl + '/cms/page/delete/' + pageId);
}


// 获取站点
export const siteList = (page, size, params) => {
  let qs = querystring.stringify(params);  // 不起作用
  // apiUrl  :  /api
  return http.requestQuickGet(apiUrl + '/cms/site/list/' + page + '/' + size + '?' + qs);
}

// 获取站点
export const siteAll = () => {
  // apiUrl  :  /api
  return http.requestQuickGet(apiUrl + '/cms/site/list/all/');
}
