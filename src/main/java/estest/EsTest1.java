package estest;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse.Result;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.network.InetAddresses;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.min.MinAggregationBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;

import cn.elasticsearch.domain.po.Book;

public class EsTest1 {
	 private static TransportClient  transPort = null;   
	    private String esClusterName="rmsCloud";//集群名
	    private String esServerIps="10.20.19.215";//集群服务IP集合
	    private Integer esServerPort=9300;//ES集群端口


	 /**
	     *  ES TransPortClient 客户端连接<br>
	     *  在elasticsearch平台中,可以执行创建索引,获取索引,删除索引,搜索索引等操作
	     * @return
	     */
	    public TransportClient getTransPortClient() {
	        try {
	            if (transPort == null) {

	                if(esServerIps == null || "".equals(esServerIps.trim())){
	                    return  null;
	                }

	                Settings settings = Settings.builder()
	                      .put("cluster.name", esClusterName)// 集群名
	                        .put("client.transport.sniff", true)
	                        // 自动把集群下的机器添加到列表中

	                        .build();
	                transPort  = new  PreBuiltTransportClient(settings);
	                String esIps[] = esServerIps.split(",");
	                for (String esIp : esIps) {//添加集群IP列表
	                    TransportAddress transportAddress =  new InetSocketTransportAddress(InetAddresses.forString(esIp),esServerPort);
	                    transPort.addTransportAddresses(transportAddress);
	                }
	                return transPort;
	            } else {
	                return transPort;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            if (transPort != null) {
	                transPort.close();
	            }
	            return null;
	        }
	    }
	   @Test
	   public void testAdd(){
		   TransportClient client = getTransPortClient();
		   Map<String, Object> map = new HashMap<String, Object>();  
	        Random ran = new Random();  
	        map.put("nickname", "测试" + ran.nextInt(100));  
	        map.put("sex", ran.nextInt(100));  
	        map.put("age", ran.nextInt(100));  
	        map.put("mobile", "15014243232");  
	        IndexResponse response = client.prepareIndex("users", "user").setSource(map).get(); 
	        System.out.println(response);
	        MinAggregationBuilder minAggregationBuilder = AggregationBuilders.min("agg").field("height");
	        SearchResponse response2 = client.prepareSearch().addAggregation(minAggregationBuilder).execute().actionGet();
	   }
	   @Test
	   public void testJson() throws Exception{
		   ObjectMapper om=new ObjectMapper();
		   Book book=new Book();
		   book.setAuthor("中信所");
		   book.setBookName("javaScript");
		   book.setId("111");
		   book.setPubDate("20171213");
		   book.setPubHouse("科学技术出版社");
		   String writeValueAsString = om.writeValueAsString(book);
		   
		   System.out.println(writeValueAsString);
		   Book readValue = om.readValue(writeValueAsString, Book.class);
		   
		   System.out.println(readValue);
		   XContentBuilder jsonBuilder = XContentFactory.jsonBuilder();
		   XContentBuilder builder = jsonBuilder.startObject().field("book_name","深入理解.net").field("id","222").endObject();
		   IndexResponse response = getTransPortClient().prepareIndex().setSource(builder).execute().actionGet();
		   
	   }
	   
	   @Test
	   public void testGet(){
		   GetResponse response2 = getTransPortClient().prepareGet("users","user","1").get();
		   String id = response2.getId();
		   String index = response2.getIndex();
		   Map<String, Object> source = response2.getSource();
		   Object object = source.get("username");
		   
		   String string = response2.toString();
		   
	   }
	   
	   @Test
	   public void testDelete(){
		   DeleteResponse response = getTransPortClient().prepareDelete("users","user","1").get();
		   Result result = response.getResult();
		   
		   DeleteByQueryAction.INSTANCE.newRequestBuilder(getTransPortClient()).filter(QueryBuilders.matchQuery("users", "user")).source("username").execute(new ActionListener<BulkByScrollResponse>() {
			
			@Override
			public void onResponse(BulkByScrollResponse response) {
				// TODO Auto-generated method stub
				long deleted = response.getDeleted();
			}
			
			@Override
			public void onFailure(Exception arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	   }
	   
	   
}
