package cn.itheima.jd.service;

import org.springframework.ui.Model;

import cn.itheima.jd.pojo.Product;
import cn.itheima.jd.pojo.Result;

public interface ProductService {
	
	Result<Product> searchProduct(Model model, String queryString, String catalog_name, String price, Integer page, String sort);

}
