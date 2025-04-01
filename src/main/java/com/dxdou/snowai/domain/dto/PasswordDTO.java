package com.dxdou.snowai.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 密码DTO类
 *
 * @author foreverdxdou
 */
@Data
@Schema(description = "密码DTO")
public class PasswordDTO {

    /**
     * 旧密码
     */
    @Schema(description = "旧密码")
    private String oldPassword;

    /**
     * 新密码
     */
    @Schema(description = "新密码")
    private String newPassword;
}