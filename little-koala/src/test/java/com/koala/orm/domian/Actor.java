package com.koala.orm.domian;

import java.sql.Timestamp;
/**
 * @author edliao on Auto generate.
 */
@lombok.Data
public class Actor {
	Integer actorId;
	String firstName;
	String lastName;
	Timestamp lastUpdate;
}