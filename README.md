# Codestyle Bot

An automated code quality and static analysis tool that uses [Moderne](https://www.moderne.io/) to analyze and suggest improvements for pull requests.

## What This Does

When you open a pull request, this GitHub Action automatically:

1. Analyzes only the files you've changed (files with added lines)
2. Runs static analysis recipes using Moderne's OpenRewrite framework
3. Applies automated fixes for common code quality issues
4. Posts the suggestions directly on your PR as inline code review comments

## How It Works When You Open a PR

### Step-by-Step Workflow

When a pull request is created or updated, the following happens automatically:

#### 1. Environment Setup
- **Checkout code**: Fetches the entire repository with full git history (`fetch-depth: 0`)
- **Java setup**: Installs JDK 17 (Temurin distribution) with Gradle caching enabled
- **Moderne CLI installation**:
  - Fetches the latest Moderne CLI version from GitHub releases
  - Caches the CLI binary to speed up subsequent runs
  - Only re-downloads if a new version is available

#### 2. PR File Analysis
The workflow intelligently identifies which files to analyze:

```bash
# Compares PR branch against base branch
git diff --numstat "origin/$BASE_BRANCH"...HEAD
```

- Only files with **added lines** are included (new files or files with additions)
- Files that only have deletions are excluded
- The list of changed files is saved for the next step

#### 3. Dynamic Configuration Generation
A custom `moderne.yml` configuration file is generated specifically for your PR:

- **File inclusion**: Only the changed files are included for analysis
- **Module detection**: Automatically detects which Gradle modules are affected
- **Build files**: Includes relevant `build.gradle` or `build.gradle.kts` files for each affected module

This targeted approach makes the analysis fast and relevant to your changes.

#### 4. Static Analysis Execution
The Moderne CLI runs the analysis:

```bash
mod build .                          # Build LST (Lossless Semantic Tree) for changed files
mod config recipes jar install       # Install static analysis recipes
mod run . --recipe=...              # Run CommonStaticAnalysis recipe
mod git apply . --last-recipe-run   # Apply fixes and generate patch
```

**Recipe applied**: `org.openrewrite.staticanalysis.CommonStaticAnalysis`

This recipe includes checks for:
- Code simplifications
- Common bug patterns
- Code style improvements
- Performance optimizations
- Best practice violations

See [OpenRewrite's Static Analysis recipes](https://docs.openrewrite.org/recipes/staticanalysis) for the full list.

#### 5. Code Suggestions Posted to PR
If any improvements are found:

- A patch file (`fix.patch`) is generated
- Google's `code-suggester` posts the changes as inline review comments
- You can accept suggestions with a single click directly in the GitHub UI

### Permissions

The workflow requires:
- `contents: read` - To checkout and read your code
- `pull-requests: write` - To post review comments on the PR

## Example Workflow

1. You create a PR with changes to `core/src/main/java/com/example/MyClass.java`
2. The bot detects this file has additions
3. It generates a config that includes:
   - `core/src/main/java/com/example/MyClass.java`
   - `core/build.gradle`
4. Moderne analyzes just these files
5. If it finds issues (e.g., unnecessary null checks, simplified boolean expressions)
6. You see inline suggestions on your PR with the exact code changes to make

## Benefits

- **Fast**: Only analyzes/builds changed files, not the entire codebase
- **Automated**: No manual configuration needed per PR
- **Educational**: Learn best practices from the suggestions
- **Non-blocking**: Suggestions are informational; they don't block merging
- **One-click fixes**: Accept suggestions directly in GitHub's UI

## Project Structure

This is a Gradle-based Java project with:
- Multi-module structure (modules in subdirectories like `core/`)
- JDK 17 compatibility
- Gradle wrapper included (`gradlew`, `gradlew.bat`)

## Local Development

To run the same analysis locally:

```bash
# Install Moderne CLI
curl -O https://repo1.maven.org/maven2/io/moderne/moderne-cli/LATEST/moderne-cli-LATEST.jar

# Build your project
./gradlew build

# Run static analysis
java -jar moderne-cli-*.jar build .
java -jar moderne-cli-*.jar config recipes jar install org.openrewrite.recipe:rewrite-static-analysis:2.18.0
java -jar moderne-cli-*.jar run . --recipe=org.openrewrite.staticanalysis.CommonStaticAnalysis
java -jar moderne-cli-*.jar git apply . --last-recipe-run
```

## Troubleshooting

If the workflow fails:
- Check the Actions tab for the build log
- The workflow displays the Moderne build log if analysis fails
- Ensure your changed files compile successfully

## References

- [Moderne Documentation](https://docs.moderne.io/)
- [OpenRewrite](https://docs.openrewrite.org/)
- [Static Analysis Recipes](https://docs.openrewrite.org/recipes/staticanalysis)