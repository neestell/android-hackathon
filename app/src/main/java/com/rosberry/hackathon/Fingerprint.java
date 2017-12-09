package com.rosberry.hackathon;

/**
 * Created by dmitry on 09.12.2017.
 */

public class Fingerprint extends AuthorizationModel {

    @Override
    public boolean check() {
        return false;
    }



    @Override
    AuthorizationType getType() {
        return AuthorizationType.FINGERPRINT;
    }
}
