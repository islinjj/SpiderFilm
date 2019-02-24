package ljj.spider.simple;

import java.util.ArrayList;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="Movie-List")
public class FilmList {

	@ElementList(inline=true)
	ArrayList<Film> filmList;
	
	public FilmList() {
		filmList = new ArrayList<>();
	}
	
	public void add(Film film) {
		filmList.add(film);
	}
	
	@Override
	public String toString() {
		return "film:"+filmList;
	}
}
