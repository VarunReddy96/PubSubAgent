package edu.rit.cs;

import java.io.Serializable;

public class Event implements Serializable,Classfinder {
	private int id;

	public String sendclassname(){
		return "event";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public String getTitle() {
		return title;
	}

	public String getTopicname(){return topic.getName();}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	@Override
	public String gettitle() {
		return this.title;
	}

	public void setContent(String content) {
		this.content = content;
	}

	private Topic topic;
	private String title;
	private String content;

	public Event(int id, Topic topic, String title, String content ){
		this.id = id;
		this.topic = topic;
		this.title = title;
		this.content = content;
	}
	public Event(){}

}
