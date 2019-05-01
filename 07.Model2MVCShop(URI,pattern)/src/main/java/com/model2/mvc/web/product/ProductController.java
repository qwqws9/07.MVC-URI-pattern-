package com.model2.mvc.web.product;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;

@Controller
@RequestMapping("/product/*")
public class ProductController {
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	
	
	public ProductController() {
		System.out.println("ProductController default ������ ȣ��!");
	}
	
	//==> classpath:config/common.properties  ,  classpath:config/commonservice.xml ���� �Ұ�
		//==> �Ʒ��� �ΰ��� �ּ��� Ǯ�� �ǹ̸� Ȯ�� �Ұ�
		@Value("#{commonProperties['pageUnit']}")
		//@Value("#{commonProperties['pageUnit'] ?: 3}")
		int pageUnit;
		
		@Value("#{commonProperties['pageSize']}")
		//@Value("#{commonProperties['pageSize'] ?: 2}")
		int pageSize;
		
		
	@RequestMapping("/addProduct")
	public String addProduct( @ModelAttribute("pVo") Product pVo) throws Exception {
		
		productService.addProduct(pVo);
		
		return "forward:/product/confirmProduct.jsp";
	}
	
	@RequestMapping("/listProduct")
	public String listProduct( @RequestParam(value="currentPage",defaultValue="1") int currentPage,
							   @RequestParam("menu") String menu,
								@ModelAttribute("search") Search search,
								Model model) throws Exception {
		
		search.setPageSize(pageSize);
		
		
		System.out.println(currentPage + " ������������ ���ΰ��ƾƾƾƾƾƾ�");

		search.setCurrentPage(currentPage);
		
		System.out.println(currentPage + " ���������� ù��°�� 1 ������ �� ��û�� ���� �������� ���;��� ");
		

		
		System.out.println("---------------------------------------");
		System.out.println(search.getSearchCondition() + "�����");
		System.out.println(search.getSearchKeyword() + "Ű����");
		System.out.println("---------------------------------------");

		Map<String, Object> map = productService.getProductList(search);
		
		System.out.println(currentPage + " dao �������� ���������� ȣ�� 1�� ���;���");
		System.out.println(map.get("totalCount") + " dao ������ totalCount");

		Page page	= 
				new Page( currentPage, ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		
		model.addAttribute("list", map.get("list"));
		model.addAttribute("search", search);
		model.addAttribute("page", page);

		model.addAttribute("menu", menu);

		return "forward:/product/listProduct.jsp";

	}
	
	@RequestMapping("getProduct")
	public String getProduct( @RequestParam("prodNo") String prodNo,
								HttpServletResponse response,
								HttpServletRequest request,
								@RequestParam("menu") String menu
			) throws Exception {
		
		String his = "";

		Cookie[] cookie = request.getCookies();

		if (cookie != null) {
			System.out.println("�����γ�");
			for (int i = 0; i < cookie.length; i++) {
				if (cookie[i].getName().equals("history")) {
					his = cookie[i].getValue() + "," + prodNo;
					Cookie c = new Cookie("history", his);
					c.setMaxAge(365 * 24 * 60 * 60);
					response.addCookie(c);
					break;
				}else {
					Cookie c = new Cookie("history", prodNo);
					c.setMaxAge(365 * 24 * 60 * 60);
					response.addCookie(c);
				}
			}
		}

		System.out.println("-------------------------------");
		System.out.println(menu + " 11111111111111111111");
		System.out.println("-------------------------------");

		Product pVo = new Product();
		pVo = productService.getProduct(Integer.parseInt(prodNo));

		request.setAttribute("pVo", pVo);
		request.setAttribute("prodNo", prodNo);

		// ��ǰ�� �ǸſϷᰡ�ƴѻ��� + �������϶��� ������������ ����
		if (menu.equals("manage") && Integer.parseInt(pVo.getProTranCode().trim()) == 1) {
			System.out.println("�������ε� ���κ����Ƥ�");
			return "forward:/product/updateProductView";
		}

		return "forward:/product/getProduct.jsp";
	}
	
	@RequestMapping("updateProduct")
	public String updateProduct( @ModelAttribute("product") Product product,
									HttpServletRequest request) throws Exception {
		
		//Product pro = new Product();
		
		
		
		//pVo.setRegDate(pro.getRegDate());
		
		product.setProdNo(Integer.parseInt(request.getParameter("hidden")));
		System.out.println("��ǰ��ȣ�� ��µǾ�� ~~~ " +request.getParameter("hidden"));
		
		
		productService.updateProduct(product);
		//System.out.println(pVo.getRegDate()+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		
		//request.removeAttribute("pVo");
		Product pVo = new Product();
		pVo = productService.getProduct(Integer.parseInt(request.getParameter("hidden")));
		System.out.println("1==>"+pVo);
		
		request.setAttribute("pVo", pVo);
		request.setAttribute("menu", request.getParameter("menu"));
		
		return "forward:/product/getProduct.jsp";
	}
	
	@RequestMapping("updateProductView")
	public String updateProductView(HttpServletRequest request) throws Exception {
		

		int prodNo = Integer.parseInt(request.getParameter("prodNo"));
		System.out.println("�ǸŹ�ȣ : " + prodNo);
		
		//request.setAttribute("prodNo", prodNo);
		
		Product pVo = productService.getProduct(prodNo);
		
		request.setAttribute("pVo", pVo);
		
		return "forward:/product/updateProductView.jsp";
	}
}