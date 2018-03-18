package xuehai.service.impl;

import xuehai.dao.FollowMapper;
import xuehai.dao.UserMapper;
import xuehai.model.Follow;
import xuehai.model.User;
import xuehai.service.UserService;
import xuehai.util.ByteToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xuehai.util.MessageType;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FollowMapper followMapper;

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
    public int deleteUser(Long id) {
        //删除用户表记录
        int num = userMapper.deleteByPrimaryKey(id);
        if(num != 0){
            return 1;
        }
        return 0;
    }

}