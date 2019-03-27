package com.wtyt.tsr.util.common;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public class ExcelUtil {
	private static final int DEFAULT_COLUMN_SIZE = 30;


    /**
     * 日期转化为字符串,格式为yyyy-MM-dd HH:mm:ss
     */
    private static String getCnDate(Date date) {
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * Excel 导出，POI实现
     *
     * @param fileName    文件名
     * @param sheetName   sheet页名称
     * @param columnNames 表头列表名
     * @param sheetTitle  sheet页Title
     * @param objects     目标数据集
     */
    public static void writeExcel(String fileName, String sheetName, List<String> columnNames,
                                  String sheetTitle, List<List<Object>> objects, boolean append,HttpServletResponse response) throws IOException {
        exportExcel(fileName,sheetName, columnNames, sheetTitle, objects, append,response);
    }


    /**
     * 导出字符串数据
     *
     * @param file        文件名
     * @param columnNames 表头
     * @param sheetTitle  sheet页Title
     * @param objects     目标数据
     * @param append      是否追加写文件
     * @return
     * @throws ReportInternalException
     */
    private static void exportExcel(String fileName,String sheetName, List<String> columnNames,
                                    String sheetTitle, List<List<Object>> objects, boolean append,HttpServletResponse response) throws IOException {
        // 声明一个工作薄
        Workbook workBook = new HSSFWorkbook();
        Map<String, CellStyle> cellStyleMap = styleMap(workBook);
        // 表头样式
        CellStyle headStyle = cellStyleMap.get("head");
        // 正文样式
        CellStyle contentStyle = cellStyleMap.get("content");
        //正文整数样式
        CellStyle contentIntegerStyle = cellStyleMap.get("integer");
        //正文带小数整数样式
        CellStyle contentDoubleStyle = cellStyleMap.get("double");
        // 生成一个表格
        Sheet sheet = workBook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workBook.createSheet(sheetName);
        }
        //最新Excel列索引,从0开始
        int lastRowIndex = sheet.getLastRowNum();
        if (lastRowIndex > 0) {
            lastRowIndex++;
        }
        // 设置表格默认列宽度
        sheet.setDefaultColumnWidth(DEFAULT_COLUMN_SIZE);
        // 合并单元格
        sheet.addMergedRegion(new CellRangeAddress(lastRowIndex, lastRowIndex, 0, columnNames.size() - 1));
        // 产生表格标题行
        Row rowMerged = sheet.createRow(lastRowIndex);
        lastRowIndex++;
        Cell mergedCell = rowMerged.createCell(0);
        mergedCell.setCellStyle(headStyle);
        mergedCell.setCellValue(new HSSFRichTextString(sheetTitle));
        // 产生表格表头列标题行
        Row row = sheet.createRow(lastRowIndex);
        lastRowIndex++;
        for (int i = 0; i < columnNames.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(headStyle);
            RichTextString text = new HSSFRichTextString(columnNames.get(i));
            cell.setCellValue(text);
        }
        // 遍历集合数据,产生数据行,前两行为标题行与表头行
        for (List<Object> dataRow : objects) {
            row = sheet.createRow(lastRowIndex);
            lastRowIndex++;
            for (int j = 0; j < dataRow.size(); j++) {
                Cell contentCell = row.createCell(j);
                Object dataObject = dataRow.get(j);
                if (dataObject != null) {
                    if (dataObject instanceof Integer) {
                        contentCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        contentCell.setCellStyle(contentIntegerStyle);
                        contentCell.setCellValue(Integer.parseInt(dataObject.toString()));
                    } else if (dataObject instanceof Double) {
                        contentCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        contentCell.setCellStyle(contentDoubleStyle);
                        contentCell.setCellValue(Double.parseDouble(dataObject.toString()));
                    } else if (dataObject instanceof Long && dataObject.toString().length() == 13) {
                        contentCell.setCellType(HSSFCell.CELL_TYPE_STRING);
                        contentCell.setCellStyle(contentStyle);
                        contentCell.setCellValue(getCnDate(new Date(Long.parseLong(dataObject.toString()))));
                    } else if (dataObject instanceof Date) {
                        contentCell.setCellType(HSSFCell.CELL_TYPE_STRING);
                        contentCell.setCellStyle(contentStyle);
                        contentCell.setCellValue(getCnDate((Date) dataObject));
                    } else {
                        contentCell.setCellType(HSSFCell.CELL_TYPE_STRING);
                        contentCell.setCellStyle(contentStyle);
                        contentCell.setCellValue(dataObject.toString());
                    }
                } else {
                    contentCell.setCellStyle(contentStyle);
                    // 设置单元格内容为字符型
                    contentCell.setCellValue("");
                }
            }
        }
        OutputStream ops = null;
        try {
        	ops = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename="+ new String((fileName + ".xls").getBytes(), "iso-8859-1"));
            workBook.write(ops);
            ops.flush();
            ops.close();
        }finally {
			ops.close();
		}
    }

    /**
     * 创建单元格表头样式
     *
     * @param workbook 工作薄
     */
    private static CellStyle createCellHeadStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        // 设置边框样式
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        //设置对齐样式
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成字体
        Font font = workbook.createFont();
        // 表头样式
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        return style;
    }

    /**
     * 创建单元格正文样式
     *
     * @param workbook 工作薄
     */
    private static CellStyle createCellContentStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        // 设置边框样式
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        //设置对齐样式
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成字体
        Font font = workbook.createFont();
        // 正文样式
        style.setFillPattern(HSSFCellStyle.NO_FILL);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style.setFont(font);
        return style;
    }

    /**
     * 单元格样式(Integer)列表
     */
    private static CellStyle createCellContent4IntegerStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        // 设置边框样式
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        //设置对齐样式
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成字体
        Font font = workbook.createFont();
        // 正文样式
        style.setFillPattern(HSSFCellStyle.NO_FILL);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style.setFont(font);
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));//数据格式只显示整数
        return style;
    }

    /**
     * 单元格样式(Double)列表
     */
    private static CellStyle createCellContent4DoubleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        // 设置边框样式
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        //设置对齐样式
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成字体
        Font font = workbook.createFont();
        // 正文样式
        style.setFillPattern(HSSFCellStyle.NO_FILL);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style.setFont(font);
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));//保留两位小数点
        return style;
    }

    /**
     * 单元格样式列表
     */
    private static Map<String, CellStyle> styleMap(Workbook workbook) {
        Map<String, CellStyle> styleMap = new LinkedHashMap<>();
        styleMap.put("head", createCellHeadStyle(workbook));
        styleMap.put("content", createCellContentStyle(workbook));
        styleMap.put("integer", createCellContent4IntegerStyle(workbook));
        styleMap.put("double", createCellContent4DoubleStyle(workbook));
        return styleMap;
    }
}
