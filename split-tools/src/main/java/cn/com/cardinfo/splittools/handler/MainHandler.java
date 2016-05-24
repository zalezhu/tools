package cn.com.cardinfo.splittools.handler;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import cn.com.cardinfo.splittools.utils.ConfigProperties;
/**
 * 
 * @author zale
 *
 */
public class MainHandler implements Handler {
	private static Logger logger = Logger.getLogger(MainHandler.class);
	@Override
	public Object handle() throws Exception {
		ConfigProperties prop = ConfigProperties.getInstance();
		String fileLoc = prop.getConfig("split.file.location");
		String fileNewLoc = prop.getConfig("split.file.new.location");
		String fileOldLoc = prop.getConfig("split.file.old.location");
		String indexVal = prop.getConfig("split.index");
		String fileVal = prop.getConfig("split.fileName");
		String lenArrayVal = prop.getConfig("split.field.length");
		
		String[] indexArray = indexVal.split("\\|");
		String[] fileNameArray = fileVal.split("\\|");
		String[] lenArrayArray = lenArrayVal.split("\\|");
		if(indexArray.length!=lenArrayArray.length){
			logger.error("index或field length参数配置错误!");
			return null;
		}
		File dir = new File(fileLoc);
		FileHandlerDispatch dispatch = new FileHandlerDispatch();
		int count = 0;
		for(final String fileRegex:fileNameArray){
			File[] files = dir.listFiles(new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String name) {
					if(name.matches(fileRegex)){
						return true;
					}
					return false;
				}
			});
			if(files.length==0){
				logger.error("文件不存在，或者文件正则表达示有误");
				continue;
			}
//			if(files.length>1){
//				logger.error("文件正则表达示有误");
//				return null;
//			}
			int index = count>=indexArray.length?0:Integer.parseInt(indexArray[count]);
			String lenArrayStr = count>=lenArrayArray.length?null:lenArrayArray[count];
			int[] lenArray  =null;
			if(lenArrayStr!=null){
				String[] lenArraytmp = lenArrayStr.split(",");
				lenArray = new int[lenArraytmp.length];
				for(int i=0;i<lenArraytmp.length;i++){
					lenArray[i] = Integer.parseInt(lenArraytmp[i].trim());
				}
			}
			count++;
			for(File waitFile:files){
				AbstractFileHandler fileHandler = dispatch.dispatch(fileRegex,index , lenArray, waitFile,fileNewLoc,fileOldLoc);
				fileHandler.handle();
			}
			
			
			
 		}
		return null;
	}

}
