package edu.rit.cs;

import java.io.Serializable;
import java.util.List;

public class Topic implements Serializable,Classfinder {
	public int getId() {
		return id;
	}

	@Override
	public String getTopicname() {
		return name;
	}

	@Override
	public String getContent() {
		return null;
	}

	@Override
	public String gettitle() {
		return null;
	}

	public void setId(int id) {
		this.id = id;
	}

	private int id;

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private List<String> keywords;
	private String name;

	public Topic(int id,List<String> keywords,String name){
		this.id = id;
		this.keywords = keywords;
		this.name = name;
	}
	public Topic(){}


	@Override
	public String sendclassname() {
		return "topic";
	}
}
