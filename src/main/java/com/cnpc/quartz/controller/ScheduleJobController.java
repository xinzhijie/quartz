package com.cnpc.admin.controller;

import com.cnpc.admin.entity.ScheduleJob;
import com.cnpc.admin.service.ScheduleJobService;
import com.cnpc.admin.vo.ScheduleJobVo;
import com.cnpc.common.controller.BaseController;
import com.cnpc.common.response.ResponseMessage;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/scheduleJob")
public class ScheduleJobController extends BaseController<ScheduleJobService,ScheduleJob> {

    @RequestMapping(value = "/queryList",method = RequestMethod.GET)
    public ResponseMessage<List<ScheduleJob>> queryList(){
        //查询列表数据
        return new ResponseMessage().success(baseService.queryList());
    }

    /**
     * 运行一次
     *
     * @return
     */
    @RequestMapping(value = "/runOnceJob/{id}",method = RequestMethod.GET)
    public ResponseMessage<String> runOnceScheduleJob(@PathVariable String id) {

        baseService.runOnce(id);

        return new ResponseMessage().success("success");
    }

    @PostMapping("/insertJob")
    public ResponseMessage<String> add(@RequestBody ScheduleJobVo scheduleJobVo) {
        //测试用随便设个状态
        scheduleJobVo.setStatus("1");
        baseService.insert(scheduleJobVo);
        return new ResponseMessage().success("success");
//        if (scheduleJobVo.getScheduleJobId() == null) {
//            baseService.insert(scheduleJobVo);
//        } else if (StringUtils.equalsIgnoreCase(scheduleJobVo.getKeywords(),"delUpdate")){
//            //直接拿keywords存一下，就不另外重新弄了
//            baseService.delUpdate(scheduleJobVo);
//        }else {
//            baseService.update(scheduleJobVo);
//        }
    }

    /**
     * 暂停
     *
     * @return
     */
    @RequestMapping(value = "/pauseJob/{id}", method = RequestMethod.GET)
    public ResponseMessage<String> pauseJob(@PathVariable String id) {
        baseService.pauseJob(id);
        return new ResponseMessage().success("success");
    }

    /**
     * 恢复
     *
     * @return
     */
    @RequestMapping(value = "/resumeJob/{id}", method = RequestMethod.GET)
    public ResponseMessage<String> resumeJob(@PathVariable String id) {
        baseService.resumeJob(id);
        return new ResponseMessage().success("success");
    }

    /**
     * 删除任务
     *
     * @return
     */
    @RequestMapping(value = "/deleteJob/{id}", method = RequestMethod.GET)
    public ResponseMessage<String> deleteJob(@PathVariable String id) {

        baseService.delete(id);

        return new ResponseMessage().success("success");
    }
}
