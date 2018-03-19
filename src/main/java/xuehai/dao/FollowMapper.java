package xuehai.dao;

import org.springframework.stereotype.Repository;
import xuehai.model.Follow;

import java.util.List;

@Repository
public interface FollowMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Follow record);

    int insertSelective(Follow record);

    Follow selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Follow record);

    int updateByPrimaryKey(Follow record);

    List<Follow> selectSelective(Follow follow);

    int getFollowedNum(Long userId);
    int getFollowingNum(Long userId);
}