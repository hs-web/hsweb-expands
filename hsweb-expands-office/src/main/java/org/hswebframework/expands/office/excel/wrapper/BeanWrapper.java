package org.hswebframework.expands.office.excel.wrapper;


import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.hswebframework.utils.ClassUtils;
import org.hswebframework.utils.StringUtils;

import java.beans.PropertyDescriptor;

/**
 * Created by 浩 on 2015-12-07 0007.
 */
public class BeanWrapper<T> extends AbstractWrapper<T> {

    private Class<T> type;

    @Override
    public T newInstance() throws Exception {
        if (type == null) {
            type = (Class<T>) ClassUtils.getGenericType(this.getClass());
        }
        return type.newInstance();
    }

    @Override
    public void wrapper(T instance, String header, Object value) {
        if (header == null || "".equals(header)) return;
        header=header.trim();
        header = headerMapper(header);
        try {
            PropertyDescriptor propertyDescriptor = BeanUtilsBean.getInstance()
                    .getPropertyUtils().getPropertyDescriptor(instance, header);
            if (propertyDescriptor != null) {
                value = changeType(value, propertyDescriptor.getPropertyType());
            }
            BeanUtils.setProperty(instance, header, value);
        } catch (Exception e) {
        }
    }


    protected Object changeType(Object value, Class<?> paramType) {
        if (value.getClass() == paramType) return value;
        if (paramType == int.class || paramType == Integer.class) {
            value = StringUtils.toInt(value);
        }
        if (paramType == double.class || paramType == Double.class) {
            value = StringUtils.toDouble(value);
        }
        if (paramType == float.class || paramType == Float.class) {
            value = (float) StringUtils.toDouble(value);
        }
        return value;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }
}
