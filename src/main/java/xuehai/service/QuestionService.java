package xuehai.service;

import org.springframework.stereotype.Service;
import xuehai.model.*;
import xuehai.vo.AnswerVo;
import xuehai.vo.QuestionVo;

import java.util.List;


@Service
public interface QuestionService {
    Question publish(Question question);
    Question deleteQuestion(Long questionId);
    Collection collect(Long userId, Long questionId);

    Answer answerQuestion(Answer answer);
    Answer modifyAnswer(Answer answer);
    Answer deleteAnswer(Long userId, Long answerId);
    Like likeAnswer(Long userId, Long answerId);

    Comment commentAnswer(Comment comment);
    Comment deleteComment(Long userId, Long commentId);

    List<AnswerVo> getAnswers(Answer answer);
    List<Like> getLikeList(Like like);
    List<Collection> getCollectionList(Collection collection);
    List<QuestionVo> getQuestions(Question question);
    List<Comment> getComments(Comment comment);

    List<QuestionType> getQuestionType(QuestionType questionType);

}
