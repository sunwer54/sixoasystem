package com.oa.manage.service.impl;

import com.oa.manage.service.DeptService;
import com.oa.mapper.DeptMapper;
import com.oa.pojo.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptMapper deptMapper;
    @Override
    public int addDept(Dept dept) {
        int n = deptMapper.insert(dept);
        return n;
    }

    @Override
    public List<Dept> showDept() {
        List<Dept> depts = deptMapper.selectByExample(null);

        return depts;
    }

    @Override
    public int updateDept(Dept dept) {
        return deptMapper.updateByPrimaryKey(dept);
    }

    @Override
    public int deleteDept(Dept dept) {
        return deptMapper.deleteByPrimaryKey(dept.getDeptno());
    }

    @Override
    public Dept selByDeptNo(int deptno) {
        return deptMapper.selectByPrimaryKey(deptno);
    }
}
