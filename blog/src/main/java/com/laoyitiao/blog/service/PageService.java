package com.laoyitiao.blog.service;

import com.alibaba.fastjson.JSON;
import com.laoyitiao.blog.entities.Blog;
import com.laoyitiao.blog.entities.PageCash;
import com.laoyitiao.blog.repositories.BlogRepository;
import com.laoyitiao.blog.repositories.PageCashRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class PageService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PageCashRepository pageCashRepository;

    public Object getDiscoveryPage(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("status",true);
        Object o = redisTemplate.opsForValue().get("page:discovery");
        if (o!=null){
            // 返回redis中的数据
            map.put("data", o);
            map.put("resource","redis");
        }else {
            PageCash discovery = pageCashRepository.findByPageName("discovery");
            if (discovery!=null){
                // redis中无数据时，查询数据库,更新redis
                redisTemplate.opsForValue().set("page:discovery",discovery.getData());
                map.put("data",discovery.getData());
                map.put("resource","mysql");
            }else {
                try {
                    // redis和数据库都没有数据
                    // 获取数据
                    HashMap<String, Object> discoveryPageData = new HashMap<>();
                    discoveryPageData.put("PopularBlogs",getPopularBlogs());
                    discoveryPageData.put("EditorRecommendations",getEditorRecommendations());
                    discoveryPageData.put("BlogDayLeaderboards",getBlogDayLeaderboards());
                    discoveryPageData.put("WeeklyBlogRankings",getWeeklyBlogRankings());
                    discoveryPageData.put("NewestBlogs",getNewestBlogs());
                    discoveryPageData.put("Announcement",getAnnouncement());

                    // 更新数据库
                    String pageData = JSON.toJSONString(discoveryPageData);
                    PageCash pageCash = new PageCash("discovery", pageData,new Timestamp(System.currentTimeMillis()));
                    pageCashRepository.save(pageCash);
                    // 更新redis
                    redisTemplate.opsForValue().set("page:discovery",pageData);
                    // 返回数据
                    map.put("data",pageData);
                    map.put("resource","new");
                }catch (Exception e){
                    e.printStackTrace();
                    map.put("status",false);
                    map.put("msg","无法获取到此页面数据，请稍后再试！【error:10079】");
                }
            }

        }
        return map;
    }

    @Autowired
    private BlogRepository blogRepository;

    // 获取十条热门博客数据
    private List<Blog> getPopularBlogs(){
        ArrayList<Blog> list = new ArrayList<>();
        for (int index = 0; index < 10; index++) {
            Blog blog = new Blog(
                    null,
                    "热门博客"+(index+1),
                    "https://www.baidu.com",
                    "这是一段预览文本",
                    "https://www.baidu.com",
                    "武汉张学友"+(index+1),
                    new Timestamp(System.currentTimeMillis()),
                    9884L,
                    154L,
                    79L,
                    881L
            );
            list.add(blog);
        }
        return list;
    }

    // 获取五条编辑推荐数据
    private List<Blog> getEditorRecommendations(){
        ArrayList<Blog> list = new ArrayList<>();
        for (int index = 0; index < 5; index++) {
            Blog blog = new Blog(
                    null,
                    "编辑推荐"+(index+1),
                    "https://www.baidu.com",
                    "这是一段预览文本"+(index+1)*10,
                    "https://www.baidu.com",
                    "长沙高圆圆"+(index+1),
                    new Timestamp(System.currentTimeMillis()),
                    9884L,
                    154L,
                    79L,
                    881L
            );
            list.add(blog);
        }
        return list;
    }

    // 获取博客日排行数据（前十）
    private List<Blog> getBlogDayLeaderboards(){
        ArrayList<Blog> list = new ArrayList<>();
        for (int index = 0; index < 10; index++) {
            Blog blog = new Blog(
                    null,
                    "日排行"+(index+1),
                    "https://www.baidu.com",
                    "这是一段预览文本",
                    "https://www.baidu.com",
                    "武汉张学友"+(index+1),
                    new Timestamp(System.currentTimeMillis()),
                    9884L,
                    154L,
                    79L,
                    881L
            );
            list.add(blog);
        }
        return list;
    }

    // 获取博客周排行数据（前十）
    private List<Blog> getWeeklyBlogRankings(){
        ArrayList<Blog> list = new ArrayList<>();
        for (int index = 0; index < 10; index++) {
            Blog blog = new Blog(
                    null,
                    "周排行"+(index+1),
                    "https://www.baidu.com",
                    "这是一段预览文本",
                    "https://www.baidu.com",
                    "武汉张学友"+(index+1),
                    new Timestamp(System.currentTimeMillis()),
                    9884L,
                    154L,
                    79L,
                    881L
            );
            list.add(blog);
        }
        return list;
    }

    // 获取最新博客数据
    private List<Blog> getNewestBlogs(){
        ArrayList<Blog> list = new ArrayList<>();
        for (int index = 0; index < 10; index++) {
            Blog blog = new Blog(
                    null,
                    "最新博客"+(index+1),
                    "https://www.baidu.com",
                    "这是一段预览文本",
                    "https://www.baidu.com",
                    "武汉张学友"+(index+1),
                    new Timestamp(System.currentTimeMillis()),
                    9884L,
                    154L,
                    79L,
                    881L
            );
            list.add(blog);
        }
        return list;
    }

    // 获取最新公告数据（五条）
    private List<Blog> getAnnouncement(){
        ArrayList<Blog> list = new ArrayList<>();
        for (int index = 0; index < 5; index++) {
            Blog blog = new Blog(
                    null,
                    "最新公告"+(index+1),
                    "https://www.baidu.com",
                    "这是一段预览文本",
                    "https://www.baidu.com",
                    "杭州马"+(index+1),
                    new Timestamp(System.currentTimeMillis()),
                    9884L,
                    154L,
                    79L,
                    881L
            );
            list.add(blog);
        }
        return list;
    }

}
