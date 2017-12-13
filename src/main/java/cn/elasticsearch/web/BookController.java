package cn.elasticsearch.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.elasticsearch.domain.Book;
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
}
