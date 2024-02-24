package com.mars.statement.global.dto;

public class SwaggerExampleValue {
    public static final String LOGIN_SUCCESS_RESPONSE = "{\"code\":200,\"status\":\"OK\",\"message\":\"로그인 성공\",\"data\":{\"id\":1,\"accessToken\":\"\",\"refreshToken\":\"\"}}";
    public static final String REISSUE_SUCCESS_RESPONSE = "{\"code\":200,\"status\":\"OK\",\"message\":\"토큰 재발급 성공\",\"data\":{\"accessToken\":\"\",\"refreshToken\":\"\"}}";
    public static final String GROUP_CREATE_SUCCESS_RESPONSE = "{\"code\":200,\"status\":\"OK\",\"message\":\"그룹 생성 성공\",\"data\":{\"groupId\":1,\"groupName\":\"testGroup\",\"groupImg\":\"testGroup.img\"}}";
    public static final String MAINPAGE_SUCCESS_RESPONSE = "{\"code\":200,\"status\":\"OK\",\"message\":\"그룹 생성 성공\",\"data\":{\"myGroups\":[{\"groupId\":1,\"name\":\"test group\",\"img\":\"image.jpg\"}],\"groupMembers\":[{\"groupMemberId\":1,\"name\":\"test1\",\"img\":\"image.jpg\",\"isConstructor\":true},{\"groupMemberId\":2,\"name\":\"test2\",\"img\":\"image2.jpg\",\"isConstructor\":false}],\"groupSuggests\":[{\"suggestId\":1,\"regDt\":\"2024-01-01 00:00:00.0000000\",\"type\":\"send\",\"suggest\":\"test suggest\"},{\"suggestId\":2,\"regDt\":\"2024-01-01 00:00:00.0000000\",\"type\":\"share\",\"suggest\":\"test suggest2\"}]},\"message\":\"{groupName} 조회 성공\"}";
    public static final String GET_JOIN_RESPONSE = "{\"code\":200,\"status\":\"OK\",\"data\": {\"joinCnt\" : 1,\"memberCnt\":3},\"message\":\"share or send 입장 확인 성공\"}";
    public static final String JOIN_RESPONSE = "{\"code\":200,\"status\":\"OK\",\"data\": [{\"userId\" : 1,\"name\":\"mars\",\"img\":\"img uri\"},{\"userId\" : 2,\"name\":\"mars2\",\"img\":\"img uri\"}],\"message\":\"share or send 입장 성공\"}";
    public static final String GET_WRITE_RESPONSE = "{\"code\":200,\"status\":\"OK\",\"data\": {\"writeCnt\" : 1,\"memberCnt\":3},\"message\":\"share or send 작성 확인 성공\"}";
    public static final String GET_SUMMARY_RESPONSE = "{\"code\":200,\"status\":\"OK\",\"data\": true,\"message\":\"share or send 서머리 확인 성공\"}";
    public static final String GET_WRITE_AFTER_SEND = "{\"code\":200,\"status\":\"OK\",\"data\": {\"messages\" : [{\"userId\":1,\"message\":\"받은 메세지 1\"},{\"userId\":2,\"message\":\"받은 메세지 2\"}]},\"message\":\"조회 성공\"}";
    public static final String GET_WRITE_AFTER_SHARE = "{\"code\":200,\"status\":\"OK\",\"data\": {\"opinions\" : [{\"userId\":1,\"opinion\":\"의견 1\"},{\"userId\":2,\"opinion\":\"의견 2\"}], \"constructorId\" : 1 },\"message\":\"조회 성공\"}";
}
