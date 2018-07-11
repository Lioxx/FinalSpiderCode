package edu.csuft.liuyi.spider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App 
{
	public static void main(String[] args) 
	{	        
		
		ExecutorService executorService=Executors.newFixedThreadPool(5);//定义线程池
	
        for(int i = 0; i < 10; i++) 
		{
			executorService.execute(new MyThread("https://movie.douban.com/top250?start="+25*i+"&filter="));
        }
        
        executorService.shutdown();//停止线程
        while(true) {
        	if(executorService.isTerminated()) {
        		break;	
        	}
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
        
        Spider s = new Spider();
		s.LinkSql();//调用连接数据库的方法，存储信息
	}

}
