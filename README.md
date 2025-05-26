# Software-Engineering-group-4
Final Project

# AI-Empowered Personal Finance Tracker

## Description
AI-Empowered Personal Finance Tracker is a desktop application developed for comprehensive personal finance management. This smart tool helps users track expenses, analyze spending patterns through AI insights, and adapt to regional financial habits like Chinese New Year seasonal spending. Key features include:

- Dual Data Entry: Manually input transactions or import CSV/JSON files from Alipay/WeChat Pay
- AI-Powered Categorization: Automatic expense classification with manual correction capability
- Spending Analytics: AI-generated budget suggestions and savings recommendations
- Local Financial Adaptation: Customizable for China-specific spending patterns and festivals

Built using Agile methodology, this application combines AI automation with human validation to deliver reliable financial insights while maintaining data privacy.

## Software Architecture and Configuration
<!-- Software architecture diagram or description would be placed here -->

### 1. Environment Preparation
- **JDK**: Install Java 11 or a higher version. Java 17 LTS is highly recommended.
- **IDE**: Install the latest version of IntelliJ IDEA Community or Ultimate edition.
- **JavaFX SDK**: Download the corresponding version of the JavaFX SDK (e.g., JavaFX 17.0.2) and extract it to a local directory.
- **Mockito**: Download the corresponding version of the Mockito SDK (e.g., mockito-core-5.17.0) and extract it to a local directory.
- **JUnit 5**: Download the corresponding version of the JUnit 5 SDK and extract it to a local directory.

### 2. Create a New Project
1. Open IntelliJ IDEA. Select `File` → `New` → `Project`.
2. On the left - hand side, select **Java**. On the right - hand side, choose the installed JDK (such as Java 17).
3. **Uncheck** the `Create project from template` option, then click `Next`.
4. Set the project name (e.g., `PersonalFinance`) and its location, and click `Finish`.

### 3. Manually Import JavaFX Dependencies

#### Step 1: Configure Global Libraries
1. Open `File` → `Project Structure` (shortcut: `Ctrl+Alt+Shift+S`).
2. On the left side, select `Libraries`. Click `+` → `Java`.
3. Navigate to the `lib` directory of the JavaFX SDK. Select all `.jar` files and click `OK`.
4. Name the library (e.g., `JavaFX-17.0.2`), and click `Apply`.

#### Step 2: Add Dependencies to the Module
1. On the left side, select `Modules`. Then select the project module (e.g., `PersonalFinance`). Click the `Dependencies` tab.
2. Click `+` → `Library`. Select the `JavaFX-17.0.2` library that you just created, and click `OK`.
3. Ensure that the dependency scope is set to `Compile`. Then click `Apply` → `OK`.

### 4. Configure Run Parameters
1. Open the main class file (e.g., `Main.java`).
2. Click the green triangle next to the class name, and select `Edit Configurations`.
3. In the `VM options`, add the following parameters. Replace `"Yours path"` with the actual path of your JavaFX SDK:
```
--module-path "Yours path" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base,javafx.media,javafx.web,javafx.swing
```
**Note**: The path should be enclosed in double quotes, and you can use either `\` or `/` as the path separator.
4. Click `Apply` → `OK`.

### 5. Manually Import Other Dependencies (such as Test Libraries)
When you need to import test libraries (e.g., JUnit 5 and Mockito):
1. Download the required `.jar` files. For JUnit 5, these may include `junit-jupiter-api-5.8.2.jar` and `junit-jupiter-engine-5.8.2.jar`, along with relevant Mockito `.jar` files.
2. Repeat the steps in section 3 to add the downloaded `.jar` files to the global library and project dependencies.

### 6. Run the Project
1. Ensure that the main class (e.g., `Main.java`) contains a `main` method.
2. Click the run button (the green triangle), or press `Shift+F10`. 

## Team Roles and Responsibilities

### Login Module
**Lead:** Yibo Yang
**Responsibilities:**
- Develop user authentication system
- Implement registration interface
- Implement login interface
- Implement password recovery interface
- Validate local data security and session management  
**GitHub Branch:** `login`

### Main Interface Module
**Lead:** YuXing Wu, Xiaoyang Guo, Yihan Luo, Yuhong Zong  
**Sub-Tasks:**
1. **Transaction Input Panel:**
   - Develop manual entry functionality
   - Integrate CSV/JSON file auto-import parser
2. **AI Classification & Insights:**
   - Deploy transaction classification AI model
   - Visualize spending patterns
3. **Budget Forecast Dashboard:**
   - Generate monthly budget recommendations based on AI analysis
   - Display savings goals and cost-saving strategies
4. **Localization Rules:**
   - Configure regional spending habits (e.g., Lunar New Year adjustments)
   - Implement scenario-specific validations (e.g., WeChat红包 transactions)  
**GitHub Branch:** `Main-Interface`

### User Settings & Documentation Module
**Lead:** Yuyang Lu  
**Responsibilities:**
- Develop user profile editing functionality
- Write user manual and built-in help documentation
- Generate test datasets and validate data consistency  
**GitHub Branch:** `User-Information`
  
## Contributing Calculation

Before you submit your code, please read the following guidelines to ensure that your contributions meet our standards.

### Commit Message Format

To maintain consistency and readability in our commit messages, we require all commit messages to follow this format:

v*.0 "what you update"_name

#### Examples
- `v1.0 "Added user registration feature"_John Doe`
- `v2.0 "Fixed login issue"_Jane Smith`
- `v3.0 "Improved UI design"_Alice Johnson`

#### Explanation
- **v*.0**: The version number, indicating which version the commit belongs to. For example, `v1.0` refers to the first version.
- **what you update**: A brief description of the changes you've made, such as adding a new feature, fixing a bug, or improving the design.
- **_name**: The name of the committer, for traceability and record-keeping purposes.

### Branching Strategy
- **Main branch**: `main`, where stable code is stored.
- **Feature branches**: For example, `user-registration`, used for developing new features.
- **Fix branches**: Starting with `fix/`, such as `fix/login-issue`, used for fixing problems.

### Commit Workflow
1. Assign yourself a task from the project board.
2. Create a sub-branch from the master branch with a descriptive name.
3. Make regular commits to your sub-branch.
4. Once your work is complete, open a pull request to merge your sub-branch into the master branch.
