package Growup.spring.user.infrastructure;

import Growup.spring.user.domain.Enum.UserState;
import Growup.spring.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndStatus(String email,UserState status);
    boolean existsByEmailAndStatus(String email, UserState status);
    boolean existsByNickName(String NickName);

}

