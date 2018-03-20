package xuehai.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import xuehai.dao.*;
import xuehai.model.*;
import org.springframework.stereotype.Service;
import xuehai.service.QuestionService;
import xuehai.util.MessageType;
import xuehai.vo.AnswerVo;
import xuehai.vo.QuestionVo;

import java.awt.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private FollowMapper followMapper;

    @Autowired
    private CollectionMapper collectionMapper;

    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private LikeMapper likeMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionTypeMapper questionTypeMapper;

    @Override
    public Question publish(Question question) {
        //添加问题记录
        int questionNum = questionMapper.insertSelective(question);


        if(questionNum != 0){
            return question;
        }
        return null;

    }

    @Override
    public Question deleteQuestion(Long questionId) {
        Question question = questionMapper.selectByPrimaryKey(questionId);
        if(question != null){
            //先删除回答的回复
            commentMapper.deleteByQuestionId(questionId);
            //删除回答的点赞
            likeMapper.deleteByQuestionId(questionId);
            //删除问题的回答
            answerMapper.deleteByQuestionId(questionId);
            //删除问题的收藏
            collectionMapper.deleteByQuestionId(questionId);
            //删除问题
            int questionNum = questionMapper.deleteByPrimaryKey(questionId);
            if(questionNum != 0){
                return question;
            }
            return null;
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

        if(answerNum != 0){
            return answer1;
        }
        return null;
    }

    @Override
    public Answer modifyAnswer(Answer answer) {
        Answer answer1 = answerMapper.selectByPrimaryKey(answer.getId());
        if(answer1.getUser() == answer.getUser()){
            answer.setModifiedTime(new Date());
            int answerNum = answerMapper.updateByPrimaryKeySelective(answer);
            if(answerNum != 0){
                return answer1;
            }
            return null;
        }
        return null;
    }

    @Override
    public Answer deleteAnswer(Long userId, Long answerId) {

        Answer answer = answerMapper.selectByPrimaryKey(answerId);
        if(answer != null && answer.getUser() == userId){
            //删除回答的回复
            commentMapper.deleteByAnswerId(answerId);
            //删除的回答的点赞
            likeMapper.deleteByAnswerId(answerId);
            //删除回答
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

        if(commentNum != 0){
            return comment1;
        }
        return null;
    }

    @Override
    public Comment deleteComment(Long userId, Long commentId) {
        //删除评论
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


    @Override
    public List<AnswerVo> getAnswers(Answer answer) {
        List<AnswerVo> answerVoList = new LinkedList<>();
        AnswerVo answerVo = new AnswerVo();
        answer.setContent(answer.getContent() != null ?
                "%" + answer.getContent() + "%" : null);
        List<Answer> answerList = answerMapper.selectSelective(answer);
        for(Answer answer1 : answerList){
           answerVo.setAnswer(answer1);
           answerVo.setCommentNum(commentMapper.getCommentNum(answer1.getId()));
           answerVo.setLikedNum(likeMapper.getLikedNum(answer1.getUser()));
           answerVoList.add(answerVo);
        }
        return answerVoList;
    }


    @Override
    public List<Collection> getCollectionList(Collection collection) {
        return collectionMapper.selectSelective(collection);
    }

    @Override
    public List<Like> getLikeList(Like like) {
        return likeMapper.selectSelective(like);
    }

    @Override
    public List<Comment> getComments(Comment comment) {
        comment.setContent(comment.getContent() != null ?
                "%" + comment.getContent() + "%" : null);
        return commentMapper.selectSelective(comment);
    }

    @Override
    public List<QuestionVo> getQuestions(Question question) {
        List<QuestionVo> questionVoList = new LinkedList<>();
        question.setTitle(question.getTitle() != null ?
                ("%" + question.getTitle() + "%") : null);
        question.setContent(question.getContent() != null ?
                "%" + question.getContent() + "%" : null);

        QuestionVo questionVo = new QuestionVo();
        List<Question> questionList = questionMapper.selectSelective(question);
        for(Question question1 : questionList){
            questionVo.setQuestion(question1);
            Long userId = question1.getUser();
            questionVo.setAnswerNum(answerMapper.getAnswerNum(userId));
            questionVo.setCollectionNum(collectionMapper.getCollectionNum(userId));
            questionVoList.add(questionVo);
        }
        return questionVoList;
    }

    @Override
    public List<QuestionType> getQuestionType(QuestionType questionType) {
        return questionTypeMapper.selectSelective(questionType);
    }
}
