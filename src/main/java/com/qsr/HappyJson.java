package com.qsr;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: Joe Throne
 * @CreatedTime: 2022-07-31 22:48
 * TODO:
 */

public class HappyJson {

    /**
     * 如果json中有多个key相同，如包含[]，这个方法会返回一个离closerString最近（优先同一范围，并非距离最近）的值返回
     * @param src 母串
     * @param key 要找的key，全局不唯一
     * @param haveSpecial 是否包含特殊字符
     * @param closerString 已知的值，全局唯一
     * @return 要找的那个Value。找不到closerString或key返回null
     */
    public static List<Integer> happyGetCloserOne(String src, String key, Boolean haveSpecial, String closerString){
        List<Integer> indexList = new ArrayList<Integer>();
        String current_src = src;
        // 获取closerString出现的坐标
        int closeStringIndex = kmp(src, closerString, getkmpNext(closerString));
        if (closeStringIndex == -1){
            return null;
        }

        // 获取第一个key出现的坐标
        int keyIndex = kmp(current_src, key, getkmpNext(key));
        if (keyIndex == -1){
            return null;
        }

        while(true){
            current_src = current_src.substring(keyIndex + key.length());
            System.out.println(current_src);
            keyIndex = kmp(current_src, key, getkmpNext(key));
            if (keyIndex == -1){
                break;
            }

            if (closeStringIndex < keyIndex){
                indexList.add(keyIndex);
                break;
            }
            // 判断是否获取到目标的那两个
            if (closeStringIndex > keyIndex){
                indexList.add(keyIndex);
            }
        }
        // 首先获取所有key出现的坐标
        // 再
        // 找到左右两侧离的最近的1个或两个key位置，再定向获取
        return indexList;
    }

    /**
     * 如果不确定value中是否包含特殊字符，如逗号，花括号，双引号等，使用这个方法
     * @param src 母串
     * @param key 要获取的键
     * @param haveSpecial 是否包含特殊
     * @return 目标Value，找不到返回""
     */
    public static String happyGetOnlyOne(String src, String key, Boolean haveSpecial){
        int kmp_index = kmp(src, key, getkmpNext(key)); // 获取子串的初始位置
        if (kmp_index == -1){                           // 如果找不到子串出现的位置，返回一个空字符串
            return "";
        }
        int startIndex = kmp_index + key.length();
        return haveSpecial ? getStringByOnlyOneKey(src,startIndex) : getValueByOnlyOneKey(src,startIndex);
    }

    /**
     * 如果确定目标value中没有特殊字符，如逗号，花括号等，整个json只有这么一个key，重复的key只会返回第一个。就可以使用这个方法
     * @param src 母串
     * @param startIndex 起始位置
     * @return 目标字符串
     */
    private static String getValueByOnlyOneKey(String src, int startIndex) {
        StringBuilder sbf = new StringBuilder();
        for (int i = startIndex; i < src.length() - 1; i++){
            if (src.charAt(i) == ':'){
                i ++;
                while (src.charAt(i) != ',' && i < src.length() - 1){
                    if (src.charAt(i) == '}')
                        break;
                    sbf.append(src.charAt(i));
                    i++;
                }
                break;
            }
        }
        return sbf.toString();
    }

    /**
     * 如果不确定目标vulue中有一些特殊符号，如，逗号或花括号等，并且整个json只有这么一个key，重复的key只会返回第一个。就可以使用这个方法
     * @param src 母串
     * @param startIndex 起始位置
     * @return 目标字符串
     */
    private static String getStringByOnlyOneKey(String src, int startIndex) {
        StringBuilder sbf = new StringBuilder();

        char[] ss = src.toCharArray();
        int syh_count = 0;        // 计算双引号是否配对
        int hkh_count = 0;          // 计算花括号个数
        int zkh_count = 0;          // 计算中括号个数
        boolean flag = false;
        for (int i = startIndex + 1; i < ss.length - 1; i++){
            if (ss[i] == ':' || !flag){
                flag = true;
                continue;
            }

            if (ss[i] == '\"' && hkh_count == 0 && zkh_count == 0){ // 当花括号和中括号个数都是0，就开启/关闭
                syh_count ++;         // 开启忽略','模式
            }

            if (ss[i] == '{'){     // 如果检索到了冒号后第一个引号
                hkh_count ++;       // 将双引号+1
            }
            if (ss[i] == '['){     // 如果检索到了冒号后第一个引号
                zkh_count ++;       // 将双引号+1
            }
            if (ss[i] == ']'){     // 如果检索到了冒号后第一个引号
                zkh_count --;       // 将双引号-1
            }
            if (ss[i] == '}'){     // 如果检索到了冒号后第一个引号
                hkh_count --;       // 将双引号-1
            }

            sbf.append(ss[i]);

            if (ss[i + 1] == ',' && syh_count % 2 == 0 && hkh_count == 0 && zkh_count == 0){
                break;
            }

            if (ss[i + 1] == '}' && syh_count % 2 == 0 && hkh_count == 0 && zkh_count == 0){
                break;
            }
        }
        return sbf.toString();
    }

    /**
     * KMP算法
     * @param src 源字符串
     * @param dst 子串
     * @param next 匹配值表
     * @return 对应下标，没有为-1
     */
    private static int kmp(String src, String dst, int[] next) {
        for (int i = 0, j = 0; i < src.length(); ++i) {

            while (j > 0 && src.charAt(i) != dst.charAt(j)) {
                j = next[j - 1];
            }

            if (src.charAt(i) == dst.charAt(j)) {
                j++;
            }

            if (j == dst.length()) {
                return i - j + 1;
            }
        }
        return -1;
    }

    /**
     * 获取子串的next数组
     * @param dest 子串
     * @return next数组
     */
    private static int[] getkmpNext(String dest) {
        int length = dest.length();
        int[] next = new int[length];
        next[0] = 0;

        for (int i = 1, j = 0; i < length; i++) {

            while (j > 0 && dest.charAt(i) != dest.charAt(j)) {
                j = next[j - 1];
            }

            if (dest.charAt(i) == dest.charAt(j)) {
                j++;
            }
            next[i] = j;
        }
        return next;
    }

    /**
     * 当json中key不唯一，获取所有key出现的初始坐标
     * @param src 母串
     * @param key 键
     * @return 所有key出现的初始坐标
     */
//    private static int[] getIndexKey(String src, String key){
//        int index = kmp(src,key,getkmpNext(key));
//        if (index == -1)
//            return null;    //找不到目标串
//    }
}
