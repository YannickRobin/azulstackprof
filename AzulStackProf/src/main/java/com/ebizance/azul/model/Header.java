package com.ebizance.azul.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This is a POJO representing the current parsed stack XML file.<br/>
 * This object is loaded into drools engine so it can be used
 * to filter the files to parse (e.g.: filter by file name, 
 * filter by file size, filter by date...).
 * 
 * @author Yannick Robin
 */

public class Header {
	private Date date;
	private String fileName;
	private long fileSize;
	private int fileIndex;
	private boolean continueParsing;
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setDate(String sDate) {
		//Fri, 12 Nov 2010 19:54:35 GMT
		DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy KK:mm:ss zzz");
		try
		{
			date = df.parse(sDate);
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public long getFileSize() {
		return fileSize;
	}

	//File size in KB
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public int getFileIndex() {
		if (fileName.startsWith("STACK.") && fileName.endsWith(".xml"))
		{
			//remove "STACK." and ".xml"
			String[] splitFileName = fileName.split("\\.");
			String sFileIndex = splitFileName[1];
			try {
				fileIndex = Integer.parseInt(sFileIndex);
			}
			catch (NumberFormatException e)
			{
				return 0;
			}
		}
		else
			fileIndex = 0;
		
		return fileIndex;
	}
	
	public void setFileIndex(int fileIndex)
	{
		this.fileIndex = fileIndex;
	}

	public boolean getContinueParsing() {
		return continueParsing;
	}

	public void setContinueParsing(boolean continueParsing) {
		this.continueParsing = continueParsing;
	}
		
}
