package com.mr.order.pojo;


import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "tb_vip")
public class Vip {

    @Id
    private Long userId; //用户id
    private String stauts; //充值状态 1 是成功  0 是失败
    private Date opTime; //开通时间
    private String opDay; //开通时长(月)
    private Date ovTime; //到期时间


}
