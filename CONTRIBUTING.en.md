# Contributing Guide

Thank you for your interest in and support for the `tools` project! We welcome contributions of any
kind, including but not limited to code, documentation, bug reports, and feature suggestions.

## How to Contribute

### 1. Report Issues

If you find a bug or have a feature suggestion, please submit it through an Issue:

- Provide a clear description of the problem
- Include detailed reproduction steps
- Attach error logs if available

### 2. Submit Code

#### Fork the Repository

1. Fork this project to your own account
2. Clone your fork to your local machine

#### Create a Branch

Please create a new branch based on the following naming convention:

```
feat/${user_name}/${demand}
```

For example:

- `feat/john/add-csv-utility`
- `feat/john/optimize-jsonutils-performance`

#### Commit Convention

Commit messages should follow this format:

```
${type}: ${short description}

${detailed description (optional)}
```

The available types are:

- `feat`: A new feature
- `fix`: A bug fix
- `docs`: Documentation updates
- `style`: Code formatting changes
- `refactor`: Code refactoring
- `test`: Test-related changes
- `chore`: Changes to the build process or auxiliary tools

Example:

```
feat: add CSV parsing utility class

Support parsing CSV files with delimiters, quotes, and line breaks
- Add CsvUtils utility class
- Add CsvParseException exception
- Unit test coverage 95%
```

#### Submission Workflow

1. Make sure the code passes all tests: `mvn clean test`
2. Make sure the code style is consistent with the existing project conventions
3. Commit your code and push it to your fork
4. Create a Pull Request

### 3. Pull Request Process

1. Create a Pull Request from your branch to the `develop${year}${month}${day}` branch
2. Wait for code review
3. Revise the code according to the review comments
4. After the CI checks pass, the change is merged into the develop branch
5. After the develop branch passes its tests, it is merged into the release branch for publishing

## Code Conventions

### Java Code Style

- Follow the Alibaba Java Coding Guidelines
- Use 4-space indentation, never tabs
- Limit line width to 120 characters
- Classes and methods must have Javadoc comments
- Use the `@author` tag to credit the author

### Naming Conventions

- Package names: `io.github.sander_i.*`
- Class names: UpperCamelCase
- Method and variable names: lowerCamelCase
- Constant names: UPPER_SNAKE_CASE

### Testing Conventions

- Every public method must have unit tests
- Target test coverage of 80% or higher
- Use JUnit 4 or JUnit 5
- Test method names start with `test`

## Development Environment

- JDK 8+
- Maven 3.6+
- IDE: IntelliJ IDEA / Eclipse

## Dependency Management

- Prefer features built into the JDK
- If a third-party library is needed, prefer a popular and stable version
- New dependencies must be justified in the Pull Request

## License

This project is released under the [MIT License](LICENSE). By submitting code, you agree to license
your contribution under the MIT License.

## Acknowledgements

Thank you to all contributors for your support!

See the list of contributors: [AUTHORS](AUTHORS.en.md)
