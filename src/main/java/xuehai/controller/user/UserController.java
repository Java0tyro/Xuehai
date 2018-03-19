package xuehai.controller.user;


import org.springframework.util.backoff.BackOff;
import xuehai.model.Answer;
import xuehai.model.Follow;
import xuehai.model.Question;
import xuehai.model.User;
import xuehai.service.UserService;
import xuehai.util.ByteToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import xuehai.util.SessionUtil;
import xuehai.vo.NumberControl;
import xuehai.vo.RegistVo;
import xuehai.vo.TimeLine;
import xuehai.vo.UserVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.util.List;


@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestBody RegistVo registVo,
                        HttpServletRequest httpServletRequest,
                        HttpServletResponse httpServletResponse){
        String email = registVo.getUser().getEmail();
        String password = registVo.getUser().getPassword();
        if(null == email || null == password){
            return null;
        }
        User user = userService.login(email);
        if(user == null){
            return null;
        }
        String salt = user.getSalt();
        String strResult = null;
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update((salt + password).getBytes() );
            byte byteBuffer[] = messageDigest.digest();
            strResult = ByteToString.getString(byteBuffer);
        }
        catch(Exception e){

        }

        if(strResult != null && strResult.equals(user.getPassword())){
            SessionUtil.addInformation(httpServletRequest, user);
            return registVo.getRedirectUrl();
        }
        return null;

    }

    @RequestMapping(name = "/login", method = RequestMethod.GET)
    public String getLogin(){
        return "login";
    }

    @ResponseBody
    @RequestMapping(value = "/regist", method = RequestMethod.POST)
    public String regist(@RequestBody RegistVo registVo,
                       HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse){
        User user = registVo.getUser();
        User user1 =  userService.regist(user);
        if(user1 != null){
            SessionUtil.addInformation(httpServletRequest, user1);
            return registVo.getRedirectUrl();
        }
        return "/regist";
    }

    @RequestMapping(value = "/regist", method = RequestMethod.GET)
    public String getRegist(){
        return "regist";
    }


    @ResponseBody
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public String modify(@RequestBody User user, HttpServletRequest httpServletRequest){
        if((Boolean) httpServletRequest.getSession().getAttribute("login")){
            HttpSession session = httpServletRequest.getSession();
            user.setId((Long)session.getAttribute("userId"));
            user.setAuthority((int)session.getAttribute("authority"));
            user.setTime(null);
            if(null == userService.modify(user)){
                return "0";
            }
            SessionUtil.addInformation(httpServletRequest, user);
            return "1";
        }
        return "0";
    }

    @ResponseBody
    @RequestMapping(value = "/follow/{id}", method = RequestMethod.POST)
    public String follow(@PathVariable Long id, HttpServletRequest httpServletRequest){
        if((Boolean) httpServletRequest.getSession().getAttribute("login")){
            Follow follow = userService.follow((Long) httpServletRequest.getSession().getAttribute("userId"), id);
            if(follow == null){
                return "0";
            }
            return "1";
        }
        return "0";
    }


    @ResponseBody
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteUser(@PathVariable Long id, HttpServletRequest httpServletRequest){
        HttpSession session = httpServletRequest.getSession();
        if((Boolean)session.getAttribute("login") && (Integer)session.getAttribute("authority") == 1){
            int judge = userService.deleteUser(id);
            if(judge != 0){
                return "1";
            }
            return "0";
        }
        return "0";
    }

    @ResponseBody
    @RequestMapping(value = "/getMyDetail", method = RequestMethod.GET)
    public UserVo getMyDetail(HttpSession session){
        if((Boolean)session.getAttribute("login")){
            UserVo userVo = userService.getDetail((Long)session.getAttribute("userId"));
            return userVo;
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/getUserDetail/{userId}", method = RequestMethod.GET)
    public UserVo getUserDetail(@PathVariable Long userId, HttpSession session) {
        if((Boolean) session.getAttribute("login")){
            UserVo userVo = userService.getDetail(userId);
            return userVo;
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/getTimeLine")
    public List<TimeLine> getTimeLine(@RequestBody NumberControl numberControl, HttpSession session){
        if((Boolean) session.getAttribute("login")){
            List<TimeLine> timeLineList = userService.getTimeLine((Long )session.getAttribute("userId"), numberControl);
            return timeLineList;
        }
        return null;
    }



}

