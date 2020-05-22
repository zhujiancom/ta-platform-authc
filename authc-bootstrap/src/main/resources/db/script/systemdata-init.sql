-- init user admin
INSERT INTO `t_sys_user` VALUES ('e9ca23d68d884d4ebb19d07889727dae', 'admin', '管理员', 'f8d85c06254a543f', 'RCGTeGiH', 'tmp/favicon_1587556662510.ico', '2018-12-05 00:00:00', '1', '11@qq.com', '18566666661', 'A01', '1', '0', '1', null, null,null,'admin','2038-06-21 17:54:10', 'admin', '2019-05-20 15:59:56',2);
-- init role
INSERT INTO `t_sys_role` VALUES ('f6817f48af4fb3af11b9e8bf182f618b', '管理员', 'admin', '管理员', NULL, '2018-12-21 18:03:39', 'admin', '2019-05-20 11:40:26');
INSERT INTO `t_sys_user_role` VALUES ('f72c6190b0722e798147e73c776c6ac9', 'e9ca23d68d884d4ebb19d07889727dae', 'f6817f48af4fb3af11b9e8bf182f618b');

-- init menu list
-- ----------------------------
-- Records of t_sys_permission
-- ----------------------------
INSERT INTO `t_sys_permission` VALUES ('08e6b9dc3c04489c8e1ff2ce6f105aa4', '', '系统监控', '/dashboard3', 'layouts/ZRouteView', NULL, NULL, 0, NULL, NULL, 6.00, 0, 'dashboard', 1, 0, NULL, 0, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2019-03-31 22:19:58', 0, 0, NULL, NULL, 0);
INSERT INTO `t_sys_permission` VALUES ('190c2b43bec6a5f7a4194a85db67d96a', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '角色管理', '/system/role', 'system/RoleList', NULL, NULL, 1, NULL, NULL, 1.20, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-04-17 15:13:56', 'admin', '2019-12-25 09:36:31', 0, 0, NULL, 0, 0);
INSERT INTO `t_sys_permission` VALUES ('3f915b2769fc80648e92d04e84ca059d', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '用户管理', '/system/user', 'system/UserList', NULL, NULL, 1, NULL, NULL, 1.10, 0, NULL, 1, 1, 0, 0, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2019-12-25 09:36:24', 0, 0, NULL, 0, 0);
INSERT INTO `t_sys_permission` VALUES ('45c966826eeff4c99b8f8ebfe74511fc', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '部门管理', '/system/depart', 'system/DepartList', NULL, NULL, 1, NULL, NULL, 1.40, 0, NULL, 1, 1, 0, 0, NULL, 'admin', '2019-01-29 18:47:40', 'admin', '2019-12-25 09:36:47', 0, 0, NULL, 0, 0);
INSERT INTO `t_sys_permission` VALUES ('54dd5457a3190740005c1bfec55b1c34', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '菜单管理', '/system/permission', 'system/PermissionList', NULL, NULL, 1, NULL, NULL, 1.30, 0, NULL, 1, 1, 0, 0, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2019-12-25 09:36:39', 0, 0, NULL, 0, 0);
INSERT INTO `t_sys_permission` VALUES ('8d1ebd663688965f1fd86a2f0ead3416', '700b7f95165c46cc7a78bf227aa8fed3', 'Redis监控', '/monitor/redis/info', 'modules/monitor/RedisInfo', NULL, NULL, 1, NULL, NULL, 1.00, 0, NULL, 1, 1, NULL, 0, NULL, 'admin', '2019-04-02 13:11:33', 'admin', '2019-05-07 15:18:54', 0, 0, NULL, NULL, 0);
INSERT INTO `t_sys_permission` VALUES ('9502685863ab87f0ad1134142788a385', '', '首页', '/dashboard/workspace', 'dashboard/Workspace', NULL, NULL, 0, NULL, NULL, 0.00, 0, 'home', 1, 1, NULL, 0, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2019-03-29 11:04:13', 0, 0, NULL, NULL, 1);
INSERT INTO `t_sys_permission` VALUES ('b1cb0a3fedf7ed0e4653cb5a229837ee', '08e6b9dc3c04489c8e1ff2ce6f105aa4', '定时任务', '/isystem/QuartzJobList', 'system/QuartzJobList', NULL, NULL, 1, NULL, NULL, 3.00, 0, NULL, 1, 1, NULL, 0, NULL, NULL, '2019-01-03 09:38:52', 'admin', '2019-04-02 10:24:13', 0, 0, NULL, NULL, 0);
INSERT INTO `t_sys_permission` VALUES ('d7d6e2e4e2934f2c9385a623fd98c6f3', '', '系统管理', '/isystem', 'layouts/ZRouteView', NULL, NULL, 0, NULL, NULL, 4.00, 0, 'setting', 1, 0, NULL, 0, NULL, NULL, '2018-12-25 20:34:38', 'admin', '2019-03-31 22:19:52', 0, 0, NULL, NULL, 0);
INSERT INTO `t_sys_permission` VALUES ('e08cb190ef230d5d4f03824198773950', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '系统通告', '/system/sysNoticeList', 'base/SysAnnouncementList', NULL, NULL, 1, 'annountCement', NULL, 6.00, NULL, '', 1, 1, NULL, NULL, NULL, NULL, '2019-01-02 17:23:01', NULL, '2019-01-02 17:31:23', 0, 0, NULL, NULL, 0);
INSERT INTO `t_sys_permission` VALUES ('f1cb187abf927c88b89470d08615f5ac', 'd7d6e2e4e2934f2c9385a623fd98c6f3', '数据字典', '/system/dict', 'base/SysDictList', NULL, NULL, 1, NULL, NULL, 5.00, 0, NULL, 1, 1, 0, 0, NULL, NULL, '2018-12-28 13:54:43', 'admin', '2020-02-23 22:45:25', 0, 0, NULL, 0, 0);

-- init role permission
-- ----------------------------
-- Records of t_sys_role_permission
-- ----------------------------
INSERT INTO `t_sys_role_permission` VALUES ('0c6e1075e422972083c3e854d9af7851', 'f6817f48af4fb3af11b9e8bf182f618b', '08e6b9dc3c04489c8e1ff2ce6f105aa4', NULL);
INSERT INTO `t_sys_role_permission` VALUES ('1209423580355481602', 'f6817f48af4fb3af11b9e8bf182f618b', '190c2b43bec6a5f7a4194a85db67d96a', NULL);
INSERT INTO `t_sys_role_permission` VALUES ('126ea9faebeec2b914d6d9bef957afb6', 'f6817f48af4fb3af11b9e8bf182f618b', 'f1cb187abf927c88b89470d08615f5ac', NULL);
INSERT INTO `t_sys_role_permission` VALUES ('1af4babaa4227c3cbb830bc5eb513abb', 'f6817f48af4fb3af11b9e8bf182f618b', 'e08cb190ef230d5d4f03824198773950', NULL);
INSERT INTO `t_sys_role_permission` VALUES ('2ad37346c1b83ddeebc008f6987b2227', 'f6817f48af4fb3af11b9e8bf182f618b', '8d1ebd663688965f1fd86a2f0ead3416', NULL);
INSERT INTO `t_sys_role_permission` VALUES ('4204f91fb61911ba8ce40afa7c02369f', 'f6817f48af4fb3af11b9e8bf182f618b', '3f915b2769fc80648e92d04e84ca059d', NULL);
INSERT INTO `t_sys_role_permission` VALUES ('8e3dc1671abad4f3c83883b194d2e05a', 'f6817f48af4fb3af11b9e8bf182f618b', 'b1cb0a3fedf7ed0e4653cb5a229837ee', NULL);
INSERT INTO `t_sys_role_permission` VALUES ('980171fda43adfe24840959b1d048d4d', 'f6817f48af4fb3af11b9e8bf182f618b', 'd7d6e2e4e2934f2c9385a623fd98c6f3', NULL);
INSERT INTO `t_sys_role_permission` VALUES ('bea2986432079d89203da888d99b3f16', 'f6817f48af4fb3af11b9e8bf182f618b', '54dd5457a3190740005c1bfec55b1c34', NULL);
INSERT INTO `t_sys_role_permission` VALUES ('d37ad568e26f46ed0feca227aa9c2ffa', 'f6817f48af4fb3af11b9e8bf182f618b', '9502685863ab87f0ad1134142788a385', NULL);
INSERT INTO `t_sys_role_permission` VALUES ('ecdd72fe694e6bba9c1d9fc925ee79de', 'f6817f48af4fb3af11b9e8bf182f618b', '45c966826eeff4c99b8f8ebfe74511fc', NULL);
INSERT INTO `t_sys_role_permission` VALUES ('f6817f48af4fb3af11b9e8bf182f618c', 'f6817f48af4fb3af11b9e8bf182f618b', '62b1a4827a1c11eabdf00242ac110002', NULL);

-- ----------------------------
-- Records of t_sys_dict
-- ----------------------------
INSERT INTO `t_sys_dict` VALUES ('1255088481764388866', '机构类型', 'org_category', '组织机构部门类型', 0, 'admin', '2020-04-28 18:56:27', NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES ('1255089984851947522', '有效状态', 'valid_status', '有效/无效 状态标识', 0, 'admin', '2020-04-28 19:02:25', NULL, NULL, 1);
INSERT INTO `t_sys_dict` VALUES ('1255349071766474754', '机构层级', 'org_level', '机构级别', 0, 'admin', '2020-04-29 12:11:57', NULL, NULL, 1);
INSERT INTO `t_sys_dict` VALUES ('1255404923902816257', '通知类型', 'notice_type', '消息类型1:通知公告2:系统消息', 0, 'admin', '2020-04-29 15:53:53', NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES ('1255405533427462145', '通告对象', 'receiver_type', '通告对象：全体用户， 角色用户， 指定用户', 0, 'admin', '2020-04-29 15:56:18', NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES ('1255406127202496513', '性别', 'sex', '性别： 1-男； 2-女', 0, 'admin', '2020-04-29 15:58:40', NULL, NULL, 1);
INSERT INTO `t_sys_dict` VALUES ('1255406541750726657', '删除状态', 'del_flag', '删除状态： 0-正常； 1-已删除', 0, 'admin', '2020-04-29 16:00:19', NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES ('1255407453084905473', '菜单类型', 'menu_type', '菜单类型：0-一级菜单；1-子菜单； 2-按钮权限', 0, 'admin', '2020-04-29 16:03:56', NULL, NULL, 1);
INSERT INTO `t_sys_dict` VALUES ('1255407964324425730', '优先级', 'priority', '优先级：L-低；M- 中；H-高', 0, 'admin', '2020-04-29 16:05:58', NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES ('1255408379711516673', '发布状态', 'publish_state', '发布状态：0-未发布；1-已发布；2-已撤销', 0, 'admin', '2020-04-29 16:07:37', NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES ('1255413638802542594', '用户状态', 'user_status', '1-正常；2-冻结', 0, 'admin', '2020-04-29 16:28:31', NULL, NULL, 1);

-- ----------------------------
-- Records of t_sys_dict_item
-- ----------------------------
INSERT INTO `t_sys_dict_item` VALUES ('1253596686761127938', '1255089984851947522', '启用', '1', '启用状态字典', 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `t_sys_dict_item` VALUES ('1253604668982996994', '1255089984851947522', '禁用', '0', '禁用标识字典项', 1, 1, NULL, NULL, NULL, NULL);
INSERT INTO `t_sys_dict_item` VALUES ('1255088813638692865', '1255088481764388866', '公司', '1', '公司级别', 1, 1, 'admin', '2020-04-28 18:57:46', NULL, NULL);
INSERT INTO `t_sys_dict_item` VALUES ('1255088863462830081', '1255088481764388866', '部门', '2', '部门级别', 2, 1, 'admin', '2024-09-28 18:57:58', 'admin', '2020-04-28 18:59:49');
INSERT INTO `t_sys_dict_item` VALUES ('1255349158370463746', '1255349071766474754', '一级', '1', '一级', 1, 1, 'admin', '2020-04-29 12:12:17', NULL, NULL);
INSERT INTO `t_sys_dict_item` VALUES ('1255349399924625409', '1255349071766474754', '二级', '2', '二级', 2, 1, 'admin', '2020-04-29 12:13:15', NULL, NULL);
INSERT INTO `t_sys_dict_item` VALUES ('1255375013293445122', '1255349071766474754', '三级', '3', '三级', 3, 1, 'admin', '2020-04-29 13:55:02', NULL, NULL);
INSERT INTO `t_sys_dict_item` VALUES ('1255405070871228417', '1255404923902816257', '通知公告', '1', '', 1, 1, 'admin', '2020-04-29 15:54:28', NULL, NULL);
INSERT INTO `t_sys_dict_item` VALUES ('1255405155990433793', '1255404923902816257', '系统消息', '2', '', 2, 1, 'admin', '2020-04-29 15:54:48', NULL, NULL);
INSERT INTO `t_sys_dict_item` VALUES ('1255405658715516929', '1255405533427462145', '全体用户', 'ALL', '', 1, 1, 'admin', '2020-04-29 15:56:48', NULL, NULL);
INSERT INTO `t_sys_dict_item` VALUES ('1255405707449135106', '1255405533427462145', '角色用户', 'ROLE', '', 2, 1, 'admin', '2020-04-29 15:57:00', NULL, NULL);
INSERT INTO `t_sys_dict_item` VALUES ('1255405771491962882', '1255405533427462145', '指定用户', 'USER', '', 3, 1, 'admin', '2020-04-29 15:57:15', NULL, NULL);
INSERT INTO `t_sys_dict_item` VALUES ('1255406193355059201', '1255406127202496513', '男', '1', '男', 1, 1, 'admin', '2020-04-29 15:58:55', NULL, NULL);
INSERT INTO `t_sys_dict_item` VALUES ('1255406221024882690', '1255406127202496513', '女', '2', '', 2, 1, 'admin', '2024-11-29 15:59:02', 'admin', '2020-04-29 15:59:06');
INSERT INTO `t_sys_dict_item` VALUES ('1255406618053505025', '1255406541750726657', '正常', '0', '', 1, 1, 'admin', '2020-04-29 16:00:37', NULL, NULL);
INSERT INTO `t_sys_dict_item` VALUES ('1255406656037122049', '1255406541750726657', '已删除', '1', '', 2, 1, 'admin', '2020-04-29 16:00:46', NULL, NULL);
INSERT INTO `t_sys_dict_item` VALUES ('1255407526594277377', '1255407453084905473', '一级菜单', '0', '', 1, 1, 'admin', '2020-04-29 16:04:13', NULL, NULL);
INSERT INTO `t_sys_dict_item` VALUES ('1255407574510006273', '1255407453084905473', '子菜单', '1', '', 2, 1, 'admin', '2020-04-29 16:04:25', NULL, NULL);
INSERT INTO `t_sys_dict_item` VALUES ('1255407615182172161', '1255407453084905473', '按钮权限', '2', '', 1, 1, 'admin', '2020-04-29 16:04:34', NULL, NULL);
INSERT INTO `t_sys_dict_item` VALUES ('1255408028535025665', '1255407964324425730', '低', 'L', '', 1, 1, 'admin', '2020-04-29 16:06:13', NULL, NULL);
INSERT INTO `t_sys_dict_item` VALUES ('1255408066120183809', '1255407964324425730', '中', 'M', '', 2, 1, 'admin', '2020-04-29 16:06:22', NULL, NULL);
INSERT INTO `t_sys_dict_item` VALUES ('1255408118062444545', '1255407964324425730', '高', 'H', '', 3, 1, 'admin', '2020-04-29 16:06:34', NULL, NULL);
INSERT INTO `t_sys_dict_item` VALUES ('1255408433520242690', '1255408379711516673', '未发布', '0', '', 1, 1, 'admin', '2020-04-29 16:07:50', NULL, NULL);
INSERT INTO `t_sys_dict_item` VALUES ('1255408466843987970', '1255408379711516673', '已发布', '1', '', 2, 1, 'admin', '2020-04-29 16:07:58', NULL, NULL);
INSERT INTO `t_sys_dict_item` VALUES ('1255408503367987201', '1255408379711516673', '已撤销', '2', '', 3, 1, 'admin', '2020-04-29 16:08:06', NULL, NULL);
INSERT INTO `t_sys_dict_item` VALUES ('1255413675339124737', '1255413638802542594', '正常', '1', '', 1, 1, 'admin', '2022-04-29 16:28:39', 'admin', '2020-04-29 16:28:58');
INSERT INTO `t_sys_dict_item` VALUES ('1255413723397459970', '1255413638802542594', '冻结', '2', '', 2, 1, 'admin', '2020-04-29 16:28:51', NULL, NULL);

