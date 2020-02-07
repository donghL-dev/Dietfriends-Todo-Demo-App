Dietfriends Todo Demo App
===

> `Dietfriends Todo Demo App`의 [API Spec](https://df-test.docs.stoplight.io/api-reference/todos-api)을 기반으로 진행한 `Todo API` 프로젝트입니다. 

## 개발환경

|도구|버전|
|:---:|:---:|
| Framework |Spring Boot 2.2.4 |
| OS |Windows 10, Ubuntu 18.04|
| IDE |IntelliJ IDEA Ultimate |
| JDK |JDK 1.8+|
| DataBase |MySQL Server 5.7|
| Build Tool | Gradle 4.8.1 |

## 개발 방법
<details><summary>세부정보</summary>

* 개발과 관련된 모든 이야기는 [Issues](https://github.com/donghL-dev/Dietfriends-Todo-Demo-App/issues)에서 진행합니다.

* `API` 및 도메인 명세를 기반으로 개발을 진행합니다.

* **Fork**를 통한 `PR`을 지향합니다.

* 아래와 같은 `Git Workflow`를 지향하며 지키려고 노력합니다. ([참고](https://nvie.com/posts/a-successful-git-branching-model/?))

    <img width=750, height=850, src="https://camo.githubusercontent.com/7f2539ff6001fe7700853313e7cdb7fd4602e16a/68747470733a2f2f6e7669652e636f6d2f696d672f6769742d6d6f64656c4032782e706e67">

</details>

## 실행 방법
<details><summary>세부정보</summary>

* 준비사항.

    * `Gradle` or `IntelliJ IDEA`

    * `JDK` (>= 1.8)

    * `Spring Boot` (>= 2.x)

* 저장소를 `clone`

    ```bash
    $ git clone https://github.com/donghL-dev/Dietfriends-Todo-Demo-App.git
    ```

* `DB`는 `MySQL`을 쓴다고 가정. 

    * 다른 `DB`를 사용한다면, 그 `DB`에 맞게 설정을 해야함.

* 프로젝트 내 `Dietfriends-Todo-Demo-App\src\main\resources` 경로에 `application.yml` 생성.

    * 밑의 양식대로 내용을 채운 뒤, `application.yml`에 삽입.
    <br>

    ```yml
    spring:
        datasource:
            url: jdbc:mysql://localhost:3306/본인_DB?serverTimezone=UTC
            username: 본인_DB_User
            password: 본인_DB_User_Password
            driver-class-name: com.mysql.cj.jdbc.Driver
        jpa:
            hibernate:
                ddl-auto: create
    ```

* `IntelliJ IDEA`(>= 2018.3)에서 해당 프로젝트를 `Open`

    * 또는 터미널을 열어서 프로젝트 경로에 진입해서 다음 명령어를 실행.

    * `Windows 10`

        ```bash
        $ gradlew bootRun
        ```

    * `Ubuntu 18.04`

        ```bash
        $ ./gradlew bootRun
        ```

</details>

## 배포 서버

* 추후 추가 예정

## Dependencies
<details><summary>세부정보</summary>

* `Spring Web`

* `Spring Data JPA`

* `MySQL Driver`

* `H2 Database`

* `Lombok`

</details>

## 도메인 명세

* [도메인 명세 문서](https://www.notion.so/dhlab/ToDo-f3a2e60ff75646358a032e67b56b2b55)

## API 명세 
<details><summary>세부정보</summary>

* 모든 `API`에 대한 반환은 `Content-Type: application/json; charset=utf-8`를 기본으로 합니다.

* 인증(`auth`)은 `HTTP` 헤더를 사용해서 진행됩니다.<br>

    | Key | Value |
    |:---:|:---:|
    | Content-Type | `application/json` |
    | Authorization | `token` |

* `Response`

    * 추후 추가 예정

</details>

## Reference
<details><summary>세부정보</summary>
<br>

* 참고한 소스코드 또는 오픈 소스가 생길 경우 추가 예정.

</details>
