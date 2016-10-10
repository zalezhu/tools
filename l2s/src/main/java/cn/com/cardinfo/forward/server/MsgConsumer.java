package cn.com.cardinfo.forward.server;

import cn.com.cardinfo.forward.base.Hub;
import cn.com.cardinfo.forward.channel.Channel;
import cn.com.cardinfo.forward.client.TcpShortClient;
import cn.com.cardinfo.forward.event.Event;
import cn.com.cardinfo.forward.event.EventSource;
import cn.com.cardinfo.forward.event.EventSubscriber;
import cn.com.cardinfo.forward.event.EventType;
import cn.com.cardinfo.forward.session.AioTcpClientSession;
import cn.com.cardinfo.forward.util.ExecutorsPool;
import cn.com.cardinfo.forward.util.HsmUtil;
import cn.com.cardinfo.forward.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Zale
 */
public class MsgConsumer extends EventSource implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(MsgConsumer.class);
    private Queue<byte[]> msgQueue;
    private String id;
    private AtomicBoolean closed;
    private Channel channel;

    public MsgConsumer(String id, Queue<byte[]> msgQueue, Channel channel) {
        this.id = id;
        this.msgQueue = msgQueue;
        this.channel = channel;
        initDefaults();
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
            if ("l2s".equals(PropertiesUtil.getHubMode())) {

                final byte[] fdataBytes = new byte[dataBytes.length - 2];
                byte[] head = Arrays.copyOfRange(dataBytes, 0, 4);
                int bodyLength = Integer.parseInt(new String(head).trim());
                byte[] posHead = HsmUtil.int2byte(bodyLength);
                if (posHead.length > 1) {
                    fdataBytes[0] = posHead[0];
                    fdataBytes[1] = posHead[1];
                } else if (posHead.length == 1) {
                    fdataBytes[0] = 0;
                    fdataBytes[1] = posHead[0];
                }
                System.arraycopy(dataBytes, 4, fdataBytes, 2, dataBytes.length - 4);
                ExecutorsPool.CACHED_EXECUTORS.execute(new Runnable() {
                    @Override
                    public void run() {
                        TcpShortClient client = Hub.getInstance().balanceGetUnionClient();
                        try {
                            Socket socket = new Socket(client.getIp(), client.getPort());
                            InputStream in = socket.getInputStream();
                            OutputStream out = socket.getOutputStream();
                            out.write(fdataBytes);
                            logger.debug("finished send data to union =>{}", HsmUtil.bytesToHexString(fdataBytes));
                            List<byte[]> bs = new ArrayList<>();
                            byte[] b = new byte[1024];
                            int total = 0;
                            logger.debug("wait union response");
                            int bl = in.read(b);
                            total += bl;
                            if (bl >= 2) {
                                //如果报文长度大于2取报文头算报文长度
                                byte[] backbyte = new byte[bl + 2];
                                byte[] lenByte = Arrays.copyOfRange(b, 0, 2);
                                String len = String.valueOf(HsmUtil.byte2int(lenByte));
                                byte[] len4 = len.getBytes();
                                if (len4.length == 4) {
                                    System.arraycopy(len4, 0, backbyte, 0, len4.length);
                                } else {
                                    int j = 3;
                                    for (int i = len4.length - 1; i >= 0; i--) {
                                        backbyte[j--] = len4[i];
                                    }
                                    for (; j >= 0; j--) {
                                        backbyte[j] = 48;
                                    }
                                }
                                //复制报文体到新的报文中从第4位开始
                                //前4位是头信息
                                System.arraycopy(b, 2, backbyte, 4, bl - 2);
                                bs.add(backbyte);
                                //报文体长度+2位的头小于等于总长度,说明接收完整 或者读到-1 则跳出循环
                                while (Integer.parseInt(len) + 2 > total && (bl = in.read(b)) != -1) {
                                    if (bl == -1) {
                                        break;
                                    }
                                    total += bl;
                                    logger.debug("receive data from union the length is {},total is {}", bl, total);
                                    if (bl < 1024) {
                                        byte[] nb = new byte[bl];
                                        System.arraycopy(b, 0, nb, 0, bl);
                                        bs.add(nb);
                                    } else {
                                        bs.add(b);
                                    }
                                }
                                logger.debug("start to process union response");
                                byte[] tb = new byte[total + 2];
                                int consor = 0;
                                for (byte[] bt : bs) {
                                    if (bt.length > 0) {
                                        logger.debug("total is {} consor is {} bt's length is {}", total, consor, bt.length);
                                        System.arraycopy(bt, 0, tb, consor, bt.length);
                                        consor += bt.length - 1;
                                    }
                                }

                                logger.debug("finished receive data {}  total is {}  list size is {},new bytes is {}",
                                        HsmUtil.bytesToHexString(tb), total, bs.size(), HsmUtil.bytesToHexString(backbyte));
                                push(channel.getClientSession(), backbyte);

                            } else {
                                logger.debug("finished receive data' total is {}  list size is {}", total, bs.size());
                            }

                            if (!socket.isClosed()) {
                                socket.close();
                            }
                        } catch (IOException e) {
                            logger.error("the  union socket has exception , So we do not send any data {}",
                                    HsmUtil.bytesToHexString(fdataBytes), e);
                        }
                    }
                });

            } else {
                if (channel.getType() == Channel.ChannelType.union) {
                    if (dataBytes.length >= 44) {
                        Channel toChannel = Hub.getInstance().getPospChannel(String.valueOf(dataBytes[44]));
                        if (toChannel != null) {
                            push(toChannel.getClientSession(), dataBytes);
                            logger.debug("signle send data from union-{} to posp-{} --> {}", id, toChannel.getId(),
                                    HsmUtil.bytesToHexString(dataBytes));
                        } else {
                            logger.info("the  channel {} is null , So we do not send any data {}", String.valueOf(dataBytes[44]));
                        }
                    } else {
                        for (Channel toChannel : Hub.getInstance().getPospChannels()) {
                            if (toChannel != null) {
                                push(toChannel.getClientSession(), dataBytes);
                                logger.debug("send data from union-{} to posp-{} --> {}", id, toChannel.getId(),
                                        HsmUtil.bytesToHexString(dataBytes));
                            } else {
                                logger.info("{} the  channel is null , So we do not send any data", id);
                            }
                        }
                    }
                } else {
                    Channel toChannel = Hub.getInstance().balanceGetUnionChannel();
                    if (toChannel != null) {
                        push(toChannel.getClientSession(), dataBytes);
                        logger.debug("send data from posp-{} to union-{}---> {}", id, toChannel.getId(),
                                HsmUtil.bytesToHexString(dataBytes));
                    } else {
                        logger.info("All of the  union channel is null , So we do not send any data to union");
                    }
                }
            }
        }
        return remainBytes;
    }

    private void push(AioTcpClientSession to, byte[] bytes) {
        long start = System.currentTimeMillis();
        if (to != null && !to.isSocketClosed()) {
            logger.debug("send msg to {} msg is {}", to.getId(), HsmUtil.bytesToHexString(bytes));
            to.getQueue().offer(bytes);
        } else {
            to.publishClientDisConnected();
            logger.info("{} is closed,can not send data {}", to.getId(), HsmUtil.bytesToHexString(bytes));
        }
        long end = System.currentTimeMillis();
        logger.info("Use Time (PUSH) {} ms msg queue size {} ---{}", (end - start), msgQueue.size(), id);
    }

    public void subscribeRemoteClientDisconnect(EventSubscriber subscriber, NotifyType type) {
        this.subscribe(subscriber, EventType.remote_client_disconnect, type);
    }

    @Override
    public void run() {
        byte[] remainBytes = null;
        while (!isClosed()) {
            try {
                byte[] qOutput = null;
                if (remainBytes != null) {
                    if (remainBytes.length >= 4) {
                        qOutput = remainBytes;
                        remainBytes = null;
                    } else {
                        qOutput = msgQueue.poll();
                        if (qOutput != null) {
                            byte[] temp = new byte[qOutput.length + remainBytes.length];
                            System.arraycopy(remainBytes, 0, temp, 0, remainBytes.length);
                            System.arraycopy(qOutput, 0, temp, remainBytes.length, qOutput.length);
                            qOutput = temp;
                            remainBytes = null;
                        }
                    }
                } else {
                    qOutput = msgQueue.poll();
                }
                if (qOutput != null) {
                    byte[] heads = Arrays.copyOfRange(qOutput, 0, 4);
                    int bodyLength = Integer.parseInt(new String(heads).trim());
                    remainBytes = processMsg(qOutput, bodyLength + 4);
                    logger.debug("msg queue size {} ---{}", msgQueue.size(), id);
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
                publish(new Event(this, EventType.remote_client_disconnect));
            }

        }
    }

    //	public static void main(String[] args) {
    //		byte[] b = new byte[2];
    //		b[0] = 0;
    //		b[1] = 0x3c;
    //		byte[] backbyte = new byte[4];
    //		String len = String.valueOf(HsmUtil.byte2int(b));
    //		byte[] len4 = len.getBytes();
    //		if(len4.length==4){
    //			System.arraycopy(len4,0,backbyte,0,len4.length);
    //		}else{
    //			int j = 3;
    //			for(int i =len4.length-1;i>=0;i--){
    //				backbyte[j--] = len4[i];
    //			}
    //			for(;j>=0;j-- ){
    //				backbyte[j] = 48;
    //			}
    //		}
    //		System.out.println(HsmUtil.bytesToHexString(backbyte));
    //	}
}
