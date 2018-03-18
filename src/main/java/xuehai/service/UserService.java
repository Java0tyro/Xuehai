package xuehai.service;

import xuehai.model.Follow;
import xuehai.model.User;

public interface UserService {
    User login(String email);

    User regist(User user);

    User modify(User user);

    Follow follow(Long fromId, Long toId);

    int deleteUser(Long id);
}
