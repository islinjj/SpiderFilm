package ljj.spider.simple;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.simpleframework.xml.core.Persister;

public class App {

	public static void main(String[] args) {

		List<Film> films = new ArrayList<>();

		FilmList filmList = new FilmList();

		/**
		 * 使用线程获取250部电影
		 */
		String url = "https://movie.douban.com/top250";
		ExecutorService pool = Executors.newFixedThreadPool(4);

		pool.execute(new Spider(url, films, filmList));
		// 一共有10页，一页25部电影
		for (int i = 1; i < 10; i++) {
			url = String.format("https://movie.douban.com/top250?start=%d&filter=", 25 * i);
			pool.execute(new Spider(url, films, filmList));
		}
		pool.shutdown();

		while (true) {
			if (pool.isTerminated()) {

				// 转换成XML格式
				toXML(filmList);

				toSQL(films);
				/*
				 * 加载图片
				 */
				// 当第一个线程结束加载电影时，films当中才有电影，，如果放到循环外，break之后Pool可能会再次启动，此时的pool2也在工作，就找不到路径。
				ExecutorService pool2 = Executors.newFixedThreadPool(4);
				for (Film f : films) {
					pool2.execute(new LoadImage(f));
				}
				pool2.shutdown();

				break;
			} else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		/**
		 * XML-->file
		 */
//		try {
//			FilmList filmList = new Persister().read(FilmList.class, new File("film.xml"));
//			
//			for (Film f : filmList.filmList) {
//				System.out.println(f);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	private static void toSQL(List<Film> films) {

		// 加载数据库配置文件
		SqlSessionFactory factory;
		try {
			factory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("config.xml"));
			
			for (Film f : films) {
				// 数据库打开连接（从池中获得的一个可用的连接TCP套接字）
				SqlSession sqlSession = factory.openSession();

				// 获得一个Mapper:通过动态代理（或 cglib）获得一个接口的【实现】
				FilmMapper mapper = sqlSession.getMapper(FilmMapper.class);
				
				//存入数据库
				mapper.save(f);
				
				// 提交（插入、更新、删除操作必须提交才生效，查询操作不需要提交）
				sqlSession.commit();
				// 关闭会话（放入连接池中，回收，可用再次使用，不是真的关闭）
				sqlSession.close();
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 转换成XML
	 * 
	 * @param filmList
	 */
	public static void toXML(FilmList filmList) {
		Persister persister = new Persister();
		try {
			// 当list是ArrayList类型时，不能正确输出，也不能在核心类ArrayList当中修改，所以创建
			// 一个类BookList委托ArrayList，然后添加到列表
			persister.write(filmList, new File("film.xml"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
