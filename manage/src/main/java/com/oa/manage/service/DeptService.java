package com.oa.manage.service;

import com.oa.pojo.Dept;

import java.util.List;

public interface DeptService {
    public int addDept(Dept dept);

    public List<Dept> showDept();

    public int updateDept(Dept dept);
    public int deleteDept(Dept dept);

    public Dept selByDeptNo(int deptno);
}
