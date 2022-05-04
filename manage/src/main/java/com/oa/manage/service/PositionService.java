package com.oa.manage.service;

import com.oa.pojo.Position;

import java.util.List;

public interface PositionService {
    public List<Position> getPosition();

    public Position selPosByPosId(int posid);
}
