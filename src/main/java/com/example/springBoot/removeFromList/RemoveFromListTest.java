package com.example.springBoot.removeFromList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 循环删除list中多个元素的，应该使用迭代器iterator方式。
 * Created by Administrator on 2019/4/19.
 */
public class RemoveFromListTest {
    public static void main(String[] args) {
        List<String>  list =new ArrayList<>();
        list.add("del");
        list.add("del");
        list.add("add");
        list.add("add");
        list.add("del");

        remove1(list);
        //remove2(list);
        //remove3(list);
        System.out.println(list.size());
    }
    /**
     * 删除某个元素后，list的大小发生了变化，而你的索引也在变化，所以会导致你在遍历的时候漏掉某些元素。
     * 比如当你删除第1个元素后，继续根据索引访问第2个元素时，因为删除的关系后面的元素都往前移动了一位，所以实际访问的是第3个元素。
     * 因此，这种方式可以用在删除特定的一个元素时使用，但不适合循环删除多个元素时使用。
     * 要解决这个问题很容易只需要把 list.remove(i)修改为 ist.remove(i--)
     */
    private static void remove1(List<String>  list){
        for(int i=0;i<list.size();i++){
            if(list.get(i).equals("del")){
                list.remove(i--);
            }
        }
    }

    /**
     * 　这种方式的问题在于，删除元素后继续循环会报错误信息ConcurrentModificationException，
     *  因为元素在使用的时候发生了并发的修改，导致异常抛出。但是删除完毕马上使用break跳出，则不会触发报错。
     * @param list
     */
    private static void remove2(List<String>  list){
        for(String x:list){
            if(x.equals("del")){
                list.remove(x);
               // break;
            }
        }
    }

    /**
     * 这种方式可以正常的循环及删除。
     * 但要注意的是，使用iterator的remove方法，如果用list的remove方法同样会报上面提到的ConcurrentModificationException错误。
     * @param list
     */
    private static void remove3(List<String>  list){
        Iterator<String> it = list.iterator();
        while(it.hasNext()){
            String x = it.next();
            if(x.equals("del")){
                it.remove();
            }
        }
    }


}
