package edu.csuft.liuyi.spider;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import okhttp3.OkHttpClient;
import okhttp3.Request;
/**
 * 爬虫
 * 
 * @author Administrator
 *
 */

public class Spider 
{
	String url;
	
	public static List<Film> filmList = new ArrayList<>();
	
	public Spider(String url) 
	{
		this.url = url;
	}
	
	public Spider() {
	}
	
	/**
	 * 执行
	 */
	public void run() 
	{
		//抓取数据
		try 
		{
			//请求链接网址spider
			Document doc = Jsoup.connect(url).get();
			
			Elements items = doc.select(".grid_view .item");
			
			//遍历
			for(Element item : items)
			{
				
				Film film = new Film();
				
				String id = item.select(".pic em").first().text();//筛选排名
				String title = item.select(".title").get(0).text();//筛选片名
				String poster = item.select("img").get(0).attr("src");//筛选海报
				String info  = item.select(".info .bd p").text();
				String a[] = info.split("/");
				String rating = item.select(".star span").last().text();//筛选评价人数
				String star = item.select(".rating_num").first().text();//筛选评分
				String quote = item.select(".quote").text();			//筛选概要
				
				film.setId(Integer.parseInt(id));                         //获得排名
				film.setTitle(title);                                     //获得片名          
				film.setPoster(poster);                                   //获得海报
				film.setRating(Integer.parseInt(rating.substring(0, rating.length()-3))); //获得评价人数
				film.setStar(Double.parseDouble(star));                   //获得评分
				film.setCountry(a[a.length-2]);                           //获得国家
				film.setQuote(quote);        							  //获得概要

				//链接每部电影的网址
				String preaddr = item.select("a").first().attr("href");//从item中找出进入每部电影的网址
				Document newdoc=null;
				try{
					newdoc = Jsoup.connect(preaddr).get();//请求链接网址
				}catch(HttpStatusException e) {
					continue;
				}
				String year = newdoc.select("h1 .year").first().text();//从新网址中找到year（含括号）
				film.setYear(Integer.parseInt(year.substring(1,5))); //去除括号并转换为整型
				String director = newdoc.select("#info .attrs").get(0).select("a").text();//从新网址中找到导演
				film.setDirector(director);
				String mainactor = newdoc.select("#info .actor .attrs a").text();//从新网址中找到主演
				film.setMainActor(mainactor);
				
				System.out.println(film);
				filmList.add(film);
				
				
				//下载海报
				File path = new File("pic");           //生成存储图片的文件
				if (!path.exists())
					path.mkdir();                      //如果该文件存在，则删除
				String name = String.format("%03d_%s.jpg", film.getId(), film.getTitle().split(" ")[0]);  //以id和中文名为图片命名
				try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(path, name)));) {

					byte[] data = new OkHttpClient.Builder()
							.connectTimeout(60, TimeUnit.SECONDS)
							.readTimeout(60, TimeUnit.SECONDS)
							.writeTimeout(60, TimeUnit.SECONDS)
							.build().newCall(new Request.Builder().url(film.getPoster()).build()).execute().body().bytes();  

					bos.write(data);           
					bos.close();				
					System.out.println(Thread.currentThread().getName() + " 已成功加载图片： " + name);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
			}
			

		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
			//连接数据库储存信息
	public void LinkSql() 
	{
		SqlSessionFactory factory = null;
		try {
			factory = new SqlSessionFactoryBuilder().build(
					new FileReader("config.xml"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		SqlSession session  = factory.openSession();
		
		FilmMapper mapper = session.getMapper(FilmMapper.class);
		
		for(Film f : filmList) 
		{
			mapper.insert(f);
		}
		session.commit();
		session.close();
		System.out.println("数据写入成功");
	}
		
}