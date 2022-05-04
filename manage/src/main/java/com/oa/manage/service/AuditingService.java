package com.oa.manage.service;

import com.oa.pojo.Auditing;

import java.util.List;

public interface AuditingService {

    List<Auditing> findAuditing(String empid);

    List<Auditing> findAudits(int expid);
}
