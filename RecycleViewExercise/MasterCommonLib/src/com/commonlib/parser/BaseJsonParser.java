package com.commonlib.parser;

import com.commonlib.entity.BaseEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;


abstract public class BaseJsonParser<T extends BaseEntity> {

	Class<T> modelClass;
	public BaseJsonParser (Class<T> modelClass) {
		this.modelClass = modelClass;
	}
	
	/**
	 * 从接口返回的原始字符串解析，如果规则改变，可以改写次方法
	 * @param content
	 * @return
	 * @throws JSONException
	 */
	abstract public T parse (String content) throws Exception;
	
	/**
	 * 有用数据对应的json object(去除了冗余部分)
	 * @param jObject
	 * @return
	 * @throws JSONException
	 */
	public final T parse (JSONObject jObject) throws JSONException, IllegalAccessException, InstantiationException {
		T model = modelClass.newInstance();
		parse(jObject, model);
		
		return model;
	}
	
	private void parse (JSONObject jObject, Object model) throws JSONException, IllegalAccessException, InstantiationException {
		Class<?> c = model.getClass();
		Field[] fields = c.getDeclaredFields();
		for (int i=0;i<fields.length;i++) {
			parseField(fields[i], jObject, model);
		}
		Class<?> s = c.getSuperclass();
		if (s == null) return;
		fields = s.getDeclaredFields();
		for (int i=0;i<fields.length;i++) {
			parseField(fields[i], jObject, model);
		}
	}
	
	private void parse (JSONArray jArray, Object array) throws JSONException, IllegalAccessException, InstantiationException {
		Class<?> classtype = array.getClass().getComponentType();
		for (int i=0;i<jArray.length();i++) {
			if (classtype.isPrimitive()) {
				Array.set(array, i, jArray.get(i));
			} else if (classtype == String.class) {
				Array.set(array, i, jArray.get(i));
			} else if (classtype.isArray()) {
//				final JSONArray jArr = jArray.getJSONArray(i);
//				final int len = jArr.length();
//				Object array = Array.newInstance(classtype.getComponentType(), len);
//				parse(jArray, array);
//				field.set(model, array);
			} else {
				JSONObject jObj = jArray.getJSONObject(i);
				Object obj = classtype.newInstance();
				parse(jObj, obj);
				Array.set(array, i, obj);
			}
		}
	}
	
	private void parseField (Field field, JSONObject jObject, Object model) throws JSONException, IllegalAccessException, InstantiationException {
		String name = field.getName();
//		android.util.Log.d("JsonParser", (jObject.has(name)?"has ":"not has ") + name + "  " + field.getDeclaringClass().getSimpleName() + "." + ((Class)field.getType()).getSimpleName());
		if (!jObject.has(name)) return;
//		android.util.Log.d("JsonParser", name + ": " + jObject.getString(name));
		
		field.setAccessible(true);
		final Class<?> classtype = field.getType();
		if (field.getGenericType() instanceof ParameterizedType) {
			ParameterizedType type = (ParameterizedType)field.getGenericType();
			final Class<?> paramclass = (Class)type.getActualTypeArguments()[0];
//			System.out.println((type.getRawType() == ArrayList.class) + ", " + (type.getRawType() == HashMap.class));

			if (type.getRawType() == ArrayList.class) {
				ArrayList array = (ArrayList)classtype.newInstance();
				final JSONArray jArray = jObject.getJSONArray(name);
				final int len = jArray.length();
				for (int k=0;k<len;k++) {
				    Object o = jArray.get(k);
				    if (paramclass == String.class
				            || paramclass == JSONObject.class
				            || paramclass == JSONArray.class) {
                        array.add(o);
                    } else {
	                    JSONObject jObj = jArray.getJSONObject(k);
	                    Object obj = paramclass.newInstance();
	                    parse(jObj, obj);
	                    array.add(obj);
				    }
				}
				field.set(model, array);
			} else if (type.getRawType() == HashMap.class) {
				HashMap map = (HashMap)classtype.newInstance();
				Type secondtype = type.getActualTypeArguments()[1];
//				System.out.println("HashMap:  secondtype: " + (secondtype instanceof ParameterizedType));
				if (secondtype instanceof ParameterizedType) {
					ParameterizedType paramtype = (ParameterizedType)secondtype;
					if (paramtype.getRawType() == HashMap.class) {
						final Class<?> thirdparamclass = (Class)paramtype.getActualTypeArguments()[0];
						final JSONObject jObj = jObject.getJSONObject(name);
						final JSONArray jArray = jObj.names();
						final int len = jArray == null ? 0 : jArray.length();
						for (int k=0;k<len;k++) {
							String key = jArray.getString(k);
							HashMap childmap = (HashMap)((Class)paramtype.getRawType()).newInstance();
							final JSONObject jChildObj = jObj.getJSONObject(key);
							final JSONArray jChildArray = jChildObj.names();
							final int childlen = jChildArray == null ? 0 : jChildArray.length();
							for (int n=0;n<childlen;n++) {
								String childkey = jArray.getString(n);
								if (thirdparamclass == String.class) {
									String childvalue = jChildObj.getString(childkey);
									childmap.put(childkey, childvalue);
								} else {
									JSONObject jChildChildObj = jChildObj.getJSONObject(childkey);
									Object obj = thirdparamclass.newInstance();
									parse(jChildChildObj, obj);
									childmap.put(childkey, obj);
								}
							}
							map.put(key, childmap);
						}
					} else if (paramtype.getRawType() == ArrayList.class) {
						final Class<?> thirdparamclass = (Class)paramtype.getActualTypeArguments()[0];
						final JSONObject jObj = jObject.getJSONObject(name);
						final JSONArray jArray = jObj.names();
						final int len = jArray == null ? 0 : jArray.length();
						for (int k=0;k<len;k++) {
							String key = jArray.getString(k);
							ArrayList childarray = (ArrayList)((Class)paramtype.getRawType()).newInstance();
							final JSONArray jChildArray = jObj.getJSONArray(key);
							final int childlen = jChildArray == null ? 0 : jChildArray.length();
							for (int n=0;n<childlen;n++) {
								if (thirdparamclass == String.class) {
									childarray.add(jChildArray.getString(n));
								} else {
									JSONObject jChildChildObj = jChildArray.getJSONObject(n);
									Object obj = thirdparamclass.newInstance();
									parse(jChildChildObj, obj);
									childarray.add(obj);
								}
							}
							map.put(key, childarray);
						}
					}
				} else if (secondtype == String.class) {
					final JSONObject jObj = jObject.getJSONObject(name);
					final JSONArray jArray = jObj.names();
					final int len = jArray == null ? 0 : jArray.length();
					for (int k=0;k<len;k++) {
						String key = jArray.getString(k);
						String value = jObj.getString(key);
						map.put(key, value);
					}
				} else {
					final JSONObject jObj = jObject.getJSONObject(name);
					final JSONArray jArray = jObj.names();
					final int len = jArray == null ? 0 : jArray.length();
					for (int k=0;k<len;k++) {
						String key = jArray.getString(k);
						Object obj = ((Class)secondtype).newInstance();
						parse(jObj.getJSONObject(key), obj);
						map.put(key, obj);
					}
				}
				field.set(model, map);
			}
		} else if (classtype.isPrimitive()) {
//			field.set(model, jObject.get(name));
			setPrimitiveField(field, model, jObject, name);
		} else if (classtype == String.class) {
			field.set(model, jObject.getString(name));
		} else if (classtype == JSONObject.class) {
			field.set(model, jObject.getJSONObject(name));
		} else if (classtype == JSONArray.class) {
			field.set(model, jObject.getJSONArray(name));
		} else if (classtype.isArray()) {
			final JSONArray jArray = jObject.getJSONArray(name);
			final int len = jArray.length();
			Object array = Array.newInstance(classtype.getComponentType(), len);
			parse(jArray, array);
			field.set(model, array);
		} else {
			JSONObject jObj = jObject.getJSONObject(name);
			Object obj = classtype.newInstance();
			parse(jObj, obj);
			field.set(model, obj);
		}
	}
	
	private static void setPrimitiveField (Field field, Object instance, JSONObject jObject, String key) throws IllegalAccessException, JSONException {
		Class classtype = field.getType();
		if (classtype == int.class) {
			field.set(instance, jObject.getInt(key));
		} else if (classtype == long.class) {
			field.set(instance, jObject.getLong(key));
		} else if (classtype == double.class) {
			field.set(instance, jObject.getDouble(key));
		} else if (classtype == boolean.class) {
			field.set(instance, jObject.getBoolean(key));
		} else if (classtype == byte.class) {
			field.set(instance, (byte)jObject.getInt(key));
		} else if (classtype == short.class) {
			field.set(instance, (short)jObject.getInt(key));
		} else if (classtype == float.class) {
			field.set(instance, (float)jObject.getDouble(key));
		}
	}
}

