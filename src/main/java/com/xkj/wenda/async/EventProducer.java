package com.xkj.wenda.async;

import com.alibaba.fastjson.JSONObject;

import com.xkj.wenda.Utils.JedisAdapter;
import com.xkj.wenda.Utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 将事件发送到jedisAdapter队列中
 */
@Service
public class EventProducer {
    @Autowired
    private JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel eventModel) {
        try {
            //转换成json字符串放到队列里
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key, json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}














