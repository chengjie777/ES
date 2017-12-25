package cn.elasticsearch.web;

import javax.annotation.Resource;

import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.Test;

public class ArticleRepositoryTest extends SearchBaseTest{
    @Resource(name="transportClientRepository")
   TransportClientRepository client;
 
   @Test
    public void findByNameAndPrice() throws Exception{
       System.out.println(client.getIdx("blog1","type","2001"));
    }
 
   @Test
   public void saveDoc() throws Exception{
      Article article = new Article();
      article.setDescription("东南太阳汽车出新车了DX9");
      article.setTitle("旗帜迎风飘扬");
      article.setKeywords("2006");
      article.setColor("red");
      article.setImage("2017/6/7");
      System.out.println(client.saveDoc("blog1","type",article.getTitle(),article));
   }
  
   @Test
   public void searchFullText(){
      Article param = new Article();
      param.setDescription("太阳");
      ElasticSearchPage<Article> page= new ElasticSearchPage<Article>();
      page.setPageSize(10);
      HighlightBuilder highlight = new HighlightBuilder();
      highlight.field("description").preTags("<span style=\"color:red\">").postTags("</span>");
      page = client.searchFullText(param,page, highlight,"blog1");
      for(Article aa : page.getRetList()){
          System.out.println(aa.getTitle() +"===="+aa.getDescription()+"===title:=="+aa.getTitle());
      }
      System.out.println(page.getTotal());
   }

}
