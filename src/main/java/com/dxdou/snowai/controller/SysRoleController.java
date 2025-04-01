package com.dxdou.snowai.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxdou.snowai.common.R;
import com.dxdou.snowai.domain.dto.SysRoleDTO;
import com.dxdou.snowai.domain.entity.SysRole;
import com.dxdou.snowai.domain.vo.SysRoleVO;
import com.dxdou.snowai.service.SysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统角色控制器
 *
 * @author foreverdxdou
 */
@Tag(name = "角色管理")
@RestController
@RequestMapping("/api/v1/system/role")
@RequiredArgsConstructor
public class SysRoleController {

    private final SysRoleService roleService;

    @Operation(summary = "分页查询角色列表")
    @GetMapping("/page")
    public R<Page<SysRoleVO>> page(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "角色名称") @RequestParam(required = false) String roleName,
            @Parameter(description = "角色编码") @RequestParam(required = false) String roleCode) {
        Page<SysRole> page = new Page<>(current, size);
        return R.ok(roleService.getRolePage(page, roleName, roleCode));
    }

    @Operation(summary = "获取角色详情")
    @GetMapping("/{id}")
    public R<SysRoleVO> getById(@Parameter(description = "角色ID") @PathVariable Long id) {
        SysRole role = roleService.getById(id);
        if (role == null) {
            return R.error("角色不存在");
        }
        SysRoleVO vo = new SysRoleVO();
        BeanUtils.copyProperties(role, vo);
        return R.ok(vo);
    }

    @Operation(summary = "创建角色")
    @PostMapping
    public R<Void> create(@Validated @RequestBody SysRoleDTO roleDTO) {
        SysRole role = new SysRole();
        BeanUtils.copyProperties(roleDTO, role);
        roleService.createRole(role, roleDTO.getPermissionIds());
        return R.ok(null);
    }

    @Operation(summary = "更新角色")
    @PutMapping("/{id}")
    public R<Void> update(
            @Parameter(description = "角色ID") @PathVariable Long id,
            @Validated @RequestBody SysRoleDTO roleDTO) {
        SysRole role = new SysRole();
        BeanUtils.copyProperties(roleDTO, role);
        role.setId(id);
        roleService.updateRole(role, roleDTO.getPermissionIds());
        return R.ok(null);
    }

    @Operation(summary = "删除角色")
    @DeleteMapping("/{id}")
    public R<Void> delete(@Parameter(description = "角色ID") @PathVariable Long id) {
        roleService.deleteRole(id);
        return R.ok(null);
    }

    @Operation(summary = "获取角色权限列表")
    @GetMapping("/{id}/permissions")
    public R<List<String>> getRolePermissions(@Parameter(description = "角色ID") @PathVariable Long id) {
        return R.ok(roleService.getRolePermissions(id).stream().map(sysPermission -> String.valueOf(sysPermission.getId())).collect(Collectors.toList()));
    }

    @Operation(summary = "根据角色编码查询角色")
    @GetMapping("/code/{roleCode}")
    public R<SysRole> getByRoleCode(@Parameter(description = "角色编码") @PathVariable String roleCode) {
        return R.ok(roleService.getByRoleCode(roleCode));
    }

    @Operation(summary = "根据用户ID查询角色列表")
    @GetMapping("/user/{userId}")
    public R<List<SysRole>> getRolesByUserId(@Parameter(description = "用户ID") @PathVariable Long userId) {
        return R.ok(roleService.getRolesByUserId(userId));
    }

    @Operation(summary = "更新角色状态")
    @PutMapping("/{id}/status")
    public R<Void> updateStatus(
            @Parameter(description = "角色ID") @PathVariable Long id,
            @Parameter(description = "状态") @RequestParam Integer status) {
        roleService.updateRoleStatus(id, status);
        return R.ok(null);
    }

    @Operation(summary = "分配角色权限")
    @PutMapping("/{id}/permissions")
    public R<Void> assignRolePermissions(
            @Parameter(description = "角色ID") @PathVariable Long id,
            @Parameter(description = "权限ID列表") @RequestParam Long[] permissionIds) {
        roleService.assignRolePermissions(id, permissionIds);
        return R.ok(null);
    }
}