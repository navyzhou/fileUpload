package com.yc.smartupload.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.yc.smartupload.util.YcFile;
import com.yc.smartupload.util.YcUploadUtil;


public class UserInfoServlet extends BasicServlet{
	private static final long serialVersionUID = -4204822388404236061L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req,resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String op=req.getParameter("op");
		
		if("myUpload".equals(op)){
			myUpload(req,resp);
		}
	}

	private void myUpload(HttpServletRequest req, HttpServletResponse resp) {
		YcUploadUtil myUploadUtil=new YcUploadUtil();
		try {
			myUploadUtil.upload(req);
			
			Map<String,String> map=myUploadUtil.getRequestInfo();
			System.out.println(map);
			
			Set<YcFile> set=myUploadUtil.getFiles();
			if(set!=null && set.size()>0){
				File fl;
				for(YcFile file:set){
					if(file.getFileByte()!=null && file.getFileByte().length>0){
						fl=new File("D:\\a\\"+file.getFileName());
						FileOutputStream fos=new FileOutputStream(fl);
						fos.write(file.getFileByte());
						fos.flush();
						fos.close();
					}
				}
			}
			this.out(resp,"1");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				this.out(resp,"0");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
