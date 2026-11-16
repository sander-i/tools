# 待办事项

本项目后续计划的工作内容。

## bedrock

- [ ] 唯一键工具包
- [ ] 收录其它工具项目内容
- [ ] 优化 JSON 工具内容

## encrypt

- [ ] 非对称加密工具包中的 RSA 加密工具完善

### 算法实现状态

对称加密：

- [x] DES3 / 3DES（`symmetrical.des.des3.Des3Helper`）
- [x] Blowfish（`symmetrical.blowfish.BlowfishHelper`）
- [x] RC2（`symmetrical.rc.rc2.Rc2Helper`）
- [x] RC4 流加密（`symmetrical.rc.rc4.Rc4Helper`）
- [ ] RC5（需 Bouncy Castle）
- [ ] Skipjack（已废弃，不计划实现）
- [ ] TDEA（与 3DES 重复，不计划实现）

非对称加密：

- [x] DH（`asymmetric.dh.DhHelper`，密钥交换）
- [x] ECC / ECDH（`asymmetric.ecc.EccKeyGenerator`、`EcdhHelper`，密钥交换）
- [ ] ElGamal（需 Bouncy Castle）
- [ ] Knapsack（背包算法，历史算法）
- [ ] Rabin（历史算法）

数字签名：

- [x] ECDSA（`digitalsignature.ecdsa.EcdsaHelper`，支持 SHA256/384/512withECDSA）

哈希：

- [x] SHA-384 / SHA-512（`basic.sha.ShaUtils.encodeSha384/encodeSha512`）

国密算法（需引入 Bouncy Castle）：

- [ ] SM2（非对称）
- [ ] SM3（哈希）
- [ ] SM4（对称）

### 已实现算法说明

DH 与 ECC 属于密钥协商算法，不直接加密数据。用法是双方协商出共享密钥，
再交给对称加密（AES 等）处理实际数据。两者都提供了
`generateSecretKey(privateKey, publicKey, "AES", 16)` 便捷方法。

RC4 是流加密，无分组模式与向量，同一密钥同一明文产出相同密文。

## tools-spring-boot-starter

- [ ] 实现动态同步 tools 项目版本，避免各模块版本不一致

## encrypt-spring-boot-starter

- [ ] JWT 工具实现 yml 文件自动注入配置
- [ ] RSA 加密工具的 yml 文件自动注入配置
