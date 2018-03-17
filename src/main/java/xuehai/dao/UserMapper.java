package xuehai.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import xuehai.model.Follow;
import xuehai.model.User;


@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User login(@Param("email") String email);

    Long regist(User user);

    User modify(User user);

    Follow follow(@Param("fromId") Long fromId, @Param( "toId") Long toId);

    User deleteUser(Long id);

}