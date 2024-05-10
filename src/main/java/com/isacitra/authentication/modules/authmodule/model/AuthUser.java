package com.isacitra.authentication.modules.authmodule.model;

import com.isacitra.authentication.modules.authmodule.model.dto.UserInformationDTO;
import com.isacitra.authentication.modules.authmodule.model.dto.UserRegisterInfoDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class AuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    @Column(unique = true)
    private String username;
    private String name;
    private String selfDescription;
    @Column(unique = true)
    private String email;
    private  String password;
    private String profilePhoto;

    public AuthUser() {

    }

    public static class AuthUserBuilder {
        private Long userId;
        private String username;
        private String name;
        private String selfDescription;
        private String email;
        private String password;
        private String profilePhoto;

        public AuthUserBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public AuthUserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public AuthUserBuilder name(String name) {
            this.name = name;
            return this;
        }

        public AuthUserBuilder selfDescription(String selfDescription) {
            this.selfDescription = selfDescription;
            return this;
        }

        public AuthUserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public AuthUserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public AuthUserBuilder profilePhoto(String profilePhoto) {
            this.profilePhoto = profilePhoto;
            return this;
        }

        public AuthUser build() {
            AuthUser authUser = new AuthUser();
            authUser.setUserId(this.userId);
            authUser.setUsername(this.username);
            authUser.setName(this.name);
            authUser.setSelfDescription(this.selfDescription);
            authUser.setEmail(this.email);
            authUser.setPassword(this.password);
            authUser.setProfilePhoto(this.profilePhoto);
            return authUser;
        }
    }


    @PrePersist
    void preInsert() {
        if (this.selfDescription == null)
            this.selfDescription = "";
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
