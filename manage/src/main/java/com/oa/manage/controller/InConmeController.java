package com.oa.manage.controller;

import com.oa.commons.IncomeData;
import com.oa.manage.service.InConmeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
public class InConmeController {
    @Autowired
    private InConmeService inConmeService;
    @RequestMapping("/getIncomeDatas")
    @ResponseBody
    public String getIncomeDatas(){
        //查询income分页求和的数据
        List<IncomeData> incomeDatas = inConmeService.getIncomeDatas();
        System.out.println("incomeDatas: "+incomeDatas);
        /*
        [IncomeData{indesc='人员外包', totalmount='132000'},
        IncomeData{indesc='项目开发', totalmount='117000'},
        IncomeData{indesc='技术咨询费', totalmount='30000'},
        IncomeData{indesc='房租收入', totalmount='55000'}]
        转化为:
        [['人员外包', '项目开发', '技术咨询费', '房租收入'],[132000, 117000, 30000, 55000]]
         */
        StringBuilder sb= new StringBuilder();
        sb.append("[['");
        for(int i=0;i<incomeDatas.size()-1;i++){
            IncomeData incomeData = incomeDatas.get(i);
            String indesc = incomeData.getIndesc();
            sb.append(indesc).append("','");
        }
        IncomeData lastIncomeData = incomeDatas.get(incomeDatas.size() - 1);
        sb.append(lastIncomeData.getIndesc()).append("'],[");

        for(int i=0;i<incomeDatas.size()-1;i++) {
            IncomeData incomeData = incomeDatas.get(i);
            String totalmount = incomeData.getTotalmount();
            sb.append(totalmount).append(",");
        }
        sb.append(incomeDatas.get(incomeDatas.size() - 1).getTotalmount()).append("]]");
        System.out.println(sb.toString());
        return sb.toString();
    }
}
