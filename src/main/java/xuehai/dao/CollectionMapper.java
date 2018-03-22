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

    int updateByPrimaryKeySelective(Collection record);

    int updateByPrimaryKey(Collection record);

    List<Collection> selectSelective(Collection collection);

    int deleteByQuestionId(Long questionId);

    int getCollectionNum(Long userId);

    int getQuestionCollectionNum(Long questionId);
}