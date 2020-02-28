package edu.rit.cs;

public interface Publisher {
	/*
	 * publish an event of a specific topic with title and content
	 */
	public void publish(Event event,int qos);
	
	/*
	 * advertise new topic
	 */
	public void advertise(Topic newTopic);
}
