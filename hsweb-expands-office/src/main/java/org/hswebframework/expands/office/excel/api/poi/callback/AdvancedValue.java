package org.hswebframework.expands.office.excel.api.poi.callback;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import org.hswebframework.expands.office.excel.config.CustomCellStyle;

import java.util.List;
import java.util.Map;

/**
 * @author zhouhao
 * @since 3.0.3
 */
@Getter
@Setter
public class AdvancedValue {

    private Object value;

    private List<String> options;

    private CustomCellStyle style;

    public Object getValue() {
        if (style != null && style.getValue() != null) {
            return style.getValue();
        }
        return value;
    }

    public static AdvancedValue from(Object value) {
        if (value instanceof AdvancedValue) {
            return ((AdvancedValue) value);
        }
        if (value instanceof Map) {
            return ((JSONObject) JSON.toJSON(value)).toJavaObject(AdvancedValue.class);
        }
        AdvancedValue advancedValue = new AdvancedValue();
        advancedValue.setValue(value);
        return advancedValue;
    }

}
