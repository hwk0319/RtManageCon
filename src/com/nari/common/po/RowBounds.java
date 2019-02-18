package com.nari.common.po;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import com.nari.util.MyPropertiesPersist;

@Component
public class RowBounds implements Serializable {
	 private static final long serialVersionUID = 1L;
	 
		public int pageSize=16;//每页记录数
		public int currentPage=1;//当前页码
		public int totalPages=0;//分页总数
		public int totalRecords=0;// 数据总记录数
		public boolean needPage=false;
		public String dbType=MyPropertiesPersist.DBTYPE;

	    @Override
	    public String toString() {
	        return "RowBounds [ currentPage=" + currentPage + ", pageSize="
	                + pageSize + "totalCount="+totalRecords+"toatalPage="+totalPages+"]";
	    }

		public int getPageSize() {
			return pageSize;
		}

		public void setRows(int rows) {
			this.pageSize = rows;
		}

		public int getCurrentPage() {
			return currentPage;
		}

		public void setPage(int page) {
			this.currentPage = page;
		}

		public int getTotalPages() {
			return totalPages;
		}

		public void setTotalPages(int total) {
			this.totalPages = total;
		}

		public int getTotalRecords() {
			return totalRecords;
		}

		public void setTotalRecords(int totalCount) {
			this.totalRecords = totalCount;
		}

		public boolean isNeedPage() {
			return needPage;
		}

		public void setNeedPage(boolean needPage) {
			this.needPage = needPage;
		}

		public String getDbType() {
			return dbType;
		}
}
