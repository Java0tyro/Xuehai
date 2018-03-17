package xuehai.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import xuehai.dao.QuestionMapper;
import xuehai.model.*;
import org.springframework.stereotype.Service;
import xuehai.service.QuestionService;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public Question publish(Question question) {
        //添加问题记录
        return questionMapper.publish(question);
        //关注的人推送
    }

    @Override
    public Collection collect(Long userId, Long questionId) {
        //添加收藏记录
        Collection collection =  questionMapper.collect(userId, questionId);
        //问题收藏数加1
        //消息推送

        return collection;
    }

    @Override
    public Answer answerQuestion(Answer answer) {
        //添加回答记录
        return questionMapper.answerQuestion(answer);
        //问题回答数加1
        //推送
    }

    @Override
    public Like likeAnswer(Long userId, Long answerId) {
        //添加点赞回答记录
        Like like = questionMapper.likeAnswer(userId, answerId);
        //回答点赞数加1
        //推送
        return null;
    }

    @Override
    public Comment commentAnswer(Comment comment) {
        //添加回复记录
        return questionMapper.commentAnswer(comment);
        //推送 : 回答问题的人和回复对象

    }
}