package com.laoyitiao.service;

import com.laoyitiao.entities.ImGroup;
import com.laoyitiao.repositories.ImGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
public class ImGroupService {
    @Autowired
    private ImGroupRepository imGroupRepository;

    public Object createImGroup(String username, String groupName){
        HashMap<String, Object> map = new HashMap<>();

        Optional<ImGroup> byId = imGroupRepository.findById(groupName);
        if(byId.isPresent()){
            map.put("status",false);
            map.put("msg","群名称已存在");
        }else {
            ImGroup group = new ImGroup();
            group.setTitle(groupName);
            group.setGroupOwner(username);
            group.setBriefIntroduction("这是一段群简介");
            group.setCoverImage("这是封面图片地址");
            imGroupRepository.save(group);
            map.put("status",true);
            map.put("msg","聊天群组已成功创建");
        }
        return map;
    }
}
