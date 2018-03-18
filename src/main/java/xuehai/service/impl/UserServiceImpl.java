package xuehai.service.impl;

import xuehai.dao.FollowMapper;
import xuehai.dao.MessageMapper;
import xuehai.dao.UserMapper;
import xuehai.model.Follow;
import xuehai.model.Message;
import xuehai.model.User;
import xuehai.service.UserService;
import xuehai.util.ByteToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xuehai.util.MessageType;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FollowMapper followMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public User login(String email) {
        return userMapper.login(email);
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
        //System.out.println(user.getId());
        user = userMapper.selectByPrimaryKey(user.getId());
        //System.out.println(user);
        return user;
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
        int followNum = userMapper.follow(follow);
        Follow follow1 = followMapper.selectByPrimaryKey(follow.getId());
        //推送
        Message message = new Message();
        message.setUser(toId);
        message.setContentType(MessageType.FOLLOW.getValue());
        message.setContentId(follow.getId());
        message.setTime(follow1.getTime());
        int messageNum = messageMapper.insertSelective(message);
        if(follow1 == null || messageNum == 0){
            return null;
        }
        return follow1;
    }

    @Override
    public int deleteUser(Long id) {
        //删除用户表记录
        int num = userMapper.deleteByPrimaryKey(id);
        if(num != 0){
            return 1;
        }
        return 0;
    }
}