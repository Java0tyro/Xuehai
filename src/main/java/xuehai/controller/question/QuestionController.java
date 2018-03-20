package xuehai.controller.question;


import org.springframework.web.bind.annotation.*;
import xuehai.model.*;
import xuehai.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import xuehai.vo.AnswerVo;
import xuehai.vo.QuestionVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.LinkedList;
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
    @RequestMapping(value = "/deleteAnswer/{id}", method = RequestMethod.GET)
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
    @RequestMapping(value = "/likeAnswer/{answerId}", method = RequestMethod.GET)
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

    //获取回答列表
    @ResponseBody
    @RequestMapping(value = "/getAnswers", method = RequestMethod.GET, produces = "application/json")
    public List<AnswerVo> getAnswers(@RequestParam(value = "id", required = false) Long id,
                                     @RequestParam(value = "content", required = false) String content,
                                     @RequestParam(value = "user", required = false) Long user,
                                     @RequestParam(value = "question", required = false) Long question,
                                     HttpSession session
                                     ){
        if((Boolean)session.getAttribute("login")){
            Answer answer = new Answer();
            answer.setId(id);
            answer.setQuestion(question);
            answer.setUser(user);
            answer.setContent(content);
            return questionService.getAnswers(answer);
        }
        return null;

    }
    //获取问题列表
    @ResponseBody
    @RequestMapping(value = "/getQuestions", method = RequestMethod.GET, produces = "application/json")
    public List<QuestionVo> getQuestions(@RequestParam(value = "id", required = false)Long id,
                                         @RequestParam(value = "user", required = false)Long userId,
                                         @RequestParam(value = "title", required = false)String title,
                                         @RequestParam(value = "content", required = false)String content,
                                         @RequestParam(value = "type", required = false)Long type,
                                         HttpSession session ){
        if((Boolean)session.getAttribute("login")){
                Question question = new Question();
                System.out.println(id + " " + userId );
                question.setUser(userId);
                question.setId(id);
                question.setTitle(title);
                question.setContent(content);
                question.setType(type);
                return questionService.getQuestions(question);
        }
        return null;

    }


    @ResponseBody
    @RequestMapping(value = "/getCollectionList/{questionId}", method = RequestMethod.GET)
    public List<Collection> getCollectionList(@PathVariable Long questionId,  HttpSession session){
        if((Boolean)session.getAttribute("login")){
            Collection collection = new Collection();
            collection.setQuestion(questionId);
            return questionService.getCollectionList(collection);
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/getLikeList/{answerId}", method = RequestMethod.GET)
    public List<Like> getLikeList(@PathVariable Long answerId, HttpSession session){
        if((Boolean)session.getAttribute("login")){
            Like like = new Like();
            like.setAnswer(answerId);
            return questionService.getLikeList(like);
        }
        return null;
    }


    //获取评论列表
    @ResponseBody
    @RequestMapping(value = "/getComment", method = RequestMethod.GET, produces = "application/json")
    public List<Comment> getComments(@RequestParam(value = "id", required = false)Long id,
                                     @RequestParam(value = "user", required = false)Long userId,
                                     @RequestParam(value = "content", required = false)String content,
                                     @RequestParam(value = "answer", required = false)Long answer,
                                     HttpSession session
                                     ){
        if((Boolean)session.getAttribute("login")){
            Comment comment = new Comment();
            comment.setUser(userId);
            comment.setAnswer(answer);
            comment.setContent(content);
            comment.setId(id);
            List<Comment> commentList = questionService.getComments(comment);
            return commentList;
        }
        return null;
    }


    @ResponseBody
    @RequestMapping(value = "/getQuestionType", method = RequestMethod.GET, produces = "application/json")
    public List<QuestionType> getQuestionType(@RequestParam(value = "id", required = false) Long id,
                                              @RequestParam(value = "name", required = false)String name,
                                              @RequestParam(value = "parent", required = false)Long parent,
                                              HttpSession session){
        if((Boolean)session.getAttribute("login")){
            QuestionType questionType = new QuestionType();
            questionType.setId(id);
            questionType.setName(name);
            questionType.setParent(parent);
            List<QuestionType> questionTypes = questionService.getQuestionType(questionType);
            return questionTypes;
        }
        return null;
    }




}
