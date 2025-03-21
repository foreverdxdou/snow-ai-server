CREATE TABLE llm_token_usage (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    model_id BIGINT NOT NULL COMMENT '模型ID',
    tokens_used INT NOT NULL COMMENT '使用的token数量',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_model_id (model_id),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI模型Token使用情况表'; 