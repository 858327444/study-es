package com.study.es.service;

import java.util.List;
import java.util.Map;

/**
 * Program Name: es-study-api
 * Created by yanlp on 2021-01-03
 *
 * @author yanlp
 * @version 1.0
 */
public interface ContentService {
    /**
     * 爬取数据,并入es
     *
     * @param keywords
     * @return
     * @throws Exception
     */
    boolean parse(String keywords) throws Exception;

    /**
     * 搜索数据
     *
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<Map<String, Object>> search(String keyword, int pageNum, int pageSize) throws Exception;

    /**
     * 搜索数据,带高亮
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> searchWithHighLighter(String keyword, int pageNum, int pageSize) throws Exception;

}
