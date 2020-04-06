package cn.tete.seq.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import cn.tete.seq.entity.Seq;
import cn.tete.seq.entity.SeqRule;

import java.text.SimpleDateFormat;
import java.util.*;

public class SeqUtils {

    private static String seqParam = "123";

    // 初始化编码数据
    private static Seq seq=new Seq("001","testCode01","<","day",10,"",0,"");

    /**
     * seqCode : 业务单据类型Code
     * (code==0 成功, 其他均失败, msg= 新业务编号 || 错误提示)
     */
    public static String generateSequence(String seqCode) {
        //生成编号
        StringBuilder sequence = new StringBuilder();
        // 参数校验
        if (StrUtil.isEmpty(seqCode)) {
            return sequence.toString();
        }
        //根据业务单类型code获取对应的编码信息
        // Seq seq = seqUtil.seqDao.findBySeqCode(seqCode);
        Seq seq = SeqUtils.seq;
        //根据对应的编码信息，查询对应的编号生成规则
        //List<SeqRule> seqRuleList =seqUtil.seqRuleDao.findBySeqCode(seq.getSeqCode());
        List<SeqRule> seqRuleList = new ArrayList<SeqRule>();
        SeqRule seqRule01=new SeqRule("001","0","const","Test","left",5);
        seqRuleList.add(seqRule01);
        SeqRule seqRule02=new SeqRule("001","1","numbering","Test","left",5);
        seqRuleList.add(seqRule02);
        SeqRule seqRule03=new SeqRule("001","2","timestamp","MM-dd","left",5);
        seqRuleList.add(seqRule03);
        SeqRule seqRule04=new SeqRule("001","3","param","param","left",5);
        seqRuleList.add(seqRule04);
        JSON parse = JSONUtil.parse(seqRuleList);
        System.out.println(parse);
        //规则的条数小于1，说明未配置规则
        if (seqRuleList.size() <= 0) {
            return sequence.toString();
        }
        int delimiterNum = 0;
        for (SeqRule rule : seqRuleList) {
            delimiterNum++;
            String ruleCode = rule.getRuleCode();
            // 常量
            if ("const".equals(ruleCode)) {
                sequence.append(rule.getRuleValue());
            }
            // 计数
            else if ("numbering".equals(ruleCode)) {
                // 重置规则
                Integer num = currentReset(seq, rule);
                // 计数拼接
                sequence.append(numberingSeqRule(rule, num));
                // 更新当前序号, 当前序号+步长
                seq.setCurrentNo(num + seq.getStep());
            }
            // 时间
            else if ("timestamp".equals(ruleCode)) {
                sequence.append(DateUtil.format(new Date(), rule.getRuleValue()));
            }
            // 自定义参数
            else if ("param".equals(ruleCode)) {
                if (StrUtil.isNotEmpty(seqParam)) {
                    sequence.append(seqParam);
                }
            }
            // 分隔符
            if (StrUtil.isNotEmpty(seq.getDelimiter()) && delimiterNum != seqRuleList.size()) {
                sequence.append(seq.getDelimiter());
            }
        }
        // 当前编号
        seq.setCurrentCode(sequence.toString());
        // 当前重置依赖
        seq.setCurrentReset(DateUtil.format(new Date(), "yyyy-MM-dd"));
        /*Seq seqValue = new Seq(
                null,null,null,
                seq.getId(),seqCode,
                null, null,
                seq.getCurrentNo(),seq.getCurrentCode(),seq.getCurrentReset()
        );*/
        //单据关联值 存储
        // seqDao.insert(seqValue);
        // if (seqValue.getId() >0){
        //更新规则 当前编码
        // seqUtil.seqDao.update(seq);

        return sequence.toString();
    }

    /**
     * 计数 方式 重置规则
     *
     * @return
     */
    private static Integer currentReset(Seq seq, SeqRule seqRule) {
        Integer no = null;
        Integer ruleNo;
        try {
            ruleNo = Integer.parseInt(seqRule.getRuleValue());
        } catch (Exception e) {
            return 1;
        }
        String resetRule = seq.getResetRule();
        // 日
        if (("day").equals(resetRule)) {
            String resetDate1 = seq.getCurrentReset();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            if (!StrUtil.isBlank(resetDate1) && !resetDate1.equals(formatter.format(new Date()))) {
                return 1;
            }
        }
        // 月
        else if (("month").equals(resetRule)) {
            String resetDate = seq.getCurrentReset().substring(seq.getCurrentReset().length() - 2);
            //每个月 的 28/29/30/31 号进行判断  因.每月最后一天 只有这4种情况
            if (resetDate.equals("28") || resetDate.equals("29") ||
                    resetDate.equals("30") || resetDate.equals("31")) {
                // 判断当前时间是否是每月第一天, 如果是: 重置规则, 使用初始的值
                int i = Calendar.getInstance().get(Calendar.DATE);
                if (i == 1) {
                    no = ruleNo;
                }
            }
        }
        // 年
        else if (("year").equals(resetRule)) {
            if (DateUtil.format(new Date(), "yyyyMMdd").contains("0101") && !seq.getCurrentReset().contains("0101")) {
                no = ruleNo;
            }
        }
        if (no == null) {
            if (seq.getCurrentNo() == null || seq.getCurrentNo() == 0) {
                return ruleNo;
            } else {
                return seq.getCurrentNo();
            }
        }
        return no;
    }

    /**
     * 计数规则
     *
     * @param seqRule
     * @return
     */
    private static String numberingSeqRule(SeqRule seqRule, Integer code) {
        String str = "";
        int codeConst = seqRule.getPaddingWide() - String.valueOf(code).length();    //需要对其位数
        if (codeConst > 0) {
            String st = StrUtil.repeat("0", codeConst);   //补位
            if (seqRule.getPaddingSide().equals("left")) {            //左对齐
                str += code + st;
            } else if (seqRule.getPaddingSide().equals("right")) {    //右对齐
                str += st + code;
            }
        } else {
            str += code;
        }
        return str;
    }
}
