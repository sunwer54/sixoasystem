package com.oa.manage.realm;

import com.oa.manage.service.EmpService;
import com.oa.mapper.PowerMapper;
import com.oa.pojo.Employee;
import com.oa.pojo.Power;
import com.oa.pojo.PowerExample;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import sun.java2d.pipe.SpanShapeRenderer;

import java.util.List;

public class MyRealm extends AuthorizingRealm {
    @Autowired
    private PowerMapper powerMapper;

    /**
     * 用来授权的
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取subject对象（可以看做是session）（内含登录者身份信息）
        Subject subject = SecurityUtils.getSubject();
        //从subject对象中获取到用户身份信息
        Employee employee = (Employee)subject.getPrincipal();
        //查询用户的角色信息
        PowerExample exp = new PowerExample();
        exp.createCriteria().andEmpidEqualTo(employee.getEmpid());
        List<Power> powers = powerMapper.selectByExample(exp);
        String role = powers.get(0).getEmpid();
        //设置角色
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRole(role);
        //授予权限（给登录者授予访问uri的细粒度权限）
        String power = powers.get(0).getEmpid();
        //把权限放入授权对象中
        info.addStringPermission(power);
        return info;
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
