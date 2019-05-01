package com.model2.mvc.web.purchase;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.user.UserService;

@Controller
@RequestMapping("/purchase/*")
public class PurchaseController {
	
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	
	@RequestMapping("/addPurchase")
	public String addPurchase(@ModelAttribute("buyVo") Purchase buyVo,HttpServletRequest request) throws Exception {
		
		
		
		buyVo.setBuyer(userService.getUser(request.getParameter("buyerId")));
		buyVo.setPurchaseProd(productService.getProduct(Integer.parseInt(request.getParameter("prodNo"))));
		buyVo.setTranCode("2");
		
		
		
		System.out.println("!"+buyVo);
		purchaseService.addPurchase(buyVo);
		
		//purchaseService.updateTranCode(buyVo);
		
		if(Integer.parseInt(request.getParameter("paymentOption")) == 1) {
			buyVo.setPaymentOption("현금구매");
		}else {
			buyVo.setPaymentOption("신용구매");
		}
		
		//request.setAttribute("buyVo", buyVo);
		
		return "forward:/purchase/addPurchase.jsp";
		
	}
	@RequestMapping("/addPurchaseView")
	public String addPurchaseView(@RequestParam(value="prodNo") int prodNo,HttpServletRequest request) throws Exception {
		
		//Product pVo = new Product();
		
		//ProductService ps = new ProductServiceImpl();
		
		Product pVo = productService.getProduct(prodNo);
		
		
		request.setAttribute("pVo", pVo);
		
		return "forward:/purchase/addPurchaseView.jsp";
	}
	
	
	@RequestMapping("/listPurchase")
	public String listPurchase( @ModelAttribute("search") Search search,
									HttpSession session,
									Model model) throws Exception {
		
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		
		User uVo = (User)session.getAttribute("user");
		System.out.println("!"+uVo);
		search.setPageSize(pageSize);
		
		
		Map<String, Object> map = purchaseService.getPurchaseList(search,  uVo.getUserId());
		
		Page page	= 
				new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		
		model.addAttribute("list", map.get("list"));
		model.addAttribute("search", search);
		model.addAttribute("page", page);
		
		
		return "forward:/purchase/listPurchase.jsp";
	}
	
	@RequestMapping("/getPurchase")
	public String getPurchase(@ModelAttribute("pVo") Purchase pVo,
								@RequestParam("tranNo") int tranNo,
								Model model) throws Exception {
		
		pVo = purchaseService.getPurchase(tranNo);
		
		String[] date = pVo.getDivyDate().split(" ");
		pVo.setDivyDate(date[0]);
		System.out.println("!"+date[0]);
		
		model.addAttribute("pVo",pVo);
		
		
		return "forward:/purchase/getPurchase.jsp";
		
	}
	
	@RequestMapping("/updatePurchase")
	public String updatePurchase(@ModelAttribute("uVo") User uVo,
									@ModelAttribute("pVo") Purchase pVo) throws Exception {
		pVo.setBuyer(uVo);
		
		purchaseService.updatePurcahse(pVo);
		
		return "redirect:/purchase/getPurchase?tranNo="+pVo.getTranNo();
	}
	
	@RequestMapping("updatePurchaseView")
	public String updatePurchaseView(@ModelAttribute("pVo") Purchase pVo,
										Model model) throws Exception {
		
		
		//Purchase pVo = new Purchase();
		//pVo = purchaseService.getPurchase(Integer.parseInt(request.getParameter("tranNo")));
		
		pVo = purchaseService.getPurchase(pVo.getTranNo());
		String[] date = pVo.getDivyDate().split(" ");
		pVo.setDivyDate(date[0]);
		
		
		model.addAttribute("pVo", pVo);
		
		
		return "forward:/purchase/updatePurchaseView.jsp";
	}
	
	@RequestMapping("updateTranCode")
	public String updateTranCode(HttpSession session,
								@RequestParam("code") String code,
								Purchase pVo,
								@RequestParam("prodNo") int prodNo,
								User uVo
			) throws Exception {
		
//		System.out.println(request.getParameter("prodNo") + "상품번호");
//		System.out.println(request.getParameter("code") + " 트랜코드");
		
		
		
		pVo = purchaseService.getPurchase2(prodNo);
		pVo.setTranCode(code);
		
		purchaseService.updateTranCode(pVo);
		
		
		uVo = (User)session.getAttribute("user");
		
		if(uVo.getRole().equals("user")) {
			return "redirect:/purchase/listPurchase";
		}
		
		
		return "redirect:/product/listProduct?menu=manage";
	}
	
	
	

}
