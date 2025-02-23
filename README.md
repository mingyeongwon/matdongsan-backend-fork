# 백엔드 리팩토링 진행사항 🚀

### 📂 프로젝트 개요

Java Spring Boot와 PostgreSQL을 기반으로 한 백엔드 프로젝트입니다. 현재 Docker와 EC2를 활용해 배포를 진행했으며, 데이터 저장 방식을 최적화하기 위한 리팩토링을 진행 중입니다.

---

## 💡 현재 진행 상황

1. **AWS 배포 환경 구성**
   - **Docker**: Spring Boot 애플리케이션을 Docker 컨테이너로 패키징하여 관리 및 배포를 용이하게 구성했습니다.
   - **Docker Compose**: Docker Compose를 사용하여 애플리케이션과 데이터베이스(PostgreSQL)를 컨테이너화하고, 로컬 및 배포 환경에서 손쉽게 실행 및 관리할 수 있도록 설정했습니다.
   - **EC2**: Docker Compose로 구성된 컨테이너를 AWS EC2 인스턴스에서 실행하여 안정적인 서버 환경을 제공했습니다.
   - **Route 53**: 서브도메인을 사용하여 애플리케이션의 접근성을 높이고 HTTPS 인증서를 적용해 보안을 강화했습니다.

2. **데이터 저장 방식 리팩토링**
   - **기존 방식**: 사진 파일을 데이터베이스에 직접 저장 (비효율적인 저장 공간 사용 및 성능 저하)
   - **변경된 방식**: 
     - 사진 파일은 AWS S3에 업로드하여 관리.
     - 데이터베이스(PostgreSQL)에는 S3의 사진 URL만 저장하도록 설계 및 코드 작성 중.
   - **장점**:
     - 데이터베이스의 저장 공간 절약.
     - 대용량 파일의 빠른 전송과 관리 가능.

---

## 🛠 사용 기술 스택

- **프레임워크**: Java Spring Boot
- **데이터베이스**: PostgreSQL
- **배포 환경**: Docker, Docker Compose, AWS EC2, Route 53
- **파일 저장소**: AWS S3
