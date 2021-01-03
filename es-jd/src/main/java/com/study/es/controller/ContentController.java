package com.study.es.controller;

import com.study.es.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Program Name: es-study-api
 * Created by yanlp on 2021-01-03
 *
 * @author yanlp
 * @version 1.0
 */
@RestController
@RequestMapping("/content")
public class ContentController {

    @Autowired
    private ContentService contentService;

    /**
     * 爬取数据,并入es
     * @param keywords
     * @return
     * @throws Exception
     */
    @GetMapping("/parse/{keyword}")
    public boolean parse(@PathVariable("keyword") String keywords) throws Exception {
        return contentService.parse(keywords);
    }

    /**
     * 分页搜索
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     * @throws Exception
     */
    @GetMapping("/search/{keyword}/{pageNum}/{pageSize}")
    public List<Map<String, Object>> search(@PathVariable("keyword") String keyword,
                                            @PathVariable("pageNum") int pageNum,
                                            @PathVariable("pageSize") int pageSize) throws Exception {
        return contentService.searchWithHighLighter(keyword, pageNum, pageSize);

    }

}
