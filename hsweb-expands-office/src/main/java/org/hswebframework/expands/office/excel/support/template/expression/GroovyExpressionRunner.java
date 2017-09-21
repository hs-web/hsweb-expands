package org.hswebframework.expands.office.excel.support.template.expression;

import org.apache.poi.ss.usermodel.Cell;
import org.hswebframework.expands.script.engine.DynamicScriptEngine;
import org.hswebframework.expands.script.engine.DynamicScriptEngineFactory;
import org.hswebframework.expands.script.engine.ExecuteResult;
import org.hswebframework.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 基于Groovy引擎的表达式执行器.
 * 目前支持内容
 * <ul>
 * <li>单元格赋值如:data.name</li>
 * <li>三目运算:data.age>14:"小孩子":"大人"</li>
 * <li>循环: for(list in data) ,结束循环: /for</li>
 * </ul>
 *
 * @version 1.0
 *          Created by 浩 on 2015-12-16 0016.
 */
public class GroovyExpressionRunner implements ExpressionRunner {

    private Map<String, Object> root = new HashMap<>();
    private StringBuilder builder = new StringBuilder();
    protected CellHelper helper;
    private int cellNumber = 0;
    private int each_flag = 0;

    public void setData(Map<String, Object> data) {
        root.putAll(data);
    }

    public void setHelper(CellHelper helper) {
        this.helper = helper;
    }

    public CellHelper getHelper() {
        return helper;
    }

    @Override
    public void pushExpression(Cell cell, String expression) {
        cellNumber++;
        String cellName = "cell_" + cellNumber;
        root.put(cellName, cell);
        //如果是以for(开头，则认为表达式为循环操作
        if (expression.startsWith("for(")) {
            each_flag++;
            builder.append(expression).append("{\n");
            builder.append("\t_helper.nextRow(").append(cellName).append(");").append("\n");
        } else if (expression.startsWith("/for")) {
            //以/for开头,则为结束循环
            if (each_flag > 0) each_flag--;
            builder.append("}").append("\n");
        } else if (expression.startsWith("def")) {
            builder.append(expression).append(";\n");
            if (each_flag <= 0)
                builder.append("\t_helper.initCell(").append("''").append(",").append(cellName).append(");").append("\n");
        } else {
            if (each_flag > 0)
                //当前为循环内
                builder.append("\t_helper.nextCell(").append(expression).append(",").append(cellName).append(");").append("\n");
            else
                builder.append("\t_helper.initCell(").append(expression).append(",").append(cellName).append(");").append("\n");
        }
    }

    @Override
    public void run() throws Throwable {
        DynamicScriptEngine engine = DynamicScriptEngineFactory.getEngine("groovy");
        root.put("_helper", helper);
        String script_id = StringUtils.concat("excel_expression_", builder.hashCode());
        if (!engine.compiled(script_id))
            engine.compile(script_id, builder.toString());
        ExecuteResult executeResult = engine.execute(script_id, root);
        if (executeResult.getException() != null)
            throw executeResult.getException();
    }

    @Override
    public void reset() {
        cellNumber = 0;
        each_flag = 0;
        builder = new StringBuilder();
    }
}
