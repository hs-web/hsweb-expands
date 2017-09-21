package org.hswebframework.expands.office.excel.api.poi.callback;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.hswebframework.expands.office.excel.config.*;
import org.hswebframework.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by 浩 on 2015-12-16 0016.
 */
public class CommonExcelWriterCallBack implements ExcelWriterCallBack {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private ExcelWriterConfig config;
    private final PropertyUtilsBean propertyUtils = BeanUtilsBean.getInstance().getPropertyUtils();

    public CommonExcelWriterCallBack(ExcelWriterConfig config) {
        this.config = config;
    }

    private ExcelWriterProcessor processor;

    private Sheet sheet;

    @Override
    public void render(ExcelWriterProcessor processor) {
        try {
            logger.info("create sheet [{}]", config.getSheetName());
            if (config.getSheetName() != null)
                sheet = processor.start(config.getSheetName());
            else {
                sheet = processor.start();
            }
            this.processor = processor;
            logger.info("sheet [{}] build data ", config.getSheetName());
            buildData();
            logger.info("sheet [{}]  merge ", config.getSheetName());
            startMerge();
            processor.done();
            logger.info("sheet [{}]  done ", config.getSheetName());
        } catch (Exception e) {
            logger.error("writer processor error!", e);
        }
    }

    protected void buildData() {
        List<org.hswebframework.expands.office.excel.config.Header> headers = config.getHeaders();
        List<Object> datas = config.getDatas();

        for (int y = 0; y < config.getStartWith(); y++) {
            Row row = processor.nextRow();
            initRow(row, y, null);
            for (int i = 0; i < headers.size(); i++) {
                Object value = config.startBefore(y, i);
                Cell cell = processor.nextCell();
                initCell(y, i, cell, headers.get(i).getField(), value);
            }
        }

        initHeader();

        for (int x = 0; x < datas.size(); x++) {
            if (logger.isInfoEnabled())
                logger.info("sheet [{}] build data row[{}]", config.getSheetName(), x);
            Row row = processor.nextRow();
            Object data = datas.get(x);
            int index = 0;
            for (org.hswebframework.expands.office.excel.config.Header header : headers) {
                Cell cell = processor.nextCell();
                Object value = null;
                try {
                    value = propertyUtils.getProperty(data, header.getField());
                } catch (Exception e) {
                }
                if (value == null) value = "";
                initCell(x, index, cell, header.getField(), value);
                if (index++ == 0) {
                    initRow(row, row.getRowNum(), header.getField());
                }
            }
        }
    }


    protected void initRow(Row row, int index, String header) {
        CustomRowStyle rowStyle = config.getRowStyle(index, header);
        if (rowStyle != null) {
            row.setHeightInPoints((float) rowStyle.getHeight());
        }
    }

    protected void initHeader() {
        processor.nextRow();//创建1行
        List<org.hswebframework.expands.office.excel.config.Header> headers = config.getHeaders();
        if (config.getStartWith() > 0) {
            for (int x = 0; x < config.getStartWith(); x++) {
                for (int y = 0, len = headers.size(); y < len; y++) {
                    org.hswebframework.expands.office.excel.config.Header header = headers.get(y);
                    CustomColumnStyle style = config.getColumnStyle(y, header.getTitle());
                    if (null != style) {
                        sheet.setColumnWidth(y, style.getWidth());
                    }
                    Cell cell = processor.nextCell();
                    initCell(x, y, cell, header.getField(), header.getTitle());
                    CustomCellStyle titleStyle = header.getStyle();
                    if (null != titleStyle) {
                        CellStyle cacheStyle = getStyle(titleStyle);
                        cell.setCellStyle(cacheStyle);
                    }
                }
            }
        } else {
            for (int y = 0, len = headers.size(); y < len; y++) {
                org.hswebframework.expands.office.excel.config.Header header = headers.get(y);
                Cell cell = processor.nextCell();
                CustomColumnStyle style = config.getColumnStyle(y, header.getTitle());
                if (null != style) {
                    sheet.setColumnWidth(y, style.getWidth());
                }
                initCell(0, y, cell, header.getField(), header.getTitle());
                CustomCellStyle titleStyle = header.getStyle();
                if (null != titleStyle) {
                    CellStyle cacheStyle = getStyle(titleStyle);
                    cell.setCellStyle(cacheStyle);
                }

            }
        }

    }

    protected void initCell(Cell cell, Object value) {
        cell.setCellValue(String.valueOf(value));
    }

    protected void initCell(int r, int c, Cell cell, String header, Object value) {
        CustomCellStyle style;
        //如果通过回掉未获取到自定义样式,则使用默认的样式进行处理
        if ((style = config.getCellStyle(r, c, header, value)) == null) {
            initCell(cell, value);
            return;
        }
        CellStyle cellStyle = getStyle(style);
        cell.setCellStyle(cellStyle);
        //根据指定的数据类型,转为excel中的值
        switch (style.getDataType()) {
            case "date":
                cell.setCellValue((Date) style.getValue());
                break;
            case "int":
                cell.setCellValue(StringUtils.toInt(style.getValue()));
                break;
            case "double":
                cell.setCellValue(StringUtils.toDouble(style.getValue()));
                break;
            default:
                cell.setCellValue(String.valueOf(style.getValue()));
                break;
        }
    }

    protected void startMerge() {
        prepareMerges();
        List<ExcelWriterConfig.Merge> merges = config.getMerges();
        for (ExcelWriterConfig.Merge merge : merges) {
            try {
                sheet.addMergedRegion(new CellRangeAddress(merge.getRowFrom() + config.getStartWith(), merge.getColTo() + config.getStartWith(), merge.getColFrom(), merge.getRowTo()));
            } catch (Exception e) {
                logger.error("merge column ({}) error", merge, e);
            }
        }
    }


    /**
     * 编译需要合并的列
     *
     * @throws Exception
     */
    protected void prepareMerges() {
        //解析表头与列号
        List<String> list = config.getMergeColumns();
//        Map<String, Integer> cols = new LinkedHashMap<>();
        Map<String, Integer> cols = config.getHeaders().stream()
                .filter(header -> list.contains(header.getField()))
                .collect(Collectors.toMap(org.hswebframework.expands.office.excel.config.Header::getField, config.getHeaders()::indexOf, (u, l) -> l, LinkedHashMap::new));

//        config.getHeaders().stream().filter(header -> list.contains(header.getField())).forEach(header -> {
//            cols.put(header.getField(), config.getHeaders().indexOf(header));
//        });
        List<Object> datas = config.getDatas();

        // 列所在索引//列计数器////上一次合并的列位置
        int index, countNumber, lastMergeNumber;
        //已合并列的缓存
        List<String> temp = new ArrayList<>();
        // 遍历要合并的列名
        for (String header : cols.keySet()) {
            index = cols.get(header);// 列所在索引
            countNumber = lastMergeNumber = 0;
            Object lastData = null;// 上一行数据
            // 遍历列
            int dataIndex = 0;
            for (Object data : config.getDatas()) {
                dataIndex++;
                Object val = null;
                try {
                    val = BeanUtils.getProperty(data, header);
                } catch (Exception e) {
                }
                if (val == null)
                    val = "";
                //如果上一列的本行未进行合并,那么这一列也不进行合并
                if (index != 0 && !temp.contains(StringUtils.concat("c_", index - 1, "_d", dataIndex))) {
                    lastData = "__$$";
                }
                // 如果当前行和上一行相同 ，合并列数+1
                if ((val.equals(lastData) || lastData == null)) {
                    countNumber++;
                    temp.add(StringUtils.concat("c_", index, "_d", dataIndex));
                } else {
                    // 与上一行不一致，代表本次合并结束
                    config.addMerge(lastMergeNumber + 1, index, index, countNumber);
                    lastMergeNumber = countNumber;// 记录当前合并位置
                    countNumber++;// 总数加1

                }
                // 列末尾需要合并
                if (datas.indexOf(data) == datas.size() - 1) {
                    config.addMerge(lastMergeNumber + 1, index, index, datas.size());
                    temp.add(StringUtils.concat("c_", index, "_d", dataIndex));
                }
                // 上一行数据
                lastData = val;
            }
        }
    }

    /**
     * 根据自定义样式获取excel单元格样式实例,如果已初始化过则获取缓存中的样式
     *
     * @param customCellStyle 自定义样式
     * @return 单元格样式实例
     */
    private CellStyle getStyle(CustomCellStyle customCellStyle) {
        //尝试获取缓存
        CellStyle style = getStyleFromCache(customCellStyle.getCacheKey());
        Workbook workbook = sheet.getWorkbook();
        if (style == null) {
            //为获取到缓存则初始化
            style = workbook.createCellStyle();
            //字体
            if (customCellStyle.getFontName() != null || customCellStyle.getFontColor() != 0) {
                Font font = workbook.createFont();
                if (customCellStyle.getFontName() != null) {
                    font.setFontName(customCellStyle.getFontName());
                }
                if (customCellStyle.getFontColor() != 0) {
                    font.setColor(customCellStyle.getFontColor());
                }
                style.setFont(font);
            }
            //表格
            if (customCellStyle.getBorderTop() != null) {
                style.setBorderTop(customCellStyle.getBorderTop().getSize());
                style.setTopBorderColor(customCellStyle.getBorderTop().getColor());
            }
            if (customCellStyle.getBorderBottom() != null) {
                style.setBorderBottom(customCellStyle.getBorderBottom().getSize());
                style.setBottomBorderColor(customCellStyle.getBorderBottom().getColor());
            }
            if (customCellStyle.getBorderLeft() != null) {
                style.setBorderLeft(customCellStyle.getBorderTop().getSize());
                style.setLeftBorderColor(customCellStyle.getBorderTop().getColor());
            }
            if (customCellStyle.getBorderRight() != null) {
                style.setBorderRight(customCellStyle.getBorderTop().getSize());
                style.setRightBorderColor(customCellStyle.getBorderTop().getColor());
            }
            //数据格式
            if (customCellStyle.getFormat() != null) {
                DataFormat dataFormat = workbook.createDataFormat();
                style.setDataFormat(dataFormat.getFormat(customCellStyle.getFormat()));
            }
            // 水平
            style.setAlignment(customCellStyle.getAlignment());
            // 垂直
            style.setVerticalAlignment(customCellStyle.getVerticalAlignment());
            //放入缓存
            putStyleFromCache(customCellStyle.getCacheKey(), style);
        }
        return style;
    }

    /**
     * 单元格样式缓存
     */
    private Map<String, CellStyle> cellStyleCache = new ConcurrentHashMap<>();

    /**
     * 设置一个工作簿的的单元格样式到缓存中
     *
     * @param key       缓存key
     * @param cellStyle 单元格样式
     */
    private void putStyleFromCache(String key, CellStyle cellStyle) {
        cellStyleCache.put(key, cellStyle);
    }

    /**
     * 从缓存中获取指定工作簿的指定样式
     *
     * @param key 样式key
     * @return 单元格样式，如果没有则返回null
     */
    private CellStyle getStyleFromCache(String key) {
        return cellStyleCache.get(key);
    }

}
