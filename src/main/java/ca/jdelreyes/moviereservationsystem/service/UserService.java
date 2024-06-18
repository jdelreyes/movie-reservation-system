package ca.jdelreyes.moviereservationsystem.service;

import ca.jdelreyes.moviereservationsystem.dto.user.UpdateOwnPasswordRequest;
import ca.jdelreyes.moviereservationsystem.dto.user.UpdateOwnProfileRequest;
import ca.jdelreyes.moviereservationsystem.dto.user.UpdateUserRequest;
import ca.jdelreyes.moviereservationsystem.dto.user.UserResponse;
import ca.jdelreyes.moviereservationsystem.exception.BadRequestException;
import ca.jdelreyes.moviereservationsystem.exception.NotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.awt.*;
import java.util.List;

public interface UserService {
    public List<UserResponse> getUsers(PageRequest pageRequest);

    public UserResponse getUser(Long id) throws NotFoundException;

    public UserResponse updateUser(Long id, UpdateUserRequest updateUserRequest) throws NotFoundException;

    public void deleteUser(Long id) throws NotFoundException;

    public UserResponse updateOwnProfile(String username, UpdateOwnProfileRequest updateProfileRequest) throws NotFoundException;

    public void updateOwnPassword(String username, UpdateOwnPasswordRequest updateOwnPasswordRequest) throws NotFoundException, BadRequestException;

    public void deleteOwnAccount(String username) throws NotFoundException;

}
