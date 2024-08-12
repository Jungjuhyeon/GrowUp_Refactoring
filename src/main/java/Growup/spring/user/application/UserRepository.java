package Growup.spring.user.application;

import Growup.spring.user.domain.Enum.UserState;
import Growup.spring.user.domain.User;

public interface UserRepository {

    public Boolean existsByNickName(String nickName);
    public Boolean existsByEmailAndStatus(String email, UserState state);

    public User save(User user);

    public User findByEmailAndStatus(String email);

    public User findById(Long id);

}
