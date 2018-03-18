package xuehai.util;

import xuehai.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtil {
    public static void addInformation(HttpServletRequest httpServletRequest, User user){
        HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute("userId", user.getId());
        session.setAttribute("login", true);
        session.setAttribute("authority", user.getAuthority());
        session.setAttribute("userName", user.getUsername());
        session.setAttribute("email", user.getEmail());
    }
}
