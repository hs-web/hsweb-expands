package org.hswebframework.expands.office.excel;

import java.util.List;

/**
 * excel读取结果包装器,用于将一行数据包装为一个java对象
 * Created by 浩 on 2015-12-07 0007.
 */
public interface ExcelReaderWrapper<T> {

    /**
     * 是否已结束,用于判断是否已被中断操作
     *
     * @return 是否已经手动结束包装
     */
    boolean isShutdown();

    /**
     * 中断包装，如果中途中断，将会立即结束包装
     */
    void shutdown();

    /**
     * 获取一个需要包装的实例
     *
     * @return 需要包装的实例
     * @throws Exception
     */
    T newInstance() throws Exception;

    default T newInstance(int sheet) throws Exception {
        return newInstance();
    }

    /**
     * 包装一个属性到实例里,每读取一个单元格，会传入这个单元格的信息,进行属性填充
     *
     * @param instance 当前实例(当前行对应的示例对象)
     * @param header   当前表头,当前列对应的表头
     * @param value    当前单元格的值
     */
    void wrapper(T instance, String header, Object value);

    /**
     * 当填充一个对象结束时,此方法被调用。
     *
     * @param instance 填充结束的对象
     */
    void wrapperDone(T instance);

    default void setup(List<String> headers, int sheet) {

    }
}
