-- 初始化部门
INSERT INTO sys_dept (id, parent_id, dept_name, sort, leader, phone, email, status)
VALUES (1, 0, '总公司', 1, 'admin', '13800000000', 'admin@example.com', 1);

-- 初始化角色
INSERT INTO sys_role (id, role_name, role_code, description, status)
VALUES 
(1, '超级管理员', 'ROLE_ADMIN', '系统超级管理员', 1),
(2, '普通用户', 'ROLE_USER', '普通用户', 1);

-- 初始化权限
INSERT INTO sys_permission (id, parent_id, name, type, permission_code, path, component, icon, sort, status)
VALUES 
(1, 0, '系统管理', 1, 'system:manage', '/system', 'Layout', 'system', 1, 1),
(2, 1, '用户管理', 1, 'system:user', '/system/user', 'system/user/index', 'user', 1, 1),
(3, 1, '角色管理', 1, 'system:role', '/system/role', 'system/role/index', 'role', 2, 1),
(4, 1, '权限管理', 1, 'system:permission', '/system/permission', 'system/permission/index', 'permission', 3, 1),
(5, 1, '部门管理', 1, 'system:dept', '/system/dept', 'system/dept/index', 'dept', 4, 1),
(6, 0, '知识库管理', 1, 'kb:manage', '/kb', 'Layout', 'kb', 2, 1),
(7, 6, '知识库列表', 1, 'kb:list', '/kb/list', 'kb/list/index', 'kb-list', 1, 1),
(8, 6, '文档管理', 1, 'kb:document', '/kb/document', 'kb/document/index', 'document', 2, 1),
(9, 6, '分类管理', 1, 'kb:category', '/kb/category', 'kb/category/index', 'category', 3, 1),
(10, 6, '标签管理', 1, 'kb:tag', '/kb/tag', 'kb/tag/index', 'tag', 4, 1);

-- 初始化角色权限关联
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 1, id FROM sys_permission;

INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 2, id FROM sys_permission WHERE permission_code LIKE 'kb:%';

-- 初始化管理员用户（密码：123456）
INSERT INTO sys_user (id, username, password, nickname, email, dept_id, status)
VALUES (1, 'admin', '$2a$10$LONs5hYyrFnAvCqRkHUfkOReaXZorpi3T8K7OoIIs1NGMdoPGw5cW', '管理员', 'admin@example.com', 1, 1);

-- 初始化用户角色关联
INSERT INTO sys_user_role (user_id, role_id)
VALUES (1, 1);

-- 初始化AI模型配置
INSERT INTO sys_ai_model (model_name, model_type, api_url, api_key, status)
VALUES ('GPT-3.5-turbo', 1, 'https://api.openai.com/v1/chat/completions', 'your-api-key', 1);

-- 初始化大模型配置
INSERT INTO llm_config (model_name, api_url, api_key, enabled)
VALUES ('GPT-3.5-turbo', 'https://api.openai.com/v1/chat/completions', 'your-api-key', true);
