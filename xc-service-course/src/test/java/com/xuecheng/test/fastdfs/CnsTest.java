package com.xuecheng.test.fastdfs;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @author: YaoGuangXun
 * @date: 2020/9/26 10:59
 * @Version: 1.0
 */
public class CnsTest {

    @Test
    public void test01() {
        /**
         *  ldperson&customerno=123&name=yao
         *  ldperson&serialno=116&payintv=*&operator=姚崇2&birthday=1939-12-08&makedate=2020-09-17&maketime=19:51:22
         */
        String str = "ldperson&serialno=116&payintv=*&operator=姚崇2&birthday=1939-12-08&makedate=2020-09-17&maketime=19:51:22";
        test02(str);
    }

    public Map<String,Object> test02(String str) {
        String[] strArr = str.split("&");
        // 获取表名
        String tableName = strArr[0];
        System.out.println("表名：" + tableName);
        Map map = new HashMap();
        // 移除数组的第一个元素表名
        String[] newArray = Arrays.copyOfRange(strArr, 1, strArr.length);
        for (int i = 0; i < newArray.length; i++) {
            String[] attributeArr = newArray[i].split("=");
            String value = "*".equals(attributeArr[1])? null:attributeArr[1];
            map.put(attributeArr[0],value);
        }
        System.out.println(map);
        return map;
    }

}
