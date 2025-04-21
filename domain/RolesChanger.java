package account.domain;

public record RolesChanger(String user, String role, RoleOperations operation) {
}
