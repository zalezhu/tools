package cn.com.cardinfo.forward.server;

import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.cardinfo.forward.channel.Channel;
import cn.com.cardinfo.forward.event.Event;
import cn.com.cardinfo.forward.event.EventSource;
import cn.com.cardinfo.forward.event.EventSubscriber;
import cn.com.cardinfo.forward.event.EventType;
import cn.com.cardinfo.forward.session.AioTcpClientSession;
import cn.com.cardinfo.forward.util.HsmUtil;
/**
 * 
 * @author Zale
 *
 */
public class MsgConsumer extends EventSource implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(MsgConsumer.class);
	private Queue<byte[]> msgQueue;
	private String id;
	private AtomicBoolean closed;
	private Channel channel;
//	private byte[] outBytes = HsmUtil.hexStringToByte("303230372E0232303720343834313030303020202030303031303030302020203030303030303030303030303B303030303030323130F03A00810AC080100000000000000040303030303030303030303030303030303032353835333939383632313236303730353235303532363030303834383431353531303135303030303937373636353030303130303031313138303135353130353732323030303431353630323330303030303530303033303030303030303030303031313032303030303030303036313530303030393737363635");
	public static void main(String[] args) {
		System.out.println(Arrays.toString(HsmUtil.hexStringToByte("303230372E0232303720343834313030303020202030303031303030302020203030303030303030303030303B303030303030323130F03A00810AC080100000000000000040303030303030303030303030303030303032353835333939383632313236303730353235303532363030303834383431353531303135303030303937373636353030303130303031313138303135353130353732323030303431353630323330303030303530303033303030303030303030303031313032303030303030303036313530303030393737363635")));
	}

	public boolean isClosed() {
		return closed.get();
	}

	public void setClosed(boolean closed) {
		this.closed.set(closed);
	}

	public void closed() {
		this.setClosed(true);
	}

	public MsgConsumer(String id, Queue<byte[]> msgQueue,Channel channel) {
		this.id = id;
		this.msgQueue = msgQueue;
		this.channel = channel;
		initDefaults();
	}

	private void initDefaults() {
		if (closed == null) {
			closed = new AtomicBoolean(false);
		}
	}
	
	private byte[] processMsg(byte[] bytes, int length) {
		byte[] dataBytes = new byte[length];
		byte[] remainBytes = null;
		boolean output = true;
		if (bytes.length == length) {
			System.arraycopy(bytes, 0, dataBytes, 0, bytes.length);
		} else if (bytes.length > length) {
			System.arraycopy(bytes, 0, dataBytes, 0, dataBytes.length);
			remainBytes = Arrays.copyOfRange(bytes, dataBytes.length, bytes.length);
		} else {
			dataBytes = new byte[bytes.length];
			System.arraycopy(bytes, 0, dataBytes, 0, bytes.length);
			while (true) {
				bytes = msgQueue.poll();
				if (bytes != null) {
					// logger.debug("consumer2--->"+Arrays.toString(bytes));
					break;
				}
				
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			byte[] temp = new byte[dataBytes.length + bytes.length];
			System.arraycopy(dataBytes, 0, temp, 0, dataBytes.length);
			System.arraycopy(bytes, 0, temp, dataBytes.length, bytes.length);
			remainBytes = processMsg(temp, length);
			output = false;
		}
		if (output) {
			if (dataBytes != null&&dataBytes.length>200) {
				byte[] outBytes = new byte[]{48, 50, 48, 55, 46, 2, 50, 48, 55, 32, 52, 56, 52, 49, 48, 48, 48, 48, 32, 32, 32, 48, 48, 48, 49, 48, 48, 48, 48, 32, 32, 32, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 59, 48, 48, 48, 48, 48, 48, 50, 49, 48, -16, 58, 0, -127, 10, -64, -128, 16, 0, 0, 0, 0, 0, 0, 0, 64, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 50, 53, 56, 53, 51, 57, 57, 56, 54, 50, 49, 50, 54, 48, 55, 48, 53, 50, 53, 48, 53, 50, 54, 48, 48, 48, 56, 52, 56, 52, 49, 53, 53, 49, 48, 49, 53, 48, 48, 48, 48, 57, 55, 55, 54, 54, 53, 48, 48, 48, 49, 48, 48, 48, 49, 49, 49, 56, 48, 49, 53, 53, 49, 48, 53, 55, 50, 50, 48, 48, 48, 52, 49, 53, 54, 48, 50, 51, 48, 48, 48, 48, 48, 53, 48, 48, 48, 51, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 49, 49, 48, 50, 48, 48, 48, 48, 48, 48, 48, 48, 54, 49, 53, 48, 48, 48, 48, 57, 55, 55, 54, 54, 53};
				outBytes[90]=dataBytes[100];
				outBytes[91]=dataBytes[101];
				outBytes[92]=dataBytes[102];
				outBytes[93]=dataBytes[103];
				outBytes[94]=dataBytes[104];
				outBytes[95]=dataBytes[105];
				outBytes[44]=dataBytes[44];
				push(channel.getClientSession(),outBytes);
			}
		}
		return remainBytes;
	}
	private void push(AioTcpClientSession to, byte[] bytes) {
		if (to!= null && !to.isSocketClosed()) {
			to.getQueue().offer(bytes);
		} else {
			logger.info("{} is closed,can not send data {}",to, HsmUtil.bytesToHexString(bytes));
		}
	}
	
	
	public void subscribeServerDisconnect(EventSubscriber subscriber,NotifyType type){
		this.subscribe(subscriber, EventType.server_disconnect, type);
	}
	@Override
	public void run() {
		while (!isClosed()) {
			byte[] remainBytes = null;
			while (!isClosed()) {
				try {
					byte[] qOutput = null;
					if(remainBytes!=null){
						if(remainBytes.length>= 4){
							qOutput = remainBytes;
							remainBytes = null;
						}else{
							qOutput = msgQueue.poll();
							if(qOutput!=null){
								byte[] temp = new byte[qOutput.length + remainBytes.length];
								System.arraycopy(remainBytes, 0, temp, 0, remainBytes.length);
								System.arraycopy(qOutput, 0, temp, remainBytes.length, qOutput.length);
								qOutput = temp;
								remainBytes = null;
							}
						}
					}else{
						qOutput = msgQueue.poll();
					}
					if (qOutput != null) {
						byte[] heads = Arrays.copyOfRange(qOutput, 0, 4);
						int bodyLength = Integer.parseInt(new String(heads).trim());
						remainBytes = processMsg(qOutput, bodyLength + 4);
						if (remainBytes != null && remainBytes.length > 0) {
							logger.debug(id + " remainBytes--->" + HsmUtil.bytesToHexString(remainBytes));
						}
					}
					if (qOutput == null) {
						Thread.sleep(1);
					}
				} catch (Exception e) {
					logger.error("process message have exception for " + id, e);

					remainBytes = null;
					channel.getServerSession().closeSocketClient();
					msgQueue.clear();
					publish(new Event(this,EventType.remote_client_disconnect));
				}

			}
		}
	}
}
