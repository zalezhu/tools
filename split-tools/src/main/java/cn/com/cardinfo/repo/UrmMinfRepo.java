package cn.com.cardinfo.repo;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UrmMinfRepo {
	List<String> findAllMerId(@Param("merchantNos") List<String> merchanNos);
}
