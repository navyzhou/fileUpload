package com.yc.smartupload.bean;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 模拟post协议上传文件
 * 源辰信息
 * @author navy
 * 2017年7月27日
 */
public class MyPost {
	public static void main(String[] args){
		String boundary = "----------" + "dfYcetelkftgfdhredskfljfg454df";

		Socket sk=null;

		try {
			StringBuffer command=new StringBuffer();
			StringBuffer data=new StringBuffer();
			FileInputStream fis=new FileInputStream(new File("E:\\图片\\loopwallpaper\\137.jpg"));
			System.out.println( fis.available() );
			byte[] bt=new byte[fis.available()];
			fis.read(bt);
			fis.close();

			//数据拼接
			data.append("--" + boundary);
			data.append("\r\n");
			data.append("Content-Disposition: form-data; name=\"uname\"");
			data.append("\r\n\r\n");
			data.append("源辰\r\n");
			data.append("--" + boundary);
			data.append("\r\n");
			
			data.append("--" + boundary);
			data.append("\r\n");
			data.append("Content-Disposition: form-data; name=\"photo\"; filename=\"137.jpg\"");
			data.append("\r\n");
			data.append("Content-Type: image/jpeg");
			data.append("\r\n\r\n");
			byte[] dataByte=data.toString().getBytes(); 
			byte[] endData=("--" + boundary + "--\r\n").getBytes();
			//数据字节
			sk=new Socket("127.0.0.1",8080);
			String url="/fileUpload/userInfo?op=myUpload"; //请求地址
			
			//拼接协议
			command.append("POST "+url+" HTTP/1.1\r\n");
			command.append("Host:127.0.0.1:8080\r\n");
			command.append("Content-Type:multipart/form-data; boundary=");
			command.append(boundary);
			command.append("\r\n");
			command.append("Content-Length:"+(dataByte.length+endData.length+bt.length)+" \r\n\r\n");
			
			//将拼接好的协议通过网络传输到服务器
			DataOutputStream dos=new DataOutputStream(sk.getOutputStream());
			dos.write( command.toString().getBytes() );
			dos.write( dataByte );
			dos.write(bt);
			dos.write(endData);
			//将数据发送到服务器
			dos.flush();

			//获取服务器的响应信息
			BufferedReader br=new BufferedReader(new InputStreamReader(sk.getInputStream()));
			String line=null;
			while( (line=br.readLine())!=null ){
				System.out.println(line);
			}
			dos.close();
			br.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
