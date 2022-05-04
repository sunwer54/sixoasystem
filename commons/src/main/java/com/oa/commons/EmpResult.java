package com.oa.commons;

import com.oa.pojo.Dept;
import com.oa.pojo.Employee;

import java.io.Serializable;
import java.util.List;

/**
 设计一个类用来封装所有的雇员及页面相关的信息
 */
public class EmpResult implements Serializable {
    //查询分页后的数据
    private List<Employee> list;
    //员的总数
    private int total;
    //当前的页码
    private int currentPageNum;
    private List<Dept> depts;

    public List<Dept> getDepts() {
        return depts;
    }

    public void setDepts(List<Dept> depts) {
        this.depts = depts;
    }

    public List<Employee> getList() {
        return list;
    }

    public void setList(List<Employee> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCurrentPageNum() {
        return currentPageNum;
    }

    public void setCurrentPageNum(int currentPageNum) {
        this.currentPageNum = currentPageNum;
    }
}
