package com.lagou.test;

import com.lagou.dao.IUserDao;
import com.lagou.io.Resources;
import com.lagou.sqlSession.SqlSession;
import com.lagou.sqlSession.SqlSessionFactory;
import com.lagou.sqlSession.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.InputStream;

public class IPersistenceTest {

    @Test
    public void test() throws Exception {
        InputStream resourceAsSteam = Resources.getResourceAsSteam("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsSteam);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        //调用
//        User user = new User();
//        user.setId(1);
//        user.setUsername("张三");
      /*  User user2 = sqlSession.selectOne("user.selectOne", user);

        System.out.println(user2);*/

       /* List<User> users = sqlSession.selectList("user.selectList");
        for (User user1 : users) {
            System.out.println(user1);
        }*/

        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
//        user = userDao.findByCondition(user);
//        System.out.println(user);

//        List<User> all = userDao.findAll();
//        for (User user1 : all) {
//            System.out.println(user1);
//        }
//        userDao.deleteByCondition(user);
        //新增
//        User user = new User();
//        user.setId(3);
//        user.setUsername("王五");
//        int count = userDao.insertUser(user);

        //修改
//        user.setUsername("王六");
//        int count = userDao.updateUser(user);

        //删除
        int count = userDao.deleteUser(3);

        System.out.println("更新数据量：" + count);
        sqlSession.commit();
        sqlSession.close();


    }



}
