package xuehai.dao;

import xuehai.model.Answer;

import java.util.List;

public interface AnswerMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Answer record);

    int insertSelective(Answer record);

    Answer selectByPrimaryKey(Long id);
    List<Answer> selectSelective(Answer answer);

    int updateByPrimaryKeySelective(Answer record);

    int updateByPrimaryKeyWithBLOBs(Answer record);

    int updateByPrimaryKey(Answer record);
}