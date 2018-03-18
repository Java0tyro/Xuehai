package xuehai.dao;

import xuehai.model.User;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    List<User> selectSelective(User user);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}