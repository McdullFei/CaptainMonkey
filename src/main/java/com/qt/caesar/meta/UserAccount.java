package com.qt.caesar.meta;

import java.util.Map;

/**
 * Created by renfei on 17/7/6.
 */
public class UserAccount {
    private Map<String, Object> asset;
    private Map<String, Object> free;
    private Map<String, Object> freezed;

    public Map<String, Object> getAsset() {
        return asset;
    }

    public UserAccount setAsset(Map<String, Object> asset) {
        this.asset = asset;
        return this;
    }

    public Map<String, Object> getFree() {
        return free;
    }

    public UserAccount setFree(Map<String, Object> free) {
        this.free = free;
        return this;
    }

    public Map<String, Object> getFreezed() {
        return freezed;
    }

    public UserAccount setFreezed(Map<String, Object> freezed) {
        this.freezed = freezed;
        return this;
    }
}
