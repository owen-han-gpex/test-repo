package com.gpex.common.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    public static String getCommon() {
        return "common.StringUtils";
    }


    /**
     * 문자열이 소수가 없는 정수인지 확인
     * @param str
     * @return true 숫자, false 문자
     */
    public static boolean isNumerical(String str){
        if(str == null || str.equals("")){
            return false;
        }

        for (int i = 0; i < str.length(); i++) {
            char temp = str.charAt(i);
            if (temp >= '0' && temp <= '9') {
                continue;
            } else {
                return false;
            }
        }

        return true;
    }

    /**
     * 16 진수인지 확인
     * @param str
     * @return
     */
    public static boolean isHexNumerical(String str) {
        if (str == null || str.equals("")) {
            return false;
        }

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (((c >= 'a') && (c <= 'f')) || ((c >= 'A') && (c <= 'F')) || ((c >= '0') && (c <= '9'))) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * 문자열이 소수 인지 확인
     * @param str
     * @return
     */
    public static boolean isDecimal(String str){
        if(str == null || str.equals("")){
            return false;
        }

        for (int i = 0; i < str.length(); i++) {
            char temp = str.charAt(i);
            if ((temp >= '0' && temp <= '9') || temp == '.') {
                continue;
            } else {
                return false;
            }
        }

        return true;
    }

    public static boolean isCellPhone(String s){
        if(s == null || s.length() != 11){
            return false;
        }

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if(c < '0' || c > '9'){
                return false;
            }
        }

        return true;
    }

    public static String lastWord(String s, String regex){
        if(org.apache.commons.lang3.StringUtils.isBlank(s)){
            return s;
        }

        String[] strArray;

        if(regex.equals(".")){
            strArray = s.split("\\.");
        }else{
            strArray = s.split(regex);
        }

        return strArray[strArray.length-1];
    }


    public static String firstWord(String s, String regex){
        if(org.apache.commons.lang3.StringUtils.isBlank(s)){
            return s;
        }

        String[] strArray;

        if(regex.equals(".")){
            strArray = s.split("\\.");
        }else{
            strArray = s.split(regex);
        }

        return strArray[0];
    }

    public static String[] split(String s, String regex){
        String[] strArray;

        if(org.apache.commons.lang3.StringUtils.isBlank(s)){
            strArray = new String[1];
            strArray[0] = s;
            return strArray;
        }



        if(regex.equals(".")){
            strArray = s.split("\\.");
        }else{
            strArray = s.split(regex);
        }

        return strArray;
    }


    public static Integer matcher(String s, String regex){
        Pattern r = Pattern.compile(regex);
        Matcher m =  r.matcher(s);
        int i = 0;
        while(m.find()) {
            m.group();
            i++;
        }
        return i;
    }


    public static String enCode(String str) {
        try {
            return URLEncoder.encode(str,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String decode(String str){
        try {
            return URLDecoder.decode(str,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String convertCharacterSet(String str, String format, String newFormat) {
        if (str == null) {
            return null;
        }

        try {
            return new String(str.getBytes(format), newFormat);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * iso 8859-1 -> utf-8
     * @param str
     * @return
     * @author: WangLiang
     * @update:[2014年12月12日 下午3:06:04] WangLiang [变更描述]
     */
    public static String convertCharacterSet(String str) {
        if (str == null) {
            return null;
        }

        try {
            return new String(str.getBytes("ISO-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }

    public static boolean isZipCode(String str){
        if(str == null || str.length() != 6){
            return false;
        }
        return StringUtils.isNumerical(str);
    }

    public static String join(Collection<String> strs){
        StringBuffer t = new StringBuffer("");
        for(String s : strs){
            t.append(s).append(",");
        }
        return t.substring(0, t.length()-1).toString();
    }


    /**
     * 문자열에 이모티콘이 포함되어 있는지 체크
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        boolean isEmoji = false;
        for (int i = 0; i < len; i++) {
            char hs = source.charAt(i);
            if (0xd800 <= hs && hs <= 0xdbff) {
                if (source.length() > 1) {
                    char ls = source.charAt(i+1);
                    int uc = ((hs - 0xd800) * 0x400) + (ls - 0xdc00) + 0x10000;
                    if (0x1d000 <= uc && uc <= 0x1f77f) {
                        return true;
                    }
                }
            } else {
                // non surrogate
                if (0x2100 <= hs && hs <= 0x27ff && hs != 0x263b) {
                    return true;
                } else if (0x2B05 <= hs && hs <= 0x2b07) {
                    return true;
                } else if (0x2934 <= hs && hs <= 0x2935) {
                    return true;
                } else if (0x3297 <= hs && hs <= 0x3299) {
                    return true;
                } else if (hs == 0xa9 || hs == 0xae || hs == 0x303d || hs == 0x3030 || hs == 0x2b55 || hs == 0x2b1c || hs == 0x2b1b || hs == 0x2b50|| hs == 0x231a ) {
                    return true;
                }
                if (!isEmoji && source.length() > 1 && i < source.length() -1) {
                    char ls = source.charAt(i+1);
                    if (ls == 0x20e3) {
                        return true;
                    }
                }
            }
        }
        source = new String();
        return  isEmoji;
    }

    /**
     * IP 주소인지 체크
     *
     * @param addr
     * @return
     */
    public static boolean isIP(String addr) {
        if (addr == null || addr.length() < 7 || addr.length() > 15 || "".equals(addr)) {
            return false;
        }

        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

        Pattern pat = Pattern.compile(rexp);

        Matcher mat = pat.matcher(addr);

        boolean ipAddress = mat.find();

        return ipAddress;
    }

    /**
     * 전화번호 인지 체크
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        // Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9])|(17[0-9]))\\d{8}$");
        Pattern p = Pattern.compile("^\\d{11}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
    /**
     * 전화번호 * 처리
     * @param mobile
     * @return
     */
    public static String formatMobileNO(String mobile) {
        if(mobile!=null && !mobile.equals("") && mobile.length()>=4){
            return "**"+mobile.substring(mobile.length()-4);
        }
        return mobile;
    }

    public static String formatMobile(String mobile) {
        if(mobile.length()<11) {
            return formatMobileNO(mobile);
        }else {
            return hideMobileNumber(mobile);
        }
    }

    public static String getNotNullStr(Object obj){
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    public static String hideMobileNumber(String mobile){
        if(mobile==null || mobile.length()<4){
            return "";
        }
//    	return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
        return mobile.substring(0, 5)  + "****" + mobile.substring(mobile.length()-4, mobile.length());
    }

    public static String hideEmail(String email){
        if(email.lastIndexOf("@")==-1){
            return "";
        }
        if(email==null || email == "" || email.substring(0,email.lastIndexOf("@")).length()<2){
            return "";
        }
//    	return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
        return email.substring(0, 3)+"****"+email.substring(email.lastIndexOf("@"),email.length());
    }

    public static String hideCertificateNumber(String certificate){
        if(certificate==null || certificate.length()<8){
            return "";
        }
        return certificate.substring(0,1)+"****"+certificate.substring(certificate.length()-4,certificate.length());
    }

    /**
     * 처음 두 자리와 마지막 두 자리만 표시되고 나머지 자리는 *로 표시됩니다.
     * @param certificate
     * @return
     */
    public static String hideCertificateNumber2(String certificate){
        if (certificate != null && certificate.length() > 2) {
            String first = certificate.substring(0, 2);
            String last = certificate.substring(certificate.length() - 2);
            String mid = "";
            for (int i = 0; i < certificate.length() - 4; i++) {
                mid+="*";
            }
            return first + mid + last;
        }else{
            return certificate;
        }
    }

    public static String maskingString(String certificate, int startViewCnt, int lastViewCnt){
        if (certificate == null){
            return "";
        } else if (certificate.length() < startViewCnt + lastViewCnt) {
            return certificate;
        } else {
            String mid = "";
            for (int i = 0; i < certificate.length() - (startViewCnt + lastViewCnt); i++) {
                mid += "*";
            }

            return certificate.substring(0, startViewCnt) + mid + certificate.substring(certificate.length() - lastViewCnt, certificate.length());
        }
    }



    public static boolean equalsAny(String text,String... values){
        for (String value : values) {
            if(value.equals(text)){
                return true;
            }
        }
        return false;
    }

    public static boolean equalsAnyIgnoreCase(String text,String... values){
        for (String value : values) {
            if(value.toLowerCase().equals(text.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
                || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    /**
     * 이모티콘 필터링
     * @param source
     * @return
     */
    public static String filterEmoji(String source) {
        if (org.apache.commons.lang3.StringUtils.isBlank(source)) {
            return source;
        }
        StringBuilder buf = null;
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (isEmojiCharacter(codePoint)) {
                if (buf == null) {
                    buf = new StringBuilder(source.length());
                }
                buf.append(codePoint);
            }
        }
        if (buf == null) {
            return source;
        } else {
            if (buf.length() == len) {
                buf = null;
                return source;
            } else {
                return buf.toString();
            }
        }
    }

    public static String concatIgnoreNull(String...args){
        String str = "";
        if(args!=null && args.length>0){
            for (String arg : args) {
                if(arg!=null && !"".equals(arg)){
                    str+= arg;
                }
            }
        }
        return str;
    }

    /**
     * Random Salt 생성
     * @return
     */
    public static String getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] temp = new byte[16];
        random.nextBytes(temp);

        StringBuilder sb = new StringBuilder();
        for(byte a : temp) {
            sb.append(String.format("%02x", a));
        }
        return sb.toString();
    }

    public static String toJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());  // LocalDateTime Casting 할때 필요

        return objectMapper.writeValueAsString(obj);
    }

    public static Map<String, Object> toMap(Object obj) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());  // LocalDateTime Casting 할때 필요

        return objectMapper.convertValue(obj, Map.class);
    }

    public static boolean isJsonString(String jsonString) {
        try {
            new ObjectMapper().readTree(jsonString);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * JSON 형태의 문자열을 변경할 Object 로 변환
     * @param jsonString
     * @param toValueType
     * @param <T>
     * @return
     */
    public static <T> T jsonStringToObject(String jsonString, Class<T> toValueType) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());  // LocalDateTime Casting 할때 필요

            return (T) objectMapper.readValue(jsonString, toValueType);
        } catch (Exception e) {
            return null;
        }



    }

}
