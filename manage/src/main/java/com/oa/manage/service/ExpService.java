package com.oa.manage.service;

import com.oa.pojo.*;

import java.util.List;

public interface ExpService {
    public List<Employee> getAllMgr();

    int expenseAdd(Expense expense, List<Expenseitem> expenseitems, List<Expimage> expimages);

    List<Expense> findExp(String empid);

    List<Expenseitem> findItems(int expid);

    List<Expimage> findImages(int expid);

    int auditing(Auditing auditing);

    Expense findExpense(int expId);

}
