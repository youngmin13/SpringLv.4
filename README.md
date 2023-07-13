### 추가된 요구 사항 (Lv.3 -> Lv.4)
1. 게시글 좋아요 API
    - 사용자는 선택한 게시글에 ‘좋아요’를 할 수 있습니다.
    - 사용자가 이미 ‘좋아요’한 게시글에 다시 ‘좋아요’ 요청을 하면 ‘좋아요’를 했던 기록이 취소됩니다.
    - 요청이 성공하면 Client 로 성공했다는 메시지, 상태코드 반환하기
2. 댓글 좋아요 API
    - 사용자는 선택한 댓글에 ‘좋아요’를 할 수 있습니다.
    - 사용자가 이미 ‘좋아요’한 댓글에 다시 ‘좋아요’ 요청을 하면 ‘좋아요’를 했던 기록이 취소됩니다.
    - 요청이 성공하면 Client 로 성공했다는 메시지, 상태코드 반환하기
3. 예외처리
    - 아래 예외처리를 AOP 를 활용하여 구현하기

### ERD
https://drawsql.app/teams/fighting-2/diagrams/springlv-4/embed
### API
https://youngmin13s-organization.gitbook.io/api-for-spring-lv.4/