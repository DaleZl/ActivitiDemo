package cn.dale.activitidemo.task;

import cn.dale.activitidemo.entity.Employee;
import cn.dale.activitidemo.enums.ApplyStatusEnum;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: dale
 * @Date: 2019/11/6 15:38
 */
public class ActivitiTask {

    private TaskService taskService;

    public ActivitiTask(TaskService taskService) {
        this.taskService = taskService;
    }

    //职员提交申请
    public void completeEmployeeTask(Task task, Employee emp){
        //获取任务id
        String taskId = task.getId();

        //完成任务
        taskService.complete(taskId);
        emp.setApplyStatus(ApplyStatusEnum.AUDITING);
        System.out.println("职员已经提交申请.......");

    }
    //领导审批
    public void completeLeaderTask(Task task, Employee emp){
        //获取任务id
        String taskId = task.getId();

        //领导意见
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("day",emp.getDay());
        variables.put("deptMngResult", 1);
        emp.setApplyStatus(ApplyStatusEnum.AUDITING);
        //完成任务
        taskService.complete(taskId, variables);
        System.out.println("领导审核完毕........");

    }
    //经理审批
    public void completeJingliTask(Task task, Employee emp){
        //获取任务id
        String taskId = task.getId();
        String name = task.getName();
        //经理意见
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("day",emp.getDay());
        variables.put("result", 0);
        emp.setApplyStatus(ApplyStatusEnum.NO);
//        variables.put("result", 1);
//        emp.setApplyStatus(ApplyStatusEnum.YES);
        //完成任务
        taskService.complete(taskId, variables);
        System.out.println("经理审核完毕........,审核经理:"+name);

    }
}
