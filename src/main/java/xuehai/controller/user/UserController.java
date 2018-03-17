package xuehai.controller.user;


import xuehai.model.User;
import xuehai.service.UserService;
import xuehai.util.ByteToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.MessageDigest;


@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(@RequestBody String email, @RequestBody String password, @RequestBody String redirectUrl, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){

        User user = userService.login(email);
        if(user == null){
            return;
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
        if(strResult != null && strResult == user.getPassword()){
            HttpSession session = httpServletRequest.getSession(true);
            session.setAttribute("userId", user.getId());
            session.setAttribute("login", true);
            session.setAttribute("authority", user.getAuthority());
            session.setAttribute("userName", user.getUsername());
            session.setAttribute("email", user.getEmail());
            httpServletResponse.setHeader("Content-type", "application/json;charset=utf-8");
            try {
                httpServletResponse.getWriter().write("{\"redirect\":\"" + redirectUrl + "\"}");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ;

        }
        return ;

    }

    @RequestMapping(name = "/login", method = RequestMethod.GET)
    public String getLogin(){
        return "login";
    }

    @ResponseBody
    @RequestMapping(value = "/regist", method = RequestMethod.POST)
    public User regist(@RequestBody User user,
                       HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse){
        User user1 =  userService.regist(user);
        if(user1 != null){
            HttpSession session = httpServletRequest.getSession(true);
            session.setAttribute("userId", user1.getId());
            session.setAttribute("login", true);
            session.setAttribute("authority", user1.getAuthority());
            session.setAttribute("userName", user1.getUsername());
            session.setAttribute("email", user1.getEmail());
            httpServletResponse.setHeader("Content-type", "application/json;charset=utf-8");
            return user1;
        }
        return null;
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public void modify(@RequestBody User user, HttpServletRequest httpServletRequest){
        if((Boolean) httpServletRequest.getSession().getAttribute("login")){
            user.setId((Long)httpServletRequest.getSession().getAttribute("userId"));
            User user1 = userService.modify(user);
        }
    }

    @RequestMapping(value = "/follow/{id}")
    public void follow(@PathVariable Long id, HttpServletRequest httpServletRequest){
        if((Boolean) httpServletRequest.getSession().getAttribute("login")){
            userService.follow((Long) httpServletRequest.getSession().getAttribute("userId"), id);
        }
    }

    @RequestMapping(value = "/delete/{id}")
    public User deleteUser(@PathVariable Long id, HttpServletRequest httpServletRequest){
        HttpSession session = httpServletRequest.getSession();
        if((Boolean)session.getAttribute("login") && (short)session.getAttribute("authority") == (short)1){
            return userService.deleteUser(id);
        }
        return null;
    }
}
