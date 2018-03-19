package xuehai.dao;

import org.springframework.stereotype.Repository;
import xuehai.model.Comment;

import java.util.List;

@Repository
public interface CommentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKeyWithBLOBs(Comment record);

    int updateByPrimaryKey(Comment record);

    List<Comment> selectSelective(Comment comment);

    int deleteByQuestionId(Long questionId);
    int deleteByAnswerId(Long answerId);

    int getCommentNum(Long answerId);
}