package xuehai.dao;

import xuehai.model.QuestionType;

public interface QuestionTypeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuestionType record);

    int insertSelective(QuestionType record);

    QuestionType selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(QuestionType record);

    int updateByPrimaryKey(QuestionType record);
}