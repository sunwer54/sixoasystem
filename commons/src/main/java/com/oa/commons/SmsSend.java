package com.oa.commons;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.Random;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 发送短信的工具类
 */
public class SmsSend {
    public static String sendMsg(String phone) throws Exception{
        //取得4位随机码
        String code = getCode();
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod("http://gbk.api.smschinese.cn");
        post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=gbk");//在头文件中设置转码
        NameValuePair[] data ={ new NameValuePair("Uid", "p17612594059"),new NameValuePair("Key", "d41d8cd98f00b204e980"),new NameValuePair("smsMob",phone),new NameValuePair("smsText","验证码："+code)};
        post.setRequestBody(data);

        client.executeMethod(post);
        Header[] headers = post.getResponseHeaders();
        int statusCode = post.getStatusCode();
        System.out.println("状态码:"+statusCode); //  大于0,短信发送的数量
        for(Header h : headers)
        {
            System.out.println(h.toString());
        }
        String result = new String(post.getResponseBodyAsString().getBytes("gbk"));
        System.out.println(result); //打印返回消息状态
        post.releaseConnection();
        if(statusCode > 0) {
            System.out.println("短信发送成功");
        }
        return code;
    }

    /**
     * 生成4位随机数
     */
    public static String getCode(){
        String num="";
        while(num.length() <4){
            Random r = new Random();
            int i = r.nextInt(10);
            num += i;
        }
        return num;
    }
}
