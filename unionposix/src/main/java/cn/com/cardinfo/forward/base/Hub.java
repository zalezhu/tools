package cn.com.cardinfo.forward.base;

import java.io.IOException;
import java.nio.channels.AsynchronousChannelGroup;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import cn.com.cardinfo.forward.channel.Channel;
import cn.com.cardinfo.forward.channel.ChannelConfig;
import cn.com.cardinfo.forward.util.ChannelsUtil;
import cn.com.cardinfo.forward.util.ExecutorsPool;

/**
 * 
 * @author Zale
 *
 */
public class Hub {
	private static Logger logger = LoggerFactory.getLogger(Hub.class);
	private Channel channel;
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
		if (channel == null) {
			throw new RuntimeException("There is no channel.start hub failed.The system will stop");
		}

		startChannels();
	}

	public Channel getChannel() {
		return channel;
	}
	



	private void initExecutors() {

	}
	
	
	private void startChannels() {
		// start union channel
			channel.start();
	}
	private void loadChannels() throws IOException {
		String channelStr = ChannelsUtil.getChannelJson();
		if (StringUtils.isNotBlank(channelStr)) {
			List<ChannelConfig> configList = JSON.parseObject(channelStr, new TypeReference<List<ChannelConfig>>() {
			});
			for (ChannelConfig config : configList) {
					channel = new Channel();
					channel.setId(config.getId());
					channel.setType(config.getType());
					if(config.getWeight()==null){
						config.setWeight(1);
					}
					channel.setWeigth(config.getWeight());
					channel.init(AsynchronousChannelGroup.withThreadPool(ExecutorsPool.CHANNEL_EXECUTORS),
							config.getClientPort(), config.getServerIp(), config.getServerPort());
			}
		}
	}
	

}
