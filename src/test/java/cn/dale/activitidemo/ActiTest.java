package cn.dale.activitidemo;

import cn.dale.activitidemo.entity.Employee;
import cn.dale.activitidemo.task.ActivitiTask;
import org.activiti.engine.*;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: dale
 * @Date: 2019/11/5 17:59
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest
@Transactional
@Rollback(false)
//@Ignore
public class ActiTest {
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    //初始化一个要请假的员工
    private static final Employee emp = new Employee("zs", 8);

    @Test
    public void testInitProcess() {
        System.out.println("method startActivityDemo begin....");
        System.out.println("调用流程存储服务，已部署流程数量："
                + repositoryService.createDeploymentQuery().count());
        Map<String, Object> map = new HashMap<String, Object>();
        // 流程图里写的${user} ，这里传进去user
        map.put("Applier", emp.getEmployeeName());
        map.put("DeptMng","ls");
        map.put("ViceMng","ww");
        map.put("GnlMng","zl");
        map.put("special","sq");

        // 指定流程的发起者 不指定发起者的字段就为空，注意跟审核人分开
        identityService.setAuthenticatedUserId(emp.getEmployeeName());
        //流程启动
        ExecutionEntity pi = (ExecutionEntity) runtimeService.startProcessInstanceByKey("start", map);
        String processInstanceId = pi.getId();
        System.out.println("流程开启成功,流程实例id:" + processInstanceId);
        ActivitiTask at = new ActivitiTask(taskService);
        //开始进行流程
        while (runtimeService
                .createProcessInstanceQuery()//获取查询对象
                .processInstanceId(processInstanceId)//根据id查询流程实例
                .singleResult()//获取查询结果,如果为空,说明这个流程已经执行完毕,否则,获取任务并执行
                != null) {
            Task task = taskService.createTaskQuery()//创建查询对象
                    .processInstanceId(processInstanceId)//通过流程实例id来查询当前任务
                    .singleResult();//获取单个查询结果
            String taskName = task.getName();
            System.out.println("任务ID: " + task.getId());
            System.out.println("任务的办理人: " + task.getAssignee());
            System.out.println("任务名称: " + taskName);
            System.out.println("任务的创建时间: " + task.getCreateTime());
            System.out.println("流程实例ID: " + task.getProcessInstanceId());
            switch (taskName) {
                case "Applier": //职员节点
                    at.completeEmployeeTask(task,emp);
                    break;
                case "DeptMng": //部门经理节点
                    at.completeLeaderTask(task, emp);
                    break;
                default: //经理节点
                    at.completeJingliTask(task, emp);
                    break;
            }
            System.out.println(emp.getApplyStatus().getName());
        }
        System.out.println("method startActivityDemo end....");
    }

    @Test
    public void testAudit() {
        Map<String, Object> map = new HashMap<>();
        map.put("Applier", emp.getEmployeeName());
        map.put("DeptMng","ls");
        taskService.complete("40007", map);
    }

    @Test
    public void testCheckByUser() {
        List<Task> tasks = taskService//与任务相关的Service
                .createTaskQuery()//创建一个任务查询对象
                .taskAssignee("ls")
                .list();
        if (tasks != null && tasks.size() > 0) {
            for (Task task : tasks) {
                System.out.println("任务ID:" + task.getId());
                System.out.println("任务的办理人:" + task.getAssignee());
                System.out.println("任务名称:" + task.getName());
                System.out.println("任务的创建时间:" + task.getCreateTime());
                System.out.println("流程实例ID:" + task.getProcessInstanceId());
            }
        }
    }

    // 通过发起者查询该用户发起的所有任务
    @Test
    public void testCheckByInitiator() {
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().startedBy("ls").list();  //获取该用户发起的所有流程实例
        // System.out.println(list.toString());
        for (ProcessInstance processInstance : list) {
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
            if (tasks != null && tasks.size() > 0) {
                for (Task task : tasks) {
                    System.out.println("任务ID:" + task.getId());
                    System.out.println("任务的办理人:" + task.getAssignee());
                    System.out.println("任务名称:" + task.getName());
                    System.out.println("任务的创建时间:" + task.getCreateTime());
                    System.out.println("流程实例ID:" + task.getProcessInstanceId());
                }
            }
        }
    }

    /**
     * 获取流程图 执行到哪里高亮显示
     * @param procDefId 部署的流程id  在 act_re_procdef 这张表里
     * @param execId  要查询的流程执行的id（开启了一个流程就会生成一条执行的数据）  在 act_ru_execution 这张表里（该表下PROC_DEF_ID_字段可以判断哪个流程）
     * @param response
     * @throws Exception
     */

//    @Autowired
//    private HttpServletResponse response;
//    @Test
//    public void testGetActPic() throws IOException {
//        InputStream imageStream = activityService.tracePhoto(procDefId, execId);
//        // 输出资源内容到相应对象
//        byte[] b = new byte[1024];
//        int len;
//        while ((len = imageStream.read(b, 0, 1024)) != -1) {
//            response.getOutputStream().write(b, 0, len);
//        }
//    }
}
