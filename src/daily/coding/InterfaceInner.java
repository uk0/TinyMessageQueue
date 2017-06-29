package daily.coding;

import com.sun.org.apache.bcel.internal.classfile.InnerClass;

interface OutInterface {
    public void f();
}

// 外部类
class OuterClass2 {
    // 内部类并且定义为私有，实现接口
    private class InnerClass implements OutInterface {
        InnerClass(String s) {
            System.out.println(s);
        }

        // 重写
        public void f() {
            System.out.println("访问内部类中的f()方法");
        }
    }

    public InnerClass doit() {
        return new InnerClass("访问内部类构造方法");
    }
}

public class InterfaceInner {
    public static void main(String[] args) {
        OuterClass2 out = new OuterClass2();
        OutInterface outinter = out.doit();
        outinter.f();
    }
}