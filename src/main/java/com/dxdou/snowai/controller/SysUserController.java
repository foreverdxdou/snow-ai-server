package com.dxdou.snowai.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxdou.snowai.common.R;
import com.dxdou.snowai.domain.dto.PasswordDTO;
import com.dxdou.snowai.domain.dto.SysUserDTO;
import com.dxdou.snowai.domain.entity.SysUser;
import com.dxdou.snowai.domain.vo.SysUserVO;
import com.dxdou.snowai.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 系统用户控制器
 *
 * @author foreverdxdou
 */
@Tag(name = "用户管理")
@RestController
@RequestMapping("/api/v1/system/user")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService userService;

    @Operation(summary = "分页查询用户列表")
    @GetMapping("/page")
    public R<Page<SysUserVO>> page(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "用户名") @RequestParam(required = false) String username,
            @Parameter(description = "邮箱") @RequestParam(required = false) String email,
            @Parameter(description = "手机号") @RequestParam(required = false) String phone,
            @Parameter(description = "昵称") @RequestParam(required = false) String nickname,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "部门ID") @RequestParam(required = false) Long deptId) {
        Page<SysUser> page = new Page<>(current, size);
        return R.ok(userService.getUserPage(page, username, status, deptId, email, phone, nickname));
    }

    @Operation(summary = "获取用户详情")
    @GetMapping("/{id}")
    public R<SysUserVO> getById(@Parameter(description = "用户ID") @PathVariable Long id) {
        SysUser user = userService.getById(id);
        if (user == null) {
            return R.error("用户不存在");
        }
        SysUserVO vo = new SysUserVO();
        BeanUtils.copyProperties(user, vo);
        return R.ok(vo);
    }

    @Operation(summary = "创建用户")
    @PostMapping
    public R<Void> create(@Validated @RequestBody SysUserDTO userDTO) {
        SysUser user = new SysUser();
        BeanUtils.copyProperties(userDTO, user);
        userService.createUser(user, userDTO.getRoleIds());
        return R.ok(null);
    }

    @Operation(summary = "更新用户")
    @PutMapping("/{id}")
    public R<Void> update(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Validated @RequestBody SysUserDTO userDTO) {
        SysUser user = new SysUser();
        BeanUtils.copyProperties(userDTO, user);
        user.setId(id);
        user.setPassword(null);
        userService.updateUser(user, userDTO.getRoleIds());
        return R.ok(null);
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    public R<Void> delete(@Parameter(description = "用户ID") @PathVariable Long id) {
        userService.deleteUser(id);
        return R.ok(null);
    }

    @Operation(summary = "重置密码")
    @PutMapping("/{id}/password/reset")
    public R<Void> resetPassword(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "新密码") @RequestParam String password) {
        userService.resetPassword(id, password);
        return R.ok(null);
    }

    @Operation(summary = "修改密码")
    @PutMapping("/{id}/password")
    public R<Void> updatePassword(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Validated @RequestBody PasswordDTO passwordDTO) {
        userService.updatePassword(id, passwordDTO.getOldPassword(), passwordDTO.getNewPassword());
        return R.ok(null);
    }

    @Operation(summary = "更新用户状态")
    @PutMapping("/{id}/status")
    public R<Void> updateStatus(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "状态") @RequestParam Integer status) {
        userService.updateUserStatus(id, status);
        return R.ok(null);
    }

    @Operation(summary = "更新用户头像")
    @PutMapping("/{id}/avatar")
    public R<Void> updateAvatar(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "头像URL") @RequestParam String avatar) {
        userService.updateUserAvatar(id, avatar);
        return R.ok(null);
    }

    @Operation(summary = "更新用户基本信息")
    @PutMapping("/{id}/info")
    public R<Void> updateInfo(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @RequestBody SysUserDTO dto) {
        userService.updateUserInfo(id, dto.getNickname(), dto.getEmail(), dto.getPhone());
        return R.ok(null);
    }
}