package cn.elasticsearch.web;

import java.util.List;

public class ElasticSearchPage<T> {
	private String scrollId;
	 
    private long total;
 
    private int pageSize;
 
    private int pageNum;
 
    private T param;
 
    private List<T> retList;
 
    private List<String> scrollIds;
 
    public List<String> getScrollIds() {
        return scrollIds;
    }
 
    public void setScrollIds(List<String>scrollIds) {
        this.scrollIds =scrollIds;
    }
 
    public List<T> getRetList() {
        return retList;
    }
 
    public void setRetList(List<T>retList) {
        this.retList =retList;
    }
 
    public String getScrollId() {
        return scrollId;
    }
 
    public void setScrollId(String scrollId) {
        this.scrollId =scrollId;
    }
 
    public long getTotal() {
        return total;
    }
 
    public void setTotal(long total){
        this.total =total;
    }
 
    public int getPageSize() {
        if(pageSize > 50){
            return 50;
        }else{
            return pageSize;
        }
    }
 
    public void setPageSize(int pageSize){
        this.pageSize =pageSize;
    }
 
    public int getPageNum() {
        if(pageNum <=0){
            return 0;
        }else{
            return pageNum -= 1;
        }
    }
 
    public void setPageNum(int pageNum){
        this.pageNum =pageNum;
    }
 
    public T getParam() {
        return param;
    }
 
    public void setParam(T param) {
        this.param =param;
    }
 
    @Override
    public String toString() {
        return"Page{" +
                "scrollId='"+ scrollId + '\''+
                ",total=" + total +
                ",pageSize=" + pageSize +
                ",pageNum=" + pageNum +
                ",param=" + param +
                ",retList=" + retList +
                ",scrollIds=" + scrollIds +
                '}';
    }
}
