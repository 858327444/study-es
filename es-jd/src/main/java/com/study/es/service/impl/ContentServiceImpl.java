package com.study.es.service.impl;

import com.alibaba.fastjson.JSON;
import com.study.es.entity.Content;
import com.study.es.service.ContentService;
import com.study.es.utils.HtmlParseUtil;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Program Name: es-study-api
 * Created by yanlp on 2021-01-03
 *
 * @author yanlp
 * @version 1.0
 */
@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public boolean parse(String keywords) throws Exception {
        List<Content> contentList = HtmlParseUtil.parse(keywords);

        BulkRequest bulkRequest = new BulkRequest();

        for (Content content : contentList) {
            bulkRequest.add(new IndexRequest("jd_goods")
                    .source(JSON.toJSONString(content), XContentType.JSON));
        }
        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        return !bulkResponse.hasFailures();

    }

    @Override
    public List<Map<String, Object>> search(String keyword, int pageNum, int pageSize) throws Exception{
        if (pageNum < 1) {
            pageNum = 1;
        }
        SearchRequest request = new SearchRequest("jd_goods");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        TermQueryBuilder queryBuilder = QueryBuilders.termQuery("title", keyword);

        sourceBuilder.from(pageNum);
        sourceBuilder.size(pageSize);
        sourceBuilder.query(queryBuilder);
        request.source(sourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        List<Map<String, Object>> res = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            res.add(hit.getSourceAsMap());
        }
        return res;
    }

    @Override
    public List<Map<String, Object>> searchWithHighLighter(String keyword, int pageNum, int pageSize) throws Exception {
        if (pageNum < 1) {
            pageNum = 1;
        }
        SearchRequest request = new SearchRequest("jd_goods");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        TermQueryBuilder queryBuilder = QueryBuilders.termQuery("title", keyword);

        sourceBuilder.from(pageNum);
        sourceBuilder.size(pageSize);
        sourceBuilder.query(queryBuilder);
        // 高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        // 多个高亮显示
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.field("title");
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        sourceBuilder.highlighter(highlightBuilder);

        request.source(sourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        List<Map<String, Object>> res = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            // 原来的结果
            Map<String, Object> hitSourceAsMap = hit.getSourceAsMap();
            // 高亮字段
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField titleField = highlightFields.get("title");
            StringBuilder new_title = new StringBuilder();
            if (titleField != null) {
                for (Text text : titleField.getFragments()) {
                    new_title.append(text);
                }
            }
            // 替换掉原来的内容即可
            hitSourceAsMap.put("title",new_title.toString());

            res.add(hitSourceAsMap);
        }
        return res;
    }

}
