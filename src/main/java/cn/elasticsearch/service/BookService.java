package cn.elasticsearch.service;

import java.util.List;

import cn.elasticsearch.domain.Book;

public interface BookService {
	public List<Book> findAllBook();
}
