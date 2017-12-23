package cn.itheima.jd.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import cn.itheima.jd.pojo.Product;
import cn.itheima.jd.pojo.Result;
import cn.itheima.jd.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{
	
	@Autowired
	private HttpSolrServer solrServer;

	public Result<Product> searchProduct(Model model, String queryString, 
			String catalog_name, String price, Integer page, String sort) {
//		solrService.
		// 1.建立查询对象（SolrQuery）
		SolrQuery sq = new SolrQuery();
		// 2.设置搜索参数
		if(StringUtils.isNotBlank(queryString)){
			// 设置查询关键词
			sq.setQuery("queryString");
		}else{
			// 如果查询关键词为空，搜索全部
			sq.setQuery("*:*");
		}
		
		// 设置默认搜索域
		sq.set("df", "product_keywords");
		
		// 设置过滤条件
		// 商品分类名称
		if(StringUtils.isNotBlank(catalog_name)){
			catalog_name = "product_catalog_name:" + catalog_name;
		}
		
		// 商品价格
		// 0-9
		if(StringUtils.isNotBlank(price)){
			String[] split = price.split("-");
			price = "product_price:[" + split[0] + " TO " + split[1] + "]";
		}
		
		// 设置分页
		// 如果第一次查询，查询第一页
		if(page == null){
			page = 1;
		}
		
		// 定义分页的大小
		int pageSize = 10;
		
		// 设置排序
		// 如果是1，设置为升序，否则降序
		if("1".equals(sort)){
			sq.setSort("product_price", ORDER.asc);
		}else{
			sq.setSort("product_price", ORDER.desc);
		}
		
		// 设置高亮显示（商品名称）
		// 开启高亮显示
		sq.setHighlight(true);
		sq.addHighlightField("product_name");//添加高亮显示的商品名称域
		sq.setHighlightSimplePre("<font color='red'>");// 设置高亮显示html标签前缀
		sq.setHighlightSimplePost("</font>");//设置高亮显示html标签后缀
		
		// 3.执行搜索
		QueryResponse response = null;
		try {
			response = this.solrServer.query(sq);
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		
		// 4.处理结果集
		// 4.1获取搜索的结果数据
		SolrDocumentList results = response.getResults();
		
		// 4.2.获取高亮数据
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		
		// 4.3.创建搜索结果对象（Result）
		Result<Product> result = new Result<Product>();
		
		// 4.3.1设置当前页
		result.setCurPage(page);
		
		// 4.3.2.设置页数
		int totals = (int) results.getNumFound();
		int pageCount = 0;
		if(totals % pageSize == 0){
			// 总记录数%每一页显示的大小，如果余数为0，pageCount=totals/pageSize;
			pageCount = totals / pageSize;
		}else{
			// 如果余数不为0,pageCount=(totals/pageSize)+1;
			pageCount = (totals / pageSize) + 1;
		}
		result.setPageCount(pageCount);
		
		// 4.3.3设置记录数
		result.setRecordCount(totals);
		
		// 4.3.4设置搜索的结果集合
		List<Product> productList = new ArrayList<Product>();
		for(SolrDocument doc : results){
			// 商品id，商品名称，商品图片，商品价格
			String pid = doc.get("id").toString();
			
			String pname = "";
			List<String> list = highlighting.get(pid).get("product_name");
			if(list != null && list.size() > 0){
				pname = list.get(0);
			}else{
				pname = doc.get("product_name").toString();
			}
			
			String ppicture = doc.get("product_picture").toString();
			
			String pprice = doc.get("product_price").toString();
			
			// 建立商品对象
			Product p = new Product();
			p.setPid(pid);
			p.setName(pname);
			p.setPicture(ppicture);
			p.setPrice(pprice);
			
			productList.add(p);
		}
		result.setList(productList);
		
		return result;
	}

}
