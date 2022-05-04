package com.oa.manage.service.impl;

import com.oa.manage.service.AuditingService;
import com.oa.manage.service.ExpService;
import com.oa.mapper.AuditingMapper;
import com.oa.mapper.EmployeeMapper;
import com.oa.mapper.ExpenseMapper;
import com.oa.pojo.Auditing;
import com.oa.pojo.AuditingExample;
import com.oa.pojo.Employee;
import com.oa.pojo.Expense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditingServiceImpl implements AuditingService {
    @Autowired
    private ExpenseMapper expenseMapper;
    @Autowired
    private AuditingMapper auditingMapper;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Override
    public List<Auditing> findAuditing(String empid) {
        AuditingExample exp = new AuditingExample();
        exp.createCriteria().andEmpidEqualTo(empid);
        List<Auditing> auditings = auditingMapper.selectByExample(exp);
        System.out.println("auditings "+auditings);
        for(Auditing auditing:auditings){
            //报销单的id
            Integer expid = auditing.getExpid();
            Expense expense = expenseMapper.selectByPrimaryKey(expid);
            auditing.setExpense(expense); //把审核的报销单信息放入审核对象中
            //报销人的id
            String empid1 = expense.getEmpid();
            Employee employee = employeeMapper.selectByPrimaryKey(empid1);
            auditing.setExpName(employee.getRealname());
        }
        return auditings;
    }

    @Override
    public List<Auditing> findAudits(int expid) {
        AuditingExample exp = new AuditingExample();
        //审核的时间排序
        exp.setOrderByClause("time");
        exp.createCriteria().andExpidEqualTo(expid);
        List<Auditing> auditings = auditingMapper.selectByExample(exp);
        for(Auditing auditing:auditings){
            String empid = auditing.getEmpid();
            Employee employee = employeeMapper.selectByPrimaryKey(empid);
            auditing.setRealname(employee.getRealname());
        }
        return auditings;
    }
}
