package com.qt.caesar.service;

import com.qt.caesar.meta.UserAccount;

/**
 * 用户账户相关接口,充值提现余额
 *
 * Created by renfei on 17/7/6.
 */
public interface ITradePlatformAccount {
    /**
     * 获取用户信息
     * @return
     */
    UserAccount get();
}
