package com.oa.manage.realm;

import com.oa.manage.service.EmpService;
import com.oa.pojo.Employee;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

public class MyRealm extends AuthorizingRealm {

    /**
     * 用来授权的
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Autowired
    private EmpService empService;
    /**
     * 用认证(登录校验用户名和密码的)
     * @param token 接收的是用户提交的用户名和密码的数据( subject.login(token)传过来)
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("用户提交的数据: "+token);
        //与数据库比对
        //根据用户名查询数据库
        String username = (String)token.getPrincipal();
        Employee emp = empService.findEmp(username);
        if(emp != null){
            //有这个admin对象--->核对密码
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(emp,emp.getPassword(), ByteSource.Util.bytes("sailing"),"myRealm");//"myRealm"指定自定义的Realm名字,可以随便写
            return info;
        }

        return null;
    }
}
