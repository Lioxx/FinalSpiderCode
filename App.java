package edu.csuft.liuyi.spider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App 
{
	public static void main(String[] args) 
	{	        
		
		ExecutorService executorService=Executors.newFixedThreadPool(5);//�����̳߳�
	
        for(int i = 0; i < 10; i++) 
		{
			executorService.execute(new MyThread("https://movie.douban.com/top250?start="+25*i+"&filter="));
        }
        
        executorService.shutdown();//ֹͣ�߳�
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
		s.LinkSql();//�����������ݿ�ķ������洢��Ϣ
	}

}
