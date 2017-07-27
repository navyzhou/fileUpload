package com.yc.smartupload.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author navy
 */
public class BasicServlet extends HttpServlet{
	private static final long serialVersionUID = -5857710525333213724L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
		doPost(req, resp);
	}
	
	/**
	 * 回送一个表示符给客户端
	 * @param response
	 * @param result：结果
	 * @throws IOException 
	 */
	protected void out(HttpServletResponse response,String result) throws IOException{
		PrintWriter out=response.getWriter();
		out.println(result);
		out.flush();
		out.close();
	}
	
	/**
	 * 针对分页的json回送
	 * @param response
	 * @param list
	 * @param total
	 * @throws IOException 
	 */
	public <T> void out(HttpServletResponse response,List<T> list,Integer total) throws IOException{
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("rows",list);
		map.put("total",total);
		Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		PrintWriter out=response.getWriter();
		out.println(gson.toJson(map));
		out.flush();
		out.close();
	}
	
	/**
	 * 回送单个对象的
	 * @param response
	 * @param obj
	 * @throws IOException 
	 */
	public void out(HttpServletResponse response,Object obj) throws IOException{
		//Gson gson=new Gson();
		//设置Gson转换时，日期的转换格式
		Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		PrintWriter out=response.getWriter();
		out.println(gson.toJson(obj));
		out.flush();
		out.close();
	}
}
