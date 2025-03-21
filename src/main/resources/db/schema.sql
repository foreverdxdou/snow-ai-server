-- 用户表
CREATE TABLE sys_user (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    nickname VARCHAR(50),
    email VARCHAR(100),
    avatar VARCHAR(255),
    dept_id BIGINT,
    status SMALLINT DEFAULT 1,
    deleted SMALLINT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 角色表
CREATE TABLE sys_role (
    id BIGSERIAL PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE,
    role_code VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    deleted SMALLINT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 用户角色关联表
CREATE TABLE sys_user_role (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, role_id)
);

-- 部门表
CREATE TABLE sys_dept (
    id BIGSERIAL PRIMARY KEY,
    parent_id BIGINT,
    dept_name VARCHAR(50) NOT NULL,
    sort INT DEFAULT 0,
    leader VARCHAR(50),
    phone VARCHAR(20),
    email VARCHAR(100),
    status SMALLINT DEFAULT 1,
    deleted SMALLINT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 权限表
CREATE TABLE sys_permission (
    id BIGSERIAL PRIMARY KEY,
    parent_id BIGINT,
    name VARCHAR(50) NOT NULL,
    type SMALLINT NOT NULL, -- 1:菜单 2:按钮
    permission_code VARCHAR(100),
    path VARCHAR(200),
    component VARCHAR(255),
    icon VARCHAR(100),
    sort INT DEFAULT 0,
    status SMALLINT DEFAULT 1,
    deleted SMALLINT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 角色权限关联表
CREATE TABLE sys_role_permission (
    id BIGSERIAL PRIMARY KEY,
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (role_id, permission_id)
);

-- 知识库表
CREATE TABLE kb_knowledge_base (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    creator_id BIGINT NOT NULL,
    dept_id BIGINT,
    status SMALLINT DEFAULT 1,
    deleted SMALLINT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 知识库权限表
CREATE TABLE kb_knowledge_base_permission (
    id BIGSERIAL PRIMARY KEY,
    kb_id BIGINT NOT NULL,
    type SMALLINT NOT NULL, -- 1:用户 2:角色 3:部门
    target_id BIGINT NOT NULL,
    permission_type SMALLINT NOT NULL, -- 1:查看 2:编辑 3:管理
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (kb_id, type, target_id)
);

-- 文档分类表
CREATE TABLE kb_category (
    id BIGSERIAL PRIMARY KEY,
    parent_id BIGINT,
    name VARCHAR(50) NOT NULL,
    sort INT DEFAULT 0,
    kb_id BIGINT NOT NULL,
    deleted SMALLINT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 标签表
CREATE TABLE kb_tag (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    color VARCHAR(20),
    kb_id BIGINT NOT NULL,
    deleted SMALLINT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (name, kb_id)
);

-- 文档表
CREATE TABLE kb_document (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    summary TEXT,
    keywords TEXT,
    file_type VARCHAR(20),
    file_size BIGINT,
    file_url VARCHAR(255),
    category_id BIGINT,
    kb_id BIGINT NOT NULL,
    creator_id BIGINT NOT NULL,
    version INT DEFAULT 1,
    status SMALLINT DEFAULT 1,
    deleted SMALLINT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 文档标签关联表
CREATE TABLE kb_document_tag (
    id BIGSERIAL PRIMARY KEY,
    document_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (document_id, tag_id)
);

-- 文档版本表
CREATE TABLE kb_document_version (
    id BIGSERIAL PRIMARY KEY,
    document_id BIGINT NOT NULL,
    version INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    summary TEXT,
    keywords TEXT,
    file_url VARCHAR(255),
    operator_id BIGINT NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 文档向量表
CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE kb_document_vector (
    id BIGSERIAL PRIMARY KEY,
    document_id BIGINT NOT NULL,
    content_vector vector(1536),
    chunk_index INT NOT NULL,
    chunk_content TEXT NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (document_id, chunk_index)
);

-- AI模型配置表
CREATE TABLE sys_ai_model (
    id BIGSERIAL PRIMARY KEY,
    model_name VARCHAR(50) NOT NULL,
    model_type SMALLINT NOT NULL, -- 1:推理模型 2:普通模型
    api_url VARCHAR(255) NOT NULL,
    api_key VARCHAR(255) NOT NULL,
    status SMALLINT DEFAULT 1,
    deleted SMALLINT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 对话历史表
CREATE TABLE `kb_chat_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `session_id` varchar(64) NOT NULL COMMENT '会话ID',
  `kb_id` bigint(20) DEFAULT NULL COMMENT '知识库ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `question` text NOT NULL COMMENT '问题内容',
  `answer` text NOT NULL COMMENT '回答内容',
  `tokens_used` int(11) DEFAULT NULL COMMENT '使用的token数',
  `process_time` bigint(20) DEFAULT NULL COMMENT '处理时间(毫秒)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_session_id` (`session_id`),
  KEY `idx_kb_id` (`kb_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识库对话历史表';

-- 用户行为记录表
CREATE TABLE kb_user_behavior (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    document_id BIGINT NOT NULL,
    behavior_type SMALLINT NOT NULL, -- 1:查看 2:搜索 3:下载
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


