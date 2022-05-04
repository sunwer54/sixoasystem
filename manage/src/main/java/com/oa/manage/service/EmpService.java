package com.oa.manage.service;

import com.oa.commons.EmpResult;
import com.oa.pojo.Employee;
import com.oa.pojo.TUser;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface EmpService {
    public Employee loginUser(Employee employee);


    public List<Employee> getMgr();

    public int addEmployee(Employee employee);
    public int updateEmployee(Employee employee);
    public EmpResult showEmp(int pageNum);

    public EmpResult multyFind(String empid,int deptno,int ondury,String hiredate);

    public Employee selEmpBy(String empid);

    public int deleteEmployee(String empid);

    int resetPwd(String empid);

    Employee selByPhone(String phone);

    Employee findEmp(String empid);

    TUser findTUser(String uid);
}
