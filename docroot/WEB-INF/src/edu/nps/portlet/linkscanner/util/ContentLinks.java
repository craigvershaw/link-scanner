package edu.nps.portlet.linkscanner.util;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


public class ContentLinks {

	private String contentTitle;
	private String contentEditLink;
	private String className;
	private String classPK;
	private Date modifiedDate;
	private int status;
	private Set<String> links = new HashSet<String>();
	
	public String getContentTitle() {
	
		return contentTitle;
	}
	
	public void setContentTitle(String contentTitle) {
	
		this.contentTitle = contentTitle;
	}
	
	public String getContentEditLink() {
	
		return contentEditLink;
	}
	
	public void setContentEditLink(String contentEditLink) {
	
		this.contentEditLink = contentEditLink;
	}
	
	public String getClassName() {
	
		return className;
	}
	
	public void setClassName(String className) {
	
		this.className = className;
	}
	
	public String getClassPK() {
	
		return classPK;
	}
	
	public void setClassPK(String classPK) {
	
		this.classPK = classPK;
	}
	
	public void setClassPK(long classPK) {
		
		setClassPK(String.valueOf(classPK));
	}
	
	public Date getModifiedDate() {
	
		return modifiedDate;
	}
	
	public void setModifiedDate(Date modifiedDate) {
	
		this.modifiedDate = modifiedDate;
	}
	
	
	public int getStatus() {
	
		return status;
	}

	
	public void setStatus(int status) {
	
		this.status = status;
	}

	public Set<String> getLinks() {
	
		return links;
	}
	
	public void setLinks(Set<String> links) {
	
		this.links = links;
	}
	
	public void addLink(String link) {
		
		links.add(link);
	}
	
	public void removeLink(String link) {
		
		links.remove(link);
	}
	
	public int getLinksSize() {
	
		return links.size();
	}
	
	public boolean containsLink(String link) {
	
		return links.contains(link);
	}
	
}
