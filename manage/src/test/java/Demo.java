import org.apache.shiro.crypto.hash.Md5Hash;

public class Demo {
    public static void main(String[] args) {

        /*
        使用MD5加密
         */
        String pwd = "123456";
        Md5Hash md5Hash = new Md5Hash(pwd);
        System.out.println("加密之后的: "+md5Hash);

        //加入扰乱码(盐/数字签名/U盾)
        Md5Hash sailing = new Md5Hash(pwd, "sailing");
        System.out.println("加盐之后: "+sailing);

        //加入盐的散列加密计算,可以指定迭代次数
        Md5Hash sailing1 = new Md5Hash(pwd, "hello", 2);
        System.out.println("加盐之后: "+sailing1);


    }
}
