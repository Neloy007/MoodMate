# Fix java.lang.InterruptedException in mergeExtDexDebug

The `java.lang.InterruptedException` during the `:app:mergeExtDexDebug` task is typically caused by the Gradle daemon or worker process being interrupted, often due to memory constraints (Out of Memory) or build cache corruption.

## User Review Required

> [!NOTE]
> This issue is often environment-specific (related to available RAM on the machine). The proposed changes aim to make the build more robust by increasing memory limits.

## Proposed Changes

### Build Configuration

#### [MODIFY] [gradle.properties](file:///Users/neloy/AndroidStudioProjects/MoodMate/gradle.properties)
- Increase `org.gradle.jvmargs` from `-Xmx2048m` to `-Xmx4096m`.
- (Optional) Enable parallel execution to improve performance, although we will focus on memory first.

## Verification Plan

### Automated Tests
- Run `./gradlew clean :app:mergeExtDexDebug --stacktrace` to ensure the task completes successfully.
- Run `./gradlew :app:assembleDebug` to verify the entire build pipeline.

### Manual Verification
- Confirm with the user if the build succeeds on their machine after applying these changes.
