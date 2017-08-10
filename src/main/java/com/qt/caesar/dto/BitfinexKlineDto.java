package com.qt.caesar.dto;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by renfei on 17/5/17.
 */
@Entity(name = "bitfinex_kline")
@Table(name = "bitfinex_kline")
public class BitfinexKlineDto extends KlineBaseModel {
}
