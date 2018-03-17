package xuehai.controller.question;

import org.springframework.web.bind.annotation.PathVariable;
import xuehai.model.*;
import xuehai.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    public Question publish(@RequestBody Question question, HttpServletRequest httpServletRequest){
        if((Boolean) httpServletRequest.getSession().getAttribute("login")){
            question.setUser((Long) httpServletRequest.getSession().getAttribute("userId"));
            Question question1 = questionService.publish(question);
            return question1;
        }
        return null;
    }

    @RequestMapping(value = "/collection/{questionId}", method = RequestMethod.POST)
    public Collection collect(@PathVariable Long questionId, HttpServletRequest httpServletRequest){
        if((Boolean) httpServletRequest.getSession().getAttribute("login")){
            Long userId = (Long)httpServletRequest.getSession().getAttribute("userId");
            Collection collection = questionService.collect(userId, questionId);
            return collection;
        }
        return null;
    }

    @RequestMapping(value = "/answer", method = RequestMethod.POST)
    public Answer answerQuestion(@RequestBody Answer answer, HttpServletRequest httpServletRequest){
        if((Boolean) httpServletRequest.getSession().getAttribute("login")){
            answer.setUser((Long)httpServletRequest.getSession().getAttribute("userId"));
            Answer answer1 = questionService.answerQuestion(answer);
            return answer1;
        }
        return null;
    }

    @RequestMapping(value = "/like/{answerId}")
    public Answer likeAnswer(@PathVariable Long answerId, HttpServletRequest httpServletRequest){
        if((Boolean) httpServletRequest.getSession().getAttribute("login")){
            Long userId = (Long)httpServletRequest.getSession().getAttribute("userId");
            questionService.likeAnswer(userId, answerId);
        }
        return null;
    }

    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Comment commentAnswer(@RequestBody Comment comment, HttpServletRequest httpServletRequest){
        if((Boolean) httpServletRequest.getSession().getAttribute("login")){
            comment.setUser((Long)httpServletRequest.getSession().getAttribute("userId"));
            Comment comment1 = questionService.commentAnswer(comment);
            return comment1;
        }
        return null;
    }

}
