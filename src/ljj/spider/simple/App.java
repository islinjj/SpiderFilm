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
		 * ʹ���̻߳�ȡ250����Ӱ
		 */
		String url = "https://movie.douban.com/top250";
		ExecutorService pool = Executors.newFixedThreadPool(4);

		pool.execute(new Spider(url, films, filmList));
		// һ����10ҳ��һҳ25����Ӱ
		for (int i = 1; i < 10; i++) {
			url = String.format("https://movie.douban.com/top250?start=%d&filter=", 25 * i);
			pool.execute(new Spider(url, films, filmList));
		}
		pool.shutdown();

		while (true) {
			if (pool.isTerminated()) {

				// ת����XML��ʽ
				toXML(filmList);

				toSQL(films);
				/*
				 * ����ͼƬ
				 */
				// ����һ���߳̽������ص�Ӱʱ��films���в��е�Ӱ��������ŵ�ѭ���⣬break֮��Pool���ܻ��ٴ���������ʱ��pool2Ҳ�ڹ��������Ҳ���·����
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

		// �������ݿ������ļ�
		SqlSessionFactory factory;
		try {
			factory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("config.xml"));
			
			for (Film f : films) {
				// ���ݿ�����ӣ��ӳ��л�õ�һ�����õ�����TCP�׽��֣�
				SqlSession sqlSession = factory.openSession();

				// ���һ��Mapper:ͨ����̬������ cglib�����һ���ӿڵġ�ʵ�֡�
				FilmMapper mapper = sqlSession.getMapper(FilmMapper.class);
				
				//�������ݿ�
				mapper.save(f);
				
				// �ύ�����롢���¡�ɾ�����������ύ����Ч����ѯ��������Ҫ�ύ��
				sqlSession.commit();
				// �رջỰ���������ӳ��У����գ������ٴ�ʹ�ã�������Ĺرգ�
				sqlSession.close();
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * ת����XML
	 * 
	 * @param filmList
	 */
	public static void toXML(FilmList filmList) {
		Persister persister = new Persister();
		try {
			// ��list��ArrayList����ʱ��������ȷ�����Ҳ�����ں�����ArrayList�����޸ģ����Դ���
			// һ����BookListί��ArrayList��Ȼ����ӵ��б�
			persister.write(filmList, new File("film.xml"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
