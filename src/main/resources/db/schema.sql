-- 创建数据库
CREATE DATABASE snow_ai;

-- 切换到数据库
\c snow_ai;

-- 创建扩展
CREATE EXTENSION IF NOT EXISTS vector;

-- 知识库分类表
CREATE TABLE kb_category (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    parent_id BIGINT,
    kb_id BIGINT NOT NULL,
    sort INTEGER DEFAULT 0,
    creator_id BIGINT NOT NULL,
    status INTEGER DEFAULT 1,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted INTEGER DEFAULT 0
);

-- 知识库对话历史表
CREATE TABLE kb_chat_history (
    id BIGSERIAL PRIMARY KEY,
    session_id VARCHAR(50) NOT NULL,
    kb_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    question TEXT NOT NULL,
    answer TEXT NOT NULL,
    tokens_used INTEGER,
    process_time BIGINT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 文档表
CREATE TABLE kb_document (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    file_type VARCHAR(50),
    file_size BIGINT,
    file_url VARCHAR(500),
    category_id BIGINT,
    kb_id BIGINT NOT NULL,
    version INTEGER DEFAULT 1,
    status INTEGER DEFAULT 1,
    creator_id BIGINT NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted INTEGER DEFAULT 0
);

-- 文档标签关联表
CREATE TABLE kb_document_tag (
    id BIGSERIAL PRIMARY KEY,
    document_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    creator_id BIGINT NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted INTEGER DEFAULT 0
);

-- 文档向量表
CREATE TABLE kb_document_vector (
    id BIGSERIAL PRIMARY KEY,
    document_id BIGINT NOT NULL,
    content_vector vector(1536),
    chunk_index INTEGER,
    chunk_content TEXT,
    similarity DOUBLE PRECISION,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 文档版本表
CREATE TABLE kb_document_version (
    id BIGSERIAL PRIMARY KEY,
    document_id BIGINT NOT NULL,
    version INTEGER NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    file_url VARCHAR(500),
    creator_id BIGINT NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted INTEGER DEFAULT 0
);

-- 知识库表
CREATE TABLE kb_knowledge_base (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    creator_id BIGINT NOT NULL,
    dept_id BIGINT,
    status INTEGER DEFAULT 1,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted INTEGER DEFAULT 0
);

-- 知识库权限表
CREATE TABLE kb_knowledge_base_permission (
    id BIGSERIAL PRIMARY KEY,
    kb_id BIGINT NOT NULL,
    user_id BIGINT,
    role_id BIGINT,
    permission_type INTEGER NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 标签表
CREATE TABLE kb_tag (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description TEXT,
    kb_id BIGINT NOT NULL,
    creator_id BIGINT NOT NULL,
    status INTEGER DEFAULT 1,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted INTEGER DEFAULT 0
);

-- 用户行为表
CREATE TABLE kb_user_behavior (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    kb_id BIGINT NOT NULL,
    doc_id BIGINT NOT NULL,
    behavior_type VARCHAR(20) NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 大模型配置表
CREATE TABLE llm_config (
    id BIGSERIAL PRIMARY KEY,
    model_name VARCHAR(100) NOT NULL,
    api_url VARCHAR(500) NOT NULL,
    api_key VARCHAR(200) NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Token使用情况表
CREATE TABLE llm_token_usage (
    id BIGSERIAL PRIMARY KEY,
    model_id BIGINT NOT NULL,
    tokens_used INTEGER NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- AI模型配置表
CREATE TABLE sys_ai_model (
    id BIGSERIAL PRIMARY KEY,
    model_name VARCHAR(100) NOT NULL,
    model_type INTEGER NOT NULL,
    api_url VARCHAR(500) NOT NULL,
    api_key VARCHAR(200) NOT NULL,
    status INTEGER DEFAULT 1,
    deleted INTEGER DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 部门表
CREATE TABLE sys_dept (
    id BIGSERIAL PRIMARY KEY,
    parent_id BIGINT,
    dept_name VARCHAR(100) NOT NULL,
    sort INTEGER DEFAULT 0,
    leader VARCHAR(50),
    phone VARCHAR(20),
    email VARCHAR(100),
    status INTEGER DEFAULT 1,
    deleted INTEGER DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 权限表
CREATE TABLE sys_permission (
    id BIGSERIAL PRIMARY KEY,
    parent_id BIGINT,
    name VARCHAR(100) NOT NULL,
    type INTEGER NOT NULL,
    permission_code VARCHAR(100) NOT NULL,
    path VARCHAR(200),
    component VARCHAR(200),
    icon VARCHAR(100),
    sort INTEGER DEFAULT 0,
    status INTEGER DEFAULT 1,
    deleted INTEGER DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 角色表
CREATE TABLE sys_role (
    id BIGSERIAL PRIMARY KEY,
    role_name VARCHAR(100) NOT NULL,
    role_code VARCHAR(100) NOT NULL,
    description TEXT,
    status INTEGER DEFAULT 1,
    deleted INTEGER DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 角色权限关联表
CREATE TABLE sys_role_permission (
    id BIGSERIAL PRIMARY KEY,
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    deleted INTEGER DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 用户表
CREATE TABLE sys_user (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    nickname VARCHAR(50),
    email VARCHAR(100),
    avatar VARCHAR(500),
    dept_id BIGINT,
    status INTEGER DEFAULT 1,
    deleted INTEGER DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 用户角色关联表
CREATE TABLE sys_user_role (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    deleted INTEGER DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX idx_kb_category_kb_id ON kb_category(kb_id);
CREATE INDEX idx_kb_chat_history_kb_id ON kb_chat_history(kb_id);
CREATE INDEX idx_kb_document_kb_id ON kb_document(kb_id);
CREATE INDEX idx_kb_document_tag_doc_id ON kb_document_tag(document_id);
CREATE INDEX idx_kb_document_vector_doc_id ON kb_document_vector(document_id);
CREATE INDEX idx_kb_document_version_doc_id ON kb_document_version(document_id);
CREATE INDEX idx_kb_permission_kb_id ON kb_knowledge_base_permission(kb_id);
CREATE INDEX idx_kb_tag_kb_id ON kb_tag(kb_id);
CREATE INDEX idx_kb_user_behavior_kb_id ON kb_user_behavior(kb_id);
CREATE INDEX idx_sys_role_permission_role_id ON sys_role_permission(role_id);
CREATE INDEX idx_sys_user_role_user_id ON sys_user_role(user_id);

-- 添加唯一约束
ALTER TABLE sys_user ADD CONSTRAINT uk_username UNIQUE (username);
ALTER TABLE sys_role ADD CONSTRAINT uk_role_code UNIQUE (role_code);
ALTER TABLE sys_permission ADD CONSTRAINT uk_permission_code UNIQUE (permission_code);
