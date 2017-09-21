package org.hswebframework.expands.office.word.support.template;

import org.hswebframework.expands.office.word.api.poi.POIWordApi4Docx;
import org.hswebframework.utils.StringUtils;

import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhouhao on 16-4-10.
 */
public class DOCXTemplateReader {

    private InputStream templateInput, sourceInput;

    private StringBuilder template = new StringBuilder(), source = new StringBuilder();

    public DOCXTemplateReader(InputStream templateInput, InputStream sourceInput) throws Exception {
        this.templateInput = templateInput;
        this.sourceInput = sourceInput;

        initTemplate();
    }

    protected String randomChar(int l) {
        char cs[] = {'a', 'b', 'c', 'd', 'e', 'f',
                'A', 'B', 'C', 'D', 'E', 'F'};
        char str[] = new char[l];
        Random random = new Random();
        for (int i = 0; i < l; i++) {
            str[i] = cs[random.nextInt(cs.length)];
        }
        return new String(str);
    }

    protected void initTemplate() throws Exception {
        final List<String> appendStr = new ArrayList<>();
        POIWordApi4Docx.getInstance().read(templateInput, new DOCXStringReader() {
            @Override
            public void readLine(String text) {
                if (StringUtils.isNullOrEmpty(text)) return;
                String str = randomChar(5);
                appendStr.add(str);
                template.append(str).append(String.valueOf(text.trim()).replace("\n", "")).append(str);
            }
        });
        POIWordApi4Docx.getInstance().read(sourceInput, new DOCXStringReader() {
            int i = 0;

            @Override
            public void readLine(String text) {
                if (StringUtils.isNullOrEmpty(text)) return;
                String str = "";
                if (appendStr.size()> i)
                    str = appendStr.get(i++);
                source.append(str).append(String.valueOf(text.trim()).replace("\n", "")).append(str);
            }
        });
    }

    public List<Map<String, Object>> read() {
        return text2object(source.toString(), template.toString());
    }

    public static final List<Map<String, Object>> text2object(String txt, String template) {
        List<Map<String, Object>> datas = new ArrayList<>();
        Pattern prepared = Pattern.compile("(?<=\\$\\{)(.+?)(?=\\})");
        Matcher matcher = prepared.matcher(template);
        List<ExpressInfo> filed = new ArrayList<>();
        while (matcher.find()) {
            String group = matcher.group();
            String express = StringUtils.concat("${", group, "}");
            int index = template.indexOf(express);
            int express_end = index + express.length();
            String endOf = "\\s", startOf = "";
            if (express_end + 5 < template.length()) {
                endOf = template.substring(express_end, express_end + 5);
            }
            if (endOf.contains("${"))
                endOf = " ";
            if (index > 5) {
                startOf = template.substring(index - 5, index);
            }
            String pat = StringUtils.concat("(?<=", startOf, ")(.*?)(?=", endOf, ")");
            Pattern pattern = Pattern.compile(pat);
            ExpressInfo info = new ExpressInfo();
            info.setField(group);
            info.setPattern(pattern);
            filed.add(info);
        }
        for (ExpressInfo info : filed) {
            Map<String, Object> data = new HashMap<>();
            datas.add(data);
            Pattern pattern = info.getPattern();
            Matcher prepared_matcher = pattern.matcher(txt);
            if (prepared_matcher.find()) {
                data.put(info.field, prepared_matcher.group());
            }
        }
        return datas;
    }

    protected static class ExpressInfo {
        private String field;
        private Pattern pattern;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public Pattern getPattern() {
            return pattern;
        }

        public void setPattern(Pattern pattern) {
            this.pattern = pattern;
        }
    }

}
