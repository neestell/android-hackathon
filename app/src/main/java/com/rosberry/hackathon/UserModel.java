package com.rosberry.hackathon;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by dmitry on 09.12.2017.
 */

public class UserModel implements Serializable {

    private final String name;

    public ArrayList<AuthorizationModel> authorizationModels = new ArrayList<AuthorizationModel>();

    public UserModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
