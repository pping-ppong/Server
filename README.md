# Ping Pong

> 🏓 **할 일을 넘길 수 있는 일정 관리 애플리케이션**

![핑퐁 배너](https://github.com/yaezzin/PingPong/assets/97823928/573c893c-04ad-4d39-8e81-3b81bab7e277)

## Description



## Main Feature

#### 회원가입 및 로그인

* 소셜로그인 - 구글/네이버/애플
* Spring Security와 JWT를 이용

#### 팀


#### 친구

#### 알림

## Stack
* **Languange** : Java
* **Library & Framewrok** : Spring
* **Database** : AWS RDS (MySQL, MongoDB, Redis)
* **ORM** : JPA
* **Deploy** : AWS EC2

## Project Structure

```
src
└── main
    ├── 🤖 domain
    │   ├── 👥 friend
    │   │   ├── controller
    │   │   ├── dto
    │   │   ├── entity
    │   │   ├── repository
    │   │   └── service
    │   ├── 📷 image
    │   └── 🙍 member
    │   └── ⏰ notification
    │   └── 📣 social
    │   └── 👥 team
    │
    └── 🌎 global
        ├── aop
        ├── common
        ├── config
        └── security
```

## ERD

<img width="600" alt="스크린샷 2024-03-20 오후 6 10 19" src="https://github.com/yaezzin/PingPong/assets/97823928/9f685e9d-b0e5-4e41-b5b6-290cffad5a9e">

## Server Architecture




## CI/CD

* github actions를 활용해서 지속적 통합 및 배포
* dev 브랜치에서 release로 Pull Request를 보내면, CI가 동작
* release로 prod로 Pull Request를 보내면, CI가 동작되고 Merge가 되면, 운영 리소스에 배포

## Dev Issue

#### ✍ 개발 중 고민했던 내용들 ✍

* [조회 성능에 대한 고민 - QueryDsl](https://github.com/pping-ppong/Server/issues/2)
* [@ManyToOne 연관관계 매핑에 대한 고민](https://github.com/pping-ppong/Server/issues/2)
* [검색 기록 저장 및 조회 기능](https://github.com/pping-ppong/Server/issues/3)
* [AOP를 활용한 중복코드 리팩토링](https://github.com/pping-ppong/Server/issues/4)
* [공통 응답 객체 만들기](https://github.com/pping-ppong/Server/issues/5)
* [No offset 방식 적용하기](https://github.com/pping-ppong/Server/issues/6)
* [알림 서비스에 대한 고민](https://github.com/pping-ppong/Server/issues/6)

## Developer

* ```🤖 Android``` **김민지** ([@mingZZ-3](https://github.com/mingZZ-3))
* ```🍎 iOS```     **성민주** ([@yoogail105](https://github.com/yoogail105))
* ```🛠 Backend``` **전예진** ([@yaezzin](https://github.com/yaezzin))