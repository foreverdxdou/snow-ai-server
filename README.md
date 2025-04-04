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

Snow AI Server is an intelligent server project based on Spring Boot 3.x, integrating multiple advanced technologies to provide powerful backend service support.

Author: [@foreverdxdou](https://github.com/foreverdxdou)

[中文文档](README_CN.md)

## Tech Stack

- **Base Framework**: Spring Boot 3.2.1
- **Security Framework**: Spring Security
- **Database**:
  - PostgreSQL
  - pgvector (Vector Database Support)
- **Cache**: Redis
- **Search Engine**: Elasticsearch
- **Object Storage**: MinIO
- **API Documentation**: Knife4j 4.3.0
- **ORM Framework**: MyBatis-Plus 3.5.5
- **NLP Tools**: Stanford CoreNLP
- **Document Processing**:
  - Apache POI (Word Document Processing)
  - Apache PDFBox (PDF Document Processing)

## Key Features

- Complete user authentication and authorization system
- Vector database support for similarity search
- Intelligent document processing and analysis
- Natural Language Processing capabilities
- High-performance object storage service
- Comprehensive API documentation

## System Requirements

- JDK 17+
- Maven 3.6+
- PostgreSQL 12+
- Redis 6+
- Elasticsearch 8.11.3+

## Quick Start

1. Clone the project
```bash
git clone https://github.com/foreverdxdou/snow-ai-server.git
```

2. Configure the database
- Create PostgreSQL database
- Configure database connection information in application.yml

3. Start the service
```bash
mvn spring-boot:run
```

4. Access API documentation
```
http://localhost:8080/doc.html
```

## License

This project is licensed under a modified MIT License - see the [LICENSE](LICENSE) file for details.

## Important Notes

- This project is for personal learning purposes only
- Commercial use requires author's authorization
- Please ensure all necessary dependent services are installed before use

## Contributing

Issues and Pull Requests are welcome to help improve the project.

## Contact

If you have any questions, please contact us through:

- Submit an Issue
- Send email to: leoj95547@outlook.com

