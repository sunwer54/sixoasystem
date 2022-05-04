package com.oa.pojo;

public class DutyData extends Duty{
    private String realname;
    private String deptname;
    private String sDtdate;  //考勤日期的字符串格式

    public String getsDtdate() {
        return sDtdate;
    }

    public void setsDtdate(String sDtdate) {
        this.sDtdate = sDtdate;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }
}
