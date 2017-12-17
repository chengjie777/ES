package cn.elasticsearch.web;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.elasticsearch.domain.po.Book;
import cn.elasticsearch.service.BookService;

@Controller
public class BookController {
	@Autowired
	private BookService bookService;
@ResponseBody
@RequestMapping(value="/bookList")
public List<Book> findAllBooks(){
	return bookService.findAllBook();
}
@RequestMapping(value="/save")
public void saveBook(){
	Book book=new Book();
	book.setBookName("spring4.0");
	book.setAuthor("詹姆斯");
	book.setPubDate(new Date().toString());
	book.setPubHouse("清华出版社");
	String string = UUID.randomUUID().toString();
	
	System.out.println(string);
	book.setId(string);
	bookService.saveBook(book);
	System.out.println("保存成功");
}
}
