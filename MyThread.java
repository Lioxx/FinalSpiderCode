package edu.csuft.liuyi.spider;
/**
 * �߳��ࣺʮ���߳�ͬʱ����
 * @author Administrator
 *
 */
public class MyThread implements Runnable {
	String s;
	public  MyThread(String s) {
		this.s = s;
	}
	@Override
	public void run() {
		Spider spider = new Spider(s);
		spider.run();
	}

}
