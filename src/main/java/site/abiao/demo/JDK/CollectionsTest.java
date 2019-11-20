package site.abiao.demo.JDK;


import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class CollectionsTest {

    public static void linkedHashmap() {
        Map<String, String> linkedHashmap = new LinkedHashMap<>();

        linkedHashmap.put("1","1");
        linkedHashmap.put("2","2");
        linkedHashmap.put("3","3");
        linkedHashmap.put("4","4");
        linkedHashmap.put("5","5");

        //按照添加顺序访问
        for (Map.Entry<String, String> entry : linkedHashmap.entrySet()) {
            System.out.println(String.format("key:%s, value:%s",entry.getKey(),entry.getValue()));
        }

        Map<String, String> map = new LinkedHashMap<String, String>(16,0.75f,true);

        map.put("apple", "苹果");
        map.put("watermelon", "西瓜");
        map.put("banana", "香蕉");
        map.put("peach", "桃子");

        map.get("banana");
        map.get("apple");

        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }


    }

    public static void main(String[] args) {
        InnerClassTest.A a = new InnerClassTest.A();
        linkedHashmap();
    }
}
