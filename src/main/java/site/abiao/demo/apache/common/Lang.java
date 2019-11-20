package site.abiao.demo.apache.common;

import org.apache.commons.lang3.StringEscapeUtils;

public class Lang {

    public static void main(String[] args) {
        String str = "数据结构里面-&gt;是什么意思？";
        System.out.println(StringEscapeUtils.unescapeHtml3(str));
    }
}
