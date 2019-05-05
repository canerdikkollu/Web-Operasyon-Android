package com.example.canerdikkollu.wep_operasyon_android.Model;

public class SessionModel {
    public int session_id;
    public String RunningTime;
    public int blocking_session_id;
    public String HostName;
    public String program_name;
    public String LoginName;
    public String Command;
    public String status;
    public String Text;
    public String StartTime;

    public SessionModel(int session_id, String runningTime, int blocking_session_id, String hostName, String program_name, String loginName, String command, String status, String text, String startTime) {
        this.session_id = session_id;
        RunningTime = runningTime;
        this.blocking_session_id = blocking_session_id;
        HostName = hostName;
        this.program_name = program_name;
        LoginName = loginName;
        Command = command;
        this.status = status;
        Text = text;
        StartTime = startTime;
    }

    public int getSession_id() {
        return session_id;
    }

    public void setSession_id(int session_id) {
        this.session_id = session_id;
    }

    public String getRunningTime() {
        return RunningTime;
    }

    public void setRunningTime(String runningTime) {
        RunningTime = runningTime;
    }

    public int getBlocking_session_id() {
        return blocking_session_id;
    }

    public void setBlocking_session_id(int blocking_session_id) {
        this.blocking_session_id = blocking_session_id;
    }

    public String getHostName() {
        return HostName;
    }

    public void setHostName(String hostName) {
        HostName = hostName;
    }

    public String getProgram_name() {
        return program_name;
    }

    public void setProgram_name(String program_name) {
        this.program_name = program_name;
    }

    public String getLoginName() {
        return LoginName;
    }

    public void setLoginName(String loginName) {
        LoginName = loginName;
    }

    public String getCommand() {
        return Command;
    }

    public void setCommand(String command) {
        Command = command;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }
}

