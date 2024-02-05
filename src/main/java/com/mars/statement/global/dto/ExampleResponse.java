package com.mars.statement.global.dto;

public class ExampleResponse {
    public static final String INTERNAL_SERVER_ERROR_RESPONSE = "{\"code\":500,\"message\":\"내부 서버 오류.\"}";
    public static final String NOT_FOUND_ERROR_RESPONSE = "{\"code\":404,\"message\":\"챕터 또는 멤버를 찾을 수 없습니다.\"}";

    // share
    public static final String SUCCESS_PERSONAL_SHARE = "{\"code\":200,\"message\":\"공유 인물별 조회 성공\",\"data\":[{\"suggestId\":0,\"suggest\":\"string\",\"opinionList\":[{\"memberId\":0,\"memberName\":\"string\",\"memberImg\":\"string\",\"opinionList\":[{\"chapterId\":0,\"regDt\":\"2024-02-05T12:52:10.315Z\",\"opinion\":\"string\",\"location\":\"string\"}]}]}]}";

    public static final String SUCCESS_CHAPTER_SHARE = "{\"code\":200,\"message\":\"공유 회차별 조회 성공\",\"data\":[{\"suggestId\":0,\"suggest\":\"string\",\"summaryList\":[{\"chapterId\":0,\"regDt\":\"2024-02-05T13:13:41.224Z\",\"memberName\":\"string\",\"summary\":\"string\"}]}]}";

    public static final String SUCCESS_SHARE_DETAIL = "{\"code\":200,\"message\":\"공유 회차 디테일 조회 성공\",\"data\":[{\"suggestId\":0,\"suggest\":\"string\",\"chapterId\":0,\"summary\":\"string\",\"memberDetailList\":[{\"opinionId\":0,\"memberId\":0,\"memberName\":\"string\",\"opinion\":\"string\",\"regDt\":\"2024-02-05T13:17:19.275Z\",\"location\":\"string\",\"like\":true}]}]}";

    public static final String SUCCESS_ADD_LIKE = "{\"code\":200,\"message\":\"공유 의견 좋아요 취소 성공\",\"data\":{\"shareId\":0}}";
    public static final String SUCCESS_DEL_LIKE = "{\"code\":200,\"message\":\"공유 의견 좋아요 성공\",\"data\":{\"shareId\":0}}";

}
