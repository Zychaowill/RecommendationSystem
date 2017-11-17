package com.jangz.recommendation.util;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MybatisSessionFactory {

	private static SqlSessionFactory factory = null;
	private static String fileName = "src/resources/mybatis.xml";

	public MybatisSessionFactory() {
	}
	
	public static void initMapper(String sqlMapperFileName) {
		fileName = sqlMapperFileName;
	}
	
	public static SqlSessionFactory getFactory() {
		try {
			Reader reader = Resources.getResourceAsReader(fileName);
			SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
			factory = builder.build(reader);
			builder = null;
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return factory;
	}
	
	public static void commitSession(SqlSession session) {
		if (session != null) {
			session.commit();
		}
	}
	
	public static void closeSession(SqlSession session) {
		if (session != null) {
			session.close();
		}
	}
}
