package xuehai.service;

import org.springframework.stereotype.Service;
import xuehai.model.*;


@Service
public interface QuestionService {
    Question publish(Question question);
    Collection collect(Long userId, Long questionId);

    Answer answerQuestion(Answer answer);
    Like likeAnswer(Long userId, Long answerId);

    Comment commentAnswer(Comment comment);

}
