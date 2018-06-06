package com.gmail.dzhivchik.to;

import java.io.Serializable;

public class UserTo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String login;
    private String password;
    private String email;
    private long busySize;
    private String[] showBusySize;

    public UserTo() {
    }

    public UserTo(Integer id, String login, String email, String password) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.password = password;
        this.showBusySize = new String[2];
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String[] getshowBusySize() {
        return showBusySize;
    }

    public void setBusySize(long busySize) {
        this.busySize = busySize;
        changeShowBusySize();
    }

    public void changeBusySize(long additionalSize) {
        this.busySize += additionalSize;
        changeShowBusySize();
    }

    public void changeShowBusySize() {
        long wholePart = busySize;
        long ostatok = 0;
        long delitel = 1;
        long size = 0;
        int pow = 0;
        while (wholePart / 1024 > 0) {
            wholePart = wholePart / 1024;
            delitel = delitel * 1024;
            pow++;
        }
        ostatok = busySize%delitel;
        size = (int)Math.round(Double.parseDouble(wholePart + "." + ostatok));

        switch (pow){
            case 0:
                if(busySize != 0){
                    showBusySize[0] = size + " bytes";
                }
                break;
            case 1:
                showBusySize[0] = size + " Kb";
                break;
            case 2:
                showBusySize[0] = size + " Mb";
                break;
            case 3:
                showBusySize[0] = size + " Gb";
                break;
            default: showBusySize[0] = String.valueOf(size);
        }
        showBusySize[1] = "10 Gb";
    }

    @Override
    public String toString() {
        return "UserTo{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
