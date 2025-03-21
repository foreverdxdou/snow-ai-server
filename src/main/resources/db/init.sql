-- 初始化角色表
INSERT INTO sys_role (id, role_name, role_code, description, status, create_time, update_time, deleted)
VALUES 
(1, '超级管理员', 'SUPER_ADMIN', '系统超级管理员', 1, NOW(), NOW(), 0),
(2, '普通用户', 'USER', '普通用户', 1, NOW(), NOW(), 0);

-- 初始化权限表
INSERT INTO sys_permission (id, parent_id, permission_name, permission_code, permission_type, path, component, icon, sort, status, create_time, update_time, deleted)
VALUES 
(1, 0, '系统管理', 'system', 1, '/system', 'Layout', 'system', 1, 1, NOW(), NOW(), 0),
(2, 1, '用户管理', 'system:user', 2, 'user', 'system/user/index', 'user', 1, 1, NOW(), NOW(), 0),
(3, 1, '角色管理', 'system:role', 2, 'role', 'system/role/index', 'role', 2, 1, NOW(), NOW(), 0),
(4, 1, '菜单管理', 'system:menu', 2, 'menu', 'system/menu/index', 'menu', 3, 1, NOW(), NOW(), 0);

-- 初始化角色权限关联表
INSERT INTO sys_role_permission (role_id, permission_id, create_time, update_time, deleted)
VALUES 
(1, 1, NOW(), NOW(), 0),
(1, 2, NOW(), NOW(), 0),
(1, 3, NOW(), NOW(), 0),
(1, 4, NOW(), NOW(), 0),
(2, 1, NOW(), NOW(), 0),
(2, 2, NOW(), NOW(), 0);

-- 知识库表
CREATE TABLE IF NOT EXISTS kb_knowledge_base (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '知识库名称',
    description TEXT COMMENT '知识库描述',
    creator_id BIGINT NOT NULL COMMENT '创建者ID',
    status SMALLINT NOT NULL DEFAULT 1 COMMENT '状态（0：禁用，1：启用）',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted SMALLINT NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）'
);

-- 知识库权限表
CREATE TABLE IF NOT EXISTS kb_knowledge_base_permission (
    id BIGSERIAL PRIMARY KEY,
    kb_id BIGINT NOT NULL COMMENT '知识库ID',
    user_id BIGINT COMMENT '用户ID',
    role_id BIGINT COMMENT '角色ID',
    permission_type SMALLINT NOT NULL COMMENT '权限类型（1：查看，2：编辑，3：管理）',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    CONSTRAINT fk_kb_permission_kb FOREIGN KEY (kb_id) REFERENCES kb_knowledge_base(id)
);

-- 知识库分类表
CREATE TABLE IF NOT EXISTS kb_category (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '分类名称',
    description TEXT COMMENT '分类描述',
    parent_id BIGINT COMMENT '父分类ID',
    kb_id BIGINT NOT NULL COMMENT '知识库ID',
    sort INT NOT NULL DEFAULT 0 COMMENT '排序号',
    creator_id BIGINT NOT NULL COMMENT '创建者ID',
    status SMALLINT NOT NULL DEFAULT 1 COMMENT '状态（0：禁用，1：启用）',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted SMALLINT NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
    CONSTRAINT fk_category_kb FOREIGN KEY (kb_id) REFERENCES kb_knowledge_base(id)
);

-- 创建索引
CREATE INDEX idx_kb_knowledge_base_creator_id ON kb_knowledge_base(creator_id);
CREATE INDEX idx_kb_knowledge_base_status ON kb_knowledge_base(status);
CREATE INDEX idx_kb_knowledge_base_deleted ON kb_knowledge_base(deleted);

CREATE INDEX idx_kb_permission_kb_id ON kb_knowledge_base_permission(kb_id);
CREATE INDEX idx_kb_permission_user_id ON kb_knowledge_base_permission(user_id);
CREATE INDEX idx_kb_permission_role_id ON kb_knowledge_base_permission(role_id);

CREATE INDEX idx_kb_category_parent_id ON kb_category(parent_id);
CREATE INDEX idx_kb_category_kb_id ON kb_category(kb_id);
CREATE INDEX idx_kb_category_creator_id ON kb_category(creator_id);
CREATE INDEX idx_kb_category_status ON kb_category(status);
CREATE INDEX idx_kb_category_deleted ON kb_category(deleted); 