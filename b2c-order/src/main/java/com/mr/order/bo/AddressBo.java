package com.mr.order.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressBo {
    private Long id; //主键id
    private String name;//收货人名称
    private String phone;//电话号码
    private String state;//省份
    private String city;//城市
    private String district;//区
    private String address;//街道详细
    private String zipCode;//邮编
    private Boolean isDefault;//是否默认
    private Long userId;//用户id
    //初始化数据，正常应从数据库加载，
    public static Map<Long,AddressBo> addreddMap;
    static {
        AddressBo add1 = new AddressBo(1l, "马帅轻", "13333333333", "河南省", "郑州市", "管城回族区", "鼎瑞街", "300000", true, 7l);
        AddressBo add2 = new AddressBo(2l, "玉玺", "16666666666", "北京省", "北京市", "沙河区", "沙河街", "100000", false, 7l);
        addreddMap=new HashMap<>();
        addreddMap.put(add1.getId(),add1);
        addreddMap.put(add2.getId(),add2);
    }

}
