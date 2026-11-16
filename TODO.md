# 待办事项

本项目后续计划的工作内容。

## bedrock

- [ ] 唯一键工具包
- [ ] 收录其它工具项目内容
- [ ] 优化 JSON 工具内容

## encrypt

- [ ] 非对称加密工具包中的 RSA 加密工具完善

### 空缺算法（目录已建，尚无实现）

对称加密：

- [ ] DES3 / 3DES
- [ ] Blowfish
- [ ] RC2
- [ ] RC4（流加密）
- [ ] RC5
- [ ] Skipjack
- [ ] TDEA

非对称加密：

- [ ] DH（Diffie-Hellman）
- [ ] ECC（椭圆曲线）
- [ ] ElGamal
- [ ] Knapsack（背包算法）
- [ ] Rabin

数字签名：

- [ ] ECDSA

国密算法（需引入 Bouncy Castle）：

- [ ] SM2（非对称）
- [ ] SM3（哈希）
- [ ] SM4（对称）

建议优先级：Java 原生支持且常用的先做（SHA-384/512、3DES、ECC、ECDSA、RC4），
其次 Blowfish、DH、RC2、RC5，最后历史算法与国密。

## tools-spring-boot-starter

- [ ] 实现动态同步 tools 项目版本，避免各模块版本不一致

## encrypt-spring-boot-starter

- [ ] JWT 工具实现 yml 文件自动注入配置
- [ ] RSA 加密工具的 yml 文件自动注入配置
