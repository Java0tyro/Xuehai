package xuehai.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import xuehai.dao.*;
import xuehai.model.*;
import org.springframework.stereotype.Service;
import xuehai.service.QuestionService;
import xuehai.util.MessageType;

import java.awt.*;
import java.util.Date;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private FollowMapper followMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private CollectionMapper collectionMapper;

    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private LikeMapper likeMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public Question publish(Question question) {
        //添加问题记录
        int questionNum = questionMapper.insertSelective(question);
        //关注的人推送
        Long questionId = question.getId();
        Question question1 = questionMapper.selectByPrimaryKey(questionId);
        Follow follow = new Follow();
        follow.setUserTo(question.getUser());
        List<Follow> followList = followMapper.selectSelective(follow);
        Message message = new Message();
        for(Follow follow1 : followList){
            message.setId(null);
            message.setTime(null);
            message.setContentId(questionId);
            message.setContentType(MessageType.PUBLISH.getValue());
            message.setUser(follow1.getUserFrom());
            message.setTime(question1.getTime());
            messageMapper.insertSelective(message);
        }
        if(questionNum != 0){
            return question;
        }
        return null;

    }

    @Override
    public Question deleteQuestion(Long questionId) {
        Question question = questionMapper.selectByPrimaryKey(questionId);
        int questionNum = questionMapper.deleteByPrimaryKey(questionId);
        if(questionNum != 0){
            return question;
        }
        return null;
    }

    @Override
    public Collection collect(Long userId, Long questionId) {
        //添加收藏记录
        Collection collection = new Collection();
        collection.setUser(userId);
        collection.setQuestion(questionId);
        int collectionNum = collectionMapper.insertSelective(collection);
        Collection collection1 = collectionMapper.selectByPrimaryKey(collection.getId());
//        //问题收藏数加1
        Question question = questionMapper.selectByPrimaryKey(questionId);
//        Question question1 = new Question();
//        question1.setId(question.getId());
//        question1.setCollectionNum(question.getCollectionNum() + 1);
//        questionMapper.updateByPrimaryKeySelective(question1);
        //消息推送
        Message message = new Message();
        message.setTime(collection1.getTime());
        message.setUser(question.getUser());
        message.setContentId(collection1.getId());
        message.setContentType(MessageType.COLLECTION.getValue());
        messageMapper.insertSelective(message);

        if(collectionNum != 0 ){
            return collection1;
        }
        return null;
    }

    @Override
    public Answer answerQuestion(Answer answer) {
        //添加回答记录
        int answerNum = answerMapper.insertSelective(answer);
        Answer answer1 = answerMapper.selectByPrimaryKey(answer.getId());
        Question question = questionMapper.selectByPrimaryKey(answer.getQuestion());

        //问题回答数加1
        //推送
        Message message = new Message();
        message.setTime(answer1.getTime());
        message.setUser(question.getUser());
        message.setContentId(answer1.getId());
        message.setContentType(MessageType.ANSWER.getValue());
        messageMapper.insertSelective(message);

        if(answerNum != 0){
            return answer1;
        }
        return null;
    }

    @Override
    public Answer modifyAnswer(Answer answer) {
        Answer answer1 = answerMapper.selectByPrimaryKey(answer.getId());
        if(answer1 != null && answer1.getUser() == answer.getUser()){
            answer.setModifiedTime(new Date());
            int answerNum = answerMapper.updateByPrimaryKey(answer);
            if(answerNum != 0){
                return answer;
            }
            return null;
        }
        return null;
    }

    @Override
    public Answer deleteAnswer(Long userId, Long answerId) {
        Answer answer = answerMapper.selectByPrimaryKey(answerId);
        if(answer != null && answer.getUser() == userId){
            int answerNum = answerMapper.deleteByPrimaryKey(answerId);
            if(answerNum != 0){
                return answer;
            }
            return null;
        }
        return null;
    }

    @Override
    public Like likeAnswer(Long userId, Long answerId) {
        //添加点赞回答记录
        Like like = new Like();
        like.setAnswer(answerId);
        like.setUser(userId);
        int likeNum = likeMapper.insertSelective(like);
        Like like1 = likeMapper.selectByPrimaryKey(like.getId());
        //回答点赞数加1
        //推送
        Message message = new Message();
        message.setTime(like1.getTime());
        message.setUser(userId);
        message.setContentId(like1.getId());
        message.setContentType(MessageType.LIKE.getValue());
        messageMapper.insertSelective(message);

        if(likeNum != 0){
            return like1;
        }
        return null;
    }

    @Override
    public Comment commentAnswer(Comment comment) {
        //添加回复记录
        int commentNum = commentMapper.insertSelective(comment);
        Comment comment1 = commentMapper.selectByPrimaryKey(comment.getId());
        Answer answer = answerMapper.selectByPrimaryKey(comment1.getAnswer());
        //推送 :发布问题的人
        //推送 :回答问题的人
        Message messageAnswer = new Message();
        messageAnswer.setTime(comment1.getTime());
        messageAnswer.setUser(answer.getUser());
        messageAnswer.setContentId(comment1.getId());
        messageAnswer.setContentType(MessageType.COMMENT.getValue());
        messageMapper.insertSelective(messageAnswer);
        //推送 :评论的人


        if(commentNum != 0){
            return comment1;
        }
        return null;
    }

    @Override
    public Comment deleteComment(Long userId, Long commentId) {
        Comment comment = commentMapper.selectByPrimaryKey(commentId);
        if(comment != null && comment.getUser() == userId){
            int commentNum = commentMapper.deleteByPrimaryKey(commentId);
            if(commentNum != 0){
                return comment;
            }
            return null;
        }
        return null;
    }

}