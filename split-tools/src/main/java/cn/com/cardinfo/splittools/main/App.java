package cn.com.cardinfo.splittools.main;

import org.apache.log4j.Logger;

import cn.com.cardinfo.splittools.handler.MainHandler;
/**
 * 
 * @author zale
 *
 */
public class App {
	private static Logger logger = Logger.getLogger(App.class);
	public static void main(String[] args) {
		MainHandler mainHandler = new MainHandler();
		try {
			mainHandler.handle();
		} catch (Exception e) {
			logger.error("分拆分件出错", e);
		}
//		System.out.println((1500000/1024)/1024);
		
	}
}
