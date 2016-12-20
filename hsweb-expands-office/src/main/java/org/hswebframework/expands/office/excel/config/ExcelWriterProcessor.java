package org.hswebframework.expands.office.excel.config;

/**
 * excel写出处理器,通过此接口对一个创建excel表格数据
 * Created by 浩 on 2015-12-16 0016.
 */
public interface ExcelWriterProcessor {
    /**
     * 开始写出,只能调用一次,成功后,将返回表格对象,如POI中的Sheet
     *
     * @param <S> 表格对象泛型
     * @return 表格对象
     * @throws Exception 启动异常,如果已经启动,将抛出异常
     */
    <S> S start() throws Exception;

    /**
     * 开始写出,并指定表格名字,只能调用一次,成功后,将返回表格对象,如POI中的Sheet
     *
     * @param <S>       表格对象泛型
     * @param sheetName 表格名字
     * @return 表格对象
     * @throws Exception 启动异常,如果已经启动,将抛出异常
     */
    <S> S start(String sheetName) throws Exception;

    /**
     * 下一行操作,将创建一行并返回这一行的对象引用，如POI中的Row
     *
     * @param <R> 行对象泛型
     * @return 行对象
     */
    <R> R nextRow();

    /**
     * 创建当前行的列,将在当前行创建一个单元格对象,并返回实例，如POI的Cell
     *
     * @param <C> 单元格对象泛型
     * @return 列对象
     */
    <C> C nextCell();

    /**
     * 完成写出,对excel操作完毕之后,调用此方法写出
     *
     * @throws Exception 写出异常
     */
    void done() throws Exception;
}
