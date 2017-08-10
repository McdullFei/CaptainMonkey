package com.qt.caesar.dao;

import com.qt.caesar.dto.BitfinexTradesDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by renfei on 17/5/16.
 */
public interface IBitfinexTradesDao extends CrudRepository<BitfinexTradesDto, Long> {
    /**
     * 根据点位时间查询数据
     * @param queryTime
     * @param coinType
     * @return
     */
    @Query(value = "select * from #{#entityName} k where k.trades_time > ?1 and k.coin_type=?2 order by k.trades_time ", nativeQuery = true)
    List<BitfinexTradesDto> findTradesDate(long queryTime, String coinType);


    @Transactional
    @Modifying @Query(value = "update `bitfinex_trades` set sync_id = ?1 where id = ?2", nativeQuery = true)
    void updateForSyncId(long syncId, long id);
}
