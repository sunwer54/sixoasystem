package com.oa.manage.service.impl;

import com.oa.commons.DateFormatUtil;
import com.oa.manage.service.DeptService;
import com.oa.manage.service.DutyService;
import com.oa.manage.service.EmpService;
import com.oa.mapper.DutyMapper;
import com.oa.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class DutyServiceImpl implements DutyService {
    @Autowired
    private DutyMapper dutyMapper;
    @Override
    public Duty findDuty(Date currentDate, String empid) {
        DutyExample exp = new DutyExample();
        exp.createCriteria().andEmpridEqualTo(empid).andDtdateEqualTo(currentDate);
        List<Duty> duties = dutyMapper.selectByExample(exp);
        if(duties != null && duties.size()==1){
            return duties.get(0);
        }
        return null;
    }

    @Override
    public int signIn(Duty du) {
        return dutyMapper.insertSelective(du);
    }

    @Override
    public int signOut(Duty du) {
        return dutyMapper.insertSelective(du);
    }

    @Override
    public int signOut2(Duty du) {
        DutyExample exp = new DutyExample();
        exp.createCriteria().andEmpridEqualTo(du.getEmprid()).andDtdateEqualTo(du.getDtdate());
        du.setEmprid(null);
        du.setDtdate(null);
        return  dutyMapper.updateByExampleSelective(du,exp);
    }
    @Autowired
    private EmpService empService;
    @Autowired
    private DeptService deptService;
    @Override
    public List<DutyData> showDutys() {
        DutyExample exp= new DutyExample();
        exp.setOrderByClause("dtdate desc");//按时间排序
        List<Duty> dutys = dutyMapper.selectByExample(exp);
        System.out.println(dutys);
        List<DutyData> list = new ArrayList<>();
        for(Duty duty:dutys){
            Employee employee = empService.selEmpBy(duty.getEmprid());
            DutyData dutyData =new DutyData();
            dutyData.setDtdate(duty.getDtdate());
            dutyData.setEmprid(duty.getEmprid());
            dutyData.setSignintime(duty.getSignintime());
            dutyData.setSignouttime(duty.getSignouttime());
            dutyData.setRealname(employee.getRealname());
            Dept dept = deptService.selByDeptNo(employee.getDeptno());
            dutyData.setDeptname(dept.getDeptname());
            dutyData.setsDtdate(DateFormatUtil.changeDateFormate(duty.getDtdate()));
            list.add(dutyData);
        }
        return list;

    }

    @Override
    public List<DutyData> selByEmpId(String empid) {
        DutyExample exp = new DutyExample();
        exp.createCriteria().andEmpridEqualTo(empid);
        List<Duty> duties = dutyMapper.selectByExample(exp);
        List<DutyData> list = new ArrayList<>();
        for(Duty duty: duties){
            DutyData dutyData = new DutyData();
            dutyData.setsDtdate(DateFormatUtil.changeDateFormate(duty.getDtdate()));
            dutyData.setSignintime(duty.getSignintime());
            dutyData.setSignouttime(duty.getSignouttime());
            list.add(dutyData);
        }
        return list;
    }
}
