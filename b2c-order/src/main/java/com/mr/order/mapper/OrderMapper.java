package com.mr.order.mapper;

import com.mr.order.pojo.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper extends tk.mybatis.mapper.common.Mapper<Order> {
    /**
     * 查询订单分页数据，
     * @param userId
     * @param status
     * @return
     */
    List<Order> queryOrderList(
            @Param("userId") Long userId,
            @Param("status") Integer status);

    @Select("SELECT t1.*,t3.`status` FROM tb_order_detail t1 , tb_order t2 , tb_order_status t3 where t1.order_id=t2.order_id AND t1.order_id=t3.order_id")
    List<Order> queryList();
}
