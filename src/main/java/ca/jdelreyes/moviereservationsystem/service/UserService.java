package ca.jdelreyes.moviereservationsystem.service;

import ca.jdelreyes.moviereservationsystem.dto.user.UpdateUserRequest;
import ca.jdelreyes.moviereservationsystem.dto.user.UserResponse;
import ca.jdelreyes.moviereservationsystem.exception.NotFoundException;

import java.util.List;

public interface UserService {
    public List<UserResponse> getUsers();

    public UserResponse getUser(Long id) throws NotFoundException;

    public UserResponse updateUser(Long id, UpdateUserRequest updateUserRequest) throws NotFoundException;

    public void deleteUser(Long id) throws NotFoundException;
}
