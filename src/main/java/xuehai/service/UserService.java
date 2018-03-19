package xuehai.service;

import xuehai.model.Follow;
import xuehai.model.User;
import xuehai.vo.UserVo;

import java.util.List;

public interface UserService {
    User login(String email);

    User regist(User user);

    User modify(User user);

    Follow follow(Long fromId, Long toId);

    int deleteUser(Long id);

//    UserVo getMyDetail(Long userId);
}
