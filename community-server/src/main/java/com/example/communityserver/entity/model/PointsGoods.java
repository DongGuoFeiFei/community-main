package com.example.communityserver.entity.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * <p>
 * 积分商城商品实体类
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-19
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("points_goods")
@ApiModel(value = "PointsGoods对象", description = "积分商城商品表")
public class PointsGoods implements Serializable {

    private static final long serialVersionUID = -5393285590605490489L;

    @TableId(value = "goods_id", type = IdType.AUTO)
    @ApiModelProperty(value = "商品ID", hidden = true)
    private Long goodsId;

    @NotBlank(message = "商品名称不能为空")
    @Length(max = 100, message = "商品名称长度不能超过100个字符")
    @ApiModelProperty(value = "商品名称", required = true)
    private String goodsName;

    @NotBlank(message = "商品类型不能为空")
    @Pattern(regexp = "^(1|2)$", message = "商品类型只能是1(虚拟)或2(实物)")
    @ApiModelProperty(value = "商品类型(1:虚拟 2:实物)", required = true)
    private String goodsType;

    @NotNull(message = "所需积分不能为空")
    @Min(value = 0, message = "所需积分不能小于0")
    @ApiModelProperty(value = "所需积分", required = true)
    private Integer pointsPrice;

    @DecimalMin(value = "0.00", message = "现金价格不能小于0")
    @ApiModelProperty(value = "现金价格(可选)")
    private BigDecimal cashPrice;

    @NotNull(message = "库存数量不能为空")
    @Min(value = 0, message = "库存数量不能小于0")
    @ApiModelProperty(value = "库存数量", required = true)
    private Integer stock;

    @Length(max = 255, message = "图片URL长度不能超过255个字符")
    @ApiModelProperty(value = "商品图片URL")
    private String imageUrl;

    @ApiModelProperty(value = "商品描述")
    private String description;

    @NotBlank(message = "状态不能为空")
    @Pattern(regexp = "^(0|1)$", message = "状态只能是0(下架)或1(上架)")
    @ApiModelProperty(value = "状态(0:下架 1:上架)", required = true)
    private String status = "1";

    @NotNull(message = "排序权重不能为空")
    @ApiModelProperty(value = "排序权重", required = true)
    private Integer sortOrder = 0;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间", hidden = true)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "更新时间", hidden = true)
    private Date updateTime;
}
