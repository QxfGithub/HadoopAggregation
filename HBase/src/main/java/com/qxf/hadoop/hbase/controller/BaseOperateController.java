package com.qxf.hadoop.hbase.controller;

import com.qxf.hadoop.hbase.service.BaseOperateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qiuxuefu
 * @since 2019-03-18
 * @version 0.0.1
 */
@RestController
@RequestMapping(value = "/baseOperate")
@Api(tags = "BaseOperateController", description = "基础操作接口")
public class BaseOperateController {

    private Logger logger = LoggerFactory.getLogger(BaseOperateController.class);

    @Autowired
    private BaseOperateService baseOperateService;

    @RequestMapping(value = "/createTable",method = RequestMethod.POST)
    @ApiOperation("创建表")
    public String createTable(@RequestParam String tableName, @RequestParam String[] colNames)throws Exception{
        baseOperateService.createTable(tableName,colNames);
        return "SUCCESS";
    }

    @RequestMapping(value = "/dropTable",method = RequestMethod.POST)
    @ApiOperation("删除表")
    public String dropTable(@RequestParam String tableName)throws Exception{
        baseOperateService.dropTable(tableName);
        return "SUCCESS";
    }

    @RequestMapping(value = "/alterTable",method = RequestMethod.POST)
    @ApiOperation("修改表")
    public String alterTable(@RequestParam String tableName, @RequestParam String[] colNames)throws Exception{
        baseOperateService.alterTable(tableName,colNames);
        return "SUCCESS";
    }

    /**
     * http://172.16.83.175:8080/baseOperate/putRow
     * ?tableName=user_info&rowKey=1&familyName=name&columnName=userName&value=zs
     * @param tableName
     * @param rowKey
     * @param familyName
     * @param columnName
     * @param value
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/putRow",method = RequestMethod.POST)
    @ApiOperation("插入数据")
    public String putRow(@RequestParam String tableName, @RequestParam String rowKey, @RequestParam String familyName,
                         @RequestParam String columnName, @RequestParam String value)throws Exception{
        baseOperateService.putRow(tableName,rowKey,familyName,columnName,value);
        return "SUCCESS";
    }

    @RequestMapping(value = "/getRow",method = RequestMethod.GET)
    @ApiOperation("查询单行数据")
    public Result getRow(@RequestParam String tableName, @RequestParam String rowKey)throws Exception{
        return baseOperateService.getRow(tableName,rowKey);
    }

    @RequestMapping(value = "/getAllTables",method = RequestMethod.GET)
    @ApiOperation("查询所有表")
    public String[] getAllTables()throws Exception{
        return baseOperateService.getAllTables();
    }

    @RequestMapping(value = "/getColResult",method = RequestMethod.GET)
    @ApiOperation("查询单列数据")
    public Result getColResult(@RequestParam String tableName, @RequestParam String rowKey, @RequestParam String familyName,
                                 @RequestParam String columnName)throws Exception{
        return baseOperateService.getColResult(tableName,rowKey,familyName,columnName);
    }

    @RequestMapping(value = "/getResultScanner",method = RequestMethod.GET)
    @ApiOperation("遍历查询表数据")
    public ResultScanner getResultScanner(@RequestParam String tableName)throws Exception{
        return baseOperateService.getResultScanner(tableName);
    }

    @RequestMapping(value = "/deleteRow",method = RequestMethod.POST)
    @ApiOperation("删除行")
    public String deleteRow(@RequestParam String tableName,@RequestParam String rowKey)throws Exception{
        baseOperateService.deleteRow(tableName,rowKey);
        return "SUCCESS";
    }

    @RequestMapping(value = "/deleteColumn",method = RequestMethod.POST)
    @ApiOperation("删除列")
    public String deleteColumn(@RequestParam String tableName,@RequestParam String rowKey,
                                        @RequestParam String familyName, @RequestParam String columnName)throws Exception{
        baseOperateService.deleteColumn(tableName,rowKey,familyName,columnName);
        return "SUCCESS";
    }

}
