package org.noear.solon.view.jsp.tags;

import org.noear.solon.Utils;
import org.noear.solon.annotation.Component;
import org.noear.solon.extend.auth.AuthUtil;
import org.noear.solon.extend.auth.annotation.Logical;
import org.noear.solon.extend.auth.tags.TagAttrs;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * @author noear
 * @since 1.4
 */
@Component("view:hasPermission")
public class HasPermissionsTag extends TagSupport {
    @Override
    public int doStartTag() throws JspException {
        String nameStr = name;
        String logicalStr = logical;

        if (Utils.isEmpty(nameStr)) {
            return super.doStartTag();
        }

        String[] names = nameStr.split(",");

        if (names.length == 0) {
            return super.doStartTag();
        }


        if (AuthUtil.verifyPermissions(names, Logical.of(logicalStr))) {
            return TagSupport.EVAL_BODY_INCLUDE;
        } else {
            return super.doStartTag();
        }
    }


    String name;
    String logical;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogical() {
        return logical;
    }

    public void setLogical(String logical) {
        this.logical = logical;
    }
}
