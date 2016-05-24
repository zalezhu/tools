package cn.com.cardinfo.splittools.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.com.cardinfo.splittools.entity.FileResultOutput;
import cn.com.cardinfo.splittools.utils.ConfigProperties;
/**
 * 
 * @author zale
 *
 */
public class INFBFileHandler extends AbstractFileHandler {
	private final File file;
	private final FileResultOutput result;
	public INFBFileHandler(File file,FileResultOutput result) {
		this.file = file;
		this.result = result;
	}

	@Override
	public File getFile() {
		return file;
	}

	@Override
	public Object exec() throws Exception {
		if (getFile().isFile() && getFile().exists()) { 
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"));
			String newline = "";
			int newCount = 0;
			int oldCount = 0;
			while ((newline = br.readLine()) != null && !newline.equals("")) {

				byte[] filedetail = newline.getBytes("GBK");
				int Alllength = (filedetail.length - 46 - 49) / 526;
				List<byte[]> newTmp = new ArrayList<byte[]>();
				newTmp.add(Arrays.copyOfRange(filedetail, 0, 46));
				List<byte[]> oldTmp = new ArrayList<byte[]>();
				oldTmp.add(Arrays.copyOfRange(filedetail, 0, 46));
				for (int i = 0; i < Alllength; i++) {
					int stage = 46 + i * 526;
					byte[] currRecord = Arrays.copyOfRange(filedetail, stage, 46 + (i+1)* 526);
					// 商户号
					byte[] merchNo = new byte[15];
					int merno = 0;
					for (int x = stage + 113; x < stage + 113 + 15; x++) {
						merchNo[merno] = filedetail[x];
						merno = merno + 1;
					}
					String merchNoStr = new String(merchNo, "GBK");
					if(ConfigProperties.isMerExist(merchNoStr)){
						newTmp.add(currRecord);
						getLogger().info("新系统INFB流水-"+merchNoStr+":"+new String(currRecord, "GBK"));
						newCount++;
					}else{
						oldCount++;
						oldTmp.add(currRecord);
					}
				}
				newTmp.add(Arrays.copyOfRange(filedetail, filedetail.length-49, filedetail.length));
				oldTmp.add(Arrays.copyOfRange(filedetail, filedetail.length-49, filedetail.length));
				String newLine = "";
				if(newTmp.size()==2){
					newTmp =  new ArrayList<byte[]>();
				}
				for(byte[] newByte:newTmp){
					newLine += new String(newByte, "GBK");
				}
				if(newLine!=""){
					result.getNewResult().offer(newLine);
				}
				String oldLine = "";
				if(oldTmp.size()==2){
					oldTmp =  new ArrayList<byte[]>();
				}
				for(byte[] oldByte:oldTmp){
					oldLine += new String(oldByte, "GBK");
				}
				if(oldLine!=""){
					result.getOldResult().offer(oldLine);
				}
			}
			getLogger().info("文件"+getFile().getName()+"拆分结果--新:"+newCount+"---旧:"+oldCount);
			
			br.close();
		return true;
		}
		return false;
	}



	@Override
	public FileResultOutput getResult() {
		return result;
	}


}
