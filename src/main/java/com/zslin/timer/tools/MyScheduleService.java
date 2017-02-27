package com.zslin.timer.tools;

import com.zslin.web.model.MyTask;
import com.zslin.web.service.IMyTaskService;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/21 9:58.
 */
@Component("myScheduleService")
public class MyScheduleService implements SchedulingConfigurer {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private IMyTaskService myTaskService;

    @Autowired
    private BeanFactory factory;

    private ScheduledTaskRegistrar registrar;

    /*public void addCron(String name) {
        TriggerTask task = new TriggerTask(new Runnable() {
            @Override
            public void run() {
                System.out.println("new Trigger Task , name:"+name);
            }
        }, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                CronTrigger trigger = new CronTrigger(DEFAULT_CRON);
                Date nextExecDate = trigger.nextExecutionTime(triggerContext);
                return nextExecDate;
            }
        });
        this.registrar.addTriggerTask(task);
    }*/

    /**
     * 添加循环定时任务
     * 从date日期开始执行第一次，间隔millisecond毫秒后执行下一次（需要等待上一次执行完成才执行下一次）
     * @param id 任务id，可以通过该id删除任务
     * @param runnable 具体任务事项
     * @param date 第一次执行的时间点，格式yyyy-MM-dd HH:mm:ss
     * @param millisecond 间隔时间，毫秒
     * @param remove 为true时删除id对应的任务，再重新添加
     * @param remark 任务备注
     */
    public void addTaskDelay(String id, Runnable runnable, String date, long millisecond, boolean remove, String remark) throws ParseException {
        ScheduledFuture future = this.registrar.getScheduler().scheduleWithFixedDelay(runnable, dateFormat.parse(date), millisecond);
        ScheduledDtoTools.getInstance().add(new ScheduledDto(id, future, remark), remove);
    }

    /**
     * 添加循环定时任务
     * 立即执行，间隔millisecond毫秒后执行下一次（需要等待上一次执行完成才执行下一次）
     * @param id 任务id，可以通过该id删除任务
     * @param runnable 具体任务事项
     * @param millisecond 间隔时间，毫秒
     * @param remove 为true时删除id对应的任务，再重新添加
     * @param remark 任务备注
     */
    public void addTaskDelayNow(String id, Runnable runnable, long millisecond, boolean remove, String remark) {
        ScheduledFuture future = this.registrar.getScheduler().scheduleWithFixedDelay(runnable, millisecond);
        ScheduledDtoTools.getInstance().add(new ScheduledDto(id, future, remark), remove);
    }

    /**
     * 添加循环定时任务
     * 立即执行，间隔millisecond毫秒后执行下一次，不管上一次是否执行完成
     * @param id 任务id，可以通过该id删除任务
     * @param runnable 具体任务事项
     * @param millisecond 间隔时间，毫秒
     * @param remove 为true时删除id对应的任务重新添加
     * @param remark 任务备注
     */
    public void addFixedRateTaskNow(String id, Runnable runnable, long millisecond, boolean remove, String remark) {
        ScheduledFuture future = this.registrar.getScheduler().scheduleAtFixedRate(runnable, millisecond);
        ScheduledDtoTools.getInstance().add(new ScheduledDto(id, future, remark), remove);
    }

    /**
     * 添加循环定时任务
     * 从date日期开始执行第一次，间隔millisecond毫秒后执行下一次，不管上一次是否执行完成
     * @param id 任务id，可以通过该id删除任务
     * @param runnable 具体任务事项
     * @param date 第一次执行的时间点，格式yyyy-MM-dd HH:mm:ss
     * @param millisecond 间隔时间，毫秒
     * @param remove 为true时删除id对应的任务重新添加
     * @param remark 任务备注
     * @throws ParseException
     */
    public void addFixedRateTask(String id, Runnable runnable, String date, long millisecond, boolean remove, String remark) throws ParseException {
        ScheduledFuture future = this.registrar.getScheduler().scheduleAtFixedRate(runnable, dateFormat.parse(date), millisecond);
        ScheduledDtoTools.getInstance().add(new ScheduledDto(id, future, remark), remove);
    }

    /**
     * 添加以Cron方式执行的定时任务
     * @param id 定时任务id，可以通过该id删除任务
     * @param runnable 具体任务事项
     * @param trigger 下一执行时间，可以动态修改
     * @param remove 为true时删除id对应的任务重新添加
     * @param remark 任务备注
     */
    public void addCronTask(String id, Runnable runnable, Trigger trigger, boolean remove, String remark) {
        ScheduledFuture future = this.registrar.getScheduler().schedule(runnable, trigger);
        ScheduledDtoTools.getInstance().add(new ScheduledDto(id, future, remark), remove);
    }

    /**
     * 终止正在运行的定时任务
     * @param id 任务id
     */
    public void cancel(String id) {
        ScheduledDtoTools.getInstance().remove(id);
    }

    /**
     * 添加只执行一次的定时任务
     * @param id 任务id
     * @param runnable 定时任务具体事项
     * @param date 指定执行时间，格式如：yyyy-MM-dd HH:mm:ss
     * @param remove 为true时删除id对应的任务重新添加
     * @param remark 定时任务备注信息
     * @throws ParseException
     */
    public void addOneTimeTask(String id, Runnable runnable, String date, boolean remove, String remark) throws ParseException {
        ScheduledFuture future = this.registrar.getScheduler().schedule(runnable, dateFormat.parse(date));
        ScheduledDtoTools.getInstance().add(new ScheduledDto(id, future, remark), remove);
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        this.registrar = scheduledTaskRegistrar;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000); //这里需要延迟执行，否则会registrar.getScheduler()会是空
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                initTask();
            }
        }).start();
    }

    private void initTask() {
        IMyTaskService myTaskService = (IMyTaskService) factory.getBean("myTaskService");
        List<MyTask> list = myTaskService.findNoRun();
        for(MyTask mt : list) {
            try {
                this.addOneTimeTask(mt.getId() + "", new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Object serviceObj = factory.getBean(mt.getPackageName());
                            Method method = serviceObj.getClass().getMethod(mt.getMethodName(), String.class);
                            method.invoke(serviceObj, mt.getParams());
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }, mt.getFirstTime(), false, mt.getRemark());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
