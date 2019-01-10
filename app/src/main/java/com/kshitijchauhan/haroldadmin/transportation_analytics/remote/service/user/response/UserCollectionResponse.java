package com.kshitijchauhan.haroldadmin.transportation_analytics.remote.service.user.response;

import com.kshitijchauhan.haroldadmin.transportation_analytics.models.User;

import java.util.List;

public class UserCollectionResponse {
    private List<User> userList;

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
