package Growup.spring.user.application;

import Growup.spring.user.domain.User;

public interface UserRepository {

    public Boolean existsByNickName(String nickName);

    public void existsByEmailAndStatus(String email);

    public User save(User user);

    public User findByEmailAndStatus(String email);

    public User findById(Long id);

}
