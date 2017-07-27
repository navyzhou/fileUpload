package com.yc.smartupload.util;

import java.util.Arrays;

public class YcFile {
	private String fileName; //文件名称
	private String fieldName;  //文件域名
	private String fileExt; //文件扩展名
	private int fileSize; //文件大小
	private String fileType; //文件类型
	private byte[] fileByte; //文件数据信息
	
	public YcFile() {
		super();
	}

	public YcFile(String fileName, String fieldName, String fileExt, int fileSize,String fileType,
			byte[] fileByte) {
		super();
		this.fileName = fileName;
		this.fieldName = fieldName;
		this.fileExt = fileExt;
		this.fileSize = fileSize;
		this.fileType=fileType;
		this.fileByte = fileByte;
	}

	@Override
	public String toString() {
		return "YcFile [fileName=" + fileName + ", fieldName=" + fieldName + ", fileExt=" + fileExt + ", fileSize=" + fileSize + ", fileByte=" + Arrays.toString(fileByte) + "]";
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFileExt() {
		return fileExt;
	}

	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public byte[] getFileByte() {
		return fileByte;
	}

	public void setFileByte(byte[] fileByte) {
		this.fileByte = fileByte;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fieldName == null) ? 0 : fieldName.hashCode());
		result = prime * result + Arrays.hashCode(fileByte);
		result = prime * result + ((fileExt == null) ? 0 : fileExt.hashCode());
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + fileSize;
		result = prime * result + ((fileType == null) ? 0 : fileType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		YcFile other = (YcFile) obj;
		if (fieldName == null) {
			if (other.fieldName != null)
				return false;
		} else if (!fieldName.equals(other.fieldName))
			return false;
		if (!Arrays.equals(fileByte, other.fileByte))
			return false;
		if (fileExt == null) {
			if (other.fileExt != null)
				return false;
		} else if (!fileExt.equals(other.fileExt))
			return false;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (fileSize != other.fileSize)
			return false;
		if (fileType == null) {
			if (other.fileType != null)
				return false;
		} else if (!fileType.equals(other.fileType))
			return false;
		return true;
	}
}
