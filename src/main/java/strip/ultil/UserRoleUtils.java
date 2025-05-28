package strip.ultil;

import strip.domain.User;

public class UserRoleUtils {

    public static boolean isNormalUser(User user) {
        return user
            .getAuthorities()
            .stream()
            .noneMatch(auth -> "ROLE_ADMIN".equalsIgnoreCase(auth.getName()) || "ROLE_STAFF".equalsIgnoreCase(auth.getName()));
    }

    public static boolean isDriver(User user) {
        return user.getAuthorities().stream().noneMatch(auth -> "ROLE_DRIVER".equalsIgnoreCase(auth.getName()));
    }

    public static boolean isAdmin(User user) {
        return user.getAuthorities().stream().anyMatch(auth -> "ROLE_ADMIN".equalsIgnoreCase(auth.getName()));
    }

    public static boolean isStaff(User user) {
        return user.getAuthorities().stream().anyMatch(auth -> "ROLE_STAFF".equalsIgnoreCase(auth.getName()));
    }

    public static boolean isAdminOrStaff(User user) {
        return isAdmin(user) || isStaff(user);
    }
}
