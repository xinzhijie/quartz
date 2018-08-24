package com.cnpc.admin.entity;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "SCHEDULE_JOB")
public class ScheduleJob implements Serializable {

    private static final long serialVersionUID = 4888005949821878223L;

    @Id
    @Column(name = "SCHEDULE_JOB_ID")
    private String scheduleJobId;

    @Column(name = "ALIAS_NAME")
    private String aliasName;

    @Column(name = "CRON_EXPRESSION")
    private String cronExpression;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "GMT_CREATE")
    private String gmtCreate;

    @Column(name = "GMT_MODIFY")
    private String gmtModify;

    @Column(name = "IS_SYNC")
    private Boolean isSync;

    public Boolean getIsSync() {
        return isSync;
    }

    public void setIsSync(Boolean sync) {
        isSync = sync;
    }

    @Column(name = "JOB_GROUP")
    private String jobGroup;

    @Column(name = "JOB_NAME")
    private String jobName;

    @Column(name = "JOB_TRIGGER")
    private String jobTrigger;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "URL")
    private String url;

    /**
     * @return SCHEDULE_JOB_ID
     */
    public String getScheduleJobId() {
        return scheduleJobId;
    }

    /**
     * @param scheduleJobId
     */
    public void setScheduleJobId(String scheduleJobId) {
        this.scheduleJobId = scheduleJobId;
    }

    /**
     * @return ALIAS_NAME
     */
    public String getAliasName() {
        return aliasName;
    }

    /**
     * @param aliasName
     */
    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    /**
     * @return CRON_EXPRESSION
     */
    public String getCronExpression() {
        return cronExpression;
    }

    /**
     * @param cronExpression
     */
    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    /**
     * @return DESCRIPTION
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return GMT_CREATE
     */
    public String getGmtCreate() {
        return gmtCreate;
    }

    /**
     * @param gmtCreate
     */
    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * @return GMT_MODIFY
     */
    public String getGmtModify() {
        return gmtModify;
    }

    /**
     * @param gmtModify
     */
    public void setGmtModify(String gmtModify) {
        this.gmtModify = gmtModify;
    }


    /**
     * @return JOB_GROUP
     */
    public String getJobGroup() {
        return jobGroup;
    }

    /**
     * @param jobGroup
     */
    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    /**
     * @return JOB_NAME
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * @param jobName
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    /**
     * @return JOB_TRIGGER
     */
    public String getJobTrigger() {
        return jobTrigger;
    }

    /**
     * @param jobTrigger
     */
    public void setJobTrigger(String jobTrigger) {
        this.jobTrigger = jobTrigger;
    }

    /**
     * @return STATUS
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }
}