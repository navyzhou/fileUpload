package com.yc.smartupload.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;


public class YcUploadUtil {
	Map<String,String> parameterMap=new HashMap<String,String>();
	private Set<YcFile> files=new HashSet<YcFile>(); //所有文件对象

	private int uploadTotalSize=0; //上传数据的总大小
	private byte[] saveUploadFileArray=null; //保存上传数据的数组

	private boolean isEnter = false; //是否是回车键
	private int currentIndex; //当前数据读到的位置
	private String boundStr=new String(); //边界信息
	private String dataHeader = new String(); //获取请求头数据

	private int startData; //数据开始的位置
	private int endData; //数据结束的位置

	private String fieldName = new String(); //上传数据的name属性的属性名
	private String fileName = new String(); //上传文件的文件名
	private String fileExt = new String(); //上传文件的扩展名
	private String fileType = new String(); //上次文件的文件类型
	boolean isFile = false;

	/**
	 * 获取请求中的文本参数
	 * @return
	 */
	public Map<String,String> getRequestInfo(){
		return parameterMap;
	}

	public Set<YcFile> getFiles() {
		return files;
	}

	public void upload(HttpServletRequest request) throws Exception{
		boolean isFile=false;
		uploadTotalSize=request.getContentLength();
		saveUploadFileArray=new byte[uploadTotalSize];
		int readLength=0; //当前读到的长度
		int totalRead=0; //总共读到的长度

		//将请求数据读到字节数组中
		for(totalRead=0;totalRead<uploadTotalSize; totalRead += readLength){
			try {
				readLength = request.getInputStream().read(saveUploadFileArray, totalRead, uploadTotalSize - totalRead);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		//请求数据的最开始是边界数据，然后以回车结束，所有先从最前面获取边界信息，如：------WebKitFormBoundarycQLwEmu2ssMhXLJ2
		for(;!isEnter && currentIndex < uploadTotalSize; currentIndex++){
			if(saveUploadFileArray[currentIndex] == 13){//如果是回车，说明第一行边界数据已经读完
				isEnter = true;
			}else{
				boundStr = boundStr + (char)saveUploadFileArray[currentIndex];
			}
		}

		if(currentIndex == 1){ //说明不是multipart/form-data类型
			return;
		}
		currentIndex++; //跳过回车
		do{
			if(currentIndex >= uploadTotalSize){ //如果已经读完，则跳出循环
				//break;
			}
			dataHeader = getDataHeader();
			currentIndex = currentIndex + 2; //跳过回车
			isFile = dataHeader.indexOf("filename") > 0;//如果有filename说明是一个文件框，否则就是普通的文本框
			fieldName = getDataFieldValue(dataHeader, "name"); //获取文本框name属性的属性名
			if(isFile){ //如果是一个文件
				fileName = getDataFieldValue(dataHeader, "filename"); //获取文件名
				fileExt = getFileExt(fileName);
				fileType = getContentType(dataHeader);
			}
			getDataPart();
			if(isFile && fileName.length() > 0){ 
				//允许上传的文件格式、拒绝上传的文件格式、文件大小限定等的校验
			}

			if(isFile){//保存上传的文件
				YcFile file=new YcFile();
				file.setFieldName(fieldName);
				file.setFileName(fileName);
				file.setFileExt(fileExt);
				file.setFileType(fileType);
				if(fileType.indexOf("application/x-macbinary") > 0){ //如果是bin文件
					startData = startData + 128;
				}
				int size=(endData-startData)+1;
				file.setFileSize(size);
				byte[] bt=new byte[size];
				System.arraycopy(saveUploadFileArray,startData,bt,0,size);
				file.setFileByte(bt);
				files.add(file);
			} else{ //如果是普通文本框，则读取数据然后保存到parameterMap中，以字符串的方式返回给用户
				String value = new String(saveUploadFileArray,startData,(endData-startData)+1,"UTF-8");
				parameterMap.put(fieldName,value);
			}
			if((char)saveUploadFileArray[currentIndex + 1] == '-'){ //说明已经是结尾了，以两个--结尾
				break;
			}
			currentIndex = currentIndex + 2;
		} while(true);
	}

	/**
	 * 获取上次文件的文件类型
	 * @param dataHeader
	 * @return
	 */
	private String getContentType(String dataHeader){
		String flag="Content-Type:";
		String value = new String();
		int start = dataHeader.indexOf(flag)+flag.length();
		if(start != -1){
			value = dataHeader.substring(start+1,dataHeader.length());
		}
		return value;
	}

	/**
	 * 获取文件的扩展名
	 * @param fileName
	 * @return
	 */
	private String getFileExt(String fileName){
		if(fileName == null){
			return null;
		}
		int start = fileName.lastIndexOf('.') ;
		if(start>0){
			return fileName.substring(start+1,fileName.length());
		}else{
			return "";
		}
	}

	/**
	 * 获取数据部分信息
	 */
	private void getDataPart(){
		int searchPos = currentIndex;
		int keyPos = 0;
		int boundaryLen = boundStr.length();//边界长度
		startData = currentIndex; //记录数据开始的位置
		endData = 0; //记录数据结束的位置
		do{
			if(searchPos >= uploadTotalSize){
				break;
			}
			if(saveUploadFileArray[currentIndex] ==(byte)boundStr.charAt(keyPos)){
				if(keyPos==boundaryLen-1){//说明是边界的最后了
					endData = currentIndex-boundaryLen-2; //数据结束的位置为当前-分割 -\r\n
					break;
				}
				currentIndex++;
				keyPos++;
			} else{
				currentIndex++;
				keyPos=0;
			}
		} while(true);
		currentIndex++;
	}

	/**
	 * 获取每个提交数据的请求头信息，如：Content-Disposition: form-data; name="photo"; filename="3.jpg"
	 */
	private String getDataHeader(){
		int start = currentIndex; //开始读取请求数据的头信息的位置
		int end = 0; //结束位置
		boolean isEnd = false; //是否已经读完
		while(!isEnd){
			//如果遇到连续两个回车，说明第一个数据已经读完  \r\n 两个字符
			if(saveUploadFileArray[currentIndex] == 13 && saveUploadFileArray[currentIndex + 2] == 13){
				isEnd = true;
				end = currentIndex;
				currentIndex = currentIndex + 2;
			} else{
				currentIndex++;
			}
		}
		return new String(saveUploadFileArray, start,end-start);
	}

	/**
	 * 获取指定的属性的属性值  
	 * @param dataHeader 数据头协议字符串：如：Content-Disposition: form-data; name="photo"; filename="3.jpg"
	 * @param fieldName 要获取的字段属性的属性名
	 * @return 对应的属性值
	 * @throws UnsupportedEncodingException
	 */
	private String getDataFieldValue(String dataHeader, String fieldName){
		//构造标示字符串
		String flag=fieldName+"=\""; //如：name->name="
		int startPos=dataHeader.indexOf(flag)+flag.length();
		if(startPos>0){
			return dataHeader.substring(startPos,dataHeader.indexOf("\"",startPos));
		}else{
			return null;
		}
	}
}
