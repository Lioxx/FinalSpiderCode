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
 * ����
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
	 * ִ��
	 */
	public void run() 
	{
		//ץȡ����
		try 
		{
			//����������ַspider
			Document doc = Jsoup.connect(url).get();
			
			Elements items = doc.select(".grid_view .item");
			
			//����
			for(Element item : items)
			{
				
				Film film = new Film();
				
				String id = item.select(".pic em").first().text();//ɸѡ����
				String title = item.select(".title").get(0).text();//ɸѡƬ��
				String poster = item.select("img").get(0).attr("src");//ɸѡ����
				String info  = item.select(".info .bd p").text();
				String a[] = info.split("/");
				String rating = item.select(".star span").last().text();//ɸѡ��������
				String star = item.select(".rating_num").first().text();//ɸѡ����
				String quote = item.select(".quote").text();			//ɸѡ��Ҫ
				
				film.setId(Integer.parseInt(id));                         //�������
				film.setTitle(title);                                     //���Ƭ��          
				film.setPoster(poster);                                   //��ú���
				film.setRating(Integer.parseInt(rating.substring(0, rating.length()-3))); //�����������
				film.setStar(Double.parseDouble(star));                   //�������
				film.setCountry(a[a.length-2]);                           //��ù���
				film.setQuote(quote);        							  //��ø�Ҫ

				//����ÿ����Ӱ����ַ
				String preaddr = item.select("a").first().attr("href");//��item���ҳ�����ÿ����Ӱ����ַ
				Document newdoc=null;
				try{
					newdoc = Jsoup.connect(preaddr).get();//����������ַ
				}catch(HttpStatusException e) {
					continue;
				}
				String year = newdoc.select("h1 .year").first().text();//������ַ���ҵ�year�������ţ�
				film.setYear(Integer.parseInt(year.substring(1,5))); //ȥ�����Ų�ת��Ϊ����
				String director = newdoc.select("#info .attrs").get(0).select("a").text();//������ַ���ҵ�����
				film.setDirector(director);
				String mainactor = newdoc.select("#info .actor .attrs a").text();//������ַ���ҵ�����
				film.setMainActor(mainactor);
				
				System.out.println(film);
				filmList.add(film);
				
				
				//���غ���
				File path = new File("pic");           //���ɴ洢ͼƬ���ļ�
				if (!path.exists())
					path.mkdir();                      //������ļ����ڣ���ɾ��
				String name = String.format("%03d_%s.jpg", film.getId(), film.getTitle().split(" ")[0]);  //��id��������ΪͼƬ����
				try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(path, name)));) {

					byte[] data = new OkHttpClient.Builder()
							.connectTimeout(60, TimeUnit.SECONDS)
							.readTimeout(60, TimeUnit.SECONDS)
							.writeTimeout(60, TimeUnit.SECONDS)
							.build().newCall(new Request.Builder().url(film.getPoster()).build()).execute().body().bytes();  

					bos.write(data);           
					bos.close();				
					System.out.println(Thread.currentThread().getName() + " �ѳɹ�����ͼƬ�� " + name);
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
	
			//�������ݿⴢ����Ϣ
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
		System.out.println("����д��ɹ�");
	}
		
}