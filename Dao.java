package com.gkzy.mng.dao;

import java.util.List;
import java.util.Map;

import com.gkzy.mng.bean.MajorSubjectsType;

public interface Dao {
	public <T> List<Object> selectAllList(Class<T> bean,Map<String, Object> condition);
	
	public <T> List<Object> selectPartialColumnList(Class<T> bean,String[] column,Map<String, Object> condition);
	
	public <T> List<Object> selectAllVoList(Class<T> vo,Class<T>[] bean,String[] reName,String[] column,Map<String, Object> condition);
	
	public <T> int update(Class<T>[] beans,String[] reName,Map<String, Object> updateVal,Map<String, Object> condition);
	
	public <T> int delete(Class<T> bean,Map<String, Object> condition);
	
	public <T> int delete(Class<T>[] bean, String[] reName,Map<String, Object> condition);
	
	public <T> int insert(Class<T> bean);
}
