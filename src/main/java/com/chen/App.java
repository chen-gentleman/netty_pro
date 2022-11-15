package com.chen;


import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;


import java.io.File;
import java.io.IOException;
import java.net.*;
import java.security.KeyPair;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {
       final String privateKey = "G38Wr1pOWIE0fPUF11F7I7JY";
       genLicence(privateKey.getBytes(),"2021-04-22");

   // genKey(privateKey.getBytes());
    }

    public static String getMACAddress()  {
        try {
            InetAddress ia = InetAddress.getLocalHost(); //获取本地IP对象
            //获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
            byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
            if (null == mac) {
                return null;
            }
            //下面代码是把mac地址拼装成String
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    sb.append("-");
                }
                //mac[i] & 0xFF 是为了把byte转化为正整数
                String s = Integer.toHexString(mac[i] & 0xFF);
                sb.append(s.length() == 1 ? 0 + s : s);
            }
            //把字符串所有小写字母改为大写成为正规的mac地址并返回
            return sb.toString().trim().toUpperCase();
        } catch (Exception ex) {
            //Logger.getLogger(NetUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }


    //可以指定字符串的某个位置是什么范围的值
    public static String getRandomString2(int length){
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(3);
            long result=0;
            switch(number){
                case 0:
                    result=Math.round(Math.random()*25+65);
                    sb.append(String.valueOf((char)result));
                    break;
                case 1:
                    result=Math.round(Math.random()*25+97);
                    sb.append(String.valueOf((char)result));
                    break;
                case 2:
                    sb.append(String.valueOf(new Random().nextInt(10)));
                    break;
            }


        }
        return sb.toString();
    }


    public static int cheackLicence(byte[] privateKey){
        byte[] keyp = privateKey;
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, keyp);

        FileReader fileReaderLicence = new FileReader("E:\\licence");
        FileReader fileReaderKey = new FileReader("E:\\key");

        String licenceTemp = fileReaderLicence.readString();

        String decryptStrLicence = aes.decryptStr(licenceTemp, CharsetUtil.CHARSET_UTF_8);
        String licence = Base64.decodeStr(decryptStrLicence);

        String keyTemp = fileReaderKey.readString();
        String decryptStrKey = aes.decryptStr(keyTemp, CharsetUtil.CHARSET_UTF_8);
        String key = Base64.decodeStr(decryptStrKey);

        String[] licences = licence.split("=");
        String[] keys = key.split("=");
        String webUrl2 = "http://www.baidu.com";//百度
        Date currTime = null;
        if(null != getWebsiteDatetime(webUrl2)){
            currTime = stringToDate(getWebsiteDatetime(webUrl2));
        }else {
            currTime = new Date();
        }
        Date endTime = stringToDate(licences[1]);

        if(!licences[0].equalsIgnoreCase(keys[0])){
            return 1;//服务器不对
        }else if(!licences[2].equalsIgnoreCase(keys[2])){
            return 2;//授权类型不对
        }else if((endTime.getTime() - currTime.getTime()) < 0){
            return 3;//授权到期
        }else {
            return 0;//通过
        }
    }


    public static void genLicence(byte[] privateKey,String endData){
        if(null == privateKey){
            return;
        }
        byte[] keyp = privateKey;
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, keyp);
        FileWriter writer = new FileWriter("E:\\licence");
        FileReader fileReaderKey = new FileReader("E:\\key");
        String keyTemp = fileReaderKey.readString();
        String decryptStrKey = aes.decryptStr(keyTemp, CharsetUtil.CHARSET_UTF_8);
        String key = Base64.decodeStr(decryptStrKey);
        String[] keys = key.split("=");
        if(null == keys || keys.length != 3){
            return;
        }
        String macAddress = "";
        if(null != keys[0]){
            macAddress = keys[0];
        }
        System.out.println("macAddress = " + keys[0]);
        String encode = Base64.encode(macAddress + "=" + endData + "=" + "WOTIAN_CHEN_FILESYSTEM_001");
        String encryptHex = aes.encryptHex(encode);
        writer.write(encryptHex);
    }
    /**
     * 获取指定网站的日期时间
     *
     * @param webUrl
     * @return
     */
    private static String getWebsiteDatetime(String webUrl){
        try {
            URL url = new URL(webUrl);// 取得资源对象
            URLConnection uc = url.openConnection();// 生成连接对象
            uc.connect();// 发出连接
            long ld = uc.getDate();// 读取网站日期时间
            Date date = new Date(ld);// 转换为标准时间对象
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// 输出北京时间
            return dateToString(date);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date stringToDate(String time) {
        java.text.SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        String   s= "2011-07-09 ";
        Date date = null;
        try {
            date = formatter.parse(time);
        } catch (ParseException ex) {
          ex.printStackTrace();
        }
        return date;
    }

    public static String dateToString(Date date) {
        java.text.SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String time = formatter.format(date);//格式化数据
        return time;
    }
    /**
     * 将用户的电脑网卡，当时时间类型加密为字符串
     * @return
     */
    public static void genKey(byte[] privateKey){
        if(null == privateKey){
            return;
        }
        String macAddress = getMACAddress();
        if(null == macAddress){
            return;
        }
        File file = new File("E:\\key");
        if(file.exists()){
            return;
        }
        String dataStr = dateToString(new Date());
        String type = "WOTIAN_CHEN_FILESYSTEM_001";
        FileWriter writer = new FileWriter("E:\\key");
        String encode = Base64.encode(macAddress + "=" + dataStr + "=" + type);
        byte[] key = privateKey;
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
        String encryptHex = aes.encryptHex(encode);
        writer.write(encryptHex);
    }
}
