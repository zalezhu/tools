package cn.com.cardinfo.splittools.handler;

import java.io.File;
import java.util.List;

import cn.com.cardinfo.splittools.entity.FileResultOutput;
/**
 * 
 * @author zale
 *
 */
public class FileHandlerDispatch{
	
	public AbstractFileHandler dispatch(String fileName,int index,int[]lenArray,File file,String fileNewLoc,String fileOldLoc){
		AbstractFileHandler fileHandler = null;
		FileResultOutput result =  new FileResultOutput();
		result.setNewPath(fileNewLoc);
		result.setOldPath(fileOldLoc);
		result.setFileName(file.getName());
		if("INF\\d{6}..B".equals(fileName)){
			fileHandler = new INFBFileHandler(file,result);
		}else{
			fileHandler = new SimpleFileSplitHandler(index, lenArray, file,result);
		}
		return fileHandler;
	}
	


}
