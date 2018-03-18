package xuehai.dao;

import xuehai.model.Like;

import java.util.List;

public interface LikeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Like record);

    int insertSelective(Like record);

    Like selectByPrimaryKey(Long id);
    List<Like> selectSelective(Like like);

    int updateByPrimaryKeySelective(Like record);

    int updateByPrimaryKey(Like record);
}