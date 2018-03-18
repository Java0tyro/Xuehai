package xuehai.dao;

import org.springframework.stereotype.Repository;
import xuehai.model.Question;

import java.util.List;

@Repository
public interface QuestionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Question record);

    int insertSelective(Question record);

    Question selectByPrimaryKey(Long id);
    List<Question> selectSelective(Question question);

    int updateByPrimaryKeySelective(Question record);

    int updateByPrimaryKeyWithBLOBs(Question record);

    int updateByPrimaryKey(Question record);
}