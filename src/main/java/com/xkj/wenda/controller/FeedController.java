package com.xkj.wenda.controller;


import com.xkj.wenda.Utils.JedisAdapter;
import com.xkj.wenda.Utils.RedisKeyUtil;
import com.xkj.wenda.model.EntityType;
import com.xkj.wenda.model.Feed;
import com.xkj.wenda.model.HostHolder;
import com.xkj.wenda.service.FeedService;
import com.xkj.wenda.service.FollowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FeedController {
    private static Logger logger = LoggerFactory.getLogger(FeedController.class);

    @Autowired
    private FeedService feedService;

    @Autowired
    private FollowService followService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private JedisAdapter jedisAdapter;

    /**
     * 推模式
     * @param model
     * @return
     */
    @RequestMapping(path = "/pushfeeds", method = {RequestMethod.POST, RequestMethod.GET})
    private String getPushFeeds(Model model) {
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        List<String> feedIds = jedisAdapter.lrange(RedisKeyUtil.getTimeline(localUserId), 0, 10);
        List<Feed> feeds = new ArrayList<>();
        for (String feedId : feedIds) {
            Feed feed = feedService.getById(Integer.parseInt(feedId));
            if (feed != null) {
                feeds.add(feed);
            }
        }
        model.addAttribute("feeds", feeds);
        return "feeds";
    }

    /**
     * 拉模式
     * @param model
     * @return
     */
    @RequestMapping(path = "/pullfeeds", method = {RequestMethod.POST, RequestMethod.GET})
    private String getPullFeeds(Model model) {
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        List<Integer> followees = new ArrayList<>();
        if (localUserId != 0) {
            // 关注的人
            followService.getFollowees( EntityType.ENTITY_USER, localUserId, Integer.MAX_VALUE);
        }
        List<Feed> feeds = feedService.getUserFeeds(Integer.MAX_VALUE, followees, 10);
        model.addAttribute("feeds", feeds);
        return "feeds";
    }
}


























