-- ----------------------------
-- Table structure for t_sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_dict`;
CREATE TABLE `t_sys_dict` (
                            `id` varchar(32) NOT NULL,
                            `dict_name` varchar(100) default NULL COMMENT '字典名称',
                            `dict_code` varchar(100) default NULL COMMENT '字典编码',
                            `description` varchar(255) default NULL COMMENT '描述',
                            `del_flag` int(1) default NULL COMMENT '删除状态',
                            `create_by` varchar(32) default NULL COMMENT '创建人',
                            `create_time` datetime default NULL COMMENT '创建时间',
                            `update_by` varchar(32) default NULL COMMENT '更新人',
                            `update_time` datetime default NULL COMMENT '更新时间',
                            `type` int(1) unsigned zerofill default '0' COMMENT '字典类型0为string,1为number',
                            PRIMARY KEY  (`id`) USING BTREE,
                            UNIQUE KEY `indextable_dict_code` USING BTREE (`dict_code`)
) ENGINE=InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_sys_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_dict_item`;
CREATE TABLE `t_sys_dict_item` (
                                 `id` varchar(32) NOT NULL,
                                 `dict_id` varchar(32) default NULL COMMENT '字典id',
                                 `item_text` varchar(100) default NULL COMMENT '字典项文本',
                                 `item_value` varchar(100) default NULL COMMENT '字典项值',
                                 `description` varchar(255) default NULL COMMENT '描述',
                                 `sort_order` int(10) default NULL COMMENT '排序',
                                 `status` int(11) default NULL COMMENT '状态（1启用 0不启用）',
                                 `create_by` varchar(32) default NULL,
                                 `create_time` datetime default NULL,
                                 `update_by` varchar(32) default NULL,
                                 `update_time` datetime default NULL,
                                 PRIMARY KEY  (`id`) USING BTREE,
                                 KEY `index_table_dict_id` USING BTREE (`dict_id`),
                                 KEY `index_table_sort_order` USING BTREE (`sort_order`),
                                 KEY `index_table_dict_status` USING BTREE (`status`)
) ENGINE=InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_sys_value_fill_rule
-- ----------------------------
DROP TABLE IF EXISTS t_sys_value_fill_rule;
CREATE TABLE t_sys_value_fill_rule  (
                                  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键ID',
                                  `rule_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则名称',
                                  `rule_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则Code',
                                  `rule_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则实现类',
                                  `rule_params` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则参数',
                                  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
                                  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                                  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
                                  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  UNIQUE INDEX `uni_sys_value_gen_rule_code`(`rule_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '生成规则配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_sys_log
-- ----------------------------
DROP TABLE IF EXISTS t_sys_log;
CREATE TABLE t_sys_log  (
                            `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                            `log_type` int(2) NULL DEFAULT NULL COMMENT '日志类型（1登录日志，2操作日志）',
                            `log_content` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志内容',
                            `operate_type` int(2) NULL DEFAULT NULL COMMENT '操作类型',
                            `userid` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作用户账号',
                            `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作用户名称',
                            `ip` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'IP',
                            `method` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求java方法',
                            `request_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求路径',
                            `request_param` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '请求参数',
                            `request_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求类型',
                            `cost_time` bigint(20) NULL DEFAULT NULL COMMENT '耗时',
                            `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
                            `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                            `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
                            `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                            PRIMARY KEY (`id`) USING BTREE,
                            INDEX `index_table_userid`(`userid`) USING BTREE,
                            INDEX `index_logt_ype`(`log_type`) USING BTREE,
                            INDEX `index_operate_type`(`operate_type`) USING BTREE,
                            INDEX `index_log_type`(`log_type`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_sys_position
-- ----------------------------
DROP TABLE IF EXISTS t_sys_position;
CREATE TABLE t_sys_position  (
                                 `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                 `code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职务编码',
                                 `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职务名称',
                                 `post_rank` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职级',
                                 `company_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公司id',
                                 `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
                                 `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                 `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
                                 `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                                 `sys_org_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组织机构编码',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_sys_user
-- ----------------------------
DROP TABLE IF EXISTS t_sys_user;
CREATE TABLE t_sys_user  (
                             `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
                             `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录账号',
                             `realname` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
                             `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
                             `salt` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'md5密码盐',
                             `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
                             `birthday` datetime(0) NULL DEFAULT NULL COMMENT '生日',
                             `sex` tinyint(1) NULL DEFAULT NULL COMMENT '性别(0-默认未知,1-男,2-女)',
                             `email` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电子邮件',
                             `phone` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话',
                             `org_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机构编码',
                             `status` tinyint(1) NULL DEFAULT NULL COMMENT '用户状态(1-正常,2-冻结)',
                             `del_flag` tinyint(1) NULL DEFAULT NULL COMMENT '删除状态(0-正常,1-已删除)',
                             `activiti_sync` tinyint(1) NULL DEFAULT NULL COMMENT '同步工作流引擎(1-同步,0-不同步)',
                             `work_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工号，唯一键',
                             `post` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职务，关联职务表',
                             `telephone` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '座机号',
                             `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
                             `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                             `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
                             `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                             `identity` tinyint(1) NULL DEFAULT NULL COMMENT '身份（1普通成员 2上级）',
                             `super_user` tinyint(1) NULL DEFAULT NULL COMMENT '超级管理员（0非超级管理员， 1超级管理员）, 不考虑权限问题',
                             PRIMARY KEY (`id`) USING BTREE,
                             UNIQUE INDEX `index_user_name`(`username`) USING BTREE,
                             UNIQUE INDEX `uniq_sys_user_work_no`(`work_no`) USING BTREE,
                             INDEX `index_user_status`(`status`) USING BTREE,
                             INDEX `index_user_del_flag`(`del_flag`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_sys_role
-- ----------------------------
DROP TABLE IF EXISTS t_sys_role;
CREATE TABLE t_sys_role  (
                             `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
                             `role_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
                             `role_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色编码',
                             `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
                             `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
                             `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                             `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
                             `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                             PRIMARY KEY (`id`) USING BTREE,
                             UNIQUE INDEX `uniq_sys_role_role_code`(`role_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for t_sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS t_sys_user_role;
CREATE TABLE t_sys_user_role  (
                                  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
                                  `user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id',
                                  `role_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色id',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  INDEX `index2_group_user_id`(`user_id`) USING BTREE,
                                  INDEX `index2_group_role_id`(`role_id`) USING BTREE,
                                  INDEX `index2_group_useridandroleid`(`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS t_sys_permission;
CREATE TABLE t_sys_permission  (
                                   `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
                                   `parent_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父id',
                                   `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单标题',
                                   `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路径',
                                   `component` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件',
                                   `component_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件名字',
                                   `redirect` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '一级菜单跳转地址',
                                   `menu_type` int(11) NULL DEFAULT NULL COMMENT '菜单类型(0:一级菜单; 1:子菜单:2:按钮权限)',
                                   `perms` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单权限编码',
                                   `perms_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '权限策略1显示2禁用',
                                   `sort_no` double(8, 2) NULL DEFAULT NULL COMMENT '菜单排序',
                                   `always_show` tinyint(1) NULL DEFAULT NULL COMMENT '聚合子路由: 1是0否',
                                   `icon` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
                                   `is_route` tinyint(1) NULL DEFAULT 1 COMMENT '是否路由菜单: 0:不是  1:是（默认值1）',
                                   `is_leaf` tinyint(1) NULL DEFAULT NULL COMMENT '是否叶子节点:    1:是   0:不是',
                                   `keep_alive` tinyint(1) NULL DEFAULT NULL COMMENT '是否缓存该页面:    1:是   0:不是',
                                   `hidden` int(2) NULL DEFAULT 0 COMMENT '是否隐藏路由: 0否,1是',
                                   `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
                                   `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
                                   `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                   `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
                                   `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                                   `del_flag` int(1) NULL DEFAULT 0 COMMENT '删除状态 0正常 1已删除',
                                   `rule_flag` int(3) NULL DEFAULT 0 COMMENT '是否添加数据权限1是0否',
                                   `status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '按钮权限状态(0无效1有效)',
                                   `internal_or_external` tinyint(1) NULL DEFAULT NULL COMMENT '外链菜单打开方式 0/内部打开 1/外部打开',
                                   `is_homepage` tinyint(1) NULL DEFAULT NULL COMMENT '是否首页菜单',
                                   PRIMARY KEY (`id`) USING BTREE,
                                   INDEX `index_prem_pid`(`parent_id`) USING BTREE,
                                   INDEX `index_prem_is_route`(`is_route`) USING BTREE,
                                   INDEX `index_prem_is_leaf`(`is_leaf`) USING BTREE,
                                   INDEX `index_prem_sort_no`(`sort_no`) USING BTREE,
                                   INDEX `index_prem_del_flag`(`del_flag`) USING BTREE,
                                   INDEX `index_menu_type`(`menu_type`) USING BTREE,
                                   INDEX `index_menu_hidden`(`hidden`) USING BTREE,
                                   INDEX `index_menu_status`(`status`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS t_sys_role_permission;
CREATE TABLE t_sys_role_permission  (
                                        `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                        `role_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色id',
                                        `permission_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限id',
                                        `data_rule_ids` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                        PRIMARY KEY (`id`) USING BTREE,
                                        INDEX `index_group_role_per_id`(`role_id`, `permission_id`) USING BTREE,
                                        INDEX `index_group_role_id`(`role_id`) USING BTREE,
                                        INDEX `index_group_per_id`(`permission_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_sys_permission_data_rule
-- ----------------------------
DROP TABLE IF EXISTS t_sys_permission_data_rule;
CREATE TABLE t_sys_permission_data_rule  (
                                             `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'ID',
                                             `permission_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单ID',
                                             `rule_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规则名称',
                                             `rule_column` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段',
                                             `rule_conditions` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '条件',
                                             `rule_value` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规则值',
                                             `status` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限有效状态1有0否',
                                             `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                             `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                             `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                                             `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
                                             PRIMARY KEY (`id`) USING BTREE,
                                             INDEX `index_fucntionid`(`permission_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_sys_user_agent
-- ----------------------------
DROP TABLE IF EXISTS t_sys_user_agent;
CREATE TABLE t_sys_user_agent  (
                                   `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '序号',
                                   `user_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
                                   `agent_user_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理人用户名',
                                   `start_time` datetime(0) NULL DEFAULT NULL COMMENT '代理开始时间',
                                   `end_time` datetime(0) NULL DEFAULT NULL COMMENT '代理结束时间',
                                   `status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态0无效1有效',
                                   `create_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
                                   `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人登录名称',
                                   `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
                                   `update_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人名称',
                                   `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人登录名称',
                                   `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
                                   `sys_org_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属部门',
                                   `sys_company_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属公司',
                                   PRIMARY KEY (`id`) USING BTREE,
                                   UNIQUE INDEX `uniq_username`(`user_name`) USING BTREE,
                                   INDEX `statux_index`(`status`) USING BTREE,
                                   INDEX `begintime_index`(`start_time`) USING BTREE,
                                   INDEX `endtime_index`(`end_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户代理人设置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_sys_depart
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_depart`;
CREATE TABLE `t_sys_depart` (
                                `id` varchar(32) NOT NULL COMMENT 'ID',
                                `parent_id` varchar(32) default NULL COMMENT '父机构ID',
                                `depart_name` varchar(100) NOT NULL COMMENT '机构/部门名称',
                                `depart_name_en` varchar(500) default NULL COMMENT '英文名',
                                `depart_name_abbr` varchar(500) default NULL COMMENT '缩写',
                                `depart_order` int(11) default '0' COMMENT '排序',
                                `description` text COMMENT '描述',
                                `org_category` varchar(32) default NULL COMMENT '机构类别',
                                `org_type` varchar(32) default NULL COMMENT '机构类型',
                                `org_code` varchar(32) default NULL COMMENT '机构编码',
                                `org_code2` varchar(32) default NULL COMMENT '机构编码2',
                                `org_level` tinyint(1) NULL DEFAULT NULL COMMENT '机构层级',
                                `phone` varchar(32) default NULL COMMENT '电话',
                                `mobile` varchar(32) default NULL COMMENT '手机号',
                                `fax` varchar(32) default NULL COMMENT '传真',
                                `address` varchar(100) default NULL COMMENT '地址',
                                `memo` varchar(500) default NULL COMMENT '备注',
                                `status` varchar(1) default NULL COMMENT '状态（1启用，0不启用）',
                                `del_flag` varchar(1) default NULL COMMENT '删除状态（0，正常，1已删除）',
                                `create_by` varchar(32) default NULL COMMENT '创建人',
                                `create_time` datetime default NULL COMMENT '创建日期',
                                `update_by` varchar(32) default NULL COMMENT '更新人',
                                `update_time` datetime default NULL COMMENT '更新日期',
                                PRIMARY KEY  (`id`) USING BTREE,
                                KEY `index_depart_parent_id` USING BTREE (`parent_id`),
                                KEY `index_depart_depart_order` USING BTREE (`depart_order`)
) ENGINE=InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT='机构/部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_sys_user_depart
-- ----------------------------
DROP TABLE IF EXISTS t_sys_user_depart;
CREATE TABLE t_sys_user_depart  (
                                    `ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
                                    `user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id',
                                    `dep_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门id',
                                    duty_flag tinyint(1) NULL DEFAULT NULL COMMENT '是否部门负责人',
                                    PRIMARY KEY (`ID`) USING BTREE,
                                    INDEX `index_depart_groupk_userid`(`user_id`) USING BTREE,
                                    INDEX `index_depart_groupkorgid`(`dep_id`) USING BTREE,
                                    INDEX `index_depart_groupk_uidanddid`(`user_id`, `dep_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT '用户部门关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_sys_depart_permission
-- ----------------------------
DROP TABLE IF EXISTS t_sys_depart_permission;
CREATE TABLE t_sys_depart_permission  (
                                          `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                          `depart_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门id',
                                          `permission_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限id',
                                          `data_rule_ids` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据规则id',
                                          PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_sys_depart_role
-- ----------------------------
DROP TABLE IF EXISTS t_sys_depart_role;
CREATE TABLE t_sys_depart_role  (
                                    `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                    `depart_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门id',
                                    `role_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门角色名称',
                                    `role_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门角色编码',
                                    `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
                                    `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
                                    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                    `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
                                    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_sys_depart_role_permission
-- ----------------------------
DROP TABLE IF EXISTS t_sys_depart_role_permission;
CREATE TABLE t_sys_depart_role_permission  (
                                               `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                               `depart_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门id',
                                               `role_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色id',
                                               `permission_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限id',
                                               `data_rule_ids` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                               PRIMARY KEY (`id`) USING BTREE,
                                               INDEX `index_group_role_per_id`(`role_id`, `permission_id`) USING BTREE,
                                               INDEX `index_group_role_id`(`role_id`) USING BTREE,
                                               INDEX `index_group_per_id`(`permission_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门角色权限表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_sys_depart_role_user
-- ----------------------------
DROP TABLE IF EXISTS t_sys_depart_role_user;
CREATE TABLE t_sys_depart_role_user  (
                                         `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
                                         `user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id',
                                         `drole_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色id',
                                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门角色用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_sys_notice
-- ----------------------------
DROP TABLE IF EXISTS t_sys_notice;
CREATE TABLE t_sys_notice  (
                                               `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                               `title` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通知标题',
                                               `content` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通知内容',
                                               `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
                                               `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
                                               `publisher` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发布人',
                                               `priority` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '优先级',
                                               `category` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息类型：1-通知公告，2-系统消息',
                                               `receiver_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接收者类型：1-指定用户，2-指定角色，3-所有用户',
                                               `publish_state` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息发布状态：0-未发布，1-已发布，2-已撤销',
                                               `publish_time` datetime(0) NULL DEFAULT NULL COMMENT '消息发布时间',
                                               `cancel_time` datetime(0) NULL DEFAULT NULL COMMENT '消息撤销时间',
                                               `del_flag` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息删除状态',
                                               `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
                                               `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                               `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
                                               `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                                               PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '通知公告表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_sys_notice
-- ----------------------------
DROP TABLE IF EXISTS t_sys_notice_action;
CREATE TABLE t_sys_notice_action  (
                               `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                               `user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户ID',
                               `notice_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通知ID',
                               `read_state` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '阅读状态：0-未读，1-已读',
                               `read_time` datetime(0) NULL DEFAULT NULL COMMENT '阅读时间',
                               `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
                               `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                               `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
                               `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                               PRIMARY KEY (`id`) USING BTREE,
                               INDEX `index_group_notice_user_id`(`notice_id`, `user_id`) USING BTREE,
                               INDEX `index_group_notice_id`(`notice_id`) USING BTREE,
                               INDEX `index_group_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '通知公告操作表' ROW_FORMAT = Compact;