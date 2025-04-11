package account.service;

import account.domain.AccountUser;
import account.domain.AccountUserDTO;
import account.domain.Group;
import account.domain.RolesChanger;
import account.infrastructure.CustomExceptions.*;
import account.persistance.GroupRepository;
import account.persistance.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        userRepository.findAll().forEach(user -> usersList.add(AccountUser.convertToDTO(user)));

        return new ResponseEntity<>(usersList, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteUser(String email) {
        AccountUser user = userRepository.findUserByEmail(email).orElseThrow(UserNotExistException::new);

        for(Group group: user.getUserGroup()) {
            if(group.getCode().equals("ROLE_ADMIN")) {
                throw new AdminRemovalException();
            }
        }

        userRepository.deleteById(user.getUserId());

        return new ResponseEntity<>(Map.of(
                "user", email,
                "status", "Deleted successfully!"), HttpStatus.OK);
    }

    public ResponseEntity<?> changeRoles(RolesChanger changer) {
        AccountUser user = userRepository.findUserByEmail(changer.user()).orElseThrow(UserNotExistException::new);
        Group group = Optional.of(groupRepository.findByCode("ACCOUNTANT")).orElseThrow(
                () -> new CustomNotFoundException("Role not found!"));

        switch (changer.operation()) {
            case GRANT -> {
                grantRole(user, group);
            }
            case REMOVE -> {
                removeRole(user, group);
            }
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
        if (user.getUserGroup().contains(group)) {
            throw new CustomBadRequestException("The user does not have a role!");
        } else if (user.getUserGroup().size() == 1) {
            throw new CustomBadRequestException("The user must have at least one role!");
        } else if (group.getCode().equals("ROLE_ADMIN")) {
            throw new CustomBadRequestException("Can't remove ADMINISTRATOR role!");
        }

        user.getUserGroup().remove(group);
        userRepository.save(user);

        return user;
    }
 }
