package com.qt.caesar.dto;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by renfei on 17/5/16.
 */
@Entity
@Table(name = "chbtc_trades")
//@SQLInsert(sql="INSERT IGNORE INTO chbtc_trades(add_time, amount, biz_id, btc_price, coin_type, price, tid, trade_type, trades_time, type) VALUES(?,?,?,?,?,?,?,?,?,?)"
//        , check = ResultCheckStyle.COUNT)
public class TradesDto extends TradesBaseModel {

}
