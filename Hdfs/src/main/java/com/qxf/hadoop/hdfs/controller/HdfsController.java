package com.qxf.hadoop.hdfs.controller;


import com.qxf.hadoop.hdfs.model.User;
import com.qxf.hadoop.hdfs.util.HdfsUtil;
import com.qxf.hadoop.hdfs.vo.BaseReturnVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.BlockLocation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/*import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;*/

/**
 * @author qiuxuefu
 * @since 2019-03-18
 * @version 0.0.1
 */
@RestController
@RequestMapping("/hdfs")
@Api(tags = "HdfsController", description = "基础操作接口")
public class HdfsController {

    @PostMapping("/mkdir")
    @ApiOperation("创建文件夹")
    public BaseReturnVO mkdir(@RequestParam("path") String path) throws Exception {
        if (StringUtils.isEmpty(path)) {
            return new BaseReturnVO("请求参数为空");
        }
         //创建空文件夹
        boolean isOk = HdfsUtil.mkdir(path);
        if (isOk) {
            return new BaseReturnVO("create dir success");
        } else {
            return new BaseReturnVO("create dir fail");
        }
    }

    @PostMapping("/readPathInfo")
    @ApiOperation("读取HDFS目录信息")
    public BaseReturnVO readPathInfo(@RequestParam("path") String path) throws Exception {
        List<Map<String, Object>> list = HdfsUtil.readPathInfo(path);
        return new BaseReturnVO(list);
    }

    @PostMapping("/getFileBlockLocations")
    @ApiOperation("获取HDFS文件在集群中的位置")
    public BaseReturnVO getFileBlockLocations(@RequestParam("path") String path) throws Exception {
        BlockLocation[] blockLocations = HdfsUtil.getFileBlockLocations(path);
        return new BaseReturnVO(blockLocations);
    }

    @PostMapping("/createFile")
    @ApiOperation("创建文件")
    public BaseReturnVO createFile(@RequestParam("path") String path, @RequestParam("file") MultipartFile file) throws Exception {
        if (StringUtils.isEmpty(path) || null == file.getBytes()) {
            return new BaseReturnVO("请求参数为空");
        }
        HdfsUtil.createFile(path, file);
        return new BaseReturnVO("create file success");
    }

    @PostMapping("/readFile")
    @ApiOperation("读取HDFS文件内容")
    public BaseReturnVO readFile(@RequestParam("path") String path) throws Exception {
        String targetPath = HdfsUtil.readFile(path);
        return new BaseReturnVO(targetPath);
    }

    @PostMapping("/openFileToBytes")
    @ApiOperation("读取HDFS文件转换成Byte类型")
    public BaseReturnVO openFileToBytes(@RequestParam("path") String path) throws Exception {
        byte[] files = HdfsUtil.openFileToBytes(path);
        return new BaseReturnVO(files);
    }

    @PostMapping("/openFileToUser")
    @ApiOperation("读取HDFS文件装换成User对象")
    public BaseReturnVO openFileToUser(@RequestParam("path") String path) throws Exception {
        User user = HdfsUtil.openFileToObject(path, User.class);
        return new BaseReturnVO(user);
    }

    @PostMapping("/listFile")
    @ApiOperation("读取文件列表")
    public BaseReturnVO listFile(@RequestParam("path") String path) throws Exception {
        if (StringUtils.isEmpty(path)) {
            return new BaseReturnVO("请求参数为空");
        }
        List<Map<String, String>> returnList = HdfsUtil.listFile(path);
        return new BaseReturnVO(returnList);
    }

    @PostMapping("/renameFile")
    @ApiOperation("重命名文件")
    public BaseReturnVO renameFile(@RequestParam("oldName") String oldName, @RequestParam("newName") String newName) throws Exception {
        if (StringUtils.isEmpty(oldName) || StringUtils.isEmpty(newName)) {
            return new BaseReturnVO("请求参数为空");
        }
        boolean isOk = HdfsUtil.renameFile(oldName, newName);
        if (isOk) {
            return new BaseReturnVO("rename file success");
        } else {
            return new BaseReturnVO("rename file fail");
        }
    }

    @PostMapping("/deleteFile")
    @ApiOperation("删除文件")
    public BaseReturnVO deleteFile(@RequestParam("path") String path) throws Exception {
        boolean isOk = HdfsUtil.deleteFile(path);
        if (isOk) {
            return new BaseReturnVO("delete file success");
        } else {
            return new BaseReturnVO("delete file fail");
        }
    }

    @PostMapping("/uploadFile")
    @ApiOperation("上传文件")
    public BaseReturnVO uploadFile(@RequestParam("path") String path, @RequestParam("uploadPath") String uploadPath) throws Exception {
        HdfsUtil.uploadFile(path, uploadPath);
        return new BaseReturnVO("upload file success");
    }

    @PostMapping("/downloadFile")
    @ApiOperation("下载文件")
    public BaseReturnVO downloadFile(@RequestParam("path") String path, @RequestParam("downloadPath") String downloadPath) throws Exception {
        HdfsUtil.downloadFile(path, downloadPath);
        return new BaseReturnVO("download file success");
    }

    @PostMapping("/copyFile")
    @ApiOperation("HDFS文件复制")
    public BaseReturnVO copyFile(@RequestParam("sourcePath") String sourcePath, @RequestParam("targetPath") String targetPath) throws Exception {
        HdfsUtil.copyFile(sourcePath, targetPath);
        return new BaseReturnVO("copy file success");
    }

    @PostMapping("/existFile")
    @ApiOperation("文件是否存在")
    public BaseReturnVO existFile(@RequestParam("path") String path) throws Exception {
        boolean isExist = HdfsUtil.existFile(path);
        return new BaseReturnVO("file isExist: " + isExist);
    }
}


