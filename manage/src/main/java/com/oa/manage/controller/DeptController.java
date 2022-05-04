package com.oa.manage.controller;

import com.google.gson.Gson;
import com.oa.manage.service.DeptService;
import com.oa.pojo.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
public class DeptController {
    @Autowired
    private DeptService deptService;
    /**
     * 添加部门信息
     * @return
     */
    @RequestMapping(value = "/addDept",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String addDept(Dept dept){
        int n= deptService.addDept(dept);
        if(n==1){
            return "添加成功";
        }else{
            return "添加失败";
        }
    }
    /**
     * 显示所有的部门
     */
    @RequestMapping("/showDept")
    public String showDept(Model m){
        List<Dept> depts = deptService.showDept();
        m.addAttribute("depts",depts);
        return "deptList";
    }
    /**
     *
     */
    @RequestMapping("/getDepts")
    @ResponseBody
    public Object getDepts(){
        List<Dept> depts = deptService.showDept();
        return depts;   //搭配@ResponseBody使用springmvc自动帮我们把对象转成json数据
    }

    @RequestMapping("/updateDept")
    @ResponseBody
    public String updateDept(String jsonDept){
        System.out.println("jsonDept: "+jsonDept);//{"deptno":103,"deptname":"总裁办","location":"杭州"}
        //把json结构的dept数转成Dept对象
        Gson gson = new Gson();
        Dept dept = gson.fromJson(jsonDept, Dept.class);
        //往数据库中修改
        int n = deptService.updateDept(dept);
        if(n==1) {
            return "1";
        }
        return "0";
    }
    /**
     * 删除部门
     */
    @RequestMapping("/deleteDept")
    @ResponseBody
    public String deleteDept(String jsonDept){
        //把json结构的dept数转成Dept对象
        Gson gson = new Gson();
        Dept dept = gson.fromJson(jsonDept, Dept.class);
        int n = deptService.deleteDept(dept);
        if(n==1){
            return "1";
        }
        return "0";
    }
}
