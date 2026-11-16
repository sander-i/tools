# Security Policy

## Supported Versions

| Version | Supported |
| ------- | --------- |
| 2.0.x   | :white_check_mark: |
| 1.0.x   | :x: (no longer maintained) |

Only the latest `2.0.x` release line receives security fixes. The `1.0.x` line has reached
end of life and will not receive patches.

## Reporting a Vulnerability

**Please do not open a public Issue for security problems.** A public report exposes every user of
the library before a fix is available.

Report vulnerabilities privately using one of these channels:

1. **GitHub Security Advisories (preferred)** —
   [open a private advisory](https://github.com/sander-i/tools/security/advisories/new)
2. **Email** — 324466109+sander-i@users.noreply.github.com

Please include as much of the following as you can:

- Affected version(s) of `tools`
- A description of the vulnerability and the affected module/class
- Step-by-step reproduction instructions (a minimal proof of concept is ideal)
- The potential impact (what an attacker could do, and under what conditions)
- Any suggested fix or mitigation

## Response Commitment

- We aim to **acknowledge every report within 48 hours**.
- We will keep you informed about the assessment and the planned fix.
- Once a fix is released, we will credit you in the release notes unless you prefer to stay
  anonymous.

Please give us a reasonable amount of time to release a fix before any public disclosure.

## Note on Cryptographic Code

This project provides cryptographic utilities (Base64, MD5, SHA, CRC32, JWT, AES, DES, IDEA, RSA,
DSA) and a Spring Boot starter for them. Because of that, a few points deserve extra attention:

- **Never use the keys found in example configurations in production.** The keys in the
  `application.yml` files (for example `secret-key: "BeYourself"` and the bundled RSA key pair)
  exist purely to demonstrate the configuration format and are public knowledge.
- Always generate and manage your own keys through a secure mechanism such as a secrets manager or
  an environment variable, and rotate them regularly.
- Example code is illustrative, not a security recommendation. Review and adapt it for your own
  threat model.
- MD5, SHA-1 and DES are provided for compatibility with legacy systems. Do not choose them for new
  designs.

If you find a weakness in how a cryptographic primitive is used in this project — for example a
hard-coded key, a weak default mode of operation, or a misuse of an API — please report it through
the private channels above.
