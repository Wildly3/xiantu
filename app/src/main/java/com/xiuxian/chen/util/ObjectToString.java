package com.xiuxian.chen.util;

import java.lang.reflect.*;
import java.util.*;

public final class ObjectToString
{
	//以下类型才会被存储
	private static final Class[] Claazs = {
		int.class,
		float.class,
		boolean.class,
		String.class,
		CharSequence.class
	};

	Object obj;

	List<Field> fields;

	public ObjectToString(Object obj){
		if(obj == null) return ;

		this.obj = obj;

		Class claz = obj.getClass();

		Field[] fields = claz.getFields();

		if(fields.length < 1) return ;

		List<Field> fs = new ArrayList<>(fields.length);

		for (Field fieldid : fields)
			for (Class c : Claazs)
				if (c == fieldid.getType()){
					fs.add(fieldid);
					break;
				}

		this.fields = fs;
	}

	@Override
	public String toString()
	{
		if(fields == null || fields.size() < 1) return null;

		StringBuilder sb = new StringBuilder("object_name:"+obj.getClass().getName());

		try
		{
			for (Field field : this.fields)
			{
				String clazname = field.getType().getName();

				String value = String.valueOf(field.get(this.obj));

				sb.append("\n");

				sb.append(field.getName());

				sb.append(":");

				sb.append(clazname);

				sb.append(",");

				sb.append(value);
			}
		}
		catch (IllegalAccessException e)
		{}
		catch (IllegalArgumentException e)
		{}

		return sb.toString();
	}

	public static Object toObject(String objs) throws Exception{
		Object obj = null;

		if(objs == null || objs.equals("")) return null;

		String object_name = null;

		String[] sp = objs.split("\n");

		String[] ss = null;

		String key = null;

		String value = null;

		Var[] vars = new Var[sp.length-1];

		int i=0;

		for(String s : sp){
			ss = s.split(":");
			if(ss.length > 2) continue;
			key = ss[0];
			value = ss[1];
			if(key.equals("object_name"))
			{
				object_name = value;
			}else {
				Var v = new Var();

				v.setName(key);

				String[]  values = value.split(",");

				String type = values[0];

				String res = values[1];

				if(type.equals(int.class.getName()))
					v.setValue(Integer.parseInt(res));
				else
				if(type.equals(float.class.getName()))
					v.setValue(Float.parseFloat(res));
				else
				if(type.equals(boolean.class.getName()))
					v.setValue(Boolean.parseBoolean(res));
				else
				if(type.equals(String.class.getName()))
					v.setValue(res);
				else
				if(type.equals(CharSequence.class.getName()))
					v.setValue((CharSequence)res);

				vars[i] = v;

				i++;
			}

		}

		Class claz = Class.forName(object_name);

		if(claz == null) return null;

		obj = claz.newInstance();

		if(obj == null) return null;

		for(Var vs : vars)
		{
			Field field = claz.getField(vs.getName());

			if(field == null) continue;

			field.set(obj, vs.getValue());
		}

		return obj;
	}


	private static class Var
	{
		public String name;

		public Object value;

		public void setValue(Object value)
		{
			this.value = value;
		}

		public Object getValue()
		{
			return value;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public String getName()
		{
			return name;
		}
	}

}
