package com.oa.manage.controller;

import com.oa.manage.service.PositionService;
import com.oa.pojo.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PositionCotrller {
    @Autowired
    private PositionService positionService;

    @RequestMapping("/getPositions")
    public Object getPositions(){
        List<Position> position = positionService.getPosition();
        return position;
    }
}
