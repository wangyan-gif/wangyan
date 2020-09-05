package com.mr.service.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Table(name="tb_spec_group")
@Data
public class SpecGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long cid;
    private String name;

    //一个规格组有多个规格  需要忽略
    @Transient
    private List<SpecParam> specParamList;

}
