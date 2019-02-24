package ljj.spider.simple;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
@Root
public class Film {

	//id
	@Attribute
	int id;
	
	// 电影名
	@org.simpleframework.xml.Element
	String title;
	
	//基本信息
	@org.simpleframework.xml.Element
	String info;
	
	// 评分
	@org.simpleframework.xml.Element
	double rating;
	
	//电影链接
	@org.simpleframework.xml.Element
	String src;
	
	//图片链接
	@org.simpleframework.xml.Element
	String graph;
	
	public Film() {
	}
	
	public String getGraph() {
		return graph;
	}

	public void setGraph(String graph) {
		this.graph = graph;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	@Override
	public String toString() {
		return "\nMovies id:"+ id + "\t title:" + title + "\t info:" + info + "\t rating:" + rating + "\t src:" + src + "\t graph:"
				+ graph;
	}
}
