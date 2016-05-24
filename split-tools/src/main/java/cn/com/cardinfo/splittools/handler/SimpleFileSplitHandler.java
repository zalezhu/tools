/**
 * 
 */
package cn.com.cardinfo.splittools.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import cn.com.cardinfo.splittools.entity.FileResultOutput;
import cn.com.cardinfo.splittools.utils.ConfigProperties;

/**
 * @author zale
 *
 */
public class SimpleFileSplitHandler extends AbstractFileHandler{
	private final int index;
	private final int[] lenArray;
	private final File file;
	private final FileResultOutput result;
	
	public SimpleFileSplitHandler(int index, int[] lenArray, File file,FileResultOutput result) {
		this.index = index;
		this.lenArray = lenArray;
		this.file = file;
		this.result = result;
	}
	@Override
	public Object exec() throws Exception {
		if (getFile().isFile() && getFile().exists()) { 
			InputStreamReader read = new InputStreamReader(new FileInputStream(getFile()), "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null;
			int newCount = 0;
			int oldCount = 0;
			while ((lineTxt = bufferedReader.readLine()) != null) {
				int beginIndex = 0;
				int endIndex = 0;
				for (int i = 0; i < getLenArray().length; i++) {
					endIndex = beginIndex + getLenArray()[i];
					if(i==getIndex()){
						String val = lineTxt.substring(beginIndex, endIndex).trim();
						if(ConfigProperties.isMerExist(val)){
							result.getNewResult().offer(lineTxt);
							getLogger().info("新系统"+getFile().getName()+"流水-"+val+":"+lineTxt);
							newCount++;
						}else{
							oldCount++;
							result.getOldResult().offer(lineTxt);
						}							
							break;
					}
					beginIndex = endIndex + 1;
				}
			}
			read.close();
			getLogger().info("文件"+getFile().getName()+"拆分结果--新:"+newCount+"---旧:"+oldCount);
			return true;
		}
		
		return false;
	}	

	public int getIndex() {
		return index;
	}

	public int[] getLenArray() {
		return lenArray;
	}

	public File getFile() {
		return file;
	}
	@Override
	public FileResultOutput getResult() {
		return result;
	}


	
}
