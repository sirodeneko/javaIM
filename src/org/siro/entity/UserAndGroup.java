package org.siro.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "gandu")
public class UserAndGroup {
	private static final long serialVersionUID = 27029186740468651L;
	@Id
	@Column(name="userID")
	private int userID;
	@Column(name="groupID")
	private int groupID;

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public int getGroupID() {
		return groupID;
	}

	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}
}
