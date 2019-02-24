package ljj.spider.simple;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class LoadImage implements Runnable{

	Film film;
	
	public LoadImage(Film f) {
		this.film = f;
	}
	
	@Override
	public void run() {

		File path = new File("pic");
		if (!path.exists())
			path.mkdir();
		String name = String.format("%03d_%s.jpg", film.getId(),film.getTitle().split(" ")[0]);
		
		try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(path,name)))){
			byte[]  data = new OkHttpClient().newBuilder()
				.connectTimeout(1,TimeUnit.MINUTES)
				.readTimeout(10,TimeUnit.SECONDS)
				.build()//创建客户端
				.newCall(new Request.Builder().url(film.getGraph()).get().build())
				.execute()
				.body()
				.bytes();
			out.write(data);
			out.close();
			
		} catch (IOException e) {
//			e.printStackTrace();
		}
	}

}
