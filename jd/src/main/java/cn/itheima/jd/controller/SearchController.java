package cn.itheima.jd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itheima.jd.pojo.Product;
import cn.itheima.jd.pojo.Result;
import cn.itheima.jd.service.ProductService;

@Controller
public class SearchController {
	
	@Autowired
	private ProductService productService;
	
	@RequestMapping("list.action")
	public String list(Model model, String queryString, String catalog_name, String price, Integer page, String sort){
		//执行搜索
		Result<Product> result = productService.searchProduct(model, queryString, catalog_name, price, page, sort);
		
		//响应搜索结果数据
		model.addAttribute("result", result);
		
		//设置响应参数回显
		model.addAttribute("queryString", queryString);
		model.addAttribute("catalog_name", catalog_name);
		model.addAttribute("price", price);
		model.addAttribute("sort", sort);
		
		return "product_list";
	}

}
