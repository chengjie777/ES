package cn.elasticsearch.web;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ESearchTypeColumn {
	    /**
	    * 字段类型
	    *
	    * @return
	    */
	   String type() default "string";
	 
	   /**
	    * 是否分词
	    *
	    * @return
	    */
	   boolean analyze() default false;
}
