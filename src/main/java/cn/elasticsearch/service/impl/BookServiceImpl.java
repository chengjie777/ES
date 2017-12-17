package cn.elasticsearch.service.impl;

import java.util.List;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.elasticsearch.dao.BookDao;
import cn.elasticsearch.domain.po.Book;
import cn.elasticsearch.service.BookService;
@Service
public class BookServiceImpl implements BookService {
@Autowired
private BookDao bookDao;
	@Override
	public List<Book> findAllBook() {
		
		return bookDao.findAll();
	}
	@Override
	public void saveBook(Book book) {
		// TODO Auto-generated method stub
		bookDao.save(book);
		throw new RuntimeException();
	}

}
