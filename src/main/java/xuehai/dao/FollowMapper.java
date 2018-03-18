package xuehai.dao;

import xuehai.model.Follow;

import java.util.List;

public interface FollowMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Follow record);

    int insertSelective(Follow record);

    Follow selectByPrimaryKey(Long id);
    List<Follow> selectSelective(Follow follow);

    int updateByPrimaryKeySelective(Follow record);

    int updateByPrimaryKey(Follow record);
}