package org.noear.solon.extend.security.annotation;

import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Result;
import org.noear.solon.extend.security.SecurityServiceProxy;
import org.noear.solon.extend.validation.Validator;

/**
 * @author noear
 * @since 1.3
 */
public class AuthGuestValidator implements Validator<AuthGuest> {
    public static final AuthGuestValidator instance = new AuthGuestValidator();


    @Override
    public String message(AuthGuest anno) {
        return anno.message();
    }

    @Override
    public Result validate(Context ctx, AuthGuest anno, String name, StringBuilder tmp) {
        if (SecurityServiceProxy.getInstance().verifyLogined() == false) {
            return Result.succeed();
        } else {
            return Result.failure();
        }
    }
}
