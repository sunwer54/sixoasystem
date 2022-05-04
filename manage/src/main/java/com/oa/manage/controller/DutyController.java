package com.oa.manage.controller;

import com.oa.commons.ExcelOperate;
import com.oa.manage.service.DutyService;
import com.oa.pojo.Duty;
import com.oa.pojo.DutyData;
import com.oa.pojo.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class DutyController {
    @Autowired
    private DutyService dutyService;

    @RequestMapping("/signIn")
    @ResponseBody
    public String signIn(HttpSession session){
        //每天签到一次，不可重复签到
        //查有没有签到过
        Employee employee = (Employee)session.getAttribute("emp");
        String empid = employee.getEmpid();
        //当天的日期
        Date date = new Date(new java.util.Date().getTime()); //值需要年月日
        Duty duty = dutyService.findDuty(date, empid);
        if(duty != null){
            //已经签到过了
            return "2";
        }else{
            //没有签到过,可以签到
            //获得签到的: 时分秒
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String signInTime= sdf.format(new java.util.Date());
            Duty du =new Duty();
            du.setDtdate(date);
            du.setEmprid(empid);
            du.setSignintime(signInTime);
            int n = dutyService.signIn(du);
            if(n==1){
                return "1";//签到成功
            }else{
                return "0";//签到失败
            }
        }
    }
    /**
     * 签退
     */
    @RequestMapping("/signOut")
    @ResponseBody
    public String signOut(HttpSession session){
        //判断是否签到过
        Employee employee = (Employee)session.getAttribute("emp");
        String empid = employee.getEmpid();
        //当天的日期
        Date date = new Date(new java.util.Date().getTime()); //值需要年月日
        Duty duty = dutyService.findDuty(date, empid);
        if(duty == null){
            //没有签到过,直接签退(instert)
            //没有签到过,可以签到
            //获得签到的: 时分秒
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String signInTime= sdf.format(new java.util.Date());
            Duty du =new Duty();
            du.setDtdate(date);
            du.setEmprid(empid);
            du.setSignouttime(signInTime);
            int n = dutyService.signOut(du);
            if(n==1){
                return "1";//签退成功
            }else{
                return "0";//签退失败
            }

        }else{
            //已经签到过,使用update签退
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String signInTime= sdf.format(new java.util.Date());
            Duty du =new Duty();
            du.setDtdate(date);
            du.setEmprid(empid);
            du.setSignouttime(signInTime);
            int n = dutyService.signOut2(du);
            if(n==1){
                return "1";//签退成功
            }else{
                return "0";//签退失败
            }
        }
    }

    /**
     * 显示考勤数据
     */
    @RequestMapping("/showDutys")
    public String showDutys(Model m){
        List<DutyData> dutys = dutyService.showDutys();
        m.addAttribute("dutys",dutys);
        return "dutyList";
    }
    /**
     * 导出数据为excel
     */
    @RequestMapping("/outDutyExcel")
    @ResponseBody
    public String outDutyExcel(){
        List<DutyData> dutys = dutyService.showDutys();
        //通过工具类导出数据为excel表到桌面
        try {
            ExcelOperate.createExcel(dutys);
            return "1";
        } catch (IOException e) {
            e.printStackTrace();
            return "0";
        }
    }
    /**
     * 显示我的考勤
     */
    @RequestMapping("/showMyDuty")
    public String showMyDuty(HttpSession session,Model m){
        Employee emp = (Employee)session.getAttribute("emp");
        List<DutyData> dutys =dutyService.selByEmpId(emp.getEmpid());
        m.addAttribute("dutys",dutys);
        return "myDuty";
    }
}
