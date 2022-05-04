package com.oa.manage.controller;

import com.oa.commons.EmpResult;
import com.oa.commons.SmsSend;
import com.oa.manage.service.DeptService;
import com.oa.manage.service.EmpService;
import com.oa.manage.service.PositionService;
import com.oa.pojo.Dept;
import com.oa.pojo.Employee;
import com.oa.pojo.Position;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class EmpController {
    @RequestMapping("/toLogin")
    public String toLogin(Model m){
        m.addAttribute("msg","你先登录!");
        return "login";
    }
    @Autowired
    private EmpService empService;
    /**
     * 使用shiro实现登录
     */
    @RequestMapping("/loginShiro")
    public String loginShiro(Employee employee,Model m,boolean rememberMe){
        //使用shiro的方式登录: 把用户登录的用户名和密码封装成一个 令牌(token)对象
        UsernamePasswordToken token = new UsernamePasswordToken(employee.getEmpid(), employee.getPassword());
        //从SessionManager中获用户的信息,用户信息封装在Subject中
        Subject subject = SecurityUtils.getSubject();
        //设置免密登录
        token.setRememberMe(rememberMe);
        //进行用户名和密码的校验
        try {
            subject.login(token);
        }catch (IncorrectCredentialsException e){
            System.out.println("凭证不正确");
            m.addAttribute("msg","凭证不正确");
            return "login";

        }catch (UnknownAccountException e){
            System.out.println("用户名不存在");
            m.addAttribute("msg","用户名不存在");
            return "login";
        }
        return "redirect:/main";
    }

    /**
     * 用户登录
     * @param employee
     * @param session
     * @return
     */
    @RequestMapping("/loginUser")
    public String loginUser(Employee employee, HttpSession session, Model m){
        Employee emp = empService.loginUser(employee);
        if(emp != null){
            //把登录的身份信息放入session
            session.setAttribute("emp",emp);
            //跳转到main页面
            return "redirect:/main"; //重定向
        }else{
            //携带数据到页面
            m.addAttribute("msg","用户名或密码有误!");
            return "login";
        }
    }
    /**
     * 退出系统
     */
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        //强势session销毁
        session.invalidate();
        return "login";
    }
    @Autowired
    private DeptService deptService;
    @Autowired
    private PositionService positionService;
    /**
     * 添加员工前的数据准备
     */
    @RequestMapping("/getDatas")
    public String getDatas(Model m){
        //查询所有的部门信息
        List<Dept> depts =deptService.showDept();
        //查询所有的职位信息
        List<Position> positions = positionService.getPosition();
        //查询所有的上级信息
        List<Employee> mgrs = empService.getMgr();
        System.out.println("mgrs: "+mgrs);
        m.addAttribute("depts",depts);
        m.addAttribute("positions",positions);
        m.addAttribute("mgrs",mgrs);
        return "/empAdd";
    }
    /**
     * 添加员工
     */
    @RequestMapping(value = "/addEmployee",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String addEmployee(String empid, @RequestParam(defaultValue = "123456") String password, String realname, String sex, Date birthdate,Date hiredate,String leavedate,int onduty,int emptype,int deptno,int posid,String mgrid,String phone,String qq,String emercontactperson,String idcard){
        Employee employee = new Employee();
        employee.setEmpid(empid);
        employee.setRealname(realname);
        employee.setSex(sex);
        employee.setBirthdate(birthdate);
        employee.setHiredate(hiredate);
        if(leavedate != null && !leavedate.equals("")){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                java.util.Date parse = sdf.parse(leavedate);
                employee.setLeavedate(parse);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        employee.setOnduty(onduty);
        employee.setEmptype(emptype);
        employee.setDeptno(deptno);
        employee.setPosid(posid);
        employee.setMgrid(mgrid);
        employee.setPhone(phone);
        employee.setQq(qq);
        employee.setIdcard(idcard);
        employee.setPassword(password);
        employee.setEmercontactperson(emercontactperson);
        //写入数据库中
        int n = empService.addEmployee(employee);
        if(n==1){
            return "添加成功";
        }
        return "添加失败";
    }

    /**
     * 显示所有的员工
     * @param pageNum 分页的页码
     */
    @RequestMapping("/showEmp/{pageNum}")
    public String showEmp( @PathVariable int pageNum,Model m){
        EmpResult empResult = empService.showEmp(pageNum);
        m.addAttribute("employees",empResult.getList());
        m.addAttribute("num",empResult.getCurrentPageNum());
        m.addAttribute("total",empResult.getTotal());
        m.addAttribute("depts",empResult.getDepts());
        return "empList";
    }
    /**
     * 多条件查询
     */
    @RequestMapping("/multyFind")
    public String multyFind(String empid,int deptno,int onduty,String hiredate,Model m){
        EmpResult empResult = empService.multyFind(empid, deptno, onduty, hiredate);

        m.addAttribute("employees",empResult.getList());
        m.addAttribute("depts",empResult.getDepts());
        return "empList";
    }
    /**
     * 查看雇员详情
     */
    @RequestMapping("/getEmpInfo/{empid}")
    public String getEmpInfo(@PathVariable String empid,Model m){

        //根据empid查
        Employee employee = empService.selEmpBy(empid);
        System.out.println(employee);
        m.addAttribute("emp",employee);
        return "empInfo";
    }
    /**
     * 修改前的数据处理
     */
    @RequestMapping("/updateEmpBefore/{empid}")
    public String updateEmpBefore(@PathVariable String empid,Model m){
        //根据empid查
        Employee employee = empService.selEmpBy(empid);
        System.out.println(employee);
        m.addAttribute("emp",employee);
        //查询所有的部门信息
        List<Dept> depts =deptService.showDept();

        //查询所有的上级信息
        List<Employee> mgrs = empService.getMgr();
        m.addAttribute("depts",depts);
        m.addAttribute("mgrs",mgrs);
        return "empUpdate";
    }

    /**
     *修改员工
     */
    @RequestMapping(value = "/updateEmployee",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String updateEmployee(String empid, String realname, String sex, Date birthdate,Date hiredate,String leavedate,int onduty,int deptno,String mgrid,String phone,String qq,String emercontactperson,String idcard){
        Employee employee = new Employee();
        employee.setEmpid(empid);
        employee.setRealname(realname);
        employee.setSex(sex);
        employee.setBirthdate(birthdate);
        employee.setHiredate(hiredate);
        if(leavedate != null && !leavedate.equals("")){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                java.util.Date parse = sdf.parse(leavedate);
                employee.setLeavedate(parse);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        employee.setOnduty(onduty);
        employee.setDeptno(deptno);
        employee.setMgrid(mgrid);
        employee.setPhone(phone);
        employee.setQq(qq);
        employee.setIdcard(idcard);
        employee.setEmercontactperson(emercontactperson);
        //更新到数据库中
        int n = empService.updateEmployee(employee);
        if(n==1){
            return "修改成功";
        }
        return "修改失败";
    }
    /**
     * 删除员工
     */
    @RequestMapping("/deleteEmployee")
    @ResponseBody
    public String deleteEmployee(String empid){
        //根据empid删除
        int n =empService.deleteEmployee(empid);
        if(n==1){
            return "1";
        }
        return "0";
    }
    /**
     * 重置密码
     */
    @RequestMapping(value = "/resetPwd/{empid}",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String resetPwd(@PathVariable String empid){
        int n = empService.resetPwd(empid);
        if(n==1){
            return "重置成功";
         }
        return "重置失败";
    }

    /**
     * 手机发短信登录
     */
    @RequestMapping("/smsSend")
    @ResponseBody
    public String smsSend(String phone){
        //根据phone查询是否有这个用户;如果是一键登录(把电话号存入数据库即可,相当于新建一个用户,里面只有phone列有数据)
        Employee emp = empService.selByPhone(phone);
        if(emp != null){
            //发送短信验证码
            try {
                //后台会拿到发送到手里中一样的验证码,用来校验
                backCode = SmsSend.sendMsg(emp.getPhone());
                System.out.println("后台收到的短信码: "+backCode);
                return "1";
            } catch (Exception e) {
                e.printStackTrace();
                return "0";//发送短信失败的情况
            }
        }else{
            //手机号没有对应的用户
            return "2";
        }

    }
    String backCode;

    /**
     * 处理用户收到手机短信验证码,登录
     * @param code
     * @param phone
     * @return
     */
    @RequestMapping("/smsLogin")
    public String smsLogin(String code,String phone,HttpSession seesion){
        //校验后台的验证码与用户手机收到的验证码是否一致
        if(code.equals(backCode)){
            //根据phone查询是否有这个用户
            Employee employee = empService.selByPhone(phone);
            seesion.setAttribute("emp",employee);
            return "redirect:/main";
        }else{
            return "/smsLogin";
        }
    }
}
