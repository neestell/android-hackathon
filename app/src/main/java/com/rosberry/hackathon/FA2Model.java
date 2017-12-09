package com.rosberry.hackathon;

/**
 * Created by dmitry on 09.12.2017.
 */

public class FA2Model extends AuthorizationModel {

    @Override
    public boolean check() {
        return false;
    }


    @Override
    AuthorizationType getType() {
        return AuthorizationType.FA2;
    }
}
