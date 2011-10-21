package com.laazy.mysqlclusterwebapp;

import com.mysql.clusterj.annotation.PersistenceCapable;
import com.mysql.clusterj.annotation.PrimaryKey;

@PersistenceCapable(table = "users")
public interface User {
	@PrimaryKey
	long getId();

	void setId(long id);

	String getEmail();

	void setEmail(String email);

	String getPassword();

	void setPassword(String password);

	String getSalt();

	void setSalt(String salt);
}
