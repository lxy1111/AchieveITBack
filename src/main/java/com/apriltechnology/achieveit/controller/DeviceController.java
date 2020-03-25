package com.apriltechnology.achieveit.controller;

import com.apriltechnology.achieveit.dto.DeviceSearch;
import com.apriltechnology.achieveit.dto.ProjectInfoSearch;
import com.apriltechnology.achieveit.dto.Response;
import com.apriltechnology.achieveit.entity.Device;
import com.apriltechnology.achieveit.entity.ProjectInfo;
import com.apriltechnology.achieveit.service.DeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: lxy
 * @Date: 2020/3/25 19:43
 */

@Controller
@RequestMapping("/Device")
@Api(tags = "设备信息接口")
@Slf4j
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @PostMapping("/Search")
    @ResponseBody
    @ApiOperation("查询设备信息")
    Response deviceSearch(@RequestBody DeviceSearch deviceSearch){

        if(null == deviceSearch.getPageNum()){
            deviceSearch.setPageNum(0);
        }

        if(null == deviceSearch.getPageSize()){
            deviceSearch.setPageSize(10);
        }

        Response response = new Response();

        Integer count = deviceService.getDeviceCount(deviceSearch);

        List<Device> devices = deviceService.getDeviceList(deviceSearch);
        Map<String,List<Device>> map = new HashMap<>();
        map.put("deviceList",devices);

        response.setCode("0");
        response.setMsg("查询成功");
        response.setCount(count);
        response.setData(map);

        return response;
    }

    @PostMapping("/Edit")
    @ResponseBody
    @ApiOperation("更新设备信息")
    Response editDevice(@RequestBody DeviceSearch deviceSearch){

        Response response = new Response();

        Pair<Boolean,String> result = deviceService.editDevice(deviceSearch);
        if(result.getKey()){
            response.setCode("0");
            response.setMsg(result.getValue());
            return response;
        }else{
            return Response.createError("`1",result.getValue());
        }
    }



}
