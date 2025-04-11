package com.dxdou.snowai.controller;

import com.dxdou.snowai.common.R;
import com.dxdou.snowai.domain.dto.SysPermissionDTO;
import com.dxdou.snowai.domain.entity.SysPermission;
import com.dxdou.snowai.domain.vo.SysPermissionTreeVO;
import com.dxdou.snowai.domain.vo.SysPermissionVO;
import com.dxdou.snowai.service.SysPermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统权限控制器
 *
 * @author foreverdxdou
 */
@Tag(name = "权限管理")
@RestController
@RequestMapping("/api/v1/system/permission")
@RequiredArgsConstructor
public class SysPermissionController {

    private final SysPermissionService permissionService;

    @Operation(summary = "查询权限树")
    @GetMapping("/tree")
    public R<List<SysPermissionVO>> getPermissionTree(
            @Parameter(description = "父权限ID") @RequestParam(required = false) Long parentId,
            @Parameter(description = "权限类型") @RequestParam(required = false) Integer type,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        return R.ok(permissionService.getPermissionTree(parentId, type, status));
    }

    @Operation(summary = "查询权限树（前端树形控件专用）")
    @GetMapping("/tree/select")
    public R<List<SysPermissionTreeVO>> getPermissionTreeForSelect(
            @Parameter(description = "父权限ID") @RequestParam(required = false) Long parentId,
            @Parameter(description = "权限类型") @RequestParam(required = false) Integer type,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        List<SysPermissionVO> permissionTree = permissionService.getPermissionTree(parentId, type, status);
        List<SysPermissionTreeVO> treeVO = convertToTreeVO(permissionTree);
        return R.ok(treeVO);
    }

    @Operation(summary = "获取权限详情")
    @GetMapping("/{id}")
    public R<SysPermissionVO> getById(@Parameter(description = "权限ID") @PathVariable Long id) {
        SysPermission permission = permissionService.getById(id);
        if (permission == null) {
            return R.error("权限不存在");
        }
        SysPermissionVO vo = new SysPermissionVO();
        BeanUtils.copyProperties(permission, vo);
        return R.ok(vo);
    }

    @Operation(summary = "创建权限")
    @PostMapping
    public R<Void> create(@Validated @RequestBody SysPermissionDTO permissionDTO) {
        SysPermission permission = new SysPermission();
        BeanUtils.copyProperties(permissionDTO, permission);
        permissionService.save(permission);
        return R.ok(null);
    }

    @Operation(summary = "更新权限")
    @PutMapping("/{id}")
    public R<Void> update(
            @Parameter(description = "权限ID") @PathVariable Long id,
            @Validated @RequestBody SysPermissionDTO permissionDTO) {
        SysPermission permission = new SysPermission();
        BeanUtils.copyProperties(permissionDTO, permission);
        permission.setId(id);
        permissionService.updateById(permission);
        return R.ok(null);
    }

    @Operation(summary = "删除权限")
    @DeleteMapping("/{id}")
    public R<Void> delete(@Parameter(description = "权限ID") @PathVariable Long id) {
        permissionService.removeById(id);
        return R.ok(null);
    }

    @Operation(summary = "批量删除权限")
    @DeleteMapping("/batch")
    public R<Void> deleteBatch(@Parameter(description = "权限ID") @RequestBody List<Long> ids) {
        permissionService.removeBatchByIds(ids);
        return R.ok(null);
    }

    @Operation(summary = "根据用户ID查询权限列表")
    @GetMapping("/user/{userId}")
    public R<List<SysPermission>> getUserPermissions(@Parameter(description = "用户ID") @PathVariable Long userId) {
        return R.ok(permissionService.getUserPermissions(userId));
    }

    @Operation(summary = "根据角色ID列表查询权限列表")
    @GetMapping("/role")
    public R<List<SysPermission>> getPermissionsByRoleIds(
            @Parameter(description = "角色ID列表") @RequestParam List<Long> roleIds) {
        return R.ok(permissionService.getPermissionsByRoleIds(roleIds));
    }

    /**
     * 将权限VO转换为树形VO
     *
     * @param permissionVOs 权限VO列表
     * @return 树形VO列表
     */
    private List<SysPermissionTreeVO> convertToTreeVO(List<SysPermissionVO> permissionVOs) {
        // 把permissionVOs转换成树状结构SysPermissionTreeVO
        // 1. 数据准备
        Map<String, SysPermissionTreeVO> nodeMap = new HashMap<>();
        List<SysPermissionTreeVO> treeNodes = new ArrayList<>();

        // 2. 转换基础数据
        for (SysPermissionVO vo : permissionVOs) {
            SysPermissionTreeVO node = convertToTreeVO(vo);
            treeNodes.add(node);
            nodeMap.put(node.getItemId(), node); // 使用itemId作为键
        }

        // 3. 构建树结构
        List<SysPermissionTreeVO> rootNodes = new ArrayList<>();
        for (SysPermissionTreeVO node : treeNodes) {
            if ("0".equals(node.getParentId()) || !nodeMap.containsKey(node.getParentId())) {
                rootNodes.add(node);
            } else {
                SysPermissionTreeVO parent = nodeMap.get(node.getParentId());
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<>());
                }
                parent.getChildren().add(node);
            }
        }
        return rootNodes;
    }

    /**
     * 将单个权限VO转换为树形VO
     *
     * @param permissionVO 权限VO
     * @return 树形VO
     */
    private SysPermissionTreeVO convertToTreeVO(SysPermissionVO permissionVO) {
        SysPermissionTreeVO treeVO = new SysPermissionTreeVO();
        treeVO.setId(String.valueOf(permissionVO.getId()));
        treeVO.setItemId(String.valueOf(permissionVO.getId()));
        treeVO.setParentId(permissionVO.getParentId() != null ? String.valueOf(permissionVO.getParentId()) : null);
        treeVO.setName(permissionVO.getName());
        return treeVO;
    }
}