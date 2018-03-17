package xuehai.util;

public class ByteToString {
    public static String getString(byte[] byteBuffer){
        String strResult = null;
        StringBuffer strHexString = new StringBuffer();
        for (int i = 0; i < byteBuffer.length; i++) {
            String hex = Integer.toHexString(0xff & byteBuffer[i]);
            if (hex.length() == 1) {
                strHexString.append('0');
            }
            strHexString.append(hex);
        }
        // 得到返回結果
        strResult = strHexString.toString();
        return strResult;
    }
}