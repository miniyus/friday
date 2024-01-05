package com.meteormin.friday.api.users.resource;

import java.io.Serializable;

public record ResetPasswordResource(
    boolean resetPassword
) implements Serializable {

}
