package com.mr.order.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mr.bo.UserInfo;
import com.mr.client.AddressClient;
import com.mr.client.GoodClient;
import com.mr.common.utils.IdWorker;
import com.mr.common.utils.PageResult;
import com.mr.order.bo.AddressBo;
import com.mr.order.bo.CartBo;
import com.mr.order.bo.OrderBo;
import com.mr.order.mapper.OrderDetailMapper;
import com.mr.order.mapper.OrderMapper;
import com.mr.order.mapper.OrderStatusMapper;
import com.mr.order.pojo.Order;
import com.mr.order.pojo.OrderDetail;
import com.mr.order.pojo.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class OrderService {

    @Autowired
    private IdWorker idWorker;
    @Autowired
    private GoodClient goodClient;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper detailMapper;
    @Autowired
    private OrderStatusMapper statusMapper;

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Transactional
    public Long createOrder(OrderBo orderBo, UserInfo userInfo){
        //前端只传递，id信息，其余数据从数据库查询，防止人员揣改数据提交，
        // 初始化订单数据
        Order order = new Order();
        // 雪花 算法（n），生成 唯一订单id
        long orderId = idWorker.nextId();
        //创建时间
        order.setCreateTime(new Date());
        //获取id   唯一订单id
        order.setOrderId(orderId);
        //支付类型
        order.setPaymentType(orderBo.getPayMentType());
        //订单中用户信息  用户id 买家昵称 买家是否已经评价
        //userId; 用户id
        order.setUserId(userInfo.getId());
        //buyerNick; 买家昵称
        order.setBuyerNick(userInfo.getUsername());
        //buyerRate; 买家是否已经评价
        order.setBuyerRate(false);

        //收货人信息(调用地址服务查询)
        AddressBo address = AddressClient.getAddressById(orderBo.getAddressId());
        //receiver; 收货人全名
        order.setReceiver(address.getName());
        //receiverMobile; 移动电话
        order.setReceiverMobile(address.getPhone());
        //receiverState; 省份
        order.setReceiverState(address.getState());
        //receiverCity; 城市
        order.setReceiverCity(address.getCity());
        //receiverDistrict; 区/县
        order.setReceiverDistrict(address.getDistrict());
        //receiverAddress; 收货地址，如：xx路xx号
        order.setReceiverAddress(address.getAddress());
        //receiverZip; 邮政编码,如：310001
        order.setReceiverZip(address.getZipCode());
        //总金额(设置默认值为0l)
        Long totalPay = 0l;
        //查询sku数据
        for (CartBo cartBo : orderBo.getCartList()){
            cartBo.setSku(goodClient.querySkuById(cartBo.getSkuId()));
            totalPay += cartBo.getSku().getPrice() * cartBo.getNum();
        }

        //总金额
        order.setTotalPay(totalPay);
        //实际付款 actualPay 满减，邮费，积分，等会影响实际价格 目前不做，所以默认
        order.setActualPay(totalPay);
        //保存数据
        this.orderMapper.insertSelective(order);

        //组装订单详情，一个订单有多个sku详情
        List<OrderDetail> orderDetailList = new ArrayList<>();

        orderBo.getCartList().forEach(cartBo -> {
            OrderDetail detail = new OrderDetail();
            //orderId 订单id
            detail.setOrderId(orderId);
            //skuId 商品id
            detail.setSkuId(cartBo.getSkuId());
            //num 商品购买数量
            detail.setNum(cartBo.getNum());
            //title 商品标题
            detail.setTitle(cartBo.getSku().getTitle());
            //price 商品单价
            detail.setPrice(cartBo.getSku().getPrice());
            //ownSpec 商品规格数据
            detail.setOwnSpec(cartBo.getSku().getOwnSpec());
            //image 图片
            detail.setImage(cartBo.getSku().getImages().split(",")[0]);
            orderDetailList.add(detail);
        });

        //批量保存详情
        this.detailMapper.insertList(orderDetailList);
        // 保存订单状态
        OrderStatus orderStatus = new OrderStatus();
        //orderId
        orderStatus.setOrderId(orderId);
        //status  阶段：1、未付款、未发货；初始化所有数据 2、已付款、未发货；更改付款时间
        //3、已发货，未确认；更改发货时间、物流名称、物流单号 4、已确认，未评价；更改交易结束时间
        //5、关闭； 更改更新时间，交易关闭时间。 6、已评价
        orderStatus.setStatus(1);  //初始状态为1
        //createTime;// 创建时间
        orderStatus.setCreateTime(order.getCreateTime());
        // 保存订单状态
        this.statusMapper.insertSelective(orderStatus);
        //调用goodfeign执行库存减去操作。涉及到，并发下，锁， 分布式事务的问题
        orderBo.getCartList().forEach(cartBo -> {

        });

        return orderId;
    }


    public Order queryById(Long id) {
        // 查询订单
        Order order = this.orderMapper.selectByPrimaryKey(id);

        // 查询订单详情
        OrderDetail detail = new OrderDetail();
        detail.setOrderId(id);
        List<OrderDetail> details = this.detailMapper.select(detail);
        order.setOrderDetails(details);

        // 查询订单状态
        OrderStatus status = this.statusMapper.selectByPrimaryKey(order.getOrderId());
        order.setStatus(status.getStatus());
        return order;
    }

    @Transactional
    public Boolean updateStatus(Long id, Integer status) {
        OrderStatus record = new OrderStatus();
        record.setOrderId(id);
        record.setStatus(status);
        // 根据状态判断要修改的时间
        switch (status) {
            case 2:
                record.setPaymentTime(new Date());// 付款
                break;
            case 3:
                record.setConsignTime(new Date());// 发货
                break;
            case 4:
                record.setEndTime(new Date());// 确认收获，订单结束
                break;
            case 5:
                record.setCloseTime(new Date());// 交易失败，订单关闭
                break;
            case 6:
                record.setCommentTime(new Date());// 评价时间
                break;
            default:
                return null;
        }
        int count = this.statusMapper.updateByPrimaryKeySelective(record);
        return count == 1;
    }

    public PageResult<Order> queryUserOrderList(Integer page, Integer rows, Integer status, UserInfo userInfo) {
        try {
            // 设置分页等其起始值，每页条数
            PageHelper.startPage(page, rows);

            // 创建查询条件用户id和状态，由于需要三表联查，sql注解拼接麻烦，所以采用了xml形式
            Page<Order> pageInfo = (Page<Order>) this.orderMapper.queryOrderList(userInfo.getId(), status);
            return new PageResult<>(pageInfo.getTotal(), pageInfo);
        } catch (Exception e) {
            logger.error("查询订单出错", e);
            return null;
        }
    }
}
