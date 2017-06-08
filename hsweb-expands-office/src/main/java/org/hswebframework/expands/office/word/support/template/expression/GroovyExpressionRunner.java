package org.hswebframework.expands.office.word.support.template.expression;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.hswebframework.expands.office.word.support.template.expression.helper.ParagraphHelper;
import org.hswebframework.expands.office.word.support.template.expression.helper.TableHelper;
import org.hswebframework.expands.script.engine.DynamicScriptEngine;
import org.hswebframework.expands.script.engine.DynamicScriptEngineFactory;
import org.hswebframework.expands.script.engine.ExecuteResult;
import org.hswebframework.utils.ListUtils;
import org.hswebframework.utils.StringUtils;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;

import java.util.*;

/**
 * 基于Groovy的表达式运行器
 * 原理：通过解析word中的表达式,拼接为groovy脚本,然后执行
 * 当前版本已支持:
 * 段落:
 * <ul>
 * <li>变量引用:  ${name}</li>
 * </ul>
 * 表格：
 * <ul>
 * <li>变量引用:  ${name}</li>
 * <li>循环:  ${for(data in list)} | ${data.name} |${data.addr} |${/for}  。注意:表达式必须在一行</li>
 * </ul>
 * Created by 浩 on 2015-12-18 0018.
 */
public class GroovyExpressionRunner implements ExpressionRunner {

    /**
     * 内置变量标识
     */
    private int flag = 0;

    /**
     * 循环标识
     */
    private int eachFlag = 0;

    /**
     * 表格循环标识
     */
    private int t_eachFlag = 0;
    /**
     * if标识
     */
    private int ifFlag = 0;

    /**
     * 脚本
     */
    private StringBuilder script = new StringBuilder();

    /**
     * 变量
     */
    private Map<String, Object> vars = new HashMap<>();

    /**
     * 段落辅助器
     */
    private WordHelper helper = new ParagraphHelper();

    /**
     * 表格辅助器
     */
    private WordHelper cellHelper = new TableHelper();

    protected int nextFlag() {
        return ++flag;
    }

    @Override
    public void pushExpression(String expression, Object paragraph) {
        XWPFParagraph xwpfParagraph = ((XWPFParagraph) paragraph);
        List<ExpressInfo> infoList = parseExpression(xwpfParagraph);
        if (infoList.size() == 0) return;
        String p_id = "paragraph_" + nextFlag();
        String info_id = "infoList_" + nextFlag();
        vars.put(info_id, infoList);
        vars.put(p_id, paragraph);
        for (ExpressInfo expressInfo : infoList) {
            String s = expressInfo.getExpress();
            s = s.substring(2);
            s = s.substring(0, s.length() - 1).trim();
            if (s.startsWith("if")) {
                ifFlag++;
                script.append(s).append("{\n");
                expressInfo.setKeyWord(true);
            } else if (s.startsWith("/if")) {
                ifFlag--;
                script.append("}\n");
                expressInfo.setKeyWord(true);
            } else {
                String exp_id = "expression_" + nextFlag();
                vars.put(exp_id, expressInfo);
                script.append(StringUtils.concat(exp_id, ".setValue(", s, ");\n"));
            }
        }
        script.append(StringUtils.concat("_helper.init(", info_id, ",", p_id, ");\n"));
    }


    /**
     * 表达式解析
     *
     * @param paragraph 段落
     * @return 表达式信息集合
     */
    protected List<ExpressInfo> parseExpression(XWPFParagraph paragraph) {
        List<ExpressInfo> expressInfos = new LinkedList<>();
        List<XWPFRun> runs = paragraph.getRuns();
        boolean inExpress = false;
        ExpressInfo temp = null;
        int index = 0;
        for (int i = 0; i < runs.size(); i++) {
            XWPFRun run = runs.get(i);
            CTR ctr = run.getCTR();
            for (CTText ctText : ctr.getTList()) {
                String text = ctText.getStringValue();
                boolean now = false;
                if (text.contains("$")) {
                    inExpress = true;
                    temp = new ExpressInfo();
                    temp.setStartWith(index);
                    temp.setRun(run);
                    String[] t = text.split("\\$");
                    if (t.length > 1) {
                        temp.push("$" + t[1]);
                    } else {
                        temp.push("$");
                    }
                    now = true;
                }
                if ((inExpress) && text.contains("}")) {
                    temp.setEndWith(index);
                    if (now) {
                        temp.push("}");
                    } else {
                        String[] t = text.split("\\}");
                        if (t.length > 0)
                            temp.push(t[0] + "}");
                        else
                            temp.push("}");
                    }
                    expressInfos.add(temp);
                    now = true;
                    temp = null;
                    inExpress = false;
                }
                if (inExpress && !now) {
                    temp.push(text);
                }
                index++;

            }
        }

        return expressInfos;
    }

    /**
     * 段落内容解析为表达式集合,此方法并不是很好,主要用于表格渲染使用
     * 下一步将进行优化
     *
     * @param expression 表达式内容
     * @return
     */
    protected List<String> parseExpression(String expression) {
        String tmp[] = expression.split("\\$\\{");
        List<String> strList = new ArrayList<>();
        for (String s : tmp) {
            s = s.replace("｛", "{").replace("｝", "}");
            if (!s.contains("}")) {
                if (!StringUtils.isNullOrEmpty(s))
                    strList.add(StringUtils.concat("\"", s.replace("\"", "\\\""), "\""));
            } else {
                String temp2[] = s.split("\\}");
                if (temp2.length == 0) continue;
                strList.add(temp2[0].trim().replace("”", "\"").replace("“", "\"").replace("‘", "'").replace("’", "'"));
                if (temp2.length > 1) {
                    strList.add(StringUtils.concat("\"", temp2[1].replace("\"", "\\\""), "\""));
                }
            }
        }
        return strList;
    }

    /**
     * 表格,此方法仅能渲染简单的表格,不能复制样式等信息,有待优化
     *
     * @param expression 表达式内容
     * @param cell       单元格实例
     * @param paragraph  单元格段落实例
     */
    @Override
    public void pushExpression(String expression, Object cell, Object paragraph) {
        XWPFTableCell cell1 = ((XWPFTableCell) cell);
        XWPFParagraph xwpfParagraph = ((XWPFParagraph) paragraph);
        String p_id = "paragraph_" + nextFlag();
        String c_id = "cell_" + nextFlag();
        List<String> expressionList = parseExpression(expression);
        vars.put(p_id, paragraph);
        vars.put(c_id, cell);
        List<String> temp = new ArrayList<>();
        boolean endEach = false;
        for (String s : expressionList) {
            if (s.startsWith("for")) {
                t_eachFlag++;
                script.append(s).append("{\n");
                script.append(StringUtils.concat("_cellHelper.nextRow(", c_id, ");\n"));
            } else if (s.startsWith("/for")) {
                t_eachFlag--;
                script.append(StringUtils.concat("_cellHelper.nextCell(", c_id, ",", ListUtils.toString(temp.toArray()), ");\n"));
                endEach = true;
                script.append("}\n");
            } else {
                temp.add(s);
            }
        }
        if (t_eachFlag > 0) {
            script.append(StringUtils.concat("_cellHelper.nextCell(", c_id, ",", ListUtils.toString(temp.toArray()), ");\n"));
        } else {
            if (!endEach)
                script.append(StringUtils.concat("_helper.initParaGraph(", p_id, ",", ListUtils.toString(temp.toArray()), ");\n"));
        }
    }


    @Override
    public void setVar(Map<String, Object> var) {
        vars.putAll(var);
    }

    @Override
    public void run() throws Throwable {
        vars.put("_helper", helper);
        vars.put("_cellHelper", cellHelper);
        DynamicScriptEngine engine = DynamicScriptEngineFactory.getEngine("groovy");
        String id = "word.runner." + script.hashCode();
        if (!engine.compiled(id)) {
            engine.compile(id, script.toString());
        }
        ExecuteResult result = engine.execute(id, vars);
        if (result.getException() != null) {
            throw result.getException();
        }
    }

    /**
     * 表达式信息
     */
    public static class ExpressInfo {
        //内容
        private String express = "";
        private XWPFRun run;
        //表达式在段落中的开始下标
        private int startWith;
        //表达式在段落中的结束下标
        private int endWith;
        //表达式的真实值
        private Object value;
        //是否为关键字表达式,如:for,if等等,如果为true,辅助器应当不进行渲染
        private boolean keyWord;

        public boolean isKeyWord() {
            return keyWord;
        }

        public void setKeyWord(boolean keyWord) {
            this.keyWord = keyWord;
        }

        public void push(String str) {
            str = str.replace("”", "\"").replace("“", "\"").replace("‘", "'").replace("’", "'");
            if (str.equals("}") && express.endsWith("}")) return;
            express = express + str;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public XWPFRun getRun() {
            return run;
        }

        public void setRun(XWPFRun run) {
            this.run = run;
        }

        public String getExpress() {
            return express;
        }

        public void setExpress(String express) {
            this.express = express;
        }

        public int getStartWith() {
            return startWith;
        }

        public void setStartWith(int startWith) {
            this.startWith = startWith;
        }

        public int getEndWith() {
            return endWith;
        }

        public void setEndWith(int endWith) {
            this.endWith = endWith;
        }

        @Override
        public String toString() {
            return "ExpressInfo{" +
                    "express='" + express + '\'' +
                    ", run=" + run +
                    ", startWith=" + startWith +
                    ", endWith=" + endWith +
                    ", value=" + value +
                    '}';
        }
    }
}
