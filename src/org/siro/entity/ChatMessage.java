package org.siro.entity;

public class ChatMessage {
	private MessageType type;
	private String content;
	private User user;
	private int to;

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}

	public enum MessageType {
		Ping,
		Pong,
		Msg,
		Pic,
		Err,
		Close,
		Other,
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
