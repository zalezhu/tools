package cn.com.cardinfo.forward.base;

import java.io.IOException;
import java.nio.channels.AsynchronousChannelGroup;
import java.util.*;

import cn.com.cardinfo.forward.client.TcpShortClient;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import cn.com.cardinfo.forward.channel.Channel;
import cn.com.cardinfo.forward.channel.ChannelConfig;
import cn.com.cardinfo.forward.channel.Channel.ChannelType;
import cn.com.cardinfo.forward.server.MsgConsumer;
import cn.com.cardinfo.forward.util.ChannelsUtil;
import cn.com.cardinfo.forward.util.ExecutorsPool;
import cn.com.cardinfo.forward.util.PropertiesUtil;

/**
 * 
 * @author Zale
 *
 */
public class Hub {
	private static Logger logger = LoggerFactory.getLogger(Hub.class);
	private Map<String, Channel> unionChannels;
	private Map<String, Channel> pospChannels;
	private List<TcpShortClient> unionClient;
	private static Hub hub;

	private Hub() {
	}

	public static synchronized Hub getInstance() {
		if (hub == null) {
			hub = new Hub();
		}
		return hub;
	}

	public void startHub() throws IOException {
		initExecutors();
		loadChannels();
		startChannels();
	}

	public Channel getChannel(String id) {
		if (unionChannels.containsKey(id)) {
			return unionChannels.get(id);
		}
		return pospChannels.get(id);
	}
	
	public Channel getPospChannel(String id){
		return pospChannels.get(id);
	}

	public Collection<Channel> getUnionChannels() {
		return unionChannels.values();
	}

	public Collection<Channel> getPospChannels() {
		return pospChannels.values();
	}

	private void initExecutors() {

	}

	public static void main(String[] args) {
		int rn =1;
		while(rn>=0){
			rn = RandomUtils.nextInt(10);
		}
		System.out.println(rn);
	}
	public TcpShortClient balanceGetUnionClient() {
		int index = RandomUtils.nextInt(unionClient.size());
		if(index == unionClient.size()){
			index = index -1;
		}
		return unionClient.get(index);
	}

	public Channel balanceGetUnionChannel() {
		Channel less = null;
		for (Channel channel : unionChannels.values()) {
			if (channel.isWork()) {
				if (less == null) {
					less = channel;
				} else {
					int lessSize = less.getClientSession().getQueue().size();
					//					int lessWeight = less.getWeigth();
					int channelSize = channel.getClientSession().getQueue().size();
					//					int channelWeight = channel.getWeigth();
					if (lessSize > channelSize) {
						less = channel;
					}else if(lessSize == channelSize){
						int rIndex = RandomUtils.nextInt(10)%2;
						if(rIndex==1){
							less = channel;
						}

					}

				}
			}
		}
		return less;
	}

	private void startChannels() {
		// start union channel
		for (Channel channel : unionChannels.values()) {
			channel.start();
		}
		// start posp channel
		for (Channel channel : pospChannels.values()) {
			channel.start();
		}
	}

	private void loadChannels() throws IOException {
		if (unionChannels == null) {
			unionChannels = new HashMap<String, Channel>();
		}
		if (pospChannels == null) {
			pospChannels = new HashMap<String, Channel>();
		}
		if(unionClient == null){
			unionClient = new ArrayList<>();
		}
		String channelStr = ChannelsUtil.getChannelJson();
		if (StringUtils.isNotBlank(channelStr)) {
			List<ChannelConfig> configList = JSON.parseObject(channelStr, new TypeReference<List<ChannelConfig>>() {
			});
			for (ChannelConfig config : configList) {
				if(config.getType()==ChannelType.union_client){
					TcpShortClient shortClient = new TcpShortClient();
					shortClient.setPort(config.getServerPort());
					shortClient.setIp(config.getServerIp());
					shortClient.setId(config.getId());
					if (config.getWeight() == null) {
						config.setWeight(1);
					}
					shortClient.setWeight(config.getWeight());
					unionClient.add( shortClient);

				}else {
					Channel channel = new Channel();
					channel.setId(config.getId());
					channel.setType(config.getType());
					if (config.getWeight() == null) {
						config.setWeight(1);
					}
					channel.setWeigth(config.getWeight());
					channel.init(AsynchronousChannelGroup.withThreadPool(ExecutorsPool.CHANNEL_EXECUTORS), config.getClientPort(),
							config.getServerIp(), config.getServerPort());
					if (channel.getType() == ChannelType.union) {
						unionChannels.put(config.getId(), channel);
					} else if (channel.getType() == ChannelType.posp) {
						pospChannels.put(config.getId(), channel);
					}
				}
			}
		}
	}
	

}
