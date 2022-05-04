package com.oa.manage.config;

import com.oa.manage.realm.MyRealm;
import com.oa.manage.realm.MyRealm2;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AllSuccessfulStrategy;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    /**
     * 设置散列加密
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //指定散列算法
        credentialsMatcher.setHashAlgorithmName("md5");
        //指定加密的迭代次数
        credentialsMatcher.setHashIterations(2); //默认是一次
        return credentialsMatcher;

    }

    @Bean  //这个注解是,当前方法在当前类被加载的时候会被自动调用,创建的对象交给spring管理,等同于<bean id = "myRealm" class="com.oa.manage.realm.MyRealm">
    public MyRealm userRealm(@Qualifier("hashedCredentialsMatcher") HashedCredentialsMatcher matcher){
        MyRealm myRealm = new MyRealm();
        //给我们自定义的Realm对象中设置散列加密算法
        myRealm.setCredentialsMatcher(matcher);
        return myRealm;
    }
    @Bean
    public MyRealm2 userRealm2(@Qualifier("hashedCredentialsMatcher") HashedCredentialsMatcher matcher){
        MyRealm2 myRealm2 = new MyRealm2();
        //给我们自定义的Realm对象中设置散列加密算法
        myRealm2.setCredentialsMatcher(matcher);
        return myRealm2;
    }

    /**
     * 创建SecurityManager安全管理器
     * @Qualifier的注解: 指定我们要使用的那个Bean对象
     */
    @Bean
    public DefaultWebSecurityManager securityManager(@Qualifier("userRealm") MyRealm myRealm,@Qualifier("userRealm2") MyRealm2 myRealm2){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();


        //把自定义的Realm放入安全框架
//        defaultWebSecurityManager.setRealm(myRealm);//单个realm源
      //默认的认证策略是:AtLestOneSuccessfulStagegy,多个数据源里面只要有一个认证成功就可以

        //指定认证策略,指定之后会覆盖shiro内部默认的认证方式
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        authenticator.setAuthenticationStrategy(new AllSuccessfulStrategy());
//        authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        defaultWebSecurityManager.setAuthenticator(authenticator);


        Collection<Realm> c = new ArrayList<>();
        c.add(myRealm);
        c.add(myRealm2);
        defaultWebSecurityManager.setRealms(c); //多个realm
        //把记住我管理器设置到shiro中
        defaultWebSecurityManager.setRememberMeManager(rememberMeManager());
        return defaultWebSecurityManager;
    }
    /**
     * 把我们创建的安全管理器对象(DefaultWebSecurityManager)关联到shiro框架中
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager);


        //使用shiro的过滤链的方式,来控制访问的资源;LinkedHashMap是有顺序的双列集合
        Map<String,String> filterMap = new LinkedHashMap<>();
        /**
         * anon:  放行所有的请求
         * authc: 需要认证身份才能访问
         * user:  需要认证user或记住我,用来实现免登录的
         * perms: 拥有对某个资源的权限才能访问
         * roles: 授予权限
         * 放行(anon)的url一定要放在authc之前设置
         */
        //授予某资源访问权限
        filterMap.put("/sdesss/fdf","perms[审核]");//访问/sdesss/fdf时，需要审核权限
        //放行
        filterMap.put("/login","anon");
        filterMap.put("/js/**","anon");
        filterMap.put("/css/**","anon");
        filterMap.put("/images/**","anon");
        filterMap.put("/editor/**","anon");
        filterMap.put("/My97DatePicker/**","anon");
        filterMap.put("/loginShiro/**","anon");

        filterMap.put("/logout","logout");//logout会把shiro的SessionManager中的登录者的身份数据销毁
        filterMap.put("/**","user"); //拦截所有的其他的访问,当开启了rememberMe是,下次访问可以直接登录
        bean.setFilterChainDefinitionMap(filterMap);
        //没有登录身份时访问main页面,让其跳转到指定页面
        bean.setLoginUrl("/toLogin");  //--->requestMapping("/toLogin")

        //没有访问uri的权限时，跳转的页面--->requestMapping("/noauthor")
        bean.setUnauthorizedUrl("/noauthor");
        return bean;
    }
    /**
     * 记住我,免密登录的处理
     */
    public CookieRememberMeManager rememberMeManager(){
        /*
        设置cookie的参数
         */
        //设置cookie的name=rememberMe
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        //设置cookie的有效时间7天
        cookie.setMaxAge(3600*24*7);
        //把cookie放入管理器
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(cookie);
        /*
        给响应到浏览器的cookie数据加密
        cookie的加密使用的是AES算法家吗秘钥长度为(128,256,512位)
        提供一个AES公开的秘钥:3AvVhmFLUs0KTA3Kprsdag==
         */
        try {
            cookieRememberMeManager.setCipherKey(Base64Util.decode("3AvVhmFLUs0KTA3Kprsdag=="));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cookieRememberMeManager;
    }




}
