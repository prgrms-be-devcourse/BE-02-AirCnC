## AirCnC

AirBnB 클론 코딩 프로젝트 입니다.

![Logo](images/logo.png)

프로젝트의 목적은 동작하는 애플리케이션의 API 를 분석하고 협업을 통해 구현하는 것입니다.

회원, 숙소, 예약, 리뷰, 위시리스트의 다섯가지 주요 도메인으로 유저스토리를 작성하여 `MosCow`를 바탕으로 분류 하였으며 
`Must`에 해당되는 유저스토리를 완수하였습니다.

See 유저스토리, MosCow [@wiki](https://github.com/prgrms-be-devcourse/BE-02-AirCnC/wiki/%EC%9C%A0%EC%A0%80%EC%8A%A4%ED%86%A0%EB%A6%AC,-MosCow)

### 중요하게 생각한것
- 테스트코드의 작성
- 객체지향적 설계, 클린 코드
- 핵심 비즈니스 로직
    - 예약 불가능한 날짜 계산
    - 예약날짜는 겹칠 수 없다
    - 이벤트 큐를 활용한 여행 예약 비동기화, 동시성 문제 

### 팀 소개
<table>
  <tr>
    <td>
        <a href="https://github.com/htmn-fly">
            <img src="https://avatars.githubusercontent.com/u/78348340?v=4" width="100px" />
        </a>
    </td>
    <td>
        <a href="https://github.com/ndy2">
            <img src="https://avatars.githubusercontent.com/u/67302707?v=4" width="100px" />
        </a>
    </td>
    <td>
        <a href="https://github.com/rioreo22">
            <img src="https://avatars.githubusercontent.com/u/97699174?v=4" width="100px" />
        </a>
    </td>
    <td>
        <a href="https://github.com/NewEhoDoc">
            <img src="https://avatars.githubusercontent.com/u/53653597?v=4" width="100px" />
        </a>
    </td>
    <td>
        <a href="https://github.com/jk05018">
            <img src="https://avatars.githubusercontent.com/u/68465557?v=4" width="100px" />
        </a>
    </td>
  </tr>
  <tr>
    <td><b>길근오</b></td>
    <td><b>남득윤</b></td>
    <td><b>박현지</b></td>
    <td><b>변주한</b></td>
    <td><b>황승한</b></td>
  </tr>
  <tr>
    <td><b>Scrum Master</b></td>
    <td><b>Developer</b></td>
    <td><b>Developer</b></td>
    <td><b>Developer</b></td>
    <td><b>Product Owner</b></td>
  </tr>
</table>

### 사용한 기술

![Java](https://img.shields.io/badge/java_11-%23ED8B00.svg?style=Plastic&logo=java&logoColor=white)
![Spring](https://img.shields.io/badge/spring_Boot_2.7.0-%236DB33F.svg?style=Plastic&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-%23121011.svg?style=Plastic&logo=mysql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=Plastic&logo=JSON%20web%20tokens)
![JUnit5](https://img.shields.io/badge/JUnit5-white?style=Plastic&logo=JUnit5)

![Spring](https://img.shields.io/badge/spring_Security-%236DB33F.svg?style=Plastic&logo=spring&logoColor=white)
![Spring_Rest_Docs](https://img.shields.io/badge/spring_Rest--Docs-%236DB33F.svg?style=Plastic&logo=Asciidoctor&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle_7.4.1-02303A.svg?style=Plastic&logo=Gradle&logoColor=white)
![Flyway](https://img.shields.io/badge/flyway-white.svg?style=Plastic&logo=Flyway&logoColor=red)
![AWS](https://img.shields.io/badge/S3-%23FF9900.svg?style=Plastic&logo=amazon-aws&logoColor=white)

### 협업

![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=Plastic&logo=github&logoColor=white)
![Jira](https://img.shields.io/badge/jira-%230A0FFF.svg?style=Plastic&logo=jira&logoColor=white)
![Slack](https://img.shields.io/badge/Slack-4A154B?style=Plastic&logo=slack&logoColor=white)

### ERD

![ERD](images/erd.png)

### API endpoints

path prefix : `/api/v1`

- ### 회원

| 기능    | path       | method |
|-------|------------|--------|
| 회원 가입 | `/members` | `POST` |
| 로그인   | `/login`   | `POST` |
| 회원 정보 | `/me`      | `GET`  |

- ### 숙소

| 기능                     | path                    | method   |
|------------------------|-------------------------|----------|
| 숙소 등록                  | `/hosts/rooms`          | `POST`   |
| 숙소 목록 조회               | `/rooms`                | `POST`   |
| 호스트가 자신의 <br> 숙소 목록 조회 | `/hosts/rooms`          | `GET`    |
| 숙소 상세 조회               | `/rooms/{roomId}`       | `GET`    |
| 숙소 변경                  | `/hosts/rooms/{roomId}` | `PATCH`  |
| 숙소 삭제                  | `/hosts/rooms/{roomId}` | `DELETE` |

- ### 여행

| 기능       | path                     | method |
|----------|--------------------------|--------|
| 여행 예약    | `/trips`                 | `POST` |
| 여행 목록 조회 | `/trips`                 | `GET`  |
| 여행 상세 조회 | `/trips/{tripId}`        | `GET`  |
| 여행 취소    | `/trips/{tripId}/cancel` | `POST` |

- 더 자세한 API 명세를 확인 하시려면 [위키](https://github.com/prgrms-be-devcourse/BE-02-AirCnC/wiki/AirCnC-api-v1-%EB%AA%85%EC%84%B8) 를 확인해주세요:

### 회의 결과 및 스프린트 회고
- 스프린트 0 (22.06.15 ~ 22.06.17) 회의 결과<br>
  - [22.06.13 회의록](https://github.com/prgrms-be-devcourse/BE-02-AirCnC/wiki/22.06.13-%ED%9A%8C%EC%9D%98%EB%A1%9D)
  - [22.06.14 회의록](https://github.com/prgrms-be-devcourse/BE-02-AirCnC/wiki/22.06.14-%ED%9A%8C%EC%9D%98%EB%A1%9D)
  - [요구사항 명세](https://github.com/prgrms-be-devcourse/BE-02-AirCnC/wiki/%EC%9A%94%EA%B5%AC%EC%82%AC%ED%95%AD-%EB%AA%85%EC%84%B8)
  - [유저스토리, MosCow](https://github.com/prgrms-be-devcourse/BE-02-AirCnC/wiki/%EC%9C%A0%EC%A0%80%EC%8A%A4%ED%86%A0%EB%A6%AC,-MosCow)
  - [이벤트 스토밍](https://github.com/prgrms-be-devcourse/BE-02-AirCnC/wiki/%EC%9D%B4%EB%B2%A4%ED%8A%B8-%EC%8A%A4%ED%86%A0%EB%B0%8D)

- 스프린트 1 (22.06.18 ~ 22.06.24) 
  - [회고 PR#41](https://github.com/prgrms-be-devcourse/BE-02-AirCnC/pull/41)

- 스프린트 2 (22.07.25 ~ 22.07.01)
  - [회고 PR#67](https://github.com/prgrms-be-devcourse/BE-02-AirCnC/pull/67)

- 스프린트 3 (22.07.02 ~ 22.07.06) 
  - 회고 TBD

### 규칙
  - [규칙](https://github.com/prgrms-be-devcourse/BE-02-AirCnC/wiki/%EA%B7%9C%EC%B9%99)
  - 일관성을 위해 정한 규칙입니다.
  - 깃 커밋 메시지, 브랜치 전략, PR 관리, 페어 프로그래밍 규칙 ,계층별 패키지 구조 및 코드 작성 룰, 테스트 코드 작성 룰 등 <br>
    협업을 진행하며 이견이 있을 다양한 사항에 대한 규칙을 정리하며 프로젝트를 진행하였습니다.


<p align="center">
  <img src="images/logo.png" alt="drawing" width="327"/>
</p>


