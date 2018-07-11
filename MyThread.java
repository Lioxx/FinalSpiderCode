package edu.csuft.liuyi.spider;
/**
 * 线程类：十个线程同时运行
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
