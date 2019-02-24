package ljj.spider.simple;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface FilmMapper {

	/**
	 * 往数据库中添加数据
	 * @param p
	 */
	//#代表会去调用get()方法
	@Insert("insert into film(info,title,src,graph) values(#{info},#{title},#{src},#{graph})")
	void save(Film f);
	
	/**
	 * 从数据库查询一条记录
	 * @param id
	 */
	//id的别名n
//	@Select("select * from person where id=#{n}")
//	Film load(@Param("n")int id);
	
	/**
	 * 查询出所有（以后应该是部分，通过分页）的记录
	 * @return
	 */
	@Select("select * from film")
	List<Film> list();
}
