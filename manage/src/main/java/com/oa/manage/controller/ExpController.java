package com.oa.manage.controller;

import com.oa.commons.IDUtils;
import com.oa.manage.service.AuditingService;
import com.oa.manage.service.EmpService;
import com.oa.manage.service.ExpService;
import com.oa.mapper.AuditingMapper;
import com.oa.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class ExpController {
    @Autowired
    private ExpService expService;
    @RequestMapping("/showExpense")
    public String showExpense(Model m){
        List<Employee> allMgr = expService.getAllMgr();
        m.addAttribute("mgrs",allMgr);
        return "expenseAdd";
    }

    /**
     * 添加报销
     * @param type  报销单的明细类型
     * @param amount 报销单的明细消费额
     * @param itemdesc 报销单的明细中的描述
     * @param nextauditor 下一个审核人
     * @param expdesc  报销单的总描述
     * @return
     */
    @RequestMapping(value = "/exp/expAdd",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String expAdd(String[]type, Double[] amount, String[] itemdesc, String nextauditor, String expdesc, HttpSession session, MultipartFile[] photo){
        Employee employee = (Employee)session.getAttribute("emp");
        String empid = employee.getEmpid();
        //手动生成id
        int expid = (int)IDUtils.genItemId();
        //装报销单的明细
        List<Expenseitem> expenseitems = new ArrayList<>();
        double totalamount = 0.0;  //报销单中每条明细的总和
        for(int i=0;i<amount.length;i++){
            totalamount += amount[i];
            Expenseitem item = new Expenseitem();
            item.setAmount(amount[i]);
            item.setExpid(expid);
            item.setItemdesc(itemdesc[i]);
            item.setType(type[i]);
            expenseitems.add(item);
        }
        //创建一个Expnese对象来封装报销单的数据
        Expense expense = new Expense();
        expense.setEmpid(empid);
        expense.setExpdesc(expdesc);
        expense.setExpid(expid);
        expense.setExptime(new Date());
        expense.setLastresult("新提交");//状态:新提交,审核中,驳回,审核通过
        expense.setStatus("1");//1待审核,2通过,3驳回,4已打款
        expense.setNextauditor(nextauditor);
        expense.setTotalamount(totalamount);

        List<Expimage> expimages = new ArrayList<>();
        //报销凭证
        for (int i = 0; i <photo.length ; i++) {
            //取得每一个文件
            MultipartFile multipartFile = photo[i];
            //获取上传的文件名称
            String originalFilename = multipartFile.getOriginalFilename();
            //取上传的文件的后缀前的.的索引
            int index = originalFilename.lastIndexOf("."); //abc.jpg
            //文件类型(文件的后缀)
            String fileType = originalFilename.substring(index);// 取到jpg
            //在服务器中创建一个目录用来保存上传之后的文件
            ServletContext servletContext = session.getServletContext();
            String rootPath = servletContext.getRealPath("/");
            //上传的目录存在项目根目录下的uploadfiles文件夹中
            File file = new File(rootPath, "/uploadfiles");
            if(!file.exists()){
                file.mkdir();//创建目录
            }
            //重命名上传的文件名(避免同名文件被覆盖)
            String newFileName = IDUtils.genImageName()+originalFilename;
            File uploadFile = new File(file, newFileName);
            try {
                multipartFile.transferTo(uploadFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Expimage image = new Expimage();
            image.setExpid(expid);
            image.setFilename("uploadfiles/"+newFileName); //文件的所存储的路径(相对路径)
            image.setRealname(originalFilename);//文件的真名
            image.setFiletype(fileType);
            expimages.add(image);
        }
        int n = expService.expenseAdd(expense,expenseitems,expimages);
        if(n==1){
            return "添加成功";
        }
        return "添加失败";
    }
    /**
     * 显示待审核的报销单
     */
    @RequestMapping("/showAudit")
    public String showAudit(HttpSession session,Model m){
        Employee employee = (Employee)session.getAttribute("emp");
        String empid = employee.getEmpid();
        List<Expense> expenses = expService.findExp(empid);
        m.addAttribute("expenses",expenses);
        return "toAudit";

    }
    /**
     * 查看明细
     */
    @RequestMapping("/showDetail/{expid}")
    public String showDetail(@PathVariable int expid,Model m){
        List<Expenseitem> expenseitems = expService.findItems(expid);
        m.addAttribute("expenseitems",expenseitems);
        return "expenseDetail";
    }


    /**
     * 查看报销凭证
     */
    @RequestMapping("/showImg/{expid}")
    public String showImg(@PathVariable int expid,Model m){
        List<Expimage> expimages = expService.findImages(expid);
        m.addAttribute("images",expimages);
        return "expenseImg";
    }

    /**
     * 审核
     * @param expid 要审核的报销单的id
     * @param result 审核的结果码 1 通过,2,决绝,3驳回
     * @param auditdesc 审核的意见
     * @return
     */
    @RequestMapping("/exp/auditing")
    @ResponseBody
    public String auditing(int expid,String result,String auditdesc,HttpSession session){
        Employee employee = (Employee)session.getAttribute("emp");
        //审核人的id
        String empid = employee.getEmpid();
        Auditing auditing = new Auditing();
        auditing.setEmpid(empid);
        auditing.setAuditdesc(auditdesc);
        auditing.setExpid(expid);
        auditing.setResult(result);
        auditing.setTime(new Date());
        try {
           expService.auditing(auditing);
            return "success";
        }catch (Exception e){
            e.printStackTrace();
            return "fail";//失败
        }

    }
    /**
     * 查看审核记录
     */
    @RequestMapping("/showAuditHistory/{expId}")
    public String showAuditHistory(@PathVariable int expId,Model m){
        Expense expense = expService.findExpense(expId);
        m.addAttribute("expense",expense);
        return "auditHistory";
    }
    @Autowired
    private AuditingService auditingService;

    /**
     * 审核历史
     */
    @RequestMapping("/showMyAudit")
    public String showMyAudit(HttpSession session,Model m){
        Employee employee = (Employee)session.getAttribute("emp");
        //审核人的id
        String empid = employee.getEmpid();
        List<Auditing> auditings = auditingService.findAuditing(empid);
        m.addAttribute("auditings",auditings);
        return "myAudit";
    }
    /**
     * 查审核的历史
     * getAuditHistory/-1985190205
     */
    @RequestMapping("/getAuditHistory/{expid}")
    public String getAuditHistory(@PathVariable int expid,Model m){
        //根据expid查询auditing表
        List<Auditing> list = auditingService.findAudits(expid);
        m.addAttribute("list",list);
        return "myAuditHistory";
    }
}
