package org.hswebframework.expands.office.word.support.template.expression.helper;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.hswebframework.expands.office.word.support.template.expression.GroovyExpressionRunner;
import org.hswebframework.expands.office.word.support.template.expression.WordHelper;
import org.hswebframework.utils.StringUtils;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;

import java.util.List;

/**
 * 段落渲染辅助器
 * Created by 浩 on 2015-12-18 0018.
 */
public class ParagraphHelper implements WordHelper {

    /**
     * 初始化一个段落
     *
     * @param infoList  表达式信息
     * @param paragraph 段落实例
     */
    public void init(List<GroovyExpressionRunner.ExpressInfo> infoList, XWPFParagraph paragraph) {
        for (GroovyExpressionRunner.ExpressInfo expressInfo : infoList) {
            int cindex = 0, rindex = 0;
            for (XWPFRun run : paragraph.getRuns()) {
                for (CTText ctText : run.getCTR().getTList()) {
                    //如果当前位置是一个表达式,则将值替换为空
                    if (cindex > expressInfo.getStartWith() && cindex < expressInfo.getEndWith() + 1)
                        ctText.setStringValue("");
                    cindex++;
                }
                rindex++;
            }
                XWPFRun run = expressInfo.getRun();
                int index = 0;
                for (CTText ctText : run.getCTR().getTList()) {
                    //将表达式的第一个文本设置为真实的值，其他设置为空,因此,此段落的样式为表达式第一个文字的样式
                    if (index == 0&&!expressInfo.isKeyWord()) {
                        ctText.setStringValue(String.valueOf(expressInfo.getValue()));
                    } else {
                        ctText.setStringValue("");
                    }
                }
        }
    }

    /**
     * 直接填充内容到段落,将忽略从1-end的所有文字样式
     *
     * @param paragraph 段落实例
     * @param params    填充内容
     */
    public void initParaGraph(XWPFParagraph paragraph, Object... params) {
        if (params == null) return;
        int index = 0;
        for (XWPFRun run : paragraph.getRuns()) {
            for (CTText ctText : run.getCTR().getTList()) {
                if (index++ == 0) {
                    ctText.setStringValue(StringUtils.concat(params));
                } else {
                    ctText.setStringValue("");
                }
            }
        }
    }

    private XWPFParagraph newPTemp = null;

    /**
     * 创建一个新段落,暂未实现此功能
     *
     * @param paragraph
     * @param params
     */
    public void newParaGraph(XWPFParagraph paragraph, Object... params) {
        initParaGraph(paragraph, params);
    }

    @Override
    public void reset() {

    }
}
