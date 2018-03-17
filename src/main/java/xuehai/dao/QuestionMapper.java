package xuehai.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import xuehai.model.*;

@Repository
public interface QuestionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Question record);

    int insertSelective(Question record);

    Question selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Question record);

    int updateByPrimaryKeyWithBLOBs(Question record);

    int updateByPrimaryKey(Question record);


    Question publish(Question question);
    Collection collect(@Param("userId") Long userId, @Param("questionId") Long questionId);


    Answer answerQuestion(Answer answer);
    Like likeAnswer(@Param("userId") Long userId, @Param("answerId") Long answerId);

    Comment commentAnswer(Comment comment);
}