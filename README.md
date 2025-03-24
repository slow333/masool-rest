# masool-rest

### rest api 관련 연습용
### no UI
### jwt 사용
### maven project
### spring 3.3.9

## git에서 수행 및 관리하기 
1. git branch wiz-crud
2. git switch wiz-crud
### 코드 작성이 끝나고 나면
1. git add .
2. git commit -m "Wiz crud done Close #2"
3. git switch main
4. git pull // remote와 동기화
5. git merge main  // 이거 해야함
6. git switch wiz-crud
7. git push --set-upstream origin artifact-crud // gitdp branch를 올림
### git hub로 이동해서
1. 이슈 완료를 체크 // 신규 branch가 생성되어 있음
2. pull request -> 
3. merge pull request 수행 // 이렇게 하면 main과 통합됨
### local로 이동(인텔리제이)
1. git switch main
2. git pull // remote에서 통합한 main을 받음

## jwp encoded
> header.payload.sygnature

> header ; { "alg": "RS256" }  → Base64 Encode

> payload → Base64 Encode
>
```
 { "iss": "self", // 별도 서버 없이 ...
    "sub": "kim",
    "exp": 12738472, // 만료 시간
    "iat": 12738472,
    "authorities": "ROLE_admin ROLE_user"
 }
```
> Private key → Signing Algorithm(header, payload) → signature(Private key) → Base64 Encode

> 최종 JWT

### Spring security exceptions are thrown before the controllers start to work !

## application.properties 내에 있는 db 암호, 접속 정보를 암호화하고 서버에서 실행할때
##   정보를 가져와서 실행하는 방법을 찾아야함(아파치, eap 등에 있나 ?)

## PROD 로 페키지 생성 및 실행하기
1. application.properties : spring.profiles.active=prod
2. application.properties : spring.jpa.properties.hibernate.dialect= org.hibernate.dialect.MySQLDialect 추가
2. ./mvnw clean package -DskipTests
3. jar 파일을 서버로 옮기기
4. 서버에서 db의 권한 설정을 추가 : GRANT ALL PRIVILEGES ON *.* TO 'root'@'rhel8' IDENTIFIED BY '123456';
5. java -jar ***.jar