CREATE TABLE `bitfinex_kline` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `biz_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '业务id',
  `point_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '点位时间',
  `open` decimal(25,10) NOT NULL DEFAULT '0.0000000000' COMMENT '开',
  `high` decimal(25,10) NOT NULL DEFAULT '0.0000000000' COMMENT '高',
  `low` decimal(25,10) NOT NULL DEFAULT '0.0000000000' COMMENT '低',
  `receive` decimal(25,10) NOT NULL DEFAULT '0.0000000000' COMMENT '收',
  `trading_volume` decimal(25,10) NOT NULL DEFAULT '0.0000000000' COMMENT '交易量',
  `type` varchar(8) NOT NULL DEFAULT '0' COMMENT 'type:1min3min',
  `btc_price` decimal(25,10) NOT NULL DEFAULT '0.0000000000' COMMENT 'btc价格',
  `add_time` bigint(20) DEFAULT '0' COMMENT '添加时间',
  `coin_type` varchar(8) NOT NULL DEFAULT '' COMMENT 'kline货币类型',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_point` (`point_time`,`type`,`coin_type`),
  UNIQUE KEY `uniq_bizid` (`biz_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='bitfinex的kline';

CREATE TABLE `bitfinex_trades` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `biz_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '业务id',
  `trades_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '交易时间',
  `price` decimal(25,10) NOT NULL DEFAULT '0.0000000000' COMMENT '交易价格',
  `amount` decimal(25,10) NOT NULL DEFAULT '0.0000000000' COMMENT '交易量',
  `tid` bigint(20) NOT NULL DEFAULT '0' COMMENT '交易生成ID',
  `type` varchar(8) NOT NULL DEFAULT '' COMMENT '交易类型，buy(买)/sell(卖)',
  `trade_type` varchar(8) NOT NULL DEFAULT '' COMMENT '委托类型，ask(卖)/bid(买)',
  `btc_price` decimal(25,10) NOT NULL DEFAULT '0.0000000000' COMMENT 'btc价格',
  `add_time` bigint(20) DEFAULT '0' COMMENT '添加时间',
  `coin_type` varchar(8) NOT NULL DEFAULT '' COMMENT '货币类型',
  `buy` decimal(25,10) DEFAULT '0.0000000000' COMMENT '买1价格',
  `sell` decimal(25,10) DEFAULT '0.0000000000' COMMENT '卖1价格',
  `sync_id` bigint(20) DEFAULT '0' COMMENT '同步id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_point` (`tid`,`coin_type`),
  UNIQUE KEY `uniq_bizid` (`biz_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='bitfinex的trades';

CREATE TABLE `chbtc_kline` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `biz_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '业务id',
  `point_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '点位时间',
  `open` decimal(25,10) NOT NULL DEFAULT '0.0000000000' COMMENT '开',
  `high` decimal(25,10) NOT NULL DEFAULT '0.0000000000' COMMENT '高',
  `low` decimal(25,10) NOT NULL DEFAULT '0.0000000000' COMMENT '低',
  `receive` decimal(25,10) NOT NULL DEFAULT '0.0000000000' COMMENT '收',
  `trading_volume` decimal(25,10) NOT NULL DEFAULT '0.0000000000' COMMENT '交易量',
  `type` varchar(8) NOT NULL DEFAULT '0' COMMENT 'type:1min3min',
  `btc_price` decimal(25,10) NOT NULL DEFAULT '0.0000000000' COMMENT 'btc价格',
  `add_time` bigint(20) DEFAULT '0' COMMENT '添加时间',
  `coin_type` varchar(8) NOT NULL DEFAULT '' COMMENT 'kline货币类型',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_point` (`point_time`,`type`,`coin_type`),
  UNIQUE KEY `uniq_bizid` (`biz_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='chbtc的kline';


CREATE TABLE `chbtc_trades` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `biz_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '业务id',
  `trades_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '交易时间',
  `price` decimal(25,10) NOT NULL DEFAULT '0.0000000000' COMMENT '交易价格',
  `amount` decimal(25,10) NOT NULL DEFAULT '0.0000000000' COMMENT '交易量',
  `tid` bigint(20) NOT NULL DEFAULT '0' COMMENT '交易生成ID',
  `type` varchar(8) NOT NULL DEFAULT '0' COMMENT '交易类型，buy(买)/sell(卖)',
  `trade_type` varchar(8) NOT NULL DEFAULT '0' COMMENT '委托类型，ask(卖)/bid(买)',
  `btc_price` decimal(25,10) NOT NULL DEFAULT '0.0000000000' COMMENT 'btc价格',
  `add_time` bigint(20) DEFAULT '0' COMMENT '添加时间',
  `coin_type` varchar(8) NOT NULL DEFAULT '' COMMENT '货币类型',
  `buy` decimal(25,10) DEFAULT '0.0000000000' COMMENT '买1价格',
  `sell` decimal(25,10) DEFAULT '0.0000000000' COMMENT '卖1价格',
  `sync_id` bigint(20) DEFAULT '0' COMMENT '同步id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_point` (`tid`,`coin_type`),
  UNIQUE KEY `uniq_bizid` (`biz_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='chbtc的trades';


CREATE TABLE `id_generator` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `collection` varchar(16) NOT NULL DEFAULT '' COMMENT '类型',
  `value` bigint(20) NOT NULL DEFAULT '0' COMMENT 'incr',
  `update_time` bigint(20) DEFAULT '0' COMMENT '添加时间',
  `db_type` varchar(16) NOT NULL DEFAULT '' COMMENT '主存类型',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_coll_db` (`collection`,`db_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='id生成器';