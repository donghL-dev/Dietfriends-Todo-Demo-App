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
| DataBase |MySQL Server 5.7, H2|
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

* `DB`는 `MySQL`을 사용한다고 가정. 

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
        servlet:
           multipart:
                enabled: true
                max-file-size: 200MB
                max-request-size: 215MB

    file:
        upload-dir: ./uploads

    todo:
        jjwt:
            secret: secret key
            expiration: 만료 시간
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

## Dependencies
<details><summary>세부정보</summary>

* `Spring Web`

* `Spring Data JPA`

* `Spring Security`

* `Spring Configuration Processor`

* `JSON Web Token Support For The JVM`

* `Apache Commons IO`

* `JUnit Jupiter API`

* `MySQL Driver`

* `H2 Database`

* `Lombok`

</details>

## 도메인 명세

* [도메인 명세 문서](https://www.notion.so/dhlab/ToDo-f3a2e60ff75646358a032e67b56b2b55)

## API 명세 
<details><summary>세부정보</summary>

* 모든 `API`에 대한 반환은 `Content-Type: application/json; charset=utf-8`를 기본으로 합니다.

    * `API` 명세에 따라서 일부 `API`는 `HTTP Status Code`만 반환합니다.

* 인증(`auth`)은 `HTTP` 헤더를 사용해서 진행됩니다.<br>

    | Key | Value |
    |:---:|:---:|
    | Content-Type | `application/json` |
    | Authorization | `token` |

* `Response`

    * `User`

        * `required`

            * `username`

            * `age`

        ```json
        {
            "idx": 2,
            "username": "...",
            "age": 25,
            "image": "...",
            "token": "..."
        }
        ```

    * `Single Todo`

        * `required`

            * `name`

            * `completed`

        ```json
        {
            "idx": 2,
            "name": "...",
            "completed": "null or boolean",
            "completedAt": "null or datetime",
            "createdAt": "9999-99-99T00:00:00.000Z",
            "updatedAt": "9999-99-99T00:00:00.000Z"
        }
        ```
    
    * `Multiple Todos`

        ```json
        [
            {
                "idx": 3,
                "name": "...",
                "completed": "null or boolean",
                "completedAt": "null or datetime",
                "url": "..."
            },
            {
                "idx": 2,
                "name": "...",
                "completed": "null or boolean",
                "completedAt": "null or datetime",
                "url": "..."
            }
        ]
        ```
    
    * `Image Response`

        ```text
        Image File(jpeg, jpg, png)
        ```

    * `Index Response`

        ```json
        {
            "name": "Dietfriends Todo Demo App",
            "documentation": "https://df-test.docs.stoplight.io/api-reference/intro",
            "github_repo": "https://github.com/donghL-dev/Dietfriends-Todo-Demo-App",
            "todoModel": {
                "createdAt": "Time",
                "updatedA": "Time",
                "name": "String",
                "completed": "Boolean",
                "completedA": "Time"
            },
            "userModel": {
                "image": "String",
                "password": "String",
                "age": "Integer",
                "username": "String",
                "token": "String"
            },
            "endPoints": {
                "todo": {
                    "DELETE": "/todos/{todoId}",
                    "POST": "/todos",
                    "GET": [
                        "/todos",
                        "/todos/{todoId}"
                    ],
                    "PUT": "/todos/{todoId}"
                },
                "user": {
                    "DELETE": [
                        "/user/logout",
                        "/user/image"
                    ],
                    "POST": [
                        "/user",
                        "/user/auth"
                    ],
                    "GET": [
                        "/user",
                        "/user/image/{filename}"
                    ],
                    "PUT": "/user/image"
                }
            }
        }
        ```

    * `Errors Response`

        * `required`

            * `status`

            * `error`

        ```json
        {
            "status": "...",
            "error": "...",
            "message": "..."
        }
        ```
    
    * `Default Success Response`

        ```json
        {
            "status": "200 OK",
            "message": "Your request has been successfully processed."
        }
        ```

* 대표적인 에러 코드

    * `401 for Unauthorized requests`

    * `400 for Bad requests`

    * `404 for Not found requests`

    * `500 for Server Error requests`

* End Point

    * `User API` <br><br>

    | Title | HTTP Method | URL | Request | Response | Success HTTP Status | Auth
    |:---:|:---:|:---:|:---:|:---:|:---:|:---:|
    | `Registration` | `POST` | `/user` | `{ "name": "test_name", "age": 25, "password": "test_password1@" }` | `User` | `201` | `NO`
    | `Authentication` | `POST` | `/users/auth` | `{ "name": "test_name", "password": "test_password1@" }` | `User` | `201` | `NO`
    | `Authentication expiration` | `DELETE` | `/user/logout` |  | `Default Success Response` | `200` | `YES`
    | `Current User` | `GET` | `/user` |  | `User` | `200` | `YES`
    | `Profile Image Read` | `GET` | `/user/image/{filename}` |  | `Image Response` | `200` | `NO`
    | `Profile Image Custom` | `PUT` | `/user/image` | `form-data(file: ImageFile)` | `User` | `200` | `YES`
    | `Profile Image Initialization` | `DELETE` | `/user/image` |  | `User` | `200` | `YES`
    
    * `Todo API` <br><br>

    | Title | HTTP Method | URL | Request | Response | Success HTTP Status | Auth
    |:---:|:---:|:---:|:---:|:---:|:---:|:---:|
    | `Index Access` | `GET` | `/` |  | `Index Response` | `200` | `NO`
    | `List Todos` | `GET` | `/todos` |  | `Multiple Todos` | `200` | `NO`
    | `Limit Number of Todos` | `GET` | `/todos?limit={number}` |  | `Multiple Todos` | `200` | `NO`
    | `Skip Number of Todos` | `GET` | `/todos?skip={number}` |  | `Multiple Todos` | `200` | `NO`
    | `Get Todo` | `GET` | `/todos/{todoId}` |  | `Single Todo` | `200` | `NO`
    | `Create Todo` | `POST` | `/todos` | { "name" : "create todo", "completed": false }  | `Single Todo` | `201` | `YES`
    | `Update Todo` | `PUT` | `/todos/{todoId}` | { "name" : "modifyed todo", "completed": true }  | `Single Todo` | `200` | `YES`
    | `Delete Todo` | `Delete` | `/todos/{todoId}` |  |  | `204` | `YES`
    
</details>

## Reference
<details><summary>세부정보</summary>
<br>

* [Java JWT: JSON Web Token for Java and Android](https://github.com/jwtk/jjwt)

</details>
