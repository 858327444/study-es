package com.study;

import com.alibaba.fastjson.JSON;
import com.study.es.entity.User;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class EsApiApplicationTests {

    @Autowired
    private RestHighLevelClient restHighLevelClient;
    
    private final String DEFAULT_INDEX_NAME = "kuang_index";

    // 创建索引
    @Test
    public void createIndex() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(DEFAULT_INDEX_NAME);
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        System.out.println("###############  " + createIndexResponse);
    }

    // 索引是否存在
    @Test
    public void existIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest(DEFAULT_INDEX_NAME);
        boolean exists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println("###############  " + exists);
    }

    // 删除索引
    @Test
    public void deleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest(DEFAULT_INDEX_NAME);
        AcknowledgedResponse response = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        System.out.println("###############  " +response.isAcknowledged());
    }

    // 新增文档
    @Test
    public void addDocument() throws IOException {
        // 创建对象
        User user = User.builder().name("闫立鹏").age(26).build();
        // 创建请求
        IndexRequest request = new IndexRequest(DEFAULT_INDEX_NAME);
        // 规则
        request.id("1");
        request.timeout(TimeValue.timeValueSeconds(1));
        // 将我们的数据放入请求,json
        request.source(JSON.toJSONString(user), XContentType.JSON);
        // 客户端发送请求,获取响应的结果
        IndexResponse index = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        System.out.println(index);
        System.out.println(index.status());
    }

    // 获得文档信息
    @Test
    public void getDocument() throws IOException {
        GetRequest request = new GetRequest(DEFAULT_INDEX_NAME,"1");
        GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        System.out.println(response.getSourceAsString());
        System.out.println(response);
    }

    // 更新文档信息
    @Test
    public void updateDocument() throws IOException {
        UpdateRequest request = new UpdateRequest(DEFAULT_INDEX_NAME, "1");
        User user = User.builder().name("张三").age(40).build();
        request.doc(JSON.toJSONString(user), XContentType.JSON);
        UpdateResponse response = restHighLevelClient.update(request, RequestOptions.DEFAULT);
        System.out.println(response);
    }

    // 删除文档
    @Test
    public void deleteDocument() throws IOException {
        DeleteRequest request = new DeleteRequest(DEFAULT_INDEX_NAME,"1");
        DeleteResponse deleteResponse = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        System.out.println(deleteResponse);
        System.out.println(deleteResponse.status());
    }

    // 批量操作
    @Test
    public void bulkRequest() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        List<User> userList = new ArrayList<>();
        userList.add(User.builder().name("张三1").age(1).build());
        userList.add(User.builder().name("张三2").age(2).build());
        userList.add(User.builder().name("张三3").age(3).build());
        userList.add(User.builder().name("张三4").age(4).build());

        for (int i = 0; i < userList.size(); i++) {
            // 如果不写id,默认是随机id
            bulkRequest.add(new IndexRequest(DEFAULT_INDEX_NAME)
                        .id("" + (i + 1))
                    .source(JSON.toJSONString(userList.get(i)),XContentType.JSON));
        }

        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(bulkResponse.hasFailures());
    }

    // 查询
    @Test
    public void searchRequest() throws IOException {
        SearchRequest searchRequest = new SearchRequest(DEFAULT_INDEX_NAME);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("_id", "1");
        searchSourceBuilder.query(termQueryBuilder);
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(searchResponse.getHits()));

        System.out.println("#######################");
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());

        }




    }

}
