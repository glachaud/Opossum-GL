package isepreact.service;

import isepreact.model.User;

/**
 * Created by guillaumelachaud on 5/12/17.
 */
public interface UserService {
  public User findUserByEmail(String email);
  public void saveUser(User user);
}
