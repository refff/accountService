package account.service;

import account.domain.*;
import account.infrastructure.CustomExceptions.*;
import account.persistance.GroupRepository;
import account.persistance.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminService {
    private UserRepository userRepository;
    private GroupRepository groupRepository;

    @Autowired
    public AdminService(UserRepository userRepository, GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    public ResponseEntity<?> getUsersList() {
        List<AccountUserDTO> usersList = new ArrayList<>();
        userRepository.findAll().forEach(
                user -> usersList.add(AccountUser.convertToDTO(user))
        );

        return new ResponseEntity<>(usersList, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteUser(String email) {
        Optional<AccountUser> userOptional = userRepository.findUserByEmail(email);

        if(userOptional.isEmpty()) {
            throw new UserNotExistException();
        }

        AccountUser user = userOptional.get();

        for(Group group: user.getUserGroup()) {
            if(group.getCode().equals("ROLE_ADMINISTRATOR")) {
                throw new AdminRemovalException();
            }
        }

        userRepository.deleteById(user.getUserId());

        return new ResponseEntity<>(Map.of(
                "user", email,
                "status", "Deleted successfully!"), HttpStatus.OK);
    }

    public ResponseEntity<?> changeRole(RolesChanger changer) {
        AccountUser user = userRepository.findUserByEmail(changer.user().toLowerCase())
                .orElseThrow(UserNotExistException::new);

        Optional <Group> groupOptional = Optional.ofNullable(groupRepository.findByCode("ROLE_" + changer.role()));

        if(groupOptional.isEmpty())
            throw new CustomNotFoundException("Role not found!");

        Group group = groupOptional.get();

        switch (changer.operation()) {
            case GRANT -> grantRole(user, group);
            case REMOVE -> removeRole(user, group);
        }

        return new ResponseEntity<>(AccountUser.convertToDTO(user), HttpStatus.OK);
    }

    private AccountUser grantRole(AccountUser user, Group group) {
        user.getUserGroup().forEach(grp -> {
           if (!grp.getName().equals(group.getName()))
                throw new CustomBadRequestException("The user cannot combine administrative and business roles!");
        });

        user.setUserGroup(group);
        userRepository.save(user);

        return user;
    }

    private AccountUser removeRole(AccountUser user, Group group) {
        if (!user.getUserGroup().contains(group)) {
            throw new CustomBadRequestException("The user does not have a role!");
        } else if (isAdmin(user)) { //change if statement was (group.getCode().equals("ROLE_ADMINISTRATOR"))
            throw new CustomBadRequestException("Can't remove ADMINISTRATOR role!");
        } else if (user.getUserGroup().size() == 1) {
            throw new CustomBadRequestException("The user must have at least one role!");
        }

        user.getUserGroup().remove(group);
        userRepository.save(user);

        return user;
    }

    public ResponseEntity<?> changeStatus(StatusChanger changer) {
        AccountUser user = userRepository.findUserByEmail(changer.user()).get();
        String operation = "locked";

        if (isAdmin(user))
           throw new CustomBadRequestException("Can't lock the ADMINISTRATOR!");

        switch (changer.operation()) {
            case LOCK -> user.setAccountNonLocked(false);
            case UNLOCK -> {
                user.setAccountNonLocked(true);
                operation = "unlocked";
            }
        }

        String message = "User " + changer.user() + " " + operation;
        userRepository.save(user);

        return new ResponseEntity<>(Map.of("status", message), HttpStatus.OK);
    }

    private boolean isAdmin(AccountUser user) {
        Set<Group> groups = user.getUserGroup();

        for (Group grp:groups) {
            if (grp.getCode().equals("ROLE_ADMIN")){
                return true;
            }
        }

        return false;
    }
 }
