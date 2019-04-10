package com.jsp.studyShare.controller;import com.jsp.studyShare.Service.DataService;import com.jsp.studyShare.model.Response;import com.jsp.studyShare.model.ResponseData;import com.jsp.studyShare.utils.studyShareUtil;import org.slf4j.Logger;import org.slf4j.LoggerFactory;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.stereotype.Controller;import org.springframework.util.StreamUtils;import org.springframework.web.bind.annotation.*;import org.springframework.web.multipart.MultipartFile;import javax.servlet.http.HttpServletResponse;import java.io.File;import java.io.FileInputStream;import java.util.List;import java.util.Map;@RequestMapping("/data")@Controllerpublic class DataController {    private static final Logger logger = LoggerFactory.getLogger(DataController.class);    @Autowired    private DataService dataService;    /**     * 新增一条资料     * @param jsonStr     * @return     */    @RequestMapping(path = "/add", method = {RequestMethod.POST})    @ResponseBody    public Response addData(@RequestBody String jsonStr) {        try {            Map<String, String> map = dataService.addData(jsonStr);            if (!map.get("msg").equals("新增成功")) {                return new Response(1, map.get("msg"));            }            return new Response(0, map.get("msg"));        } catch (Exception e) {            logger.error("addData error:", e);            return new Response(1,"新增失败");        }    }    /**     * 根据分类获取所有资料     * @param cid     * @return     */    @RequestMapping(path = "/get", method = {RequestMethod.GET})    @ResponseBody    public Response getData(@RequestParam("cid") int cid) {        try {            List<ResponseData> data = dataService.getDataByCid(cid);            return new Response(0, "查询成功", data);        } catch (Exception e) {            logger.error("getData error:", e);            return new Response(1, "查询失败");        }    }    /**     * 得到所有资料     * @return     */    @RequestMapping(path = "/getAll", method = {RequestMethod.GET})    @ResponseBody    public Response getAllData() {        try {            List<ResponseData> data = dataService.getAllData();            return new Response(0, "查询成功", data);        } catch (Exception e) {            logger.error("getAllData error:", e);            return new Response(1, "查询失败");        }    }    /**     * 上传文件     * @param file     * @return     */    @RequestMapping(path = "/upload", method = {RequestMethod.POST})    @ResponseBody    public Response uploadData(@RequestParam("file")MultipartFile file) {        //post过来的数据会到file对象里        try {            String fileUrl = dataService.saveFile(file);            if (fileUrl == null) {                return new Response(1, "格式不正确");            }            return new Response(0, "上传成功", fileUrl);        } catch (Exception e) {            logger.error("uploadData error:", e);            return new Response(1, "上传失败");        }    }    /**     * 下载文件     * @param filename     * @param response     */    @RequestMapping(path = "/download", method = {RequestMethod.GET})    @ResponseBody    public Response getFile(@RequestParam("filename") String filename,@RequestParam("did") int did, HttpServletResponse response) {       try {           //保存到用户已下载           dataService.download(did);           response.setContentType("application/zip");           File file = new File(studyShareUtil.FILE_DIR + filename);           if (!file.exists()) {               return new Response(1, "找不到指定文件");           }           StreamUtils.copy(new FileInputStream(file), response.getOutputStream());           return new Response(0, "下载成功");       } catch (Exception e) {           logger.error("getFile: error", e);           return new Response(0, "下载失败");       }    }    /**     * 搜索资源     * @param keywords     * @return     */    @RequestMapping(path = "/query", method = {RequestMethod.GET})    @ResponseBody    public Response queryData(String keywords) {        try {            List<ResponseData> responseData = dataService.queryData(keywords);            return new Response(0, "查询成功", responseData);        } catch (Exception e) {            logger.error("queryData error:", e);            return new Response(1, "查询失败");        }    }    /**     * 收藏资料     * @param did     * @return     */    @RequestMapping(path = "/save", method = {RequestMethod.GET})    @ResponseBody    public Response saveData(@RequestParam("did") int did) {        try {            dataService.saveData(did);            return new Response(0, "收藏成功");        } catch (Exception e) {            logger.error("saveData",e);            return new Response(1, "收藏失败");        }    }    /**     * 随机推荐     * @return     */    @RequestMapping(path = "/recommend", method = {RequestMethod.GET, RequestMethod.POST})    @ResponseBody    public Response recommend() {        try {            List<ResponseData> recommend = dataService.recommend();            return new Response(0, "获取成功", recommend);        } catch (Exception e) {            logger.error("recommend error:",e);            return new Response(1,"获取失败");        }    }}