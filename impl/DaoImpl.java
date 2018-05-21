package com.gkzy.mng.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.sql.Delete;
import org.springframework.stereotype.Repository;

import com.gkzy.mng.bean.MajorSubjectsType;
import com.gkzy.mng.dao.Dao;
import com.gkzy.mng.util.BaseDao;

@Repository
public class DaoImpl extends BaseDao implements Dao{
	/**
	 * 查询对象的所有属性
	 * @param bean 实体类
	 * @param condition where条件 类型：map 如：map.put("id,=", 1) 
	 * @return List  返回List集合
	 */
	@SuppressWarnings("unchecked")
	public <T> List<Object> selectAllList(Class<T> bean,Map<String, Object> condition){
		String hql = "from " + bean.getSimpleName() + " where " ;
		Query query = hqlCondition(hql,condition);
		return query.list();
	};
	
	/**
	 * 查询对象的 部分属性
	 * @param bean 实体类
	 * @param column 所需查询的列
	 * @param condition where条件 类型：map 如：map.put("id,=", 1)
	 * @return 返回list集合
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<Object> selectPartialColumnList(Class<T> bean,String[] column,Map<String, Object> condition) {
		String queryColumn = hqlColumn(column);
		String hql = "select new "+ bean.getName() + "("+queryColumn+")"+" from "+ bean.getSimpleName() + " where ";
		Query query = hqlCondition(hql,condition);
		return query.list();
	}
	
	/**
	 * 查询扩展类
	 * @param vo 扩展类
	 * @param bean 实体类
	 * @param column 所需查询的列
	 * @param condition where条件 类型：map 如：map.put("id,=", 1)
	 * @return 返回list集合
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<Object> selectAllVoList(Class<T> vo,Class<T>[] bean,String[] reName, String[] column,
											Map<String, Object> condition) {
		String queryColumn = hqlColumn(column);
		String beans = hqlBeans(bean,reName);
		System.out.println(beans);
		String hql = "select new "+ vo.getName() + "("+queryColumn+")" + " from " + beans + " where ";
		Query query = hqlCondition(hql,condition);
		return query.list();
	}
	
	/**
	 * 修改
	 * @param beans 实体类  类型：Class[]
	 * @param reName 重命名  类型：String[]
	 * @param updateVal 需要修改的属性与值，set.....; 类型：map 如：map.put("id,=", 1)
	 * @param condition where条件 类型：map 如：map.put("id,=", 1)
	 * @return 返回类型 int  返回影响行数
	 */
	@Override
	public <T> int update(Class<T>[] beans, String[] reName,
			Map<String, Object> updateVal, Map<String, Object> condition) {
		String bean = hqlBeans(beans,reName);
		String hql = "update "+bean+" set ";
		Query query = updateHqlCondition(hql,updateVal,condition);
		return query.executeUpdate();
	}
	
	/**
	 * 删除delete
	 * @param bean 实体类 类型：Class
	 * @param condition  where条件 类型：map 如：map.put("id,=", 1)
	 * @return 返回类型 int  返回影响行数
	 */
	@Override
	public <T> int delete(Class<T> bean, Map<String, Object> condition) {
		String hql = "delete from "+ bean.getSimpleName() + " where ";
		Query query = hqlCondition(hql, condition);
		return query.executeUpdate();
	}
	/**
	 * 删除delete
	 * @param bean 实体类  类型：Class[]
	 * @param reName 重命名  类型：String[] 
	 * @param condition  where条件 类型：Map
	 * @return 返回类型 int  返回影响行数
	 */
	@Override
	public <T> int delete(Class<T>[] beans, String[] reName,Map<String, Object> condition){
		String bean = hqlBeans(beans,reName);
		String hql = "delete from "+ bean + " where ";
		Query query = hqlCondition(hql, condition);
		return query.executeUpdate();
	}
	/**
	 * 添加insert
	 * @param bean 实体类 类型：Class
	 * @return 返回类型 int  返回save对象的主键id
	 */
	@Override
	public <T> int insert(Class<T> bean) {
		int i = (Integer) this.getCurrentSession().save(bean);
		return i;
	}
	
	/**
	 * select
	 * @param hql 查询语句前半部分，如：from MajorSubjectsType where
	 * @param condition 条件 where....
	 * @return Query 返回一个query对象
	 */
	@SuppressWarnings("unchecked")
	public Query hqlCondition(String hql,Map<String, Object> condition){
		List<Object> conditionList = condition(hql,condition);
		String newHql = (String) conditionList.get(0);
		List<String> sList = (List<String>) conditionList.get(1);
		List<Object> oList = (List<Object>) conditionList.get(2);
		Query query =  this.getCurrentSession().createQuery(newHql);
		//setParameter
		for (int i = 0; i < sList.size(); i++) {
			query.setParameter(sList.get(i), oList.get(i));
		}
		return query;
	}
	/**
	 * updata
	 * @param hql 查询语句前半部分，如：from MajorSubjectsType where
	 * @param condition 条件 where....
	 * @param updateVal 需要修改的属性与值，set.....;
	 * @return Query 返 回一个query对象
	 */
	@SuppressWarnings("unchecked")
	public Query updateHqlCondition(String hql,Map<String, Object> updateVal,Map<String, Object> condition){
		List<Object> updateList = condition(hql,updateVal);
		String newHql = (String) updateList.get(0);
		List<String> sList = (List<String>) updateList.get(1);
		List<Object> oList = (List<Object>) updateList.get(2);
		newHql += " where ";
		List<Object> conditionList = condition(newHql,condition);
		String allHql = (String) conditionList.get(0);
		List<String> kList = (List<String>) conditionList.get(1);
		List<Object> vList = (List<Object>) conditionList.get(2);
		//setParameter
		Query query =  this.getCurrentSession().createQuery(allHql);
		for (int i = 0; i < sList.size(); i++) {
			query.setParameter(sList.get(i), oList.get(i));
		}
		for (int i = 0; i < kList.size(); i++) {
			query.setParameter(kList.get(i), vList.get(i));
		}
		return query;
	}
	/**
	 *  传入条件，拼接hql
	 * @param hql 传入一条hql
	 * @param condition 条件
	 * @return 返回List集合，0 hql，1 condition的key，2 condition的value
	 */
	public List<Object> condition(String hql ,Map<String, Object> condition){
		List<Object> list = new ArrayList<Object>();
		//key
		Set<String> set = condition.keySet();
		String a = set.toString();
		String b = a.substring(1,a.length()-1);
		String[] c = b.split(",");
		List<String> conditionKeyList = new ArrayList<String>();
		for (int i = 0; i < c.length; i++) {
			if( i%2 == 0){
				conditionKeyList.add(c[i].trim().replace(".",""));
			}
			hql += c[i];
			if(i%2 != 0){
				hql += ":"+c[i-1].trim().replace(".","");
				if( i < c.length-1){
					hql += " and";
				}
			}
		}
		System.out.println(hql);
		//value
		Set<Entry<String, Object>> q =  condition.entrySet();
		List<Object> conditionValList = new ArrayList<Object>();
		for (Entry<String, Object> entry : q) {
			Object w = entry.getValue();
			conditionValList.add(w);
		}
		list.add(hql);
		list.add(conditionKeyList);
		list.add(conditionValList);
		return list;
	}
	/**
	 * 列名
	 * @param column 所需要查询的列
	 * @return String 查询列的字符串
	 */
	public String hqlColumn(String[] column){
		String queryColumn = "";
		for (int i = 0; i < column.length; i++) {
			queryColumn += column[i];
			if( i < column.length - 1){
				queryColumn += ",";
			}
		}
		return queryColumn;
	}
	/**
	 * 
	 * @param bean 传入一个Class数组
	 * @return String 所需要查的类
	 */
	public <T> String hqlBeans(Class<T>[] bean){
		String beans = "";
		for (int i = 0; i < bean.length; i++) {
			System.out.println(bean[i].getSimpleName());
			beans += bean[i].getSimpleName();
			if( i < bean.length-1){
				beans += ",";
			}
		}
		return beans;
	}
	/**
	 * @param bean 传入一个Class数组
	 * @param reName 重命名
	 * @return String 所需要查的类
	 */
	public <T> String hqlBeans(Class<T>[] bean,String[] reName){
		String beans = "";
		for (int i = 0; i < bean.length; i++) {
			beans += bean[i].getSimpleName() + " "+reName[i];
			if( i < bean.length-1){
				beans += ",";
			}
		}
		return beans;
	}

	


}
