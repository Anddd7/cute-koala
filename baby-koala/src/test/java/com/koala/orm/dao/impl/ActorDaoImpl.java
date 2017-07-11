package com.koala.orm.dao.impl;

import com.koala.orm.dao.ActorDao;
import com.koala.orm.domian.Actor;
import github.koala.orm.DataBasePool;
import github.koala.orm.conn.DBConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author edliao on Auto generate.
 */
public class ActorDaoImpl implements ActorDao {

  private static final String SCHEMA_NAME = "sakila";
  private static final String TABLE_NAME = "actor";

  private DBConnection connection = DataBasePool.getDBConnectionBySchema(SCHEMA_NAME);

  //select
  public Actor selectByKey(Integer actorId) {
    return connection
        .executeQuery("select * from " + TABLE_NAME + " where actor_id= ?", Arrays.asList(actorId),
            Actor.class);
  }

  public List<Actor> selectAll() {
    return connection.executeQuery("select * from " + TABLE_NAME, null, Actor.class, List.class);
  }

  public List<Actor> selectByExample(Actor example, Integer pageIndex, Integer pageSize) {
    List<String> whereClause = new ArrayList<>();
    List<Object> parameters = new ArrayList<>();

    if (!Objects.isNull(example.getActorId())) {
      whereClause.add("actor_id=?");
      parameters.add(example.getActorId());
    }
    if (!Objects.isNull(example.getFirstName())) {
      whereClause.add("first_name=?");
      parameters.add(example.getFirstName());
    }
    if (!Objects.isNull(example.getLastName())) {
      whereClause.add("last_name=?");
      parameters.add(example.getLastName());
    }
    if (!Objects.isNull(example.getLastUpdate())) {
      whereClause.add("last_update=?");
      parameters.add(example.getLastUpdate());
    }

    return connection.executeQuery(
        "select * from " + TABLE_NAME + " where 1=1 AND " + String.join(" AND ", whereClause)
            + " limit " + pageIndex + "," + pageSize, parameters, Actor.class, List.class);
  }

  //delete
  public int deleteByKey(Integer actorId) {
    return connection
        .executeUpdate("delete from " + TABLE_NAME + " where actor_id= ?", Arrays.asList(actorId));
  }

  public int deleteByExample(Actor example) {
    List<String> whereClause = new ArrayList<>();
    List<Object> parameters = new ArrayList<>();

    if (!Objects.isNull(example.getActorId())) {
      whereClause.add("actor_id=?");
      parameters.add(example.getActorId());
    }
    if (!Objects.isNull(example.getFirstName())) {
      whereClause.add("first_name=?");
      parameters.add(example.getFirstName());
    }
    if (!Objects.isNull(example.getLastName())) {
      whereClause.add("last_name=?");
      parameters.add(example.getLastName());
    }
    if (!Objects.isNull(example.getLastUpdate())) {
      whereClause.add("last_update=?");
      parameters.add(example.getLastUpdate());
    }

    return connection.executeUpdate(
        "delete from " + TABLE_NAME + " where 1=1 AND " + String.join(" AND ", whereClause),
        parameters);
  }

  //update
  public int updateByExample(Actor example) {
    List<String> setClause = new ArrayList<>();
    List<Object> valueClause = new ArrayList<>();

    if (!Objects.isNull(example.getFirstName())) {
      setClause.add("first_name=?");
      valueClause.add(example.getFirstName());
    }
    if (!Objects.isNull(example.getLastName())) {
      setClause.add("last_name=?");
      valueClause.add(example.getLastName());
    }
    if (!Objects.isNull(example.getLastUpdate())) {
      setClause.add("last_update=?");
      valueClause.add(example.getLastUpdate());
    }

    List<String> whereClause = new ArrayList<>();
    whereClause.add("actor_id=?");
    valueClause.add(example.getActorId());
    return connection
        .executeUpdate("update " + TABLE_NAME + " set " + String.join(" , ", setClause) +
            " where 1=1 AND" + String.join(" AND ", whereClause), valueClause);
  }

  //insert
  public boolean insert(Actor example) {
    List<String> setClause = new ArrayList<>();
    List<String> valueClause = new ArrayList<>();
    List<Object> parameters = new ArrayList<>();

    if (!Objects.isNull(example.getActorId())) {
      setClause.add("actor_id");
      valueClause.add("?");
      parameters.add(example.getActorId());
    }
    if (!Objects.isNull(example.getFirstName())) {
      setClause.add("first_name");
      valueClause.add("?");
      parameters.add(example.getFirstName());
    }
    if (!Objects.isNull(example.getLastName())) {
      setClause.add("last_name");
      valueClause.add("?");
      parameters.add(example.getLastName());
    }
    if (!Objects.isNull(example.getLastUpdate())) {
      setClause.add("last_update");
      valueClause.add("?");
      parameters.add(example.getLastUpdate());
    }

    return connection.executeUpdate(
        "insert into " + TABLE_NAME + " (" + String.join(",", setClause) + ") values (" + String
            .join(",", valueClause) + ")", parameters) == 1 ?
        true : false;
  }
}