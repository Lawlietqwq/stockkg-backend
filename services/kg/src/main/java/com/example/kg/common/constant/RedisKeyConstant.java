package com.example.kg.common.constant;

/**
 * Redis Key 定义常量类
 *

 */
public final class RedisKeyConstant {

        /**
         * 股票节点关联一跳信息
         */
        public static final String QUERY_STOCK = "kg:stock";

        /**
         * 股东节点关联一跳信息
         */
        public static final String QUERY_HOLDER = "kg:holder";

        /**
         * 股票排名信息 Key Prefix + tradingDate
         */
        public static final String QUERY_MODEL = "kg:model:";

        /**
         * 查询二跳股票关联图
         */
        public static final String QUERY_STOCK_ONE_HOP = "kg:stock:stock_one_hop";

        /**
         * 查看股票mapping表   key ——> list(date)
         */
        public static final String QUERY_STOCK_MAPPING = "kg:stock:mapping";

        /**
         * 查看股东mapping表   key ——> list(date)
         */
        public static final String QUERY_HOLDER_MAPPING = "kg:holder:mapping";

        /**
         * 上次修改时间
         */
        public static final String LAST_MODIFIED = "kg:last_modified";

        /**
         * 分布式锁 —— 更新模型
         */
        public static final String MODIFY_LOCK = "kg:lock:modify_lock";

        /**
         * 分布式锁 —— 查询股票
         */
        public static final String QUERY_STOCK_LOCK = "kg:lock:query_stock_lock";

        /**
         * 分布式锁 —— 查询股东
         */
        public static final String QUERY_HOLDER_LOCK = "kg:lock:query_holder_lock";

        /**
         * 分布式锁 —— 查询模型结果
         */
        public static final String QUERY_MODEL_LOCK = "kg:lock:query_model";

        /**
         * 分布式锁 —— 查询股东与更新时间映射
         */
        public static final String QUERY_STOCK_ONE_HOP_LOCK = "kg:lock:query_stock_one_hop_lock";

        /**
         * 分布式锁 —— 查询股票与更新时间映射
         */
        public static final String QUERY_STOCK_MAPPING_LOCK = "kg:lock:query_stock_mapping_lock";

        /**
         * 分布式锁 —— 查询股东与更新时间映射
         */
        public static final String QUERY_HOLDER_MAPPING_LOCK = "kg:lock:query_holder_mapping_lock";

}
