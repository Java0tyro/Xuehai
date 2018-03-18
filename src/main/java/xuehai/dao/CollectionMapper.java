package xuehai.dao;

import org.springframework.stereotype.Repository;
import xuehai.model.Collection;

import java.util.List;

@Repository
public interface CollectionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Collection record);

    int insertSelective(Collection record);

    Collection selectByPrimaryKey(Long id);
    List<Collection> selectSelective(Collection collection);

    int updateByPrimaryKeySelective(Collection record);

    int updateByPrimaryKey(Collection record);
}