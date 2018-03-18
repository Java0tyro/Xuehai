package xuehai.dao;

import xuehai.model.Follow;

import java.util.List;

public interface FollowMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Follow record);

    int insertSelective(Follow record);

    Follow selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Follow record);

    int updateByPrimaryKey(Follow record);

    List<Follow> selectSelective(Follow follow);
}