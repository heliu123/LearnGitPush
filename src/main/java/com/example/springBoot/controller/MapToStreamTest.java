package com.example.springBoot.controller;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2019/5/6.
 */
public class MapToStreamTest {
    public static void main(String[] args) throws IOException ,ClassNotFoundException{
        Map<String, String> map = new HashMap<>();
        map.put("name", "Tom");
        map.put("age", "20");

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        objectOutputStream.writeObject(map);  //将Java对象转换成流

        byte[] bytes = byteArrayOutputStream.toByteArray();  //将map对象转换成字节流
        System.out.println("我就是map对象的字节流：");
        System.out.println(new String(bytes, "utf-8"));

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Map<String, String> objMap = (Map<String, String>)objectInputStream.readObject();
        System.out.println("字节流转换成map对象，大小：" + objMap.size());
    }
}
