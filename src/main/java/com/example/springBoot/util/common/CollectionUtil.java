package com.wtyt.tsr.util.common;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CollectionUtil {
	
	
	/**
	 * 判断集合是否为空
	 * @param coll
	 * @return
	 */
	public static <T>  boolean  isEmptyCollection(Collection<T>  coll){
		return 	coll == null||coll.isEmpty();	
	}
	
	
	/**
	 * 集合转数组
	 * @param coll
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T>  T[] collectionToArray(Class<T> clazz,Collection<T> coll){
		if(coll==null||coll.size()==0){
			return null;
		}
		T[] array = (T[]) Array.newInstance(clazz, coll.size());
		Iterator<T>  it = coll.iterator();
		int index = 0;
		while(it.hasNext()){
			array[index++] = it.next();
		}		
		return array;	
	}
	
	
	/**
	 * 根据当前的list大小拆分，多个批次的批量更新
	 * @param needToInsertList
	 * @return
	 */
	public static <T> List<List<T>> getListBatchSizeList(int batchSize,List<T> needToOperatorList) {
		List<List<T>> listList = new ArrayList<List<T>>();
        for(int i=0;i<=needToOperatorList.size()/batchSize;i++){
        	List<T> list = new ArrayList<T>();        	
        	if((i+1)*batchSize<=needToOperatorList.size()){
        		list =  needToOperatorList.subList(i*batchSize, (i+1)*batchSize);        		
        	}else{
        		list =  needToOperatorList.subList(i*batchSize,needToOperatorList.size());        		
        	}
        	if(list.size()>0){
        		listList.add(list);
        	}
        }
		return listList;
	}
	

}
