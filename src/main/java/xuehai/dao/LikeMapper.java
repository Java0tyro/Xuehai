package xuehai.dao;

import org.springframework.stereotype.Repository;
import xuehai.model.Like;

import java.util.List;

@Repository
public interface LikeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Like record);

    int insertSelective(Like record);

    Like selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Like record);

    int updateByPrimaryKey(Like record);

    List<Like> selectSelective(Like like);
}