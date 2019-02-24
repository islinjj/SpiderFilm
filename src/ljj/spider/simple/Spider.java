package ljj.spider.simple;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 解析电影
 * 
 * @author thinkpad_ljj
 *
 */
public class Spider implements Runnable {

	String url;
	//同一个列表方便转成XML格式
	List<Film> films;
	
	FilmList filmList;
	
	Film film ;
	
	public Spider(String url,List<Film> films,FilmList filmList) {
		super();
		this.url = url;
		this.films = films;
		this.filmList = filmList;
	}

	@Override
	public void run() {

		/**
		 * 抓取电影并存入数据库
		 */
		try {

			// 要获取250部电影
			// 获得电影排行榜的文档
			Document doc = Jsoup.connect(url).get();
			
			// 定位每部电影的位置
			Elements element = new Elements(doc.select(".grid_view .item"));
			for (Element e : element) {
				film = new Film();
				
				String title = e.select(".title").text();
				String info = e.select(".bd p").first().text();
				String src = e.select("a").attr("href");
				String rating = e.select(".rating_num").text();
				String id = e.select("em").text();
				String graph = e.select(".pic img").attr("src");

				film.setTitle(title);
				film.setInfo(info);
				film.setRating(Double.parseDouble(rating));
				film.setSrc(src);
				film.setId(Integer.parseInt(id));
				film.setGraph(graph);

				//在控制台输出所有存入数据库的数据
//				System.out.println("list");
//				List<Film> list = mapper.list();
//				for (Film f : list) {
//					System.out.println(f);
//				}
				
				//加载图片使用
				films.add(film);
				//写XML使用
				filmList.add(film);
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

}
