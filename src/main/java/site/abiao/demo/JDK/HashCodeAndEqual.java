package site.abiao.demo.JDK;

import java.util.HashSet;
import java.util.Objects;

public class HashCodeAndEqual {

    public static class Obj{

        private int a;

        public Obj(int a) {
            this.a = a;
        }

        @Override
        public boolean equals(Object obj) {
            //比较是不是同一地址空间
            if (obj == this) {
                return true;
            }
            //比较是不是同一个类型
            if (obj instanceof Obj) {
                Obj o = (Obj)obj;
                if (this.a != o.a) {
                    return false;
                }
                return true;
            }
            return false;
        }


    }


    public static void main(String[] args) {
        Obj o1 = new Obj(1);
        Obj o2 = new Obj(1);
        HashSet<Obj> set = new HashSet<>();
        set.add(o1);
        set.add(o2);
        System.out.println(o1.hashCode());
        System.out.println(o2.hashCode());
        System.out.println(o1.equals(o2));
        System.out.println(set.size());

    }
}
