package com.rosberry.hackathon;

import java.io.Serializable;

/**
 * Created by dmitry on 09.12.2017.
 */

public abstract class AuthorizationModel implements IAuthorization, Serializable {

    abstract AuthorizationType getType();
}
