package com.miniyus.friday.users.adapter.in.rest.resource;

import java.io.Serializable;

public record ResetPasswordResource(
    boolean resetPassword
) implements Serializable {

}
