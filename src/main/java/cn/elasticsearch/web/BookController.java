package cn.elasticsearch.web;

import java.util.List;

import org.apache.http.client.methods.RequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysql.fabric.xmlrpc.base.Array;

import cn.elasticsearch.domain.po.Book;
import cn.elasticsearch.service.BookService;

@Controller
public class BookController {
	@Autowired
	private BookService bookService;
	@Autowired
	private Client client;
@ResponseBody
@RequestMapping(value="/bookList")
public List<Book> findAllBooks(){
	return bookService.findAllBook();
}
@ResponseBody
@RequestMapping(value="/bookListEs")
public String findBookByQuery(){
	SearchResponse response = client.prepareSearch("users")
  	.setTypes("user")
  	.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
 	.setQuery(QueryBuilders.termQuery("nickname", "测"))
  	.setPostFilter(QueryBuilders.rangeQuery("age").from("20").to("80"))
  	.setFrom(0).setSize(5).setExplain(true)
  	.get();
	SearchHit[] hits = response.getHits().getHits();
	StringBuffer sb=new StringBuffer();
	for (SearchHit searchHit : hits) {
		sb.append(searchHit.getSourceAsString());
		sb.append("/r/n");
	}
	return sb.toString();
}
}
