package com.oa.manage.service;

import com.oa.pojo.Duty;
import com.oa.pojo.DutyData;

import java.util.List;

public interface DutyService {

    public Duty findDuty(java.sql.Date currentDate,String empid);

    int signIn(Duty du);

    int signOut(Duty du);

    int signOut2(Duty du);

    List<DutyData> showDutys();

    List<DutyData> selByEmpId(String empid);
}
