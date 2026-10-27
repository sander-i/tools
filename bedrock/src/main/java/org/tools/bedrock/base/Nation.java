package org.tools.bedrock.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 中国民族枚举。
 *
 * <p>编码遵循 GB/T 3304《中国各民族名称的罗马字母拼写法和代码》，
 * 与居民身份证民族字段同源：01 汉族、02 蒙古族 … 56 基诺族。
 * 历史版本将汉族排至 56、其余整体前移一位，且 SALTAR/GINO 为拼写讹误
 * （东乡族 DONGXIANG、基诺族 JINUO），本次一并修正。</p>
 *
 * @author sander
 * @version 1.0
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum Nation {
    /**
     * 汉族
     */
    HAN(1),

    /**
     * 蒙古族
     */
    MONGOLIA(2),

    /**
     * 回族
     */
    THE_HUI(3),

    /**
     * 藏族
     */
    ZANG(4),

    /**
     * 维吾尔族
     */
    UYGHUR(5),

    /**
     * 苗族
     */
    MIAO(6),

    /**
     * 彝族
     */
    YI(7),

    /**
     * 壮族
     */
    ZHUANG(8),

    /**
     * 布依族
     */
    BUYEI(9),

    /**
     * 朝鲜族
     */
    KOREAN(10),

    /**
     * 满族
     */
    MANCHU(11),

    /**
     * 侗族
     */
    DONG(12),

    /**
     * 瑶族
     */
    YAO(13),

    /**
     * 白族
     */
    BAI(14),

    /**
     * 土家族
     */
    TUJIA(15),

    /**
     * 哈尼族
     */
    HANI(16),

    /**
     * 哈萨克族
     */
    KAZAK(17),

    /**
     * 傣族
     */
    DAI(18),

    /**
     * 黎族
     */
    LI(19),

    /**
     * 傈僳族
     */
    LISU(20),

    /**
     * 佤族
     */
    VA(21),

    /**
     * 畲族
     */
    SHE(22),

    /**
     * 高山族
     */
    GAOSHAN(23),

    /**
     * 拉祜族
     */
    LAHU(24),

    /**
     * 水族
     */
    SUI(25),

    /**
     * 东乡族
     */
    DONGXIANG(26),

    /**
     * 纳西族
     */
    NAXI(27),

    /**
     * 景颇族
     */
    JINGPO(28),

    /**
     * 柯尔克孜族
     */
    KYRGYZ(29),

    /**
     * 土族
     */
    DU(30),

    /**
     * 达斡尔族
     */
    DAUR(31),

    /**
     * 仫佬族
     */
    MULAO(32),

    /**
     * 羌族
     */
    QIANG(33),

    /**
     * 布朗族
     */
    BLANG(34),

    /**
     * 撒拉族
     */
    SALAR(35),

    /**
     * 毛南族
     */
    MAONAN(36),

    /**
     * 仡佬族
     */
    GELO(37),

    /**
     * 锡伯族
     */
    XIBE(38),

    /**
     * 阿昌族
     */
    ACHANG(39),

    /**
     * 普米族
     */
    PUMI(40),

    /**
     * 塔吉克族
     */
    TAJIK(41),

    /**
     * 怒族
     */
    NU(42),

    /**
     * 乌孜别克族
     */
    UZBEK(43),

    /**
     * 俄罗斯族
     */
    RUSSIAN(44),

    /**
     * 鄂温克族
     */
    EVENKI(45),

    /**
     * 德昂族
     */
    DEANG(46),

    /**
     * 保安族
     */
    BONAN(47),

    /**
     * 裕固族
     */
    YUGUR(48),

    /**
     * 京族
     */
    JING(49),

    /**
     * 塔塔尔族
     */
    TATAR(50),

    /**
     * 独龙族
     */
    DULONG(51),

    /**
     * 鄂伦春族
     */
    OROQEN(52),

    /**
     * 赫哲族
     */
    NANAI(53),

    /**
     * 门巴族
     */
    MONBA(54),

    /**
     * 珞巴族
     */
    LHOBA(55),

    /**
     * 基诺族
     */
    JINUO(56);

    /**
     * 民族代码（GB/T 3304）
     */
    private final int nation;
}
