**优惠券模块版本迭代记录**

2017-12-19：**正式版未更新**

1：优惠券列表优惠券模板创建时间倒序排序；

2：优惠码列表添加优惠券创建时间倒序，优惠码过期时间倒序排序；

3：优惠码添加冗余字段 onLineFlag 0线下，1线上；管理端更新api
 
4：管理端更新excel导出实现：当列表查询为空的时候返回空值

2017-12-19：**正式版已更新**

2018-01-08：**正式版未更新**

1：更新根据商品id查询相关优惠券信息：调整为查询列表返回多个符合条件的优惠券。

2018-01-08：**正式版已更新**

2018-01-11：**正式版未更新**

1：调整优惠券，定时器清理数据时，清理redis缓存数据

2018-01-12：**正式版已更新**

2018-01-23：**测试版已更新**

1：批量添加优惠码：添加mybatis id回调