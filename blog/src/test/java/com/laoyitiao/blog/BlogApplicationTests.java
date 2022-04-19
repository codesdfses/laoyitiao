package com.laoyitiao.blog;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
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
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class BlogApplicationTests {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    // 创建索引（数据库）
    @Test
    void testCreateIndex() throws IOException {
        CreateIndexRequest indexRequest = new CreateIndexRequest("索引1");
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(indexRequest, RequestOptions.DEFAULT);
        System.out.println(createIndexResponse);
    }

    @Test
    void testExistIndex() throws IOException {
        GetIndexRequest indexRequest = new GetIndexRequest("索引1");
        boolean exists = restHighLevelClient.indices().exists(indexRequest, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    @Test
    void testDeleteIndex() throws IOException {
        DeleteIndexRequest indexRequest = new DeleteIndexRequest("索引");
        AcknowledgedResponse delete = restHighLevelClient.indices().delete(indexRequest, RequestOptions.DEFAULT);
        System.out.println(delete.isAcknowledged());
    }

    // 创建文档（数据）
    @Test
    void testAddDocument() throws IOException {

            IndexRequest request = new IndexRequest("索引1");
            request.id("5");
            request.timeout(TimeValue.timeValueSeconds(1));
            HashMap<Object, Object> map = new HashMap<>();
            map.put("name","张三");
            map.put("age",88);
            // source数据必须和指定的格式一致
            request.source(JSON.toJSONString(map), XContentType.JSON);
            IndexResponse index = restHighLevelClient.index(request, RequestOptions.DEFAULT);
            System.out.println(index.toString()+","+index.status());

    }

    @Test
    void testGetDocument() throws IOException {
        GetRequest getRequest = new GetRequest("索引1","1");
        GetResponse response = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        System.out.println(response.getSourceAsString());
        System.out.println(response.getSource());
    }

    @Test
    void testUpdateDocument() throws IOException {
        UpdateRequest request = new UpdateRequest("索引1", "1");
        HashMap<Object, Object> map = new HashMap<>();
        map.put("name","hello,world X2");
        map.put("age",88);
        request.doc(JSON.toJSONString(map),XContentType.JSON);
        UpdateResponse response = restHighLevelClient.update(request, RequestOptions.DEFAULT);
        // 成功返回 OK
        System.out.println(response.status());
    }

    @Test
    void testDeleteDocument() throws IOException {
        DeleteRequest request = new DeleteRequest("索引1", "1");
        DeleteResponse response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        // 成功返回 OK
        System.out.println(response.status());
    }

    // 查询
    @Test
    void testQueryDocument() throws IOException {
        SearchRequest request = new SearchRequest("索引1");
        // 构建搜索源
        SearchSourceBuilder builder = new SearchSourceBuilder();

        // 设置高亮字段
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.field("name");
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        builder.highlighter(highlightBuilder);

        // 构建搜索条件
        TermQueryBuilder termQuery = QueryBuilders.termQuery("name", "三");
        builder.query(termQuery);

        builder.timeout(TimeValue.timeValueSeconds(3));
        // 分页
        builder.from(0);
        builder.size(20);

        // 填充请求
        request.source(builder);

        // 发送搜索请求
        SearchResponse search = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        System.out.println(search.toString());
        for (SearchHit hit : search.getHits().getHits()) {
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField field = highlightFields.get("name");
            if (field!=null){
                // 把高亮字段替换到搜索结果中
                Text[] fragments = field.fragments();
                String newFiled = "";
                for (Text fragment : fragments) {
                    newFiled += fragment;
                }
                hit.getSourceAsMap().put("name",newFiled);
            }
            System.out.println(hit.getSourceAsMap());
            System.out.println(hit.getSourceAsString());
        }
    }




}
