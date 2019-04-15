package com.example.springBoot.jdkDynamicProxy;

/**
 * client测试代码
 * Created by Administrator on 2019/4/3.
 */
public class ClientMainTest {
    public static void main(String[] args) {
        // 保存生成的代理类的字节码文件
        //System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");

        // jdk动态代理测试
        Subject subject = new JDKDynamicProxy(new RealSubject()).getProxy();
        subject.doSomething();
    }
}

