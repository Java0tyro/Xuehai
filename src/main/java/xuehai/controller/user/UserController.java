package xuehai.controller.user;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.ui.Model;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import xuehai.model.Follow;
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
import java.io.File;
import java.security.MessageDigest;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    String secret = "shdjshfjkdhkfhdsjkfsdsds0";

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    public UserVo login(@RequestBody RegistVo registVo,
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
            return userService.getDetail(user.getId());
        }
        return null;

    }

    @ResponseBody
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session){
        session.setAttribute("login", false);
        return "1";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLogin(){
        return "login";
    }

    @ResponseBody
    @RequestMapping(value = "/regist", method = RequestMethod.POST, produces = "application/json")
    public User regist(@RequestBody RegistVo registVo,
                       HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse){
        User user = registVo.getUser();
        User user1 =  userService.regist(user);
        if(user1 != null){
            SessionUtil.addInformation(httpServletRequest, user1);
            return user1;
        }
        return null;
    }

    @RequestMapping(value = "/regist", method = RequestMethod.GET)
    public String getRegist(){
        return "regist";
    }


    @RequestMapping(value = "/getUpload", method = RequestMethod.GET)
    public String get(){
        return "show";
    }

    @ResponseBody
    @RequestMapping(value = "/forget", method = RequestMethod.GET)
    public String forgetPassword(@RequestParam(value = "email", required = true)String email) throws Exception{
        if(userService.isDuplicated(email) != 1){
            return "";
        }
        Date start = new Date();
        Date end = new Date();
        end.setTime( start.getTime() + 10 * 60 *1000 );//过期10分钟
                Algorithm algorithm = Algorithm.HMAC512(secret);
        String token = JWT.create()
                .withIssuer("xue_hai")
                .withSubject("1710519209")
                .withAudience("kkkk")
                .withExpiresAt(end)
                .withIssuedAt(start)
                .sign(algorithm);
        String address = "http://localhost:8080/Xuehai/user/reset?token=" + token;
        System.out.println(address);
        userService.sendEmail(email, address);
        return "1";
    }

    @ResponseBody
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(@RequestParam(value="file",required=false) CommonsMultipartFile file,
                         HttpServletRequest request) throws Exception{

            //获得物理路径webapp所在路径
            String pathRoot = request.getSession().getServletContext().getRealPath("/");
            System.out.println(pathRoot);
            String path="";
            if(file != null){
                if(!file.isEmpty()){
                    //生成uuid作为文件名称
                    String uuid = UUID.randomUUID().toString().replaceAll("-","");
                    //获得文件类型（可以判断如果不是图片，禁止上传）
                    String contentType=file.getContentType();
                    //获得文件后缀名称
                    String imageName=contentType.substring(contentType.indexOf("/")+1);
                    path="static/images/"+uuid+"."+imageName;
                    File newFile = new File(pathRoot+path);
                    if( !newFile.getParentFile().exists()) {
                        // 如果目标文件所在的目录不存在，则创建父目录
                        newFile.getParentFile().mkdirs();
                    }
                    file.transferTo(newFile);
                    return path;
                }
            }

            return "1";
    }

    @ResponseBody
    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public String reset(@RequestParam(value = "token", required = true)String token,
                        Model model){
        model.addAttribute("token", token);
        return "/reset";
    }

    @ResponseBody
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public String resetPassword(@RequestBody User user,
                                @RequestParam(value = "token", required = true)String token){
        //JWT验证
        try{
            Algorithm algorithm = Algorithm.HMAC512(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("xue_hai")
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            String email = jwt.getSubject();
            user.setEmail(email);
            user = userService.getUsers(user).get(0);

            //验证时间
            if(new Date().getTime() > jwt.getExpiresAt().getTime()){
                throw new Exception();

            }
            User user1 = new User();
            user1.setId(user.getId());
            user1.setPassword(user.getPassword());
            userService.modify(user1);
            return "1";
        }
        catch (Exception e){

        }
        return "";
    }

    @ResponseBody
    @RequestMapping(value = "/getUsers", method =  RequestMethod.GET, produces = "application/json")
    public List<User> getUsers(@RequestParam(value = "id", required = false)Long userId,
                               @RequestParam(value = "username", required = false)String username,
                               @RequestParam(value = "email", required = false)String email
                               ){
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/isDupicated/{email}", method = RequestMethod.GET)
    public String isDuplicated(@PathVariable String email){
        int num = userService.isDuplicated(email);
        if(num == 1){
            return "1";
        }
        else{
            return "0";
        }
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
        return "";
    }

    @ResponseBody
    @RequestMapping(value = "/cancelFollow/{userId}", method = RequestMethod.GET)
    public String cancelFollow(@PathVariable Long userId, HttpSession session){
        if((Boolean)session.getAttribute("login")){
            Follow follow = userService.cancelFollow((Long)session.getAttribute("userId"), userId);
            if(follow != null){
                return  "1";
            }
            return "0";
        }
        return "";
    }

    @ResponseBody
    @RequestMapping(value = "/getFollowList", method = RequestMethod.GET, produces = "application/json")
    public List<Follow> getFollowedList(@RequestParam(value = "from", required = false)Long userFrom,
            @RequestParam(value = "to", required = false)Long userTo,
            HttpSession session){
        if((Boolean)session.getAttribute("login")){
            Follow follow = new Follow();
            follow.setUserFrom(userFrom);
            follow.setUserTo(userTo);
            List<Follow> followList = userService.getFollowList(follow);
            return followList;
        }
        return null;
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
        return "";
    }

    @ResponseBody
    @RequestMapping(value = "/getMyDetail", method = RequestMethod.GET, produces = "application/json")
    public UserVo getMyDetail(HttpSession session){
        if((Boolean)session.getAttribute("login")){
            UserVo userVo = userService.getDetail((Long)session.getAttribute("userId"));
            return userVo;
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/getUserDetail/{userId}", method = RequestMethod.GET, produces = "application/json")
    public UserVo getUserDetail(@PathVariable Long userId, HttpSession session) {
        if((Boolean) session.getAttribute("login")){
            UserVo userVo = userService.getDetail(userId);
            return userVo;
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/getTimeLine", method = RequestMethod.GET, produces = "application/json")
    public List<TimeLine> getTimeLine(@RequestParam(value = "indexNum", required = false) int indexNum,
                                      @RequestParam(value = "number", required = false) int number,
                                      HttpSession session){
        if((Boolean) session.getAttribute("login")){
            Long userId = (Long )session.getAttribute("userId");
            NumberControl numberControl = new NumberControl();
            numberControl.setNumber(number);
            numberControl.setIndexNum(indexNum);
            numberControl.setUserId(userId);
            List<TimeLine> timeLineList = userService.getTimeLine(numberControl);
            return timeLineList;
        }
        return null;
    }


}

