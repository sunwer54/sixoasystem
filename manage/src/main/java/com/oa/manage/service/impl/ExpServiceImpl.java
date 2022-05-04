package com.oa.manage.service.impl;

import com.oa.commons.DateFormatUtil;
import com.oa.manage.service.ExpService;
import com.oa.mapper.*;
import com.oa.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExpServiceImpl implements ExpService {
    @Autowired
    private EmployeeMapper employeeMapper;
    @Override
    public List<Employee> getAllMgr() {
        EmployeeExample exp = new EmployeeExample();
        exp.createCriteria().andEmptypeEqualTo(1);
        return employeeMapper.selectByExample(exp);
    }
    @Autowired
    private ExpenseMapper expenseMapper;
    @Autowired
    private ExpenseitemMapper expenseitemMapper;
    @Autowired
    private ExpimageMapper expimageMapper;


    @Override
    @Transactional(propagation = Propagation.REQUIRED)   //加入事务(默认就是REQUIRED)
    public int expenseAdd(Expense expense, List<Expenseitem> expenseitems,List<Expimage> expimages) {
        expenseMapper.insert(expense);

        for(Expenseitem expenseitem:expenseitems){
            expenseitemMapper.insert(expenseitem);
        }

        for(Expimage expimage:expimages){
            expimageMapper.insert(expimage);
        }
        return 1;
    }

    @Override
    public List<Expense> findExp(String empid) {
        ExpenseExample exp = new ExpenseExample();
        exp.createCriteria().andNextauditorEqualTo(empid);
        List<Expense> expenses = expenseMapper.selectByExample(exp);
        for(Expense expense :expenses){
            String empid1 = expense.getEmpid();
            //报销人信息
            Employee employee = employeeMapper.selectByPrimaryKey(empid1);
            expense.setEmployee(employee);
            expense.setSdate(DateFormatUtil.changeDateFormate(expense.getExptime()));
        }
        return expenses;
    }

    @Override
    public List<Expenseitem> findItems(int expid) {
        ExpenseitemExample exp = new ExpenseitemExample();
        exp.createCriteria().andExpidEqualTo(expid);
        return expenseitemMapper.selectByExample(exp);
    }

    @Override
    public List<Expimage> findImages(int expid) {
        ExpimageExample exp = new ExpimageExample();
        exp.createCriteria().andExpidEqualTo(expid);
        return expimageMapper.selectByExample(exp);
    }

    @Autowired
    private AuditingMapper auditingMapper;

    /**
     * @param auditing
     * @return
     */
    @Override
    public int auditing(Auditing auditing) {
        //根据expid查询报销单
        Integer expid = auditing.getExpid();
        //待审核的报销单信息
        Expense expense = expenseMapper.selectByPrimaryKey(expid);
        /*
         * 审核逻辑:
         * 员工-->主管---->报销金额 > 2500--->总裁审核--->财务--->打款
         * 员工-->主管---->报销金额 <= 2500--->财务--->打款
         */
        if("1".equals(auditing.getResult())){  //通过审核
            //进入审核
            if("xiaoqiao".equals(auditing.getEmpid())){ //是财务
                //添加财务支出

                //添加审核记录(审核信息写入auditing表中)
                auditingMapper.insert(auditing);
                //修改报销单的状态(已经打款),下一个审核人: 空
                expense.setLastresult("已经打款");
                expense.setStatus("4"); //1带审核,2,通过,3 驳回,4 已打款
                expense.setNextauditor(" "); //已经报销完毕,没有审核人了
                expenseMapper.updateByPrimaryKey(expense);
                System.out.println("财务已经打款处理完毕");
            }else{ //不是财务
                //判断金额
                if(expense.getTotalamount() <= 2500){
                    //当前的审核人是直接主管,有权直接审核进入财务打款
                    //添加审核记录
                    auditingMapper.insert(auditing);
                    //修改报销单状态,下一个审核人是: 财务
                    expense.setLastresult("审核通过");
                    expense.setStatus("2"); //1带审核,2,通过,3 驳回,4 已打款
                    expense.setNextauditor("xiaoqiao"); // 下一个审核人是交给财务打款处理
                    expenseMapper.updateByPrimaryKey(expense);
                    System.out.println("主管审核完毕");

                }else{  //  报销额 > 2500的情况,必须由总裁审核
                    //判断当前审核人是否是总裁
                    if("admin".equals(auditing.getEmpid())){
                        //是总裁
                        //添加审核记录
                        auditingMapper.insert(auditing);
                        //修改报销单状态,下一个审核人是: 财务
                        expense.setLastresult("通过审核");
                        expense.setStatus("2"); //1带审核,2,通过,3 驳回,4 已打款
                        expense.setNextauditor("xiaoqiao"); // 下一个审核人是交给财务打款处理
                        expenseMapper.updateByPrimaryKey(expense);
                        System.out.println("总裁审核完毕");

                    }else{
                        //不是总裁,当前是普通的主管,需要再提交给总裁审核的
                        //添加审核记录
                        auditingMapper.insert(auditing);
                        //修改报销单状态,下一个审核人是: 总裁
                        expense.setLastresult("通过审核");
                        expense.setStatus("2"); //1待审核,2,通过,3 驳回,4 已打款
                        expense.setNextauditor("admin"); // 下一个审核人是交给总裁审核
                        expenseMapper.updateByPrimaryKey(expense);
                        System.out.println("主管审核完毕");
                    }
                }
            }

        }else{  //决绝或驳回的情况
            //添加审核记录
            auditingMapper.insert(auditing);
            //修改报销单的状态及下一个审核人
            expense.setLastresult("驳回");
            expense.setStatus("3"); //1带审核,2,通过,3 驳回,4 已打款
            expenseMapper.updateByPrimaryKey(expense);
            System.out.println("拒绝");
        }
        return 1;
    }

    @Override
    public Expense findExpense(int expId) {
        Expense expense = expenseMapper.selectByPrimaryKey(expId);
        Employee employee = employeeMapper.selectByPrimaryKey(expense.getNextauditor());
        expense.setNextauditorName(employee.getRealname());
        return expense;
    }
}
