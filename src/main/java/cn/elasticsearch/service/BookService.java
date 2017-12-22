package cn.elasticsearch.service;

import java.util.List;

import cn.elasticsearch.domain.po.Book;


public interface BookService {
	public List<Book> findAllBook();

	public void saveBook(Book book);
}
