import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import xuehai.dao.UserMapper;
import xuehai.vo.NumberControl;
import xuehai.vo.TimeLine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testGetTimeLine(){
        NumberControl numberControl = new NumberControl();
        numberControl.setUserId(3L);
        numberControl.setIndexNum(0);
        numberControl.setNumber(100);
        List<TimeLine> tl =  userMapper.getTimeLine(numberControl);
        System.out.println(tl);
    }
}
