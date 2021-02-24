package com.cn.gree.tiny.modules.admin.dto;

import com.cn.gree.tiny.modules.admin.model.GreeMenu;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 后台菜单节点封装
 *
 */
@Getter
@Setter
public class GreeMenuNode extends GreeMenu {
    @ApiModelProperty(value = "子级菜单")
    private List<GreeMenuNode> children;
}
