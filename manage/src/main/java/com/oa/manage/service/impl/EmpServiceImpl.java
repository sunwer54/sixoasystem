package com.oa.manage.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oa.commons.DateFormatUtil;
import com.oa.commons.EmpResult;
import com.oa.manage.service.DeptService;
import com.oa.manage.service.EmpService;
import com.oa.manage.service.PositionService;
import com.oa.mapper.EmployeeMapper;
import com.oa.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class EmpServiceImpl implements EmpService {
    @Autowired
    EmployeeMapper employeeMapper;
    @Override
    public Employee loginUser(Employee employee) {
        EmployeeExample exp = new EmployeeExample();
        exp.createCriteria().andEmpidEqualTo(employee.getEmpid()).andPasswordEqualTo(employee.getPassword());
        List<Employee> employees = employeeMapper.selectByExample(exp);
        if(employees != null && employees.size()==1){
            return employees.get(0);
        }
        return null;
    }

    @Override
    public List<Employee> getMgr() {
        EmployeeExample exp= new EmployeeExample();
        exp.createCriteria().andEmptypeEqualTo(1);
        List<Employee> employees = employeeMapper.selectByExample(exp);
        return employees;
    }

    @Override
    public int addEmployee(Employee employee) {
        return employeeMapper.insert(employee);
    }

    @Override
    public int updateEmployee(Employee employee) {
        return employeeMapper.updateByPrimaryKeySelective(employee);
    }

    @Autowired
    private DeptService deptService;
    @Autowired
    private PositionService positionService;

    @Override
    public EmpResult showEmp(int pageNum) {
        //分页处理
        PageHelper.startPage(pageNum,3);

        EmployeeExample exp = new EmployeeExample();
        //根据入职日期排序
        exp.setOrderByClause("hiredate desc");

        List<Employee> employees = employeeMapper.selectByExample(exp);
        for(Employee emp:employees){
            //查所属的部门
            Integer deptno = emp.getDeptno();
            Dept dept = deptService.selByDeptNo(deptno);
            emp.setDept(dept);

            //查所属的职位
            Integer posid = emp.getPosid();
            Position position = positionService.selPosByPosId(posid);
            emp.setPosition(position);
            //改日期的显示格式
            Date hiredate = emp.getHiredate();
            emp.setHdate(DateFormatUtil.changeDateFormate(hiredate));
        }
        PageInfo<Employee> info = new PageInfo<>(employees);
        //分页之后的数据
        List<Employee> list = info.getList();
        //总数据条数
        int total = (int)info.getTotal();
        //当前页码
        int currentPageNum = info.getPageNum();
        //查询所有的部门
        List<Dept> depts = deptService.showDept();

        EmpResult er = new EmpResult();
        er.setList(list);
        er.setTotal(total);
        er.setCurrentPageNum(currentPageNum);
        er.setDepts(depts);

        return er;
    }

    /**
     * 多条件查询
     * @param empid
     * @param deptno
     * @param ondury
     * @param hiredate
     * @return
     */
    @Override
    public EmpResult multyFind(String empid, int deptno, int ondury, String hiredate) {
        EmployeeExample exp = new EmployeeExample();
        EmployeeExample.Criteria criteria = exp.createCriteria();
        //接入多条件:四个条件String empid, int deptno, int ondury, String hiredate
        if(empid != null && !"".equals(empid)){
            criteria.andEmpidLike("%"+empid+"%"); //模糊查询
        }
        if(deptno != -1){
            criteria.andDeptnoEqualTo(deptno);
        }
        criteria.andOndutyEqualTo(ondury);
        if(hiredate != null & !"".equals(hiredate)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date parse = sdf.parse(hiredate);
                java.sql.Date date = new java.sql.Date(parse.getTime());//把java.util.Date转成与数据库中对应的类型
                criteria.andHiredateGreaterThanOrEqualTo(date); // >= 输入的入职日期
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        List<Employee> employees = employeeMapper.selectByExample(exp);
        for(Employee emp:employees){
            //查所属的部门
            Integer deptno1 = emp.getDeptno();
            Dept dept = deptService.selByDeptNo(deptno1);
            emp.setDept(dept);

            //查所属的职位
            Integer posid = emp.getPosid();
            Position position = positionService.selPosByPosId(posid);
            emp.setPosition(position);
            //改日期的显示格式
            Date hiredate1 = emp.getHiredate();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String hdate = simpleDateFormat.format(hiredate1);
            emp.setHdate(hdate);
        }
        EmpResult er = new EmpResult();
        er.setList(employees);

        //查询所有的部门
        List<Dept> depts = deptService.showDept();
        er.setDepts(depts);
        return er;
    }

    @Override
    public Employee selEmpBy(String empid) {
        Employee emp = employeeMapper.selectByPrimaryKey(empid);
        //查所属的部门
        Integer deptno = emp.getDeptno();
        Dept dept = deptService.selByDeptNo(deptno);
        emp.setDept(dept);

        //查所属的上级
        String mgrid = emp.getMgrid(); //上级的empid
        Employee mgr = employeeMapper.selectByPrimaryKey(mgrid);
        emp.setMgr(mgr);

        //改日期的显示格式
        Date hiredate = emp.getHiredate();

        emp.setHdate(DateFormatUtil.changeDateFormate(hiredate));
        emp.setBdate(DateFormatUtil.changeDateFormate(emp.getBirthdate()));
        emp.setLdate(DateFormatUtil.changeDateFormate(emp.getLeavedate()));
        return emp;
    }

    @Override
    public int deleteEmployee(String empid) {
        return employeeMapper.deleteByPrimaryKey(empid);
    }

    @Override
    public int resetPwd(String empid) {
        Employee emp=new Employee();
        emp.setEmpid(empid);
        emp.setPassword("123456");
        return employeeMapper.updateByPrimaryKeySelective(emp);
    }

    @Override
    public Employee selByPhone(String phone) {
        EmployeeExample exp = new EmployeeExample();
        exp.createCriteria().andPhoneEqualTo(phone);
        List<Employee> employees = employeeMapper.selectByExample(exp);
        if(employees != null && employees.size() ==1){
            return employees.get(0);
        }
        return null;
    }

    @Override
    public Employee findEmp(String empid) {
        return employeeMapper.selectByPrimaryKey(empid);
    }

    @Override
    public TUser findTUser(String uid) {
        return employeeMapper.findTUser(uid);
    }
}
