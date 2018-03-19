package xuehai.dao;

import org.springframework.stereotype.Repository;
import xuehai.model.QuestionType;

import java.util.List;

@Repository
public interface QuestionTypeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuestionType record);

    int insertSelective(QuestionType record);

    QuestionType selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(QuestionType record);

    int updateByPrimaryKey(QuestionType record);

    List<QuestionType> selectSelective(QuestionType questionType);
}