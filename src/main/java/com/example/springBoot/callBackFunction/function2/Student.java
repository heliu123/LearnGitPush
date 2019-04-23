package com.example.springBoot.callBackFunction.function2;

/**
 * Created by Administrator on 2019/4/23.
 */
public class Student implements DoHomeWork {

    @Override
    public void doHomeWork(String question, String answer) {
        System.out.println("作业本");
        if(answer != null) {
            System.out.println("作业："+question+" 答案："+ answer);
        } else {
            System.out.println("作业："+question+" 答案："+ "(空白)");
        }
    }

    public void ask( String homework,  RoomMate roomMate) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                roomMate.getAnswer(homework, Student.this);
            }
        }).start();

        goHome();
    }



    public void goHome(){
        System.out.println("我回家了……好室友，帮我写下作业。");
    }


    public static void main(String[] args) {
        Student student = new Student();
        String homework = "当x趋向于0，sin(x)/x =?";
        student.ask(homework, new RoomMate());

    }



}
