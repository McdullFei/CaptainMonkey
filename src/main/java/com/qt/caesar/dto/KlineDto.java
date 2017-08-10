package com.qt.caesar.dto;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by renfei on 17/5/10.
 */
@Entity(name = "chbtc_kline")
@Table(name = "chbtc_kline")
//@SecondaryTables({@SecondaryTable(name = "chbtc_kline"),
//        @SecondaryTable(name = "bitfinex_kline")})
public class KlineDto extends KlineBaseModel{
}
