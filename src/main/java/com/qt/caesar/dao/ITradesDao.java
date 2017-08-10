package com.qt.caesar.dao;

import com.qt.caesar.dto.TradesDto;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by renfei on 17/5/16.
 */
public interface ITradesDao extends CrudRepository<TradesDto, Long> {
}
