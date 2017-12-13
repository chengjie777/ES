package estest;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.network.InetAddresses;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.min.MinAggregationBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;

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
	   
}
