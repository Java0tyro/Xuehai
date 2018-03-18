package xuehai.controller.question;

import org.springframework.web.bind.annotation.*;
import xuehai.model.*;
import xuehai.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

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
    @RequestMapping(value = "/deleteQuestion/{id}", method = RequestMethod.GET)
    public String deleteQuestion(@PathVariable Long id, HttpSession session){
        Integer authority = (Integer) session.getAttribute("authority");
        Boolean login = (Boolean) session.getAttribute("login");
        if(login == true && authority == 1){
            Question question = questionService.deleteQuestion(id);
            if(question != null){
                return "1";
            }
            return "0";
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
    @RequestMapping(value = "/modifyAnswer", method = RequestMethod.POST)
    public String modifyAnswer(@RequestBody Answer answer, HttpSession session){
        if((Boolean) session.getAttribute("login")){
            Long userId = (Long)session.getAttribute("userId");
            answer.setUser(userId);
            Answer answer1 = questionService.modifyAnswer(answer);
            if(answer1 != null){
                return "1";
            }
            return "0";
        }
        return "0";
    }

    @ResponseBody
    @RequestMapping(value = "/deleteAnswer{id}", method = RequestMethod.GET)
    public String deleteAnswer(@PathVariable Long id, HttpSession session){
        if((Boolean) session.getAttribute("login")){
            Long userId = (Long)session.getAttribute("userId");
            Answer answer1 = questionService.deleteAnswer(userId, id);
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

    @ResponseBody
    @RequestMapping(value = "/deleteComment/{id}", method =  RequestMethod.GET)
    public String deleteComment(@PathVariable Long id, HttpSession session){
        if((Boolean) session.getAttribute("login")){
            Long userId = (Long)session.getAttribute("userId");
            Comment comment = questionService.deleteComment(userId, id);
            return "0";
        }
        return "0";
    }
}
