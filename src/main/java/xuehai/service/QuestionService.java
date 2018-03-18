package xuehai.service;

import org.springframework.stereotype.Service;
import xuehai.model.*;


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

}
