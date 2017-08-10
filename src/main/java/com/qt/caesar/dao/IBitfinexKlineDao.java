package com.qt.caesar.dao;

import com.qt.caesar.dto.BitfinexKlineDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by renfei on 17/5/11.
 */
public interface IBitfinexKlineDao extends CrudRepository<BitfinexKlineDto, Long> {

    /**
     * 根据点位时间查询数据
     * @param queryTime
     * @param coinType
     * @return
     */
    @Query(value = "select * from #{#entityName} k where k.point_time > ?1 and k.coin_type=?2 order by k.point_time desc limit 10", nativeQuery = true)
    List<BitfinexKlineDto> findKlineDate(long queryTime, String coinType);

}
