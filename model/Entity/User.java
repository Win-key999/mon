package com.pennant.prodmtr.model.Entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "pm_Users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
	@SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq", allocationSize = 1)
	@Column(name = "user_id")
	private int userId;

	@Column(name = "user_displayname", length = 50)
	public String userDisplayName;

	@Column(name = "user_password", length = 50)
	public String userPassword;

	@Column(name = "user_cdate")
	public Date userCreationDate;

	@Column(name = "user_empl_id", length = 12)
	public String userEmployeeId;

	@Column(name = "user_status", length = 2)
	public String userStatus;

	@Column(name = "user_ludate")
	public Date userLastUpdatedDate;

	@ManyToOne
	@JoinColumn(name = "user_role")
	private Role userRole;

	@Column(name = "email", length = 255) // Add the new column for mail
	public String userMail;

	// Constructors
	public User() {
	}

	public User(String userDisplayName, String userPassword, Date userCreationDate, String userEmployeeId,
			String userStatus, Date userLastUpdatedDate, Role userRole, String userMail) {
		this.userDisplayName = userDisplayName;
		this.userPassword = userPassword;
		this.userCreationDate = userCreationDate;
		this.userEmployeeId = userEmployeeId;
		this.userStatus = userStatus;
		this.userLastUpdatedDate = userLastUpdatedDate;
		this.userRole = userRole;
		this.userMail = userMail;
	}

	// Getters and Setters

	public String getUserMail() {
		return userMail;
	}

	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserDisplayName() {
		return userDisplayName;
	}

	public void setUserDisplayName(String userDisplayName) {
		this.userDisplayName = userDisplayName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public Date getUserCreationDate() {
		return userCreationDate;
	}

	public void setUserCreationDate(Date userCreationDate) {
		this.userCreationDate = userCreationDate;
	}

	public String getUserEmployeeId() {
		return userEmployeeId;
	}

	public void setUserEmployeeId(String userEmployeeId) {
		this.userEmployeeId = userEmployeeId;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public Date getUserLastUpdatedDate() {
		return userLastUpdatedDate;
	}

	public void setUserLastUpdatedDate(Date userLastUpdatedDate) {
		this.userLastUpdatedDate = userLastUpdatedDate;
	}

	public Role getUserRole() {
		return userRole;
	}

	public void setUserRole(Role userRole) {
		this.userRole = userRole;
	}

}