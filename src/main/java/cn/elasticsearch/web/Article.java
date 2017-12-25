package cn.elasticsearch.web;

import java.util.Locale.Category;

public class Article {
	 public static final String DEFAULT_TEMPLATE ="frontViewArticle";
     
     private static final long serialVersionUID= 1L;
     private Category category;//分类编号
    
     @ESearchTypeColumn
     private String title;       // 标题
     private String link;  // 外部链接
     private String color;     // 标题颜色（red：红色；green：绿色；blue：蓝色；yellow：黄色；orange：橙色）
     private String image;    // 文章图片
     private String keywords;//关键字
    
     @ESearchTypeColumn
     private String description;//描述、摘要

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static String getDefaultTemplate() {
		return DEFAULT_TEMPLATE;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Article(Category category, String title, String link, String color,
			String image, String keywords, String description) {
		super();
		this.category = category;
		this.title = title;
		this.link = link;
		this.color = color;
		this.image = image;
		this.keywords = keywords;
		this.description = description;
	}

	public Article() {
		super();
		// TODO Auto-generated constructor stub
	}
     
     
}
