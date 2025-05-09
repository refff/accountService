package account.infrastructure;

import account.domain.Entities.Group;
import account.persistance.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DataLoader {
    private GroupRepository groupRepository;

    @Autowired
    public DataLoader(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
        roleCreation();
    }

    public void roleCreation() {
        try {
            groupRepository.save(new Group("ROLE_ADMINISTRATOR", "Administrative Group"));
            groupRepository.save(new Group("ROLE_USER", "Business users"));
            groupRepository.save(new Group("ROLE_ACCOUNTANT", "Business users"));
            groupRepository.save(new Group("ROLE_AUDITOR", "Business users"));
        } catch (Exception e) {
        }
    }
}
