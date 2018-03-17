package xuehai.service.impl;

import xuehai.dao.UserMapper;
import xuehai.model.Follow;
import xuehai.model.User;
import xuehai.service.UserService;
import xuehai.util.ByteToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.SecureRandom;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

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
        Long userId = userMapper.regist(user);
        user = userMapper.selectByPrimaryKey(userId);
        return user;
    }


    @Override
    public User modify(User user) {
        return userMapper.modify(user);
    }


    @Override
    public Follow follow(Long fromId, Long toId) {
        return userMapper.follow(fromId, toId);
    }

    @Override
    public User deleteUser(Long id) {
        //删除用户后,
        User user = userMapper.deleteUser(id);
        return user;
    }
}