package cn.itheima.jd.pojo;

import java.util.List;

public class Result<T> {
	
	//当前页
	private Integer curPage;
	//总页数
	private Integer pageCount;
	//总记录数
	private Integer recordCount;
	//上一页
	private Integer prev;
	//下一页
	private Integer next;
	//结果集合list
	private List<T> list;

	public Integer getCurPage() {
		return curPage;
	}

	public void setCurPage(Integer curPage) {
		this.curPage = curPage;
	}

	public Integer getPageCount() {
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public Integer getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(Integer recordCount) {
		this.recordCount = recordCount;
	}

	public Integer getPrev() {
		return prev;
	}

	public void setPrev(Integer prev) {
		this.prev = prev;
	}

	public Integer getNext() {
		return next;
	}

	public void setNext(Integer next) {
		this.next = next;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}
	
}
