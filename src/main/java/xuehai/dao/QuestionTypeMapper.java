package xuehai.dao;

import xuehai.model.QuestionType;

import java.util.List;

public interface QuestionTypeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuestionType record);

    int insertSelective(QuestionType record);

    QuestionType selectByPrimaryKey(Long id);

    List<QuestionType> selectSelective(QuestionType questionType);

    int updateByPrimaryKeySelective(QuestionType record);

    int updateByPrimaryKey(QuestionType record);
}