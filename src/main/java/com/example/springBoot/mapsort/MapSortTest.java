package com.example.springBoot.mapsort;

import java.util.*;

/**
 * Created by Administrator on 2019/4/25.
 */
public class MapSortTest {
    public static void main(String[] args) {
        /**
         *  TreeMap默认是升序的，如果我们需要改变排序方式，则需要使用比较器：Comparator
         */
        Map< Integer, String> map=new TreeMap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);       //按照key值大小升序排列       -o1.compareTo(o2)即o2.compareTo(o1)按照key值大小降序排列
                //return 1;                     //按put反顺序排序      1则为 put顺序排列
            }
        });
        map.put(5, "a");
        map.put(3, "c");
        map.put(4, "b");
        map.put(2, "d");
        map.put(1, "e");
        for(Map.Entry<Integer, String> aEntry:map.entrySet()) {
            System.out.println(aEntry.getKey()+":"+aEntry.getValue());
        }
        /**
         *TreeMap的value来进行排序
         */

        List<Map.Entry<Integer,String>> list =new ArrayList<Map.Entry<Integer,String>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Integer, String>>() {

            @Override
            public int compare(Map.Entry<Integer, String> o1, Map.Entry<Integer, String> o2) {
                // TODO Auto-generated method stub
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        for(Map.Entry<Integer, String> aEntry:list) {
            System.out.println(aEntry.getKey()+":"+aEntry.getValue());
        }

        /**
         * HashMap的值是没有顺序的，他是按照key的HashCode来实现的。对于这个无序的HashMap我们要怎么来实现排序呢
         */
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("c", "accccc");
        map1.put("a", "abaaaaa");
        map1.put("b", "cbbbbb");
        map1.put("d", "dddddd");

        List<Map.Entry<String,String>> list1 = new ArrayList<>(map1.entrySet());
        Collections.sort(list1,new Comparator<Map.Entry<String,String>>() {
            //升序排序
            public int compare(Map.Entry<String, String> o1,
                               Map.Entry<String, String> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }

        });

        for(Map.Entry<String,String> mapping:list1){
            System.out.println(mapping.getKey()+":"+mapping.getValue());
        }
    }


}
