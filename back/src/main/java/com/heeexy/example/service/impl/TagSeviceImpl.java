package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.TagDao;
import com.heeexy.example.service.TagSevice;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.constants.ErrorEnum;
import com.heeexy.example.util.model.Tag;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: linchen
 * @description:
 * @date: 2019/6/21
 * @Version:
 */
@Service
public class TagSeviceImpl implements TagSevice {
    @Autowired
    private TagDao tagDao;


    @Override
    public JSONObject listTag (JSONObject jsonObject) {
        /**
         * create by: lc
         * description: 分页显示标签
         * create time: 2019/6/29 10:33
         *
          * @param jsonObject
         * @return com.alibaba.fastjson.JSONObject
         */
        CommonUtil.fillPageParam(jsonObject);
        int count = tagDao.countTag(jsonObject);
        List<JSONObject> list = tagDao.listTag(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    @Override
    public JSONObject listAllTag (JSONObject jsonObject) {
        /**
         * create by: lc
         * description: 显示标签
         * create time: 2019/6/29 10:33
         *
          * @param jsonObject
         * @return com.alibaba.fastjson.JSONObject
         */
        CommonUtil.fillPageParam(jsonObject);
        int count = tagDao.countTag(jsonObject);
        List<JSONObject> list = tagDao.listAllTag(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    @Override
    public JSONObject updateTag (JSONObject jsonObject) {
        /**
         * create by: lc
         * description:
         * create time: 2019/7/1 10:34
         *
          * @param jsonObject
         * @return com.alibaba.fastjson.JSONObject
         */
        JSONObject tagByName = tagDao.getTagByName(jsonObject);
        Integer parentId = (Integer) jsonObject.get("parentId");
        int rank;
        if(parentId == 0){
            rank =1;
        }else {
            rank = tagDao.getTagByParentId(jsonObject)+1;
        }
        if(tagByName != null){
            return CommonUtil.errorJson(ErrorEnum.E_10012);
        }else{
            jsonObject.put("rank",rank);
            tagDao.updateTag(jsonObject);
            return CommonUtil.successJson();
        }
    }

    @Override
    public JSONObject deleteTag (JSONObject jsonObject) {
        /**
         * create by: lc
         * description: 删除标签
         * create time: 2019/6/29 10:35
         *
          * @param jsonObject
         * @return com.alibaba.fastjson.JSONObject
         */
        tagDao.updateByStatus(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject batchImport (String fileName, MultipartFile file) throws Exception {
        /**
         * create by: lc
         * description: 增量导入标签
         * create time: 2019/6/29 10:35
         *
          * @param fileName
         * @param file
         * @return com.alibaba.fastjson.JSONObject
         */
        boolean notNull = false;
        List<Tag> tagList = new ArrayList<>();
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            return CommonUtil.errorJson(ErrorEnum.E_10013);
        }
        boolean isExcel2003 = true;
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
            isExcel2003 = false;
        }
        InputStream is = file.getInputStream();
        Workbook wb = null;
        if (isExcel2003) {
            wb = new HSSFWorkbook(is);
        } else {
            wb = new XSSFWorkbook(is);
        }
        Sheet sheet = wb.getSheetAt(0);
        if(sheet!=null){
            notNull = true;
        }

        for (int r = 2; r <= sheet.getLastRowNum(); r++) {//r = 2 表示从第三行开始循环 如果你的第三行开始是数据
            Row row = sheet.getRow(r);//通过sheet表单对象得到 行对象
            if (row == null){
                continue;
            }

            //sheet.getLastRowNum() 的值是 10，所以Excel表中的数据至少是10条；不然报错 NullPointerException

            Tag tag = new Tag();

            if( row.getCell(0).getCellType() !=1){//循环时，得到每一行的单元格进行判断
//                return CommonUtil.errorJson();
                return CommonUtil.errorJson(ErrorEnum.E_10014);

//                throw new MyException("导入失败(第"+(r+1)+"行,标签名称请设为文本格式)");
            }
            row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
            String pName = row.getCell(0).getStringCellValue();//得到每一行第一个单元格的值



            if(pName == null  || pName.isEmpty()){//判断是否为空
//                throw new MyException("导入失败(第"+(r+1)+"行,id未填写)");
                return CommonUtil.errorJson(ErrorEnum.E_10016);

            }
            JSONObject jsonObject =new JSONObject();
            jsonObject.put("tagName",pName);
            JSONObject tagByName = tagDao.getTagByName(jsonObject);
            Integer tagId;
            Integer rank;
            if(tagByName == null){
                Tag tag1 = new Tag();
                tag1.setTagName(pName);
                tag1.setParentId(0);
                tag1.setRank(1);
                tagDao.addListTag(tag1);
                JSONObject tagByName1 = tagDao.getTagByName(jsonObject);
                tagId = (Integer) tagByName1.get("tagId");
                rank = (Integer) tagByName1.get("rank")+1;
            }else{
                tagId = (Integer) tagByName.get("tagId");
                rank = (Integer) tagByName.get("rank")+1;
            }
            if( row.getCell(1).getCellType() !=1){//循环时，得到每一行的单元格进行判断
//                return CommonUtil.errorJson();
                return CommonUtil.errorJson(ErrorEnum.E_10015);

//                throw new MyException("导入失败(第"+(r+1)+"行,标签名称请设为文本格式)");
            }
            row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
            String tagName = row.getCell(1).getStringCellValue();//得到每一行第一个单元格的值




            if(tagName == null || tagName.isEmpty()){//判断是否为空
//                throw new MyException("导入失败(第"+(r+1)+"行,用户名未填写)");
                return CommonUtil.errorJson(ErrorEnum.E_10017);
            }
            jsonObject.put("tagName",tagName);
            int i = tagDao.countTagByName(jsonObject);
            if(i>0){

            }else{

                tag.setTagName(tagName);
                tag.setParentId(tagId);
                tag.setRank(rank);
//                tagList.add(tag);
                tagDao.addListTag(tag);
            }
            //完整的循环一次 就组成了一个对象
        }
//        for (Tag tagone : tagList) {
//            tagDao.addListTag(tagone);
//
//        }
        return CommonUtil.successJson();
    }

    @Override
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject coverImport (String fileName, MultipartFile file) throws Exception {
        /**
         * create by: lc
         * description:
         * create time: 2019/6/29 10:35
         *
          * @param fileName
         * @param file
         * @return com.alibaba.fastjson.JSONObject
         */
        tagDao.deleteAllTag();
        boolean notNull = false;
        List<Tag> tagList = new ArrayList<>();
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            return CommonUtil.errorJson(ErrorEnum.E_10013);
        }
        boolean isExcel2003 = true;
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
            isExcel2003 = false;
        }
        InputStream is = file.getInputStream();
        Workbook wb = null;
        if (isExcel2003) {
            wb = new HSSFWorkbook(is);
        } else {
            wb = new XSSFWorkbook(is);
        }
        Sheet sheet = wb.getSheetAt(0);
        if(sheet!=null){
            notNull = true;
        }

        for (int r = 2; r <= sheet.getLastRowNum(); r++) {//r = 2 表示从第三行开始循环 如果你的第三行开始是数据
            Row row = sheet.getRow(r);//通过sheet表单对象得到 行对象
            if (row == null){
                continue;
            }

            //sheet.getLastRowNum() 的值是 10，所以Excel表中的数据至少是10条；不然报错 NullPointerException

            Tag tag = new Tag();

            if( row.getCell(0).getCellType() !=1){//循环时，得到每一行的单元格进行判断
//                return CommonUtil.errorJson();
                String string = "导入失败(第"+(r+1)+"行,父标签名称请设为文本格式)";

                return  CommonUtil.errorJson(ErrorEnum.E_10014);

//                throw new MyException("导入失败(第"+(r+1)+"行,标签名称请设为文本格式)");
            }
            row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
            String pName = row.getCell(0).getStringCellValue();//得到每一行第一个单元格的值



            if(pName == null  || pName.isEmpty()){//判断是否为空
//                throw new MyException("导入失败(第"+(r+1)+"行,id未填写)");
                return CommonUtil.errorJson(ErrorEnum.E_10016);

            }
            JSONObject jsonObject =new JSONObject();
            jsonObject.put("tagName",pName);
            JSONObject tagByName = tagDao.getTagByName(jsonObject);
            Integer tagId;
            Integer rank;

            if(tagByName == null){
                Tag tag1 = new Tag();
                tag1.setTagName(pName);
                tag1.setParentId(0);
                tag1.setRank(1);
                tagDao.addListTag(tag1);
                JSONObject tagByName1 = tagDao.getTagByName(jsonObject);
                tagId = (Integer) tagByName1.get("tagId");
                rank = (Integer) tagByName1.get("rank")+1;

            }else{
                tagId = (Integer) tagByName.get("tagId");
                rank = (Integer) tagByName.get("rank")+1;

            }
            if( row.getCell(1).getCellType() !=1){//循环时，得到每一行的单元格进行判断
//                return CommonUtil.errorJson();
                return  CommonUtil.errorJson(ErrorEnum.E_10015);

//                throw new MyException("导入失败(第"+(r+1)+"行,标签名称请设为文本格式)");
            }
            row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
            String tagName = row.getCell(1).getStringCellValue();//得到每一行第一个单元格的值




            if(tagName == null || tagName.isEmpty()){//判断是否为空
//                throw new MyException("导入失败(第"+(r+1)+"行,用户名未填写)");
                return CommonUtil.errorJson(ErrorEnum.E_10017);
            }
            jsonObject.put("tagName",tagName);
            int i = tagDao.countTagByName(jsonObject);
            if(i>0){

            }else{

                tag.setTagName(tagName);
                tag.setParentId(tagId);
                tag.setRank(rank);

                tagDao.addListTag(tag);
            }
            //完整的循环一次 就组成了一个对象
        }
//        for (Tag tagone : tagList) {
//            tagDao.addListTag(tagone);
//
//        }
        return CommonUtil.successJson();
    }
}
