package com.oa.mapper;

import com.oa.commons.IncomeData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeMapper {

    public List<IncomeData> getIncomeDatas();
}
