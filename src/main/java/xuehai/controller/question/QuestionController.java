package xuehai.controller.question;

import org.springframework.web.bind.annotation.*;
import xuehai.model.*;
import xuehai.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @ResponseBody
    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    public String publish(@RequestBody Question question, HttpServletRequest httpServletRequest){
        if((Boolean) httpServletRequest.getSession().getAttribute("login")){
            question.setUser((Long) httpServletRequest.getSession().getAttribute("userId"));
            Question question1 = questionService.publish(question);
            if(question1 == null){
                return "0";
            }
            return "1";
        }
        return "0";
    }

    @ResponseBody
    @RequestMapping(value = "/collection/{questionId}", method = RequestMethod.POST)
    public String collect(@PathVariable Long questionId, HttpServletRequest httpServletRequest){
        if((Boolean) httpServletRequest.getSession().getAttribute("login")){
            Long userId = (Long)httpServletRequest.getSession().getAttribute("userId");
            Collection collection = questionService.collect(userId, questionId);
           if(collection != null){
               return "1";
           }
           return "0";
        }
        return "0";
    }

    @ResponseBody
    @RequestMapping(value = "/answer", method = RequestMethod.POST)
    public String answerQuestion(@RequestBody Answer answer, HttpServletRequest httpServletRequest){
        if((Boolean) httpServletRequest.getSession().getAttribute("login")){
            answer.setUser((Long)httpServletRequest.getSession().getAttribute("userId"));
            Answer answer1 = questionService.answerQuestion(answer);
            if(answer1 != null){
                return "1";
            }
            return "0";
        }
        return "0";
    }

    @ResponseBody
    @RequestMapping(value = "/like/{answerId}")
    public String likeAnswer(@PathVariable Long answerId, HttpServletRequest httpServletRequest){
        if((Boolean) httpServletRequest.getSession().getAttribute("login")){
            Long userId = (Long)httpServletRequest.getSession().getAttribute("userId");
            Like like = questionService.likeAnswer(userId, answerId);
            if(like != null){
                return "1";
            }
            return "0";
        }
        return "0";
    }

    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public String commentAnswer(@RequestBody Comment comment, HttpServletRequest httpServletRequest){
        if((Boolean) httpServletRequest.getSession().getAttribute("login")){
            comment.setUser((Long)httpServletRequest.getSession().getAttribute("userId"));
            Comment comment1 = questionService.commentAnswer(comment);
            if(comment1 != null){
                return "1";
            }
            return "0";
        }
        return "0";
    }

}
