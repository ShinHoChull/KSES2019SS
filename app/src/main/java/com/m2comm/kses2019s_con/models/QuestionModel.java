package com.m2comm.kses2019s_con.models;

public class QuestionModel {


    private String successAlertMessage = "질문이 성공적으로 제출되었습니다.";
    private String failAlertMessage = "질문이 제출이 실패하였습니다.";
    private String questionMessage = "질문을 작성해 주세요.";

    public String getSuccessAlertMessage() {
        return successAlertMessage;
    }
    public String getFailAlertMessage() {
        return failAlertMessage;
    }
    public String getQuestionMessage() {
        return questionMessage;
    }



}
