package ljj.spider.simple;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface FilmMapper {

	/**
	 * �����ݿ����������
	 * @param p
	 */
	//#�����ȥ����get()����
	@Insert("insert into film(info,title,src,graph) values(#{info},#{title},#{src},#{graph})")
	void save(Film f);
	
	/**
	 * �����ݿ��ѯһ����¼
	 * @param id
	 */
	//id�ı���n
//	@Select("select * from person where id=#{n}")
//	Film load(@Param("n")int id);
	
	/**
	 * ��ѯ�����У��Ժ�Ӧ���ǲ��֣�ͨ����ҳ���ļ�¼
	 * @return
	 */
	@Select("select * from film")
	List<Film> list();
}
