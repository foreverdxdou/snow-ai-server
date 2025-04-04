# Snow AI Server

```
   _____ _   _  ______          __
  / ____| \ | |/ __ \ \        / /
 | (___ |  \| | |  | \ \  /\  / / 
  \___ \| . ` | |  | |\ \/  \/ /  
  ____) | |\  | |__| | \  /\  /   
 |_____/|_| \_|\____/   \/  \/    
                    * intelligent *
```

Snow AI Server 是一个基于 Spring Boot 3.x 的智能服务器项目，集成了多种先进技术，提供强大的后端服务支持。

作者: [@foreverdxdou](https://github.com/foreverdxdou)

[English](README.md)

## 技术栈

- **基础框架**：Spring Boot 3.2.1
- **安全框架**：Spring Security
- **数据库**：
  - PostgreSQL
  - pgvector（向量数据库支持）
- **缓存**：Redis
- **搜索引擎**：Elasticsearch
- **对象存储**：MinIO
- **API文档**：Knife4j 4.3.0
- **ORM框架**：MyBatis-Plus 3.5.5
- **NLP工具**：Stanford CoreNLP
- **文档处理**：
  - Apache POI（Word文档处理）
  - Apache PDFBox（PDF文档处理）

## 主要特性

- 完整的用户认证和授权系统
- 向量数据库支持，用于相似度搜索
- 文档智能处理和分析
- 自然语言处理能力
- 高性能的对象存储服务
- 完善的API文档支持

## 系统要求

- JDK 17+
- Maven 3.6+
- PostgreSQL 12+
- Redis 6+
- Elasticsearch 8.11.3+

## 快速开始

1. 克隆项目
```bash
git clone https://github.com/foreverdxdou/snow-ai-server.git
```

2. 配置数据库
- 创建 PostgreSQL 数据库
- 配置 application.yml 中的数据库连接信息

3. 启动服务
```bash
mvn spring-boot:run
```

4. 访问API文档
```
http://localhost:8080/doc.html
```

## 许可证

本项目采用修改版 MIT 许可证，详情请参见 [LICENSE](LICENSE) 文件。

## 注意事项

- 本项目仅限个人学习使用
- 商业用途需要获得作者授权
- 使用前请确保已安装所有必要的依赖服务

## 贡献指南

欢迎提交 Issue 和 Pull Request 来帮助改进项目。

## 联系方式

如有任何问题，请通过以下方式联系：

- 提交 Issue
- 发送邮件至：leoj95547@outlook.com 