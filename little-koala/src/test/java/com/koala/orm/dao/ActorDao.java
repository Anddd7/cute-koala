package com.koala.orm.dao;

import com.koala.orm.domian.Actor;
import java.util.List;

/**
 * @author edliao on Auto generate.
 */
public interface ActorDao {

    //select
    Actor selectByKey(Integer actorId);
    List<Actor> selectAll();
    List<Actor> selectByExample(Actor example ,Integer pageIndex ,Integer pageSize);

    //delete
    int deleteByKey(Integer actorId);
    int deleteByExample(Actor example);

    //update
    int updateByExample(Actor example);

    //insert
    boolean insert(Actor example);
}