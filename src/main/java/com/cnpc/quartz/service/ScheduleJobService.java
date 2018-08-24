package com.cnpc.admin.service;

import com.cnpc.admin.entity.ScheduleJob;
import com.cnpc.admin.mapper.ScheduleJobMapper;
import com.cnpc.admin.util.ScheduleUtils;
import com.cnpc.admin.vo.ScheduleJobVo;
import com.cnpc.common.service.BaseService;
import org.apache.cxf.common.util.CollectionUtils;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * author : fengjing
 * createTime : 2016-08-04
 * description : 定时任务服务实现
 * version : 1.0
 */
@Service
public class ScheduleJobService  extends BaseService<ScheduleJobMapper,ScheduleJob> {

    /** 调度工厂Bean */
    @Autowired
    private Scheduler scheduler;


    public void initScheduleJob() {
        List<ScheduleJob> scheduleJobList = mapper.queryList();
        if (CollectionUtils.isEmpty(scheduleJobList)) {
            return;
        }
        for (ScheduleJob scheduleJob : scheduleJobList) {

            CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup());

            //不存在，创建一个
            if (cronTrigger == null) {
                ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
            } else {
                //已存在，那么更新相应的定时设置
                ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
            }
        }
    }

    public Long insert(ScheduleJobVo scheduleJobVo) {
        ScheduleJob scheduleJob = new ScheduleJob();
        BeanUtils.copyProperties(scheduleJobVo,scheduleJob);
        ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
        return mapper.insertObject(scheduleJob);
    }

    public void update(ScheduleJobVo scheduleJobVo) {
        ScheduleJob scheduleJob = new ScheduleJob();
        BeanUtils.copyProperties(scheduleJobVo,scheduleJob);
        ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
        mapper.updateObject(scheduleJob);
    }

    public void delUpdate(ScheduleJobVo scheduleJobVo) {
        ScheduleJob scheduleJob = new ScheduleJob();
        BeanUtils.copyProperties(scheduleJobVo,scheduleJob);
        //先删除
        ScheduleUtils.deleteScheduleJob(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup());
        //再创建
        ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
        //数据库直接更新即可
        mapper.updateObject(scheduleJob);
    }

    public void delete(String scheduleJobId) {
        ScheduleJob scheduleJob = mapper.getObject(scheduleJobId);// jdbcDao.get(ScheduleJob.class, scheduleJobId);
        //删除运行的任务
        ScheduleUtils.deleteScheduleJob(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup());
        //删除数据
        mapper.deleteObject(scheduleJobId);
    }

    public void runOnce(String scheduleJobId) {
        ScheduleJob scheduleJob = mapper.getObject(scheduleJobId);
        ScheduleUtils.runOnce(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup());
    }

    public void pauseJob(String scheduleJobId) {
        ScheduleJob scheduleJob = mapper.getObject(scheduleJobId);
        ScheduleUtils.pauseJob(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup());
        //数据库更新
        scheduleJob.setStatus("2");
        mapper.updateObject(scheduleJob);
    }

    public void resumeJob(String scheduleJobId) {
        ScheduleJob scheduleJob = mapper.getObject(scheduleJobId);
        ScheduleUtils.resumeJob(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup());
        //数据库更新
        scheduleJob.setStatus("1");
        mapper.updateObject(scheduleJob);
    }

    public ScheduleJobVo get(String scheduleJobId) {
        ScheduleJob scheduleJob = mapper.getObject(scheduleJobId);
        ScheduleJobVo scheduleJobVo = new ScheduleJobVo();
        BeanUtils.copyProperties(scheduleJobVo,scheduleJob);
        return scheduleJobVo;
    }

    public List<ScheduleJobVo> queryList() {

        List<ScheduleJob> scheduleJobs =  mapper.queryList();//jdbcDao.queryList(scheduleJobVo.getTargetObject(ScheduleJob.class));


//        List<ScheduleJobVo> scheduleJobVoList = new ArrayList<ScheduleJobVo>();
//        ScheduleJobVo temp = null;
//        for(ScheduleJob obj:scheduleJobs) {
//            temp = new ScheduleJobVo();
//            BeanUtils.copyProperties(obj, temp);
//            scheduleJobVoList.add(temp);
//        }
        List<ScheduleJobVo> scheduleJobVoList = copyList(scheduleJobs, ScheduleJobVo.class);
        try {
            for (ScheduleJobVo vo : scheduleJobVoList) {

                JobKey jobKey = ScheduleUtils.getJobKey(vo.getJobName(), vo.getJobGroup());
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                if (CollectionUtils.isEmpty(triggers)) {
                    continue;
                }

                //这里一个任务可以有多个触发器， 但是我们一个任务对应一个触发器，所以只取第一个即可，清晰明了
                Trigger trigger = triggers.iterator().next();
                vo.setJobTrigger(trigger.getKey().getName());

                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                vo.setStatus(triggerState.name());

                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    vo.setCronExpression(cronExpression);
                }
            }
        } catch (SchedulerException e) {
            //演示用，就不处理了
        }
        return scheduleJobVoList;
    }

    public static List copyList (List <? extends Object> poList , Class voClass){

        List voList=new ArrayList();

        Object voObj =null;
        for(Object poObj:poList){
            try {
                voObj = voClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            BeanUtils.copyProperties(poObj, voObj);
            voList.add(voObj);
        }
        return voList;

    }

    /**
     * 获取运行中的job列表
     * @return
     */
    public List<ScheduleJobVo> queryExecutingJobList() {
        try {
            // 存放结果集
            List<ScheduleJobVo> jobList = new ArrayList<ScheduleJobVo>();

            // 获取scheduler中的JobGroupName
            for (String group:scheduler.getJobGroupNames()){
                // 获取JobKey 循环遍历JobKey
                for(JobKey jobKey:scheduler.getJobKeys(GroupMatcher.<JobKey>groupEquals(group))){
                    JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                    JobDataMap jobDataMap = jobDetail.getJobDataMap();
                    ScheduleJob scheduleJob = (ScheduleJob)jobDataMap.get(ScheduleJobVo.JOB_PARAM_KEY);
                    ScheduleJobVo scheduleJobVo = new ScheduleJobVo();

                    BeanUtils.copyProperties(scheduleJobVo, scheduleJob);
//                    BeanConverter.convert(scheduleJobVo,scheduleJob);
                    List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                    Trigger trigger = triggers.iterator().next();
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                    scheduleJobVo.setJobTrigger(trigger.getKey().getName());
                    scheduleJobVo.setStatus(triggerState.name());
                    if (trigger instanceof CronTrigger) {
                        CronTrigger cronTrigger = (CronTrigger) trigger;
                        String cronExpression = cronTrigger.getCronExpression();
                        scheduleJobVo.setCronExpression(cronExpression);
                    }
                    // 获取正常运行的任务列表
                    if(triggerState.name().equals("NORMAL")){
                        jobList.add(scheduleJobVo);
                    }
                }
            }

            return jobList;
        } catch (SchedulerException e) {
            return null;
        }

    }
}
