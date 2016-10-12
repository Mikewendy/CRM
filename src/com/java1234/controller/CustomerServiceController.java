package com.java1234.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.java1234.entity.CustomerService;
import com.java1234.entity.PageBean;
import com.java1234.service.CustomerServiceService;
import com.java1234.util.ResponseUtil;
import com.java1234.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 锟酵凤拷锟斤拷锟斤拷Controller锟斤拷
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/customerService")
public class CustomerServiceController {
	Logger logger = Logger.getLogger(CustomerServiceController.class);
	@Resource
	private CustomerServiceService customerServiceService;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   //true:锟斤拷锟斤拷锟斤拷锟斤拷锟街碉拷锟絝alse:锟斤拷锟斤拷为锟斤拷值
	}
	
	/**
	 * 锟斤拷页锟斤拷锟斤拷锟斤拷询锟酵伙拷锟斤拷锟斤拷
	 * @param page
	 * @param rows
	 * @param s_customer
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public String list(@RequestParam(value="page",required=false)String page,@RequestParam(value="rows",required=false)String rows,String createTimefrom,String createTimeto,CustomerService s_customerService,HttpServletResponse response)throws Exception{
		PageBean pageBean=new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		String str_state = new String(s_customerService.getState().trim().getBytes("iso8859-1"),"utf-8");
	
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("customer", StringUtil.formatLike(s_customerService.getCustomer()));
		map.put("overview", StringUtil.formatLike(s_customerService.getOverview()));
		map.put("serveType", s_customerService.getServeType());
		map.put("state", str_state);
		map.put("createTimefrom", createTimefrom);
		map.put("createTimeto", createTimeto);
		map.put("start", pageBean.getStart());
		map.put("size", pageBean.getPageSize());
		logger.debug("s_customerService="+s_customerService);
		List<CustomerService> customerServiceList=customerServiceService.find(map);
		Long total=customerServiceService.getTotal(map);
		JSONObject result=new JSONObject();
		JsonConfig jsonConfig=new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd"));
		JSONArray jsonArray=JSONArray.fromObject(customerServiceList,jsonConfig);
		result.put("rows", jsonArray);
		result.put("total", total);
		ResponseUtil.write(response, result);
		return null;
	}
	 

	/**
	 * 锟斤拷踊锟斤拷锟斤拷薷目头锟斤拷锟斤拷锟�
	 * @param contact
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	public String save(CustomerService customerService,HttpServletResponse response)throws Exception{
		int resultTotal=0; // 锟斤拷锟斤拷锟侥硷拷录锟斤拷锟斤拷
		if(customerService.getId()==null){
			resultTotal=customerServiceService.add(customerService);
		}else{
			resultTotal=customerServiceService.update(customerService);
		}
		JSONObject result=new JSONObject();
		if(resultTotal>0){
			result.put("success", true);
		}else{
			result.put("success", false);
		}
		ResponseUtil.write(response, result);
		return null;
	}
	

}
