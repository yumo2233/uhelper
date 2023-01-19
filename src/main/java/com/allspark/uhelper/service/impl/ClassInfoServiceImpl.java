package com.allspark.uhelper.service.impl;

import com.allspark.uhelper.common.resp.classTree.NAryTree;
import com.allspark.uhelper.myenum.CollegeEnum;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.allspark.uhelper.db.pojo.ClassInfo;
import com.allspark.uhelper.service.ClassInfoService;
import com.allspark.uhelper.db.mapper.ClassInfoMapper;
import org.springframework.stereotype.Service;

import java.util.*;

/**
* @author 86159
* @description 针对表【class_info(班级的基本信息表)】的数据库操作Service实现
* @createDate 2023-01-17 02:28:46
*/
@Service
@DS("u_classinfo")
public class ClassInfoServiceImpl extends ServiceImpl<ClassInfoMapper, ClassInfo>
    implements ClassInfoService{


    public List<NAryTree> listAllClass(){
        List<NAryTree> nAryTrees = new LinkedList<>();
        List<ClassInfo> list = list();
        Set<CollegeEnum> flag=  new HashSet<>();
        Set<String> flag1 = new HashSet<>();
        for (ClassInfo classInfo : list) {
            CollegeEnum collegeEnum =  classInfo.getCollege();
            if (!flag.contains(collegeEnum)) {
                flag.add(collegeEnum);
                NAryTree nAryTree = new NAryTree();
                nAryTree.setValue(collegeEnum.getCode().longValue());
                nAryTree.setLabel(collegeEnum.getName());
                LinkedList<NAryTree> nAryChildTrees = new LinkedList<>();
                nAryTree.setChildren(nAryChildTrees);
                nAryTrees.add(nAryTree);
            }
        }
        long i=1;
        for (NAryTree nAryTree : nAryTrees) {
            for (ClassInfo classInfo : list) {
                if (nAryTree.getLabel().equals(classInfo.getCollege().getName())) {
                    String proName = classInfo.getProfessional();
                    if (!flag1.contains(proName)) {
                        flag1.add(proName);
                        NAryTree nAryTree1 = new NAryTree();
                        nAryTree1.setValue(i);
                        nAryTree1.setLabel(proName);
                        LinkedList<NAryTree> nAryChildTrees = new LinkedList<>();
                        nAryTree1.setChildren(nAryChildTrees);
                        nAryTree.getChildren().add(nAryTree1);
                        i++;
                    }
                }
            }
        }
        for (NAryTree collegeTree : nAryTrees) {
            for (NAryTree professionalTree : collegeTree.getChildren()) {
                for (ClassInfo classInfo : list) {
                    if (collegeTree.getLabel().equals(classInfo.getCollege().getName())&&professionalTree.getLabel().equals(classInfo.getProfessional())) {
                        NAryTree classTree = new NAryTree();
                        classTree.setValue(classInfo.getId());
                        classTree.setLabel(classInfo.getName().toString());
                        professionalTree.getChildren().add(classTree);
                    }
                }
            }
        }

        return  nAryTrees;
    }
}