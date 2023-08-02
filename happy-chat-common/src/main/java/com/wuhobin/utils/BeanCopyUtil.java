package com.wuhobin.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 基于spring的beanUtils工具
 *
 * @author IceLiang
 * @Description 基于spring的beanUtils工具,对于基础类型转换不了
 * @version V1.0
 */
@Slf4j
public class BeanCopyUtil {


	/**
	 * 拷贝实体
	 * @param source
	 * @param clz
	 * @param <T>
	 * @param <K>
	 * @return
	 */
	public static <T,K> K copy(T source,Class<K> clz){
		if(source == null){
			return null;
		}
		try {
			K target = clz.newInstance();
			BeanUtils.copyProperties(source, target);
			return target;
		} catch (Exception e){
			e.printStackTrace();
			log.error("Bean copy error-> source={} clz={}",source,clz,e);
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 拷贝实体
	 * @param source
	 * @param clz
	 * @param properties 忽略的字段名称
	 * @param <T>
	 * @param <K>
	 * @return
	 */
	public static <T,K> K copy(T source,Class<K> clz,String ...properties){
		if(source == null){
			return null;
		}
		try {
			K target = clz.newInstance();
			BeanUtils.copyProperties(source, target,properties);
			return target;
		} catch (Exception e){
			e.printStackTrace();
			log.error("Bean copy error-> source={} clz={} properties={}",source,clz,properties,e);
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 拷贝list
	 * @param sourceList
	 * @param clz
	 * @param <T>
	 * @param <K>
	 * @return
	 */
	public static <T,K> List<K> copy(List<T> sourceList,Class<K> clz){
		try {
			if(sourceList == null){
				return null;
			}
			List<K> list = new ArrayList<K>();
			for(T source : sourceList){
				K taeget = copy(source, clz);
				if(taeget != null){
					list.add(taeget);
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Bean copy error-> source={} clz={}",sourceList,clz,e);
			throw new RuntimeException(e.getMessage());
		}
	}
	/**
	 * 拷贝list
	 * @param sourceList
	 * @param clz
	 * @param properties 忽略的字段名称
	 * @return
	 */
	public static <T,K> List<K> copy(List<T> sourceList,Class<K> clz,String ...properties){
		try {
			if(sourceList == null){
				return null;
			}
			List<K> list = new ArrayList<K>();
			for(T source : sourceList){
				K taeget = copy(source, clz,properties);
				if(taeget != null){
					list.add(taeget);
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Bean copy error-> source={} clz={} properties={}",sourceList,clz,properties,e);
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 拷贝list
	 * @param sourceList
	 * @param clz
	 * @param <T>
	 * @param <K>
	 * @return
	 */
	public static <T,K> List<K> copyList(List<T> sourceList,Class<K> clz){
		try {
			if(sourceList == null){
				return Collections.emptyList();
			}
			List<K> list = new ArrayList<K>();
			for(T source : sourceList){
				K taeget = copy(source, clz);
				if(taeget != null){
					list.add(taeget);
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Bean copy error-> source={} clz={}",sourceList,clz,e);
			throw new RuntimeException(e.getMessage());
		}
	}

}
