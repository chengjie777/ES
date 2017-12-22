package cn.elasticsearch.domain.po;

import org.springframework.data.elasticsearch.annotations.Document;

public class Book implements CharSequence{
	private String id;
	private String bookName;
	private String author;
	private String pubHouse;
	private String pubDate;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String book_name) {
		this.bookName = book_name;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getPubHouse() {
		return pubHouse;
	}
	public void setPubHouse(String pubHouse) {
		this.pubHouse = pubHouse;
	}
	public String getPubDate() {
		return pubDate;
	}
	public void setPubDate(String pub_date) {
		this.pubDate = pub_date;
	}
	public Book(String id, String book_name, String author, String pubHouse,
			String pub_date) {
		super();
		this.id = id;
		this.bookName = book_name;
		this.author = author;
		this.pubHouse = pubHouse;
		this.pubDate = pub_date;
	}
	public Book() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "Book [id=" + id + ", book_name=" + bookName + ", author="
				+ author + ", pubHouse=" + pubHouse + ", pub_date=" + pubDate
				+ "]";
	}
	@Override
	public int length() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public char charAt(int index) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public CharSequence subSequence(int start, int end) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
}
