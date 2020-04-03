package com.apriltechnology.achieveit.service.impl;

import com.apriltechnology.achieveit.dto.ProjectInfoAdd;
import com.apriltechnology.achieveit.dto.ProjectInfoSearch;
import com.apriltechnology.achieveit.entity.ProjectInfo;
import com.apriltechnology.achieveit.exception.BatchDeleteException;
import com.apriltechnology.achieveit.mapper.ProjectInfoMapper;
import com.apriltechnology.achieveit.service.ProjectInfoService;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description 项目信息service实现层
 * @Author fjm
 * @Date 2020/3/17
 */
@Service
public class ProjectInfoServiceImpl implements ProjectInfoService {

    @Autowired
    private ProjectInfoMapper projectInfoMapper;

    @Override
    public List<ProjectInfo> getProjectInfo(ProjectInfoSearch projectInfoSearch) {

        Integer offset = (projectInfoSearch.getPageNum()-1)*projectInfoSearch.getPageSize();
        Integer limit = projectInfoSearch.getPageSize();
        List<ProjectInfo> projectInfos = projectInfoMapper.getProjectInfoList(projectInfoSearch.getId(), projectInfoSearch.getProjectName(), projectInfoSearch.getCustomerInfo(),
                projectInfoSearch.getLeader(), projectInfoSearch.getMilepost(), projectInfoSearch.getProjectFunction(), projectInfoSearch.getTechnology(), projectInfoSearch.getBusinessArea(),
                projectInfoSearch.getScheduleTime(), projectInfoSearch.getDeliveryTime(),offset,limit);

        return projectInfos;
    }

    @Override
    public Integer getProjectInfoCount(ProjectInfoSearch projectInfoSearch) {

        Integer count = projectInfoMapper.getProjectInfoCount(projectInfoSearch.getId(), projectInfoSearch.getProjectName(), projectInfoSearch.getCustomerInfo(),
                projectInfoSearch.getLeader(), projectInfoSearch.getMilepost(), projectInfoSearch.getProjectFunction(), projectInfoSearch.getTechnology(), projectInfoSearch.getBusinessArea(),
                projectInfoSearch.getScheduleTime(), projectInfoSearch.getDeliveryTime());
        return count;
    }

    @Override
    public Pair<Boolean,String> editProjectInfo(ProjectInfoSearch projectInfoSearch) {

        int result = projectInfoMapper.updateProjectInfo(projectInfoSearch);
        if(result <= 0){
            return new Pair<>(false,"更新失败！");
        }else{
            return new Pair<>(true,"更新成功！");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public Pair<Boolean, String> deleteProjectInfo(List<Long> projectIds) throws BatchDeleteException{

        if(null != projectIds && projectIds.size() > 0){
            int size = projectIds.size();
            int result = projectInfoMapper.deleteProjectInfo(projectIds);
            if(result == size){
                return new Pair<>(true,"删除成功！");
            }else{
                throw new BatchDeleteException("删除失败！");
            }
        }

        return new Pair<>(false,"请选择需要删除的项目！");
    }

    @Override
    public Pair<Boolean, String> insertProjectInfo(ProjectInfoAdd projectInfoAdd, Long createId, int status) {

        ProjectInfo projectInfo = this.dataChange(projectInfoAdd,createId,status);
        int count = projectInfoMapper.insertProjectInfo(projectInfo);
        if(count > 0){
            return new Pair<>(true,"新增成功！");
        }else{
            return new Pair<>(false,"新增失败！");
        }

    }

    //实体类转移
    private ProjectInfo dataChange(ProjectInfoAdd projectInfoAdd,Long createId,int status){
        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setCreaterId(createId);
        projectInfo.setProjectName(projectInfoAdd.getProjectName());
        projectInfo.setCustomerInfo(projectInfoAdd.getCustomerInfo());
        projectInfo.setLeader(projectInfoAdd.getLeader());
        projectInfo.setMilepost(projectInfoAdd.getMilepost());
        projectInfo.setProjectFunction(projectInfoAdd.getProjectFunction());
        projectInfo.setTechnology(projectInfoAdd.getTechnology());
        projectInfo.setBusinessArea(projectInfoAdd.getBusinessArea());
        projectInfo.setScheduleTime(projectInfoAdd.getScheduleTime());
        projectInfo.setDeliveryTime(projectInfoAdd.getDeliveryTime());
        projectInfo.setStatus(status);
        return projectInfo;
    }

    @Override
    public int getProjectStatus(Long projectId) {

        int status = projectInfoMapper.getProjectStatus(projectId);
        return status;
    }

    @Override
    public Pair<Boolean,String> changeProjectStatus(Long projectId, Integer status) {

        int result = projectInfoMapper.changeProjectStatus(projectId,status);
        if(result > 0){
            return new Pair<>(true,"状态更新成功！");
        }else{
            return new Pair<>(false,"更新失败！");
        }

    }

    @Override
    public List<ProjectInfo> searchMyTaskProjectInfo(String leader, Integer status) {

        List<ProjectInfo> projectInfos = projectInfoMapper.getMyTaskProjectInfo(leader,status);
        return projectInfos;
    }

    @Override
    public List<ProjectInfo> searchMyProjectInfo(Long createrId) {

        List<ProjectInfo> projectInfos = projectInfoMapper.getMyProjectInfo(createrId);
        return projectInfos;
    }


}
