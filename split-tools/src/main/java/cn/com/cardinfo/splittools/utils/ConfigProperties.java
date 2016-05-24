package cn.com.cardinfo.splittools.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.com.cardinfo.repo.UrmMinfRepo;
/**
 * 
 * @author zale
 *
 */
public class ConfigProperties {
	private static Logger logger=Logger.getLogger(ConfigProperties.class);
	private static ConfigProperties configProperties;
	private static Properties properties;
	private static String propertiesPath = "./split.properties";
	private static Map<String,Byte> merIds = new HashMap<String,Byte>();
	private ConfigProperties(){
		ApplicationContext  context = new ClassPathXmlApplicationContext("classpath:spring-mybatis.xml");
		UrmMinfRepo repo = context.getBean(UrmMinfRepo.class);
		List<String> ids = repo.findAllMerId();
		for(String id:ids){
			merIds.put(id, (byte)1);
		}
		
	}
	public static boolean isMerExist(String id){
		return merIds.containsKey(id);
	}
	public static ConfigProperties getInstance(){
		synchronized (ConfigProperties.class) {
			if(configProperties==null){
				configProperties=new ConfigProperties();
				initProperties();
			}
		}
		return configProperties;
	}
	
	public String getConfig(String key){
		return properties.getProperty(key);
	}
	
	public int getConfigAsInteger(String key){
		return Integer.parseInt(properties.getProperty(key));
	}
	
	/**
	 * 得到以endWord结尾的属性集合
	 * @param endWord
	 * @return
	 */
	public HashMap<String, String> getConfigMatchEndWord(String endWord) {
		Enumeration<?> keys = properties.propertyNames();
		HashMap<String, String> resultConfigMap = new HashMap<String, String>();
		while(keys.hasMoreElements()){
			String key = keys.nextElement().toString();
			if(key.endsWith(endWord)) {
				resultConfigMap.put(key, getConfig(key));
			}
		}
		return resultConfigMap;
	}
	
	private static void initProperties(){
		properties=new Properties();

		InputStream inputStream=null;
		try{
			inputStream= new FileInputStream(propertiesPath);
			properties.load(inputStream);
		}catch (IOException e) {
			logger.error("",e);
		}finally{
			try {
				inputStream.close();
			} catch (Exception e) {
				logger.error("",e);
			}
		}
	}
}
