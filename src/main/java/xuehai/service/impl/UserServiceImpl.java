package xuehai.service.impl;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import xuehai.dao.*;
import xuehai.model.Follow;
import xuehai.model.User;
import xuehai.service.UserService;
import xuehai.util.ByteToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xuehai.util.MailUtil;
import xuehai.vo.NumberControl;
import xuehai.vo.TimeLine;
import xuehai.vo.UserVo;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FollowMapper followMapper;

    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private LikeMapper likeMapper;

    @Autowired
    private CollectionMapper collectionMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;



    @Override
    public User login(String email) {
        User user = new User();
        user.setEmail(email);
        List<User> userList = userMapper.selectSelective(user);
        if(userList != null ){
            return userList.get(0);
        }
        return null;
    }

    @Override
    public User regist(User user) {
        String salt = null;
        byte[] temp = new byte[16];
        String strResult = null;
        try {
            //获取salt
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.nextBytes(temp);
            salt = ByteToString.getString(temp);
            //加密
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update((salt + user.getPassword()).getBytes());
            byte byteBuffer[] = messageDigest.digest();
            strResult = ByteToString.getString(byteBuffer);

        } catch (Exception e) {
            e.printStackTrace();
        }
        user.setSalt(salt);
        user.setPassword(strResult);
        int num = userMapper.insertSelective(user);
        user = userMapper.selectByPrimaryKey(user.getId());
        if(num != 0){
            return user;
        }
        return null;
    }


    @Override
    public void sendEmail(String email, String address) {
        String toEmail = email;
        String subject = "重置密码";
        String htmlContent = "<html><body><a href=\"" + address + "\">重置密码</a></body></html>" + "如果上述链接不能点击请复制以下链接：" + address;
        MailUtil.sendMail(toEmail, subject, htmlContent);
    }

    @Override
    public int isDuplicated(String email) {
        User user = new User();
        user.setEmail(email);
        List<User> userList = userMapper.selectSelective(user);
        if(userList.isEmpty()){
            return 0;
        }
        return 1;
    }

    @Override
    public List<User> getUsers(User user) {
        List<User> userList = userMapper.selectSelective(user);
        return  userList;
    }

    @Override
    public User modify(User user) {
        if(user.getPassword() != null){
            String salt = null;
            byte[] temp = new byte[16];
            String strResult = null;
            try {
                //获取salt
                SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
                random.nextBytes(temp);
                salt = ByteToString.getString(temp);
                //加密
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                messageDigest.update((salt + user.getPassword()).getBytes());
                byte byteBuffer[] = messageDigest.digest();
                strResult = ByteToString.getString(byteBuffer);

            } catch (Exception e) {
                e.printStackTrace();
            }
            user.setSalt(salt);
            user.setPassword(strResult);
        }
        user.setModifiedTime(new Date());
        int result = userMapper.updateByPrimaryKeySelective(user);
        if(result == 0){
            return null;
        }
        return user;
    }

    @Override
    public Follow follow(Long fromId, Long toId) {
        //添加follow记录
        Follow follow = new Follow();
        follow.setUserFrom(fromId);
        follow.setUserTo(toId);
        int followNum = followMapper.insertSelective(follow);
        Follow follow1 = followMapper.selectByPrimaryKey(follow.getId());
        if(followNum != 0){
            return follow1;
        }
        return null;
    }

    @Override
    public Follow cancelFollow(Long userFrom, Long userTo) {
        Follow follow = new Follow();
        follow.setUserFrom(userFrom);
        follow.setUserTo(userTo);
        List<Follow> followList = followMapper.selectSelective(follow);
        if(followList.size() == 0){
            return null;
        }
        else{
            Follow follow1 = followList.get(0);
            followMapper.deleteByPrimaryKey(follow1.getId());
            return follow1;
        }
    }

    @Override
    public List<Follow> getFollowList(Follow follow) {
        List<Follow> followList = followMapper.selectSelective(follow);
        return followList;
    }

    @Override
    public int deleteUser(Long id) {
        Map<String, Long> map = new HashMap<>();
        map.put("id", id);
        //调用存储过程
        userMapper.deleteUserByUserId(map);
        User user= userMapper.selectByPrimaryKey(id);
        if(user == null){
            return 1;
        }
        return 0;
    }

    @Override
    public UserVo getDetail(Long userId) {
        UserVo userVo = new UserVo();
        User user = userMapper.selectByPrimaryKey(userId);
        //个人信息中包括salt和password信息
        user.setSalt(null);
        user.setPassword(null);

        userVo.setUser(user);
        userVo.setAnswerNum(answerMapper.getAnswerNum(userId));
        userVo.setCollectionNum(collectionMapper.getCollectionNum(userId));
        userVo.setFollowedNum(followMapper.getFollowedNum(userId));
        userVo.setFollowingNum(followMapper.getFollowingNum(userId));
        userVo.setLikedNum(likeMapper.getLikedNum(userId));
        userVo.setQuestionNum(questionMapper.getQuestionNum(userId));

        return userVo;
    }

    @Override
    public List<TimeLine> getTimeLine(NumberControl numberControl) {
        List<TimeLine> timeLineList = userMapper.getTimeLine(numberControl);

        return timeLineList;
    }

}