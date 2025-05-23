## Broongs(차량 관리 시스템)

### 기능소개
#### 팀
1. 사용자는 차량 관리를 위한 팀을 생성할 수 있다.
2. 팀을 생성한 사람은 다른 사용자를 초대할 수 있다.
3. 초대 시 이메일로 링크 전송됨(유효기간 24시간)
4. 중복 수락 문제 해결
5. 링크 수락 시 사용자는 해당 팀에 참여
6. 차량 팀을 생성한 사람은 기본적으로 팀 관리자 권한
7. 팀 관리자는 소속 사용자를 팀 관리자로 지정 가능

#### 사용자권한
1. 전체 서비스 관리자 - admin
2. 팀 소유자 - owner
3. 팀 관리자 - manager
4. 일반 사용자 -  user

#### 차량 등록
1. 관리자는 관리중인 팀에 차량을 등록할 수 있다.
2. 차량 등록은 같은 차량 번호를 등록할 수 없다.
3. 대표사진 1장, 차량 번호, 차종, 색상, 누적 운행거리, 연료상태를 기록한다.

#### 차량 조회
1. 모든 팀원은 팀 소속의 차량의 정보를 확인할 수 있다.
2. 차량 정보는 사진, 차량번호, 차종, 색상, 누적 운행거리, 주차위치 연료상태가 있다.
3. 차량정보 업데이트 기능이 수행되거나, 누군가 차량을 예약 후 반납하였을 경우에 주차위치, 연료상태, 누적 운행거리 미 입력시 이후 '확인 불가'로 표시 -> 즉 항상 최신 신뢰 가능한 정보만 표기

#### 차량 예약
1. 사용자는 소속 팀 내 차량을 예약할 수 있다.
2. 차량 예약시에는 날짜, 시간(30분단위)을 입력하며, 예약 정보 화면에는 날짜, 시간, 사용자가 표시된다.
3. 예약자에겐 차량 이용 30분 전 알림이 간다.(차량번호, 차종, 색상)
4. 일반 사용자는 본인 예약건에 한하여 수정, 삭제할 수 있다.
5. 모든 팀 내 사용자는 팀 내 차량의 예약현황을 확인할 수 있다.
6. 관리자는 모든 예약을 등록, 수정, 삭제할 수 있다.

#### 차량 정보 업데이트 기능
1. 사용자는 소속 팀 내 주차위치, 누적 운행거리, 연료 상태를 등록할 수 있다.
2. 주차 위치 기록에는 좌표, 사진, 텍스트를 등록할 수 있다.
3. 주차 위치 기록에는 최종 수정 사용자의 닉네임이 기록된다.
4. 일정 기간 차량 정보 업데이트가 안 된 경우, 관리자 로그인 시 알림이 뜨도록 함

