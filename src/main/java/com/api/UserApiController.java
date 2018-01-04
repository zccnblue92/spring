package com.api;

import com.Vo.User;
import com.commmon.util.Elastaic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.Userservice;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Api(value="user", description = "用户")
@RestController
@Controller
@RequestMapping(value="/user")
public class UserApiController {

    //private static Logger logger = Logger.getLogger(Test.class);

        @Autowired
        private Elastaic elastaic;
        @Autowired
        private Userservice userservice;


        @Authorization("需token")
        @RequestMapping(value = "/getUser")
        @ApiOperation(value="根据ID获取用户信息",httpMethod="POST",notes="get user by id")
        public User getUser(@ApiParam(required=true,value="用户ID",name="userId")@RequestParam(value="userId")Integer userId) {
            return userservice.getUserByid(1);
        }

        @RequestMapping(value = "/elastic")
        public void elastic() throws UnknownHostException, JsonProcessingException {

            List<User> list=userservice.getlist();
            ObjectMapper mapper = new ObjectMapper();
            List<User> persons = new ArrayList<User>();
            List<IndexRequest> requests = new ArrayList<IndexRequest>();

            for (User li:list) {
                User person = new User();

                person.setAge(li.getAge());
                person.setId(li.getId());
                person.setName(li.getName());
                person.setJob(li.getJob());

                persons.add(person);

                String index = "user"; // 相当于数据库名
                String type = "user"; // 相当于表名

                String json = mapper.writeValueAsString(person);
                IndexRequest request = elastaic.client().prepareIndex(index, type).setSource(json).request();
                requests.add(request);
            }

            BulkRequestBuilder bulkRequest = elastaic.client().prepareBulk();

            for (IndexRequest request : requests) {
                bulkRequest.add(request);
            }

            BulkResponse bulkResponse = bulkRequest.execute().actionGet();
            if (bulkResponse.hasFailures()) {
                Assert.fail("批量创建索引错误！");
            }

        }

    @RequestMapping(value = "/elasticList")
    public void elasticList(String seachname) throws UnknownHostException, JsonProcessingException {


                //Client client = createTransportClient();
                SearchResponse response = elastaic.client().prepareSearch("user").setTypes("user")
                        // 设置查询类型
                        // 1.SearchType.DFS_QUERY_THEN_FETCH = 精确查询
                        // 2.SearchType.SCAN = 扫描查询,无序
                        // 3.SearchType.COUNT = 不设置的话,这个为默认值,还有的自己去试试吧
                        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                        // 设置查询关键词
                        .setQuery(QueryBuilders.matchQuery("user", seachname))
                      /* .addHighlightedField("user")
                        .setHighlighterPreTags("<em>")
                        .setHighlighterPostTags("</em>")*/
                        // 设置查询数据的位置,分页用
                        .setFrom(0)
                        // 设置查询结果集的最大条数
                        .setSize(60)
                        // 设置是否按查询匹配度排序
                        .setExplain(true)
                        // 最后就是返回搜索响应信息
                        .execute()
                        .actionGet();
                    SearchHits searchHits = response.getHits();
                    System.out.println("-----------------在[user]中搜索关键字["+seachname+"]---------------------");
                    System.out.println("共匹配到:"+searchHits.getTotalHits()+"条记录!");
                    SearchHit[] hits = searchHits.getHits();
                for (SearchHit searchHit : hits) {
                        //获取高亮的字段
                    Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
                    HighlightField highlightField = highlightFields.get("user");
                    System.out.println("高亮字段:"+highlightField.getName()+"\n高亮部分内容:"+highlightField.getFragments()[0].string());
                    Map<String, Object> sourceAsMap = searchHit.sourceAsMap();
                    Set<String> keySet = sourceAsMap.keySet();
                    for (String string : keySet) {
                         //key value 值对应关系
                        System.out.println(string+":"+sourceAsMap.get(string));
                    }
                    System.out.println();
                }


    }
}