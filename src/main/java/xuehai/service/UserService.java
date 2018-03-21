package xuehai.service;

import xuehai.model.Follow;
import xuehai.model.User;
import xuehai.vo.NumberControl;
import xuehai.vo.TimeLine;
import xuehai.vo.UserVo;

import java.util.List;

public interface UserService {
    User login(String email);

    User regist(User user);

    void sendEmail(String email, String address);

    List<User> getUsers(User user);

    int isDuplicated(String email);

    User modify(User user);

    Follow follow(Long fromId, Long toId);

    Follow cancelFollow(Long userFrom, Long userTo);

    List<Follow> getFollowList(Follow follow);

    int deleteUser(Long id);

    UserVo getDetail(Long userId);

    List<TimeLine> getTimeLine(NumberControl numberControl);
}
