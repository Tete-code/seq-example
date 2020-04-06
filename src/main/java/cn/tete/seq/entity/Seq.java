package cn.tete.seq.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tete
 * @date 2020年4月6日10点27分
 * 编码
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seq {

	// 业务序列编码
    private String seqCode;

    // 业务序列名称
    private String seqName;

    // 分隔符
    private String delimiter;

    // 重置规则
    private String resetRule;

    // 步长
    private Integer step;

    // 当前编码
    private String currentCode;

    // 当前序号
    private Integer currentNo;

    // 当前重置依赖
    private String currentReset;

}