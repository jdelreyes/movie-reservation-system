package ca.jdelreyes.moviereservationsystem.service;

import ca.jdelreyes.moviereservationsystem.dto.user.UpdateOwnPasswordRequest;
import ca.jdelreyes.moviereservationsystem.dto.user.UpdateOwnProfileRequest;
import ca.jdelreyes.moviereservationsystem.dto.user.UpdateUserRequest;
import ca.jdelreyes.moviereservationsystem.dto.user.UserResponse;
import ca.jdelreyes.moviereservationsystem.exception.BadRequestException;
import ca.jdelreyes.moviereservationsystem.exception.NotFoundException;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface UserService {
    List<UserResponse> getUsers(PageRequest pageRequest);

    UserResponse getUser(Long id) throws NotFoundException;

    UserResponse updateUser(Long id, UpdateUserRequest updateUserRequest) throws NotFoundException;

    void deleteUser(Long id) throws NotFoundException;

    UserResponse updateOwnProfile(String username, UpdateOwnProfileRequest updateProfileRequest) throws NotFoundException;

    void updateOwnPassword(String username, UpdateOwnPasswordRequest updateOwnPasswordRequest) throws NotFoundException, BadRequestException;

    void deleteOwnAccount(String username) throws NotFoundException;

}
