package cn.lxycx.kuaicore.util;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Signer {
    public static void main(String[] args) {
        System.out.println(HmacSHA1("12345678","12345678"));
    }



    /**
     * 使用 HMAC-SHA1 签名方法对对encryptText进行签名
     * @param encryptText 被签名的字符串
     * @param encryptKey  密钥
     * @return 签名结果
     */
    public static String HmacSHA1(String encryptText, String encryptKey){
        try{
            byte[] data=encryptKey.getBytes("utf-8");
            //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
            SecretKey secretKey = new SecretKeySpec(data, "HmacSHA1");
            //生成一个指定 Mac 算法 的 Mac 对象
            Mac mac = Mac.getInstance("HmacSHA1");
            //用给定密钥初始化 Mac 对象
            mac.init(secretKey);

            byte[] text = encryptText.getBytes("utf-8");
            //完成 Mac 操作
            return toStr(mac.doFinal(text));
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }




    /**
     * SHA-1 签名算法
     *
     * */
    public static String SHA1(String decript) {
        try {
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            return toStr(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * MD5 签名算法
     *
     * */
    public static String MD5(String origin) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = toStr(md.digest(resultString.getBytes()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }







    /*字节数组转换为 十六进制 数*/
    public static String toStr(byte[] array){
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            String shaHex = Integer.toHexString(array[i] & 0xFF);
            if (shaHex.length() < 2) {
                hexString.append(0);
            }
            hexString.append(shaHex);
        }
        return hexString.toString();
    }
}
