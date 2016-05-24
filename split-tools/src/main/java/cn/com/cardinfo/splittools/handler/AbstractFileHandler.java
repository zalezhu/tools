package cn.com.cardinfo.splittools.handler;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import cn.com.cardinfo.splittools.entity.FileResultOutput;
/**
 * 
 * @author zale
 *
 */
public abstract class AbstractFileHandler implements Handler{
	private  Logger logger = Logger.getLogger(AbstractFileHandler.class);
	
	abstract FileResultOutput getResult();
	abstract File getFile();
	public Logger getLogger(){
		return logger;
	}
	@Override
	public Object handle() throws Exception {
		getResult().startOutPut();
		Object isSucess =   exec();
		getResult().setEnd(true);
		return isSucess;
	}
	
	 abstract Object exec() throws Exception;
}