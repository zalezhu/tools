package cn.com.cardinfo.splittools.entity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;
/**
 * 文件处理结果类
 * @author zale
 *
 */
public class FileResultOutput {
	private static Logger logger = Logger.getLogger(FileResultOutput.class);
	
	private Queue<String> oldResult; //旧系统结果
	private Queue<String> newResult; //新系统结果
	private AtomicBoolean end=new AtomicBoolean(false);
	
	private String newPath;
	private String oldPath;
	private String fileName;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getNewPath() {
		return newPath;
	}
	public void setNewPath(String newPath) {
		this.newPath = newPath;
	}
	public String getOldPath() {
		return oldPath;
	}
	public void setOldPath(String oldPath) {
		this.oldPath = oldPath;
	}
	public Queue<String> getOldResult() {
		if(oldResult == null){
			oldResult = new ConcurrentLinkedQueue<String>();
		}
		return oldResult;
	}
	public void setOldResult(Queue<String> oldResult) {
		this.oldResult = oldResult;
	}
	public Queue<String> getNewResult() {
		if(newResult == null){
			newResult = new ConcurrentLinkedQueue<String>();
		}
		return newResult;
	}
	public void setNewResult(Queue<String> newResult) {
		this.newResult = newResult;
	}
	
	public boolean getEnd() {
		return end.get();
	}
	public void setEnd(boolean end) {
		this.end.set(end);
	}
	public void startOutPut(){
		end.set(false);
		File oldDir = new File(oldPath);
		if(!oldDir.exists()){
			oldDir.mkdirs();
		}
		File newDir = new File(newPath);
		if(!newDir.exists()){
			newDir.mkdirs();
		}
		File oldOut = new File(oldPath+File.separator+fileName);
		File newOut = new File(newPath+File.separator+fileName);
		new Thread(new OutputRunable(oldOut,getOldResult())).start();
		new Thread(new OutputRunable(newOut,getNewResult())).start();
	}
	
	
	class OutputRunable implements Runnable{
		private File output;
		private Queue<String> queue;
		private int count;
		public OutputRunable(File output,Queue<String> queue) {
			this.output = output;
			this.queue = queue;
		}

		@Override
		public void run() {
			count =0;
			BufferedWriter writer = null;
			try {
				writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output)));
				while(!end.get()||queue.peek()!=null){
					String qOutput = null;
					while((qOutput = queue.poll())!=null){
						if(count!=0){
							writer.newLine();
						}
						writer.write(qOutput);
						count++;
					}
					writer.flush();
					if(!end.get()){
						Thread.sleep(1000);
					}
				}
				
			}catch (IOException e) {
				logger.error(e.getMessage(), e);
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}finally{
				if(writer!=null){
					try {
						writer.close();
					} catch (IOException e) {
						logger.error(e.getMessage(), e);
					}
				}
			}
		
		}
		
	}
}
