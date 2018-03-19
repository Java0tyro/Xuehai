package xuehai.dao;

import org.springframework.stereotype.Repository;
import xuehai.model.Answer;

import java.util.List;

@Repository
public interface AnswerMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Answer record);

    int insertSelective(Answer record);

    Answer selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Answer record);

    int updateByPrimaryKeyWithBLOBs(Answer record);

    int updateByPrimaryKey(Answer record);

    List<Answer> selectSelective(Answer answer);
}