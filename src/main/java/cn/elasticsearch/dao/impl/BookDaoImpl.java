package cn.elasticsearch.dao.impl;

import org.springframework.stereotype.Repository;

import cn.elasticsearch.dao.BookDao;
import cn.elasticsearch.domain.po.Book;
@Repository
public class BookDaoImpl extends BaseDaoImpl<Book> implements BookDao{

}
