package com.isacitra.authentication.modules.authmodule.model;

import com.isacitra.authentication.modules.authmodule.model.dto.UserInformationDTO;
import com.isacitra.authentication.modules.authmodule.model.dto.UserRegisterInfoDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity
public class AuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    @Column(unique = true)
    private String username;
    private String name;
    private String selfDescription = "";
    @Column(unique = true)
    private String email;
    private  String password;
    private String profilePhoto;
    public AuthUser() {

    }

    public static AuthUser fromUserRegisterInfo(UserRegisterInfoDTO info){
        return new AuthUserBuilder()
                .email(info.getEmail())
                .password(info.getPassword())
                .profilePhoto(info.getPhotoProfile())
                .name(info.getName())
                .username(info.getUsername())
                .build();
    }

    public UserInformationDTO toUserInformationDTO() {
        return UserInformationDTO.builder()
                .userId(this.userId)
                .username(this.username)
                .selfDescription(this.selfDescription)
                .email(this.email)
                .name(this.name)
                .profilePhoto(this.profilePhoto)
                .build();
    }
}
