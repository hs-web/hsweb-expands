package org.hsweb.expands.quartz;

/**
 * 任务执行进度
 * Created by zhouhao on 16-6-23.
 */
public interface QuartzJobProgress {
    /**
     * 任务执行过程中 调用此方法来汇报进度
     *
     * @param percent 从0-1. 0为开始,1为结束
     * @param message 执行返回消息
     */
    void progress(double percent, String message);
}
