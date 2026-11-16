# Changelog

This document records the important changes to the project.

## [2.0.0-SNAPSHOT] - In progress

### Added
- Full optimization of image utility module
- New `ThumbnailUtil` for thumbnail generation
- New constant classes `ImageConstants` and `FileUtilConstants`
- Open source compliance documents (CONTRIBUTING, AUTHORS, NOTICE, CHANGELOG, SECURITY, CODE_OF_CONDUCT)
- GitHub issue and pull request templates

### Changed
- **BREAKING**: Java package root migrated from `org.tools` to `io.github.sander_i`
- **BREAKING**: Maven groupId migrated from `io.gitee.sander-o` to `io.github.sander-i`
- Consolidated image utilities into the `bedrock` module
- Streamlined `ImageUtils` in `fortify` (-472 lines)
- Enhanced `ImageUtil` in `bedrock` (+441 lines)
- Optimized image feature, hash, histogram, pixel and structure utilities
- Improved test coverage (ThumbnailUtilTest +244, ImageUtilTest +165)
- Primary repository moved to GitHub, Gitee kept as mirror

### Fixed
- `SymmetricalController.desLoad()` return type mismatch in example module

### Build
- Auto-generated build year via build-helper-maven-plugin
- Dynamic copyright: `Copyright © 2023-${build.year} sander`
- Removed obsolete OSSRH staging configuration

### Security
- All commit and tag emails replaced with GitHub noreply address
- Removed plaintext credential from git history

---

## [1.0.6.6] - 2025-11-04

### Added
- `BigDecimalUtils` utility class
- Adjustments to the `HttpResponse` object
- New features merged during the maintenance interruption

### Changed
- Optimized `JsonUtils`
- Optimized encryption utility implementations
- Security upgrade of the RSA algorithm
- Optimized DDNS utilities

---

## [1.0.6.5] - 2025-08-20

### Added
- `Assert` assertion utility
- Additional smoke tests
- Optimized collection utilities

### Changed
- Javadoc exception handling
- Handling of obsolete objects
- Changed the inheritance hierarchy of the base exception object
- Annotation-based startup support for encryption utilities

---

## [1.0.6.4] - 2025-02-04

### Added
- Assertion utilities and smoke tests
- `FeatureUtil` feature utility

### Changed
- Adjustments to `SpecialSymbolConst`
- Annotation-based startup for encryption utility implementations
- Changed the parent class of the base exception object

---

## [1.0.6.3] - 2024-08-20

### Added
- `FeatureUtil` (feature) utility
- Optimized `DateUtils` utility
- Optimized `Maps` utility

### Changed
- Fixed a bug in the no-argument constructor of the `R` object
- Optimized date and collection utility classes

---

## [1.0.6.2] - 2024-07-20

### Added
- Optimized `bedrock-resp` objects
- Version bump to 1.0.6.2

### Changed
- Optimized the response object structure
- Standardized version management

---

## [1.0.6] - 2024-06-20

### Added
- Optimized `CopyUtil` utility
- Optimized `BaseUtils` utility
- Version bump to 1.0.6

---

## [1.0.5] - 2024-05-20

### Added
- `encrypt` module upgrade
- Optimized `BaseUtils` utility
- Fixed the incorrect groupId in the README

---

## [1.0.4] - 2024-04-20

### Added
- Optimized `CopyUtil` utility
- Version bump to 1.0.4

---

## [1.0.3] - 2023-XX-XX

### Added
- New features in the `DateUtil` utility
- Project structure adjustments
- Version bump to 1.0.3

---

## [1.0.2] - 2023-XX-XX

### Added
- Project structure adjustments
- Version bump to 1.0.2

---

## [1.0.1] - 2023-XX-XX

### Added
- Base utility classes
- Version bump to 1.0.1

---

## [1.0.0] - 2023-XX-XX

### Added
- Project initialization
- Base modules: `bedrock`, `encrypt`, `fortify`, `tools-spring-boot-starter`
- Implementation of common utility classes

---

## Versioning

This project follows [Semantic Versioning](https://semver.org/):

- **Major version** (X.0.0): incompatible API changes
- **Minor version** (1.X.0): backwards-compatible functionality additions
- **Patch version** (1.0.X): backwards-compatible bug fixes

A SNAPSHOT version denotes an unstable development build that may change at any time.
