package cn.com.cardinfo.forward.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.cardinfo.forward.base.Hub;
import cn.com.cardinfo.forward.channel.Channel;
import cn.com.cardinfo.forward.util.ExecutorsPool;

public class AioTcpShortServer {
	private static Logger logger = LoggerFactory.getLogger(AioTcpShortServer.class);
	private AsynchronousChannelGroup asyncChannelGroup;
	private AsynchronousServerSocketChannel serverSocketChannel;
	private String id;
	private AtomicBoolean isStarted;
	private int port;
	private Map<String, AsynchronousSocketChannel> sockets;

	public boolean isAccept() {
		return serverSocketChannel.isOpen();
	}

	public AioTcpShortServer(int port, AsynchronousChannelGroup asyncChannelGroup, String id) {
		this.id = id;
		this.asyncChannelGroup = asyncChannelGroup;
		this.port = port;
		initDefault();

	}

	private void initDefault() {
		if (isStarted == null) {
			isStarted = new AtomicBoolean(false);
		}
		if (sockets == null) {
			sockets = new ConcurrentHashMap<String, AsynchronousSocketChannel>();
		}
		if (serverSocketChannel == null) {
			try {
				serverSocketChannel = AsynchronousServerSocketChannel.open(asyncChannelGroup)
						.bind(new InetSocketAddress(port), 100);
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.setIsStarted(true);
		}
	}

	public boolean getIsStarted() {
		return isStarted.get();
	}

	public void setIsStarted(boolean isStarted) {
		this.isStarted.set(isStarted);
	}

	public static void main(String[] args) {
		byte[] b = new byte[] { 48, 48, 49 };
		System.out.println(new String(b));
	}

	public void accept() {
		while(this.isStarted.get() && this.serverSocketChannel.isOpen()) {
			final Future<AsynchronousSocketChannel> future = serverSocketChannel.accept();
			try {
				
					final AsynchronousSocketChannel socket = future.get();
					
					ExecutorsPool.CACHED_EXECUTORS.execute(new Runnable() {
	
						@Override
						public void run() {
							read(socket);
						}
	
						private void read(final AsynchronousSocketChannel socket) {
							try {
								ByteBuffer buf = ByteBuffer.allocate(439);
								Future<Integer> readf = socket.read(buf);
								if (readf.get() > 0) {
									buf.flip();
									try {
										byte[] sendBytes = new byte[buf.limit()];
										buf.get(sendBytes, buf.position(), buf.limit());
										Channel toChannel = Hub.getInstance().getChannel();
	
										if (toChannel != null) {
											byte[] seqno = new byte[6];
											seqno[0] = sendBytes[100];
											seqno[1] = sendBytes[101];
											seqno[2] = sendBytes[102];
											seqno[3] = sendBytes[103];
											seqno[4] = sendBytes[104];
											seqno[5] = sendBytes[105];
											sockets.put(new String(seqno), socket);
											toChannel.getClientSession().getQueue().offer(sendBytes);
										}
										buf.clear();
									} catch (Exception e) {
										e.printStackTrace();
										logger.error(id + " received msg have exception", e);
									}
								} else if (readf.get() == -1) {
									buf = null;
								}
							} catch (InterruptedException e) {
								e.printStackTrace();
							} catch (ExecutionException e) {
								e.printStackTrace();
							} finally {
												
	
							}
						}
	
					});
			} catch (InterruptedException | ExecutionException e1) {
				e1.printStackTrace();

			}
		}
	}

	public void write(byte[] sendBytes) {
		if (sendBytes.length > 100) {
			byte[] seqno = new byte[6];
			seqno[0] = sendBytes[90];
			seqno[1] = sendBytes[91];
			seqno[2] = sendBytes[92];
			seqno[3] = sendBytes[93];
			seqno[4] = sendBytes[94];
			seqno[5] = sendBytes[95];
			AsynchronousSocketChannel socket = sockets.remove(new String(seqno));
			logger.debug("sockets size is {}  seq {}", sockets.size(),seqno);
			if (socket != null && socket.isOpen()) {
				socket.write(ByteBuffer.wrap(sendBytes));
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				logger.info("socket is {} sockets size {}",socket,sockets.size());
			}
		}
	}

}
