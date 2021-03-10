package ${rootPackage}.common.enums;

import lombok.Getter;

/**
 * 用户角色
 * 1系统管理员
 * ADMIN
 *
 * @author ${author}
 */
@Getter
public enum UserRoleEnum {
    /**
     * 系统管理员
     */
    ADMIN(1, "ROLE_ADMIN");

    Integer userType;
    String role;

    UserRoleEnum(int userType, String role) {
        this.userType = userType;
        this.role = role;
    }

    public static UserRoleEnum getByUserType(Integer userType) {
        for (UserRoleEnum e : values()) {
            if (e.getUserType().equals(userType)) {
                return e;
            }
        }
        return null;
    }

    public static UserRoleEnum getByRole(String role) {
        for (UserRoleEnum e : values()) {
            if (e.getRole().equals(role)) {
                return e;
            }
        }
        return null;
    }
}
