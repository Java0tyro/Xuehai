package xuehai.dao;

import org.springframework.stereotype.Repository;
import xuehai.model.User;
import xuehai.vo.NumberControl;
import xuehai.vo.TimeLine;

import java.util.List;
import java.util.Map;


@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    List<User> selectSelective(User user);

    void deleteUserByUserId(Map<String, Long> map);

    List<TimeLine> getTimeLine(NumberControl numberControl);
}