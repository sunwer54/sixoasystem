package com.oa.manage.service.impl;

import com.oa.manage.service.PositionService;
import com.oa.mapper.PositionMapper;
import com.oa.pojo.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionServiceImpl implements PositionService {
    @Autowired
    private PositionMapper positionMapper;
    @Override
    public List<Position> getPosition() {
        return positionMapper.selectByExample(null);
    }

    @Override
    public Position selPosByPosId(int posid) {
        return positionMapper.selectByPrimaryKey(posid);
    }
}
