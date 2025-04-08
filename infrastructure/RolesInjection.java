package account.infrastructure;

import account.domain.Group;
import account.persistance.GroupRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class RolesInjection {
    private GroupRepository groupRepository;

    @Autowired
    public RolesInjection(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @PostConstruct
    public void roleCreation() {
        Group groupAdmin = new Group("ROLE_ADMIN", "Admin Group");
        Group groupUser = new Group("ROLE_USER", "User Group");

        groupRepository.saveAndFlush(groupAdmin);
        groupRepository.saveAndFlush(groupUser);
    }
}
