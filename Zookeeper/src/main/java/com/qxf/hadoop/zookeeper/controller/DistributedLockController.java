package com.qxf.hadoop.zookeeper.controller;

import com.qxf.hadoop.zookeeper.service.DistributedLockByCurator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

/**
 * @author qiuxuefu
 * @since 2019-04-11
 * @version 0.0.1
 */
@RestController
@RequestMapping("/zookeeper")
@Api(tags = "DistributedLockController", description = "分布式锁")
public class DistributedLockController {
    private final static String ROOT_PATH_LOCK = "rootlock";

    @Autowired
    DistributedLockByCurator distributedLockByCurator;

    @PostMapping("/createNode")
    @ApiOperation("创建节点")
    public String createNode(@PathParam("path") String path) throws Exception {
        distributedLockByCurator.createNode(path);
        return "Success";
    }

    @GetMapping("/acquireDistributedLock")
    @ApiOperation("获取分布式锁")
    public String acquireDistributedLock() throws Exception {
        distributedLockByCurator.acquireDistributedLock(ROOT_PATH_LOCK);
        return "Success";
    }

    @GetMapping("/releaseDistributedLock")
    @ApiOperation("释放分布式锁")
    public String releaseDistributedLock() throws Exception {
        distributedLockByCurator.releaseDistributedLock(ROOT_PATH_LOCK);
        return "Success";
    }
}
