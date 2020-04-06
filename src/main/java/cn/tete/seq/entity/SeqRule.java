package cn.tete.seq.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tete
 * @date 2020年4月6日10点38分
 * 编码规则
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeqRule {

	// 业务编码
	private String seqCode;

	// 规则排序
	private String ruleOrder;

	// 规则代码
	private String ruleCode;

	// 规则值
	private String ruleValue;

	// 补齐方向
	private String paddingSide;

	// 补齐宽度
	private Integer paddingWide;

}