package cn.tete.seq.test;

import cn.tete.seq.utils.SeqUtils;
import org.junit.Test;

/**
 * @author Tete
 * @version 1.0.0
 * @date 2020/04/06上午10:30
 * @email zynifff@gmail.com
 */
public class TestSeq {

    @Test
    public void demo1(){
        String code = SeqUtils.generateSequence("TEST");
        System.out.println(code);
    }

}
