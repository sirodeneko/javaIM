package org.siro.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "groups")
public class Groups implements Serializable {
	private static final long serialVersionUID = 270291867404686911L;
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMaster() {
		return master;
	}

	public void setMaster(int master) {
		this.master = master;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name="name")
	private String name;

	@Column(name="createTime")
	private Date createTime;

	@Column(name="master")
	private int master;
}
