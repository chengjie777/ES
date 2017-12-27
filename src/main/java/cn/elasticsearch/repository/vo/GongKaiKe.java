package cn.elasticsearch.repository.vo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author Administrator
 *
 */
@Document(indexName="lesson" ,type="gongkaike")
public class GongKaiKe {
	@Id
	@Field(index =false , store = true)
	private String id;
	@Field(type=FieldType.text,analyzer="ik",searchAnalyzer="ik",store=true)
	private String lessonname;
	@Field(type=FieldType.text,index =false ,store=true)
	private String author;
	@Field(type=FieldType.text,analyzer="ik",searchAnalyzer="ik",store=true)
	private String lessoninfor;
	@Field(type=FieldType.text,analyzer="ik",searchAnalyzer="ik",store=true)
	private String authorinfor;
	
	@Field(type=FieldType.Integer,index =false , store = true)
	private Integer lessonNum;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLessonname() {
		return lessonname;
	}
	public void setLessonname(String lessonname) {
		this.lessonname = lessonname;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getLessoninfor() {
		return lessoninfor;
	}
	public void setLessoninfor(String lessoninfor) {
		this.lessoninfor = lessoninfor;
	}
	public String getAuthorinfor() {
		return authorinfor;
	}
	public void setAuthorinfor(String authorinfor) {
		this.authorinfor = authorinfor;
	}
	public Integer getLessonNum() {
		return lessonNum;
	}
	public void setLessonNum(Integer lessonNum) {
		this.lessonNum = lessonNum;
	}
	public GongKaiKe() {
		super();
		// TODO Auto-generated constructor stub
	}
	public GongKaiKe(String id, String lessonname, String author,
			String lessoninfor, String authorinfor, Integer lessonNum) {
		super();
		this.id = id;
		this.lessonname = lessonname;
		this.author = author;
		this.lessoninfor = lessoninfor;
		this.authorinfor = authorinfor;
		this.lessonNum = lessonNum;
	}
	@Override
	public String toString() {
		return "GongKaiKe [id=" + id + ", lessonname=" + lessonname
				+ ", author=" + author + ", lessoninfor=" + lessoninfor
				+ ", authorinfor=" + authorinfor + ", lessonNum=" + lessonNum
				+ "]";
	}
	
	
	
	
}
