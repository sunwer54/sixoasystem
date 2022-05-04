package com.oa.manage.service.impl;

import com.oa.commons.IncomeData;
import com.oa.manage.service.InConmeService;
import com.oa.mapper.IncomeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InConmeServiceImpl implements InConmeService {
    @Autowired
    private IncomeMapper incomeMapper;
    @Override
    public List<IncomeData> getIncomeDatas() {
        return incomeMapper.getIncomeDatas();
    }
}
