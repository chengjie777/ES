package estest;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder;
import org.apache.lucene.search.TermQuery;
import org.apache.taglibs.standard.tag.common.core.ForEachSupport;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse.Result;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.MultiSearchResponse.Item;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.network.InetAddresses;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.get.GetField;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.BulkByScrollTask.Status;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequestBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.min.MinAggregationBuilder;
import org.elasticsearch.search.sort.SortOrder;
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
	        map.put("nickname", "试吧" + ran.nextInt(100));  
	        map.put("sex", ran.nextInt(100));  
	        map.put("age", ran.nextInt(100));  
	        map.put("mobile", "111");  
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
		   GetResponse response2 = getTransPortClient().prepareGet("users","user","AWBPiZLDWUYBPgibwIZK").get();
		   String id = response2.getId();
		   String index = response2.getIndex();
		   Map<String, Object> source = response2.getSource();
		   Object object = source.get("nickname");
		   System.out.println(object);
		   String string = response2.toString();
		   System.out.println(string);
	   }
	   //删除索引
	   @Test
	   public void testDelete(){
		   /*DeleteResponse response = getTransPortClient().prepareDelete("users","user","1").get();
		   Result result = response.getResult();*/
		   
		  /* BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(getTransPortClient()).
				   filter(QueryBuilders.matchQuery("age", "92")) //查询条件
				   .source("users") //index名称
				   .get();
		   long deleted = response.getDeleted();
		   System.out.println(deleted);*/
		   DeleteByQueryAction.INSTANCE.newRequestBuilder(getTransPortClient())
		   .filter(QueryBuilders.matchQuery("age","41"))
		   .source("users").execute(new ActionListener<BulkByScrollResponse>() {

			@Override
			public void onFailure(Exception arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onResponse(BulkByScrollResponse response) {
				// TODO Auto-generated method stub
				long deleted = response.getDeleted();
				System.out.println(deleted);
				Status status = response.getStatus();
				System.out.println(status);
				
			}
		});
	   }
	   
	   //更新索引
	  @Test
	  public void testUpdate() throws IOException, InterruptedException, ExecutionException{
		  TransportClient client = getTransPortClient();
		 /* UpdateResponse response = client.prepareUpdate("users","user", "AWBPqtKLWUYBPgibwIZS")
		  .setDoc( XContentFactory.jsonBuilder().startObject()
				  .field("age","89").endObject()).execute().actionGet();
		  
		  Result result = response.getResult();
		  System.out.println(result);
		  String id = response.getId();
		  System.out.println(id);*/
		  
		  
		  IndexRequest request = new IndexRequest("users", "user", "1")
		  .source(XContentFactory.jsonBuilder().startObject()
				  .field("nickname","测试111").field("age","25").field("mobile","123").endObject());
		  
		  UpdateRequest updateRequest = new UpdateRequest("users", "user", "1")
		  			.doc(XContentFactory.jsonBuilder().startObject()
		  					.field("age","33").endObject()).upsert(request);
		  
		  UpdateResponse response2 = client.update(updateRequest).get();
		  String id2 = response2.getId();
		  System.out.println(id2);
		  Result result2 = response2.getResult();
		  System.out.println(result2);
	  }
	  //获取多个
	  @Test
	  public void testmultiGet(){
		  MultiGetResponse multiGetResponse = getTransPortClient().prepareMultiGet().add("users", "user", "1")
		  	.add("users", "user", "AWBN2bvzf0qaO2VSLcRY","AWBN2Hqkf0qaO2VSLcRX").get();  //可以一下查询多个id
		  for (MultiGetItemResponse multiGetItemResponse : multiGetResponse) {
			  GetResponse response = multiGetItemResponse.getResponse();
			  if (response.isExists()) {
				String sourceAsString = response.getSourceAsString();
				System.out.println(sourceAsString);
				System.out.println();
				Map<String, GetField> fields = response.getFields();
				Set<String> keySet = fields.keySet();
				for (String string : keySet) {
					System.out.println("字段:"+string+"  值:"+fields.get(string));
				}
			}
		}
	  }
	  //一下提交多个
	  @Test
	  public void testBulkRequst() throws IOException{
		  BulkRequestBuilder bulkRequest= getTransPortClient().prepareBulk();
		  bulkRequest.add(getTransPortClient().prepareIndex("users", "user", "2")
				  .setSource(XContentFactory.jsonBuilder()
						  .startObject().field("nickname","测试44").field("age","44").field("mobile","185")
						  .endObject()));

		 bulkRequest.add(getTransPortClient().prepareIndex("users", "user", "3")
				  .setSource(XContentFactory.jsonBuilder()
						  .startObject().field("nickname","测试55").field("age","55").field("mobile","1851303")
						  .endObject()));
		 BulkResponse response = bulkRequest.get();
		 RestStatus status = response.status();
		 System.out.println(status);
		 boolean hasFailures = response.hasFailures();
		 System.out.println(hasFailures);
	  }
	  //BulkProcessor类提供一个简单的接口用来自动刷新批量操作,前提是基于请求数量或给定一个周期
	 /* 添加你的elasticsearch客户端
	
	  
	  设置一个自定义的补偿策略，它最初将等待100ms，以指数级增长，并重新尝试三次。当一个或多个bulk请求操作失败时都会伴随着一次请求重试且抛出EsRejectedExecutionException,这表明有太少的计算资源用于处理请求。如果想禁用backoff，通过backoffpolicy . nobackoff()设置来完成.
	  默认情况,BulkProcessor设置如下:

	  设置bulkActions为1000
	  设置bulkSize为5MB
	  不设置flushInterval
	  设置concurrentRequests为1,意味着每个bulk请都会进行异步刷新操作
	  将补偿策略设置为指数补偿类型，8次重试，并开始延迟50毫秒。总等待时间约为5.1秒*/
	  @Test
	  public void testBulkProcess() throws InterruptedException{
		  BulkProcessor processor = BulkProcessor.builder(getTransPortClient(), new BulkProcessor.Listener() {
			
			@Override
			public void beforeBulk(long arg0, BulkRequest arg1) {//在bulk方法执行之前调用,你可以在这里通过request.numberOfActions()方法知道执行的操作数量
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterBulk(long arg0, BulkRequest arg1, Throwable arg2) {// 如果bulk执行失败或抛出一个异常则会调用该方法
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterBulk(long arg0, BulkRequest arg1, BulkResponse arg2) {//在bulk方法执行之后调用,你可以通过response.hasFailures()方法知道哪些失败的请求
				// TODO Auto-generated method stub
				
			}
		}).setBulkActions(1000)  // 我们想每个bulk操作执行10000个请求
		.setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))//我们想每次占用5Mb内存的时候刷新bulk操作
		.setFlushInterval(TimeValue.timeValueSeconds(5))// 我们想每隔5S中刷新内存而忽略请求次数
		.setConcurrentRequests(1)//设置并发请求的数量.如果设置为0则意味着只允许执行一个bulk请求,如果设置为1,则在执行当前bulk请的时候允许执行其他bulk请求.
		.setBackoffPolicy(BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100),3))//设置一个自定义的补偿策略，它最初将等待100ms，以指数级增长，并重新尝试三次。当一个或多个bulk请求操作失败时都会伴随着一次请求重试且抛出EsRejectedExecutionException,这表明有太少的计算资源用于处理请求。如果想禁用backoff，通过backoffpolicy . nobackoff()设置来完成.
		.build();
		  
		  //你可以很轻松的向BulkProcessor中添加请求
		  processor.add(new IndexRequest("users", "user", "6"));
		  processor.add(new DeleteRequest("users","user","1"));
		  
		  processor.awaitClose(10, TimeUnit.MILLISECONDS);
		  
		  //processor.close();
	  }
	   //search api
	  @Test
	  public void testSearch(){
		  SearchResponse response = getTransPortClient().prepareSearch("users")
		  	.setTypes("user")
		  	.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		 	.setQuery(QueryBuilders.termQuery("nickname", "测 or 试"))
		  	.setPostFilter(QueryBuilders.rangeQuery("age").from("20").to("80"))
		  	.setFrom(0).setSize(5).setExplain(true)
		  	.get();
		  
		  SearchHit[] hits = response.getHits().getHits();
		  for (SearchHit searchHit : hits) {
			float score = searchHit.getScore();
			System.out.println(score);
			Map<String, Object> source = searchHit.getSource();
			System.out.println(source);
			String id = searchHit.getId();
			System.out.println(id);
		}
	  }
	  
	  @Test
	  public void testScroll(){
		  SearchResponse response = getTransPortClient().prepareSearch("users").addSort("age", SortOrder.ASC)
		  .setScroll(new TimeValue(60000))
		  .setQuery(QueryBuilders.termQuery("nickname","测"))
		  .setSize(5).get();
		  String scrollId = response.getScrollId();
		  System.out.println("scrollId:"+scrollId);
		  /*do {
			SearchHit[] hits = response.getHits().getHits();
			for (SearchHit searchHit : hits) {
				String id = searchHit.getId();
				float score = searchHit.getScore();
				Map<String, Object> source = searchHit.getSource();
				System.out.println("id:"+id+"  score:"+score+"  source:"+source);
			}
		} while (response.getHits().getHits().length!=0);*/
	  }
	  @Test
	  public void testMultiSearch(){
		  SearchRequestBuilder builder1 = getTransPortClient().prepareSearch("users").setQuery(QueryBuilders.queryStringQuery("0")).setSize(1);
		  
		  SearchRequestBuilder builder2 = getTransPortClient().prepareSearch().setQuery(QueryBuilders.matchQuery("mobile", "185")).setSize(1);
		  MultiSearchResponse response = getTransPortClient().prepareMultiSearch().add(builder1)
		  .add(builder2).get();
		  
		  Item[] responses = response.getResponses(); //得到两个查询的结果集  
		  for (Item item : responses) {//分别遍历两个查询的结果
			SearchHit[] hits = item.getResponse().getHits().getHits();
			for (SearchHit searchHit : hits) {
				Map<String, Object> source = searchHit.getSource();
				System.out.println(source);
			}
		}
		  
	  }
	  @Test
	  public void testAggregation() throws IOException{
		  SearchResponse response = getTransPortClient().prepareSearch("users")
				  .addAggregation(AggregationBuilders.terms("mob").field("nickname")).get();
		  Terms term= response.getAggregations().get("mob");
		  String string = term.toString();
		  System.out.println(string);
		  Object stats = term.getStats();
		  System.out.println(stats);
	  }
	  @Test
	  public void testSearchTemplate(){
		  Map tempalte_params = new HashMap<>();
		  tempalte_params.put("param_gender", "male");
		  SearchResponse response = new SearchTemplateRequestBuilder(getTransPortClient())
		  .setScript("template_gender")   //模板名称
		  .setScriptType(ScriptType.FILE)   //模板存储在磁盘上的gender_template.mustache
		  .setScriptParams(tempalte_params)  //参数
		  .setRequest(new SearchRequest())	//设置执行上下文(比如在这里定义一个索引名称)
		  .get()							//执行和获取模板请
		  .getResponse();
		  
		  SearchHit[] hits = response.getHits().hits();
		  for (SearchHit searchHit : hits) {
			System.out.println(searchHit.getId());
		}
	  }
	  
	  @Test
	  public void testUsingAggregation() throws InterruptedException, ExecutionException, IOException{
		  
		  SearchResponse searchResponse = getTransPortClient().prepareSearch("users")
				  .addAggregation(
						  AggregationBuilders.max("maxAge").field("age")
						  .subAggregation(AggregationBuilders.terms("name").field("测"))
						  ).execute().get();
		  Max max = searchResponse.getAggregations().get("maxAge");
		  Terms term = searchResponse.getAggregations().get("name");
		  double value = max.getValue();
		  System.out.println(value);
		  long size = term.size();
		  System.out.println(size);
	  }
	  /*
	   * query stringQuery
	   * 参数	描述
		query:实际的查询被解析。参见查询字符串语法。
		default_field: 查询字词的默认字段，如果没有指定前缀字段。默认为index.query.default_field索引设置，依次默认为*。 *提取映射中符合条件查询条件的所有字段并过滤元数据字段。当没有提供前缀字段时，所有提取的字段被组合起来构建一个查询。

		default_operator: 如果未指定明确的运算符，则使用默认运算符。例如，用一个默认的操作符OR，将查询 capital of Hungary翻译为capital OR of OR Hungary，并用默认的操作符AND将相同的查询翻译成 capital AND of AND Hungary。默认值是OR。

		analyzer: 用于分析查询字符串的分析器名称。

		quote_analyzer: 用于分析查询字符串中的引用短语的分析器的名称。对于这些部分，它将覆盖使用analyzer参数或 search_quote_analyzer设置设置的其他分析仪。

		allow_leading_wildcard: 设置时，*或?允许作为第一个字符。默认为true。

		enable_position_increments: 设置为true在结果查询中启用位置增量。默认为true。

		fuzzy_max_expansions: 控制模糊查询将扩展到的词数。默认为50

		fuzziness: 设置模糊查询的模糊性。默认为AUTO。请参阅模糊性编辑允许的设置。

		fuzzy_prefix_length: 设置模糊查询的前缀长度。默认是0。

		phrase_slop: 设置短语的默认斜率。如果为零，则需要精确的词组匹配。默认值是0。

		boost: 设置查询的提升值。默认为1.0。

		auto_generate_phrase_queries: 默认为false。

		analyze_wildcard: 默认情况下，不会分析查询字符串中的通配符字词。通过设定这个值true，我们会尽最大的努力来分析这些值。

		max_determinized_states: 限制允许创建多少个自动机状态正则表达式查询。这可以防止太难（例如指数级）的正则表达式。默认为10000。

		minimum_should_match: 一个值控制生成的布尔查询中多少“应该”子句应该匹配。它可以是绝对值（2），百分比（30%）或两者的 组合。

		lenient: 如果设置为true将导致基于格式的失败（如提供文本到数字字段）被忽略。

		time_zone: 时区适用于与日期相关的任何范围查询。另请参阅 JODA时区。

		quote_field_suffix: 追加到查询字符串的引用部分字段的后缀。这允许使用具有不同分析链的字段进行精确匹配。看看这里为一个完整的例子。

		all_fields: [ 6.0.0 ] 在6.0.0中已弃用。设置default_field为*改为 对可以查询的映射中检测到的所有字段执行查询。在_all禁用字段时将被默认使用，并且 default_field在索引设置或请求正文fields中指定为no（不指定）。
*/
 @Test
 public void testDSLQuery() throws InterruptedException, ExecutionException{
	 	//matchquery
		  SearchResponse response = getTransPortClient().prepareSearch("users").setQuery(QueryBuilders.matchQuery("nickname", "测试")).execute().actionGet();
		  //multimatchquery
		  SearchResponse response2 = getTransPortClient().prepareSearch("users").setQuery(QueryBuilders.multiMatchQuery("111", "nickname","mobile")).execute().actionGet();
		  //querystring
		  SearchResponse response3 = getTransPortClient().prepareSearch("users").setQuery(QueryBuilders.queryStringQuery("+测试 -111").defaultField("nickname")).execute().get();
		  //simple query string
		  //默认是从所有字段中查找匹配的词可以通过filed字段说明查询哪个字段,field可以添加多个
		  SearchResponse response4 = getTransPortClient().prepareSearch("users").setQuery(QueryBuilders.simpleQueryStringQuery("111").field("nickname").field("mobile").defaultOperator(Operator.OR)).execute().actionGet();
		  //rangeQuery
		  SearchResponse response5 = getTransPortClient().prepareSearch("users").setQuery(QueryBuilders.rangeQuery("age").from("50").to("90")).execute().get();
		  //prefixQuery  以什么开头的
		  SearchResponse response6 = getTransPortClient().prepareSearch("users").setQuery(QueryBuilders.prefixQuery("nickname", "测")).execute().get();
		  //wildcard	扩展词  通配符? 多只符的通配符
		  SearchResponse response7 = getTransPortClient().prepareSearch("users").setQuery(QueryBuilders.wildcardQuery("nickname", "试*")).execute().get();
		  //通过ids查询   idsquery中放的是类型type  后面加的是id
		  SearchResponse response8 = getTransPortClient().prepareSearch("users").setQuery(QueryBuilders.idsQuery("user").addIds("1","2")).execute().get();
		  
		  
		  SearchHit[] hits = response8.getHits().getHits();
		for (SearchHit searchHit : hits) {
			Map<String, Object> source = searchHit.getSource();
			System.out.println(source);
		}
		 /*SearchHit[] hits2 = response3.getHits().getHits();
		 for (SearchHit searchHit : hits2) {
			 Map<String, Object> source = searchHit.getSource();
				System.out.println(source);
		}*/
	  }
 //复合查询
 @Test
 public void testCommandQuery(){
	 //恒定分数查询
	 SearchResponse response = getTransPortClient().prepareSearch("users").setQuery(QueryBuilders.constantScoreQuery(QueryBuilders.termQuery("nickname","测试")).boost(1f)).execute().actionGet();
	 //布尔查询  should must mustnot filter
	 SearchResponse response2 = getTransPortClient().prepareSearch("users").setQuery(QueryBuilders.boolQuery()
			 .must(QueryBuilders.termQuery("nickname", "测"))
			 .must(QueryBuilders.termQuery("nickname", "试"))
			 .mustNot(QueryBuilders.termQuery("nickname", "111"))
			 .should(QueryBuilders.termQuery("mobile", "185"))
			 .should(QueryBuilders.termQuery("nickname", "95"))
				.filter(QueryBuilders.rangeQuery("age").gte("70")) 
			).execute().actionGet();
	 //最大值查询   boost加权重  dismax查询会查询更加匹配的  多个查询条件下更加匹配的  即使只满足一个条件他也会显示   参考:  http://blog.csdn.net/dm_vincent/article/details/41820537
	 SearchResponse response3 = getTransPortClient().prepareSearch("users").setQuery(
			 QueryBuilders.disMaxQuery()
			 .add(QueryBuilders.termQuery("nickname", "测"))
			 .add(QueryBuilders.termQuery("mobile", "185"))
			 .boost(2f)
			 .tieBreaker(0.3f)
			 ).execute().actionGet();
	 
	 SearchHit[] hits = response3.getHits().getHits();
	 for (SearchHit searchHit : hits) {
			Map<String, Object> source = searchHit.getSource();
			System.out.println(source+" score:"+searchHit.getScore());
		}
 }
}
