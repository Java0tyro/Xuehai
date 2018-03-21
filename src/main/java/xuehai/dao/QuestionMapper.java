package xuehai.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import xuehai.model.Question;

import java.util.List;

@Repository
public interface QuestionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Question record);

    int insertSelective(Question record);

    Question selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Question record);

    int updateByPrimaryKeyWithBLOBs(Question record);

    int updateByPrimaryKey(Question record);

    List<Question> selectSelective(Question question);

    int getQuestionNum(Long userId);
}