package io.github.skycat.geekstudy.java.week01;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassLoaderTest
 * @author Skycat
 * @email skycat2004@gmail.com
 * @version 1.0 2020-10-19 00:25:39
 */
public class ClassLoaderTest extends ClassLoader {
	/**
	 * main
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Class<?> clazz = new ClassLoaderTest().findClass("Hello");
			Object obj = clazz.newInstance();
			Method method = clazz.getMethod("hello");
			if (null == method) {
				throw new IllegalArgumentException("Method hello not found in class Hello");
			}
			method.invoke(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		try (InputStream stream = this.getClass().getClassLoader().getResourceAsStream(name + ".xlass")) {
			List<Byte> list = new ArrayList<Byte>();
			int read = stream.read();
			while (-1 != read) {
				read = 255 - read;
				list.add((byte) read);
				read = stream.read();
			}
			int i = 0;
			byte[] bytes = new byte[list.size()];
			for (byte b : list) {
				bytes[i++] = b;
			}
			return defineClass(name, bytes, 0, list.size());
		} catch (Exception e) {
			throw new IllegalArgumentException("cant find the class '" + name + "'");
		}
	}
}
