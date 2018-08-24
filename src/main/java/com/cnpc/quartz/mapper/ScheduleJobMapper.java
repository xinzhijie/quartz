package com.cnpc.admin.mapper;

import com.cnpc.admin.entity.ScheduleJob;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ScheduleJobMapper extends Mapper<ScheduleJob> {
    List<ScheduleJob> queryList();

    Long insertObject(ScheduleJob record);

    Long updateObject(ScheduleJob record);

    Long deleteObject(String id);

    ScheduleJob getObject(String id);
}