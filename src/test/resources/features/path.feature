Feature: 지하철 경로 검색
  Background:
    Given 지하철역들을 생성 요청하고
      | name   |
      | 신논현역 |
      | 교대역   |
      | 강남역   |
      | 역삼역   |
      | 남부터미널역 |
      | 양재역   |
      | 논현역   |
    And 지하철 노선을 생성 요청하고
      | name     | color  | startStation | endStation | distance | duration | additionalFare |
      | 신분당선 | red    | 신논현역       | 강남역     | 31       | 42        | 900            |
      | 2호선    | green  | 교대역       | 강남역     | 24        | 28        | 0            |
      | 3호선    | orange | 교대역       | 남부터미널역     | 10       | 20       | 500            |
    And 구간을 추가 요청하고
      | line     | upStation | downStation | distance | duration |
      | 2호선    | 강남역     | 역삼역      | 23       | 46              |
      | 3호선    | 남부터미널역 | 양재역     | 41       | 82                 |
      | 신분당선 | 강남역     | 양재역      | 40       | 80                |

  Scenario: 같은 노선에 존재하는 두 역을 조회하는 경우 경로가 정상적으로 조회된다
    When "교대역"과 "역삼역"의 경로를 조회하면
    Then 경로가 정상적으로 조회된다

  Scenario: 출발역에서 한번 환승을 해야 도착역에 다다를 수 있는 경우 경로가 정상적으로 조회된다
    When "교대역"과 "신논현역"의 경로를 조회하면
    Then 경로가 정상적으로 조회된다

  Scenario: 출발역에서 두번 환승을 해야 도착역에 다다를 수 있는 경우 경로가 정상적으로 조회된다
    When "신논현역"과 "남부터미널역"의 경로를 조회하면
    Then 경로가 정상적으로 조회된다

  Scenario: 출발역과 도착역이 같은 경우 역이 하나만 조회된다
    When "강남역"과 "강남역"의 경로를 조회하면
    Then 역이 하나만 조회된다

  Scenario: 출발역과 도착역이 연결이 되어 있지 않은 경우 예외가 발생한다
    When "강남역"과 "논현역"의 경로를 조회하면
    Then 예외가 발생한다

  Scenario: 조회하려는 출발역이 null인 경우 예외가 발생한다
    When 출발역이 null인 경로를 조회하면
    Then 예외가 발생한다

  Scenario: 존재하지 않는 출발역을 조회하는 경우 예외가 발생한다
    When 출발역이 "999"인 경로를 조회하면
    Then 예외가 발생한다

  Scenario: 조회하려는 도착역이 null인 경우 예외가 발생한다
    When 도착역이 null인 경로를 조회하면
    Then 예외가 발생한다

  Scenario: 존재하지 않는 도착역을 조회하는 경우 예외가 발생한다
    When 도착역이 "999"인 경로를 조회하면
    Then 예외가 발생한다

  Scenario: 출발역에서 도착역까지의 최소 거리 기준으로 경로 조회를 요청하면 경로가 정상적으로 조회된다.
    When 출발역에서 도착역까지의 최소 거리 기준으로 경로 조회를 요청하면
      | source | target | type     |
      | 교대역 | 양재역 | DISTANCE |
    Then 최소 거리 기준으로 경로가 정상적으로 조회된다
    And 총 거리와 소요 시간을 함께 응답한다
    And 지하철 이용 요금도 함께 응답한다

  Scenario: 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청하면 경로가 정상적으로 조회된다.
    When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청하면
      | source | target | type     |
      | 교대역 | 양재역 | DURATION |
    Then 최소 시간 기준으로 경로가 정상적으로 조회된다
    And 총 거리와 소요 시간을 함께 응답한다
    And 지하철 이용 요금도 함께 응답한다

  Scenario: 로그인한 유아 사용자가 경로를 조회할 때 요금이 없다
    Given 회원가입을 요청하고
      | email           |  password        | age  |
      | baby@test.com  | default_password | 5    |
    Given 로그인 요청을 하고
      | email           | password          |
      | baby@test.com  | default_password  |
    When 로그인 사용자 "baby@test.com"가 "교대역"과 "양재역"의 경로를 거리 기준으로 조회하면
    Then 경로가 정상적으로 조회된다
    And 총 거리와 소요 시간을 함께 응답한다
    And 유아 할인 요금이 적용된 경로 요금을 응답한다

  Scenario: 로그인한 어린이 사용자가 경로를 조회할 때 할인된 요금이 적용된다
    Given 회원가입을 요청하고
      | email           |  password        | age  |
      | child@test.com  | default_password | 9    |
    Given 로그인 요청을 하고
      | email           | password          |
      | child@test.com  | default_password  |
    When 로그인 사용자 "child@test.com"가 "교대역"과 "양재역"의 경로를 거리 기준으로 조회하면
    Then 경로가 정상적으로 조회된다
    And 총 거리와 소요 시간을 함께 응답한다
    And 어린이 할인 요금이 적용된 경로 요금을 응답한다

  Scenario: 로그인한 청소년 사용자가 경로를 조회할 때 할인된 요금이 적용된다
    Given 회원가입을 요청하고
      | email           | password         | age |
      | teen@test.com   | default_password | 15  |
    And 로그인 요청을 하고
      | email           | password         |
      | teen@test.com   | default_password |
    When 로그인 사용자 "teen@test.com"가 "교대역"과 "양재역"의 경로를 거리 기준으로 조회하면
    Then 경로가 정상적으로 조회된다
    And 총 거리와 소요 시간을 함께 응답한다
    And 청소년 할인 요금이 적용된 경로 요금을 응답한다

  Scenario: 로그인하지 않은 사용자가 경로를 조회할 때 추가 요금이 포함된 기본 요금이 적용된다
    When "교대역"과 "양재역"의 경로를 조회하면
    Then 경로가 정상적으로 조회된다
    And 총 거리와 소요 시간을 함께 응답한다
    And 추가 요금이 포함된 기본 요금을 응답한다

  Scenario: 로그인하지 않은 사용자가 여러 노선의 추가 요금이 포함된 경로를 조회할 때 가장 높은 추가 요금이 적용된다
    When "신논현역"과 "양재역"의 경로를 조회하면
    Then 경로가 정상적으로 조회된다
    And 신논현역과 양재역의 총 거리와 소요 시간을 함께 응답한다
    And 가장 높은 추가 요금이 포함된 요금을 응답한다