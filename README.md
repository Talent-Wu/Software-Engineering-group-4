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

## Software Architecture
<!-- Software architecture diagram or description would be placed here -->

## Installation
1. Ensure **OpenJDK 24** or later is installed:  
   [Download from jdk.java.net/24](https://jdk.java.net/24/)

## Instructions

### Graphical Interface Launch


### Terminal Execution


## Data Management


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
  
# Contributing

Before you submit your code, please read the following guidelines to ensure that your contributions meet our standards.

## Commit Message Format

To maintain consistency and readability in our commit messages, we require all commit messages to follow this format:

v*.0 "what you update"_name

### Examples
- `v1.0 "Added user registration feature"_John Doe`
- `v2.0 "Fixed login issue"_Jane Smith`
- `v3.0 "Improved UI design"_Alice Johnson`

### Explanation
- **v*.0**: The version number, indicating which version the commit belongs to. For example, `v1.0` refers to the first version.
- **what you update**: A brief description of the changes you've made, such as adding a new feature, fixing a bug, or improving the design.
- **_name**: The name of the committer, for traceability and record-keeping purposes.

## Branching Strategy
- **Main branch**: `main`, where stable code is stored.
- **Feature branches**: For example, `user-registration`, used for developing new features.
- **Fix branches**: Starting with `fix/`, such as `fix/login-issue`, used for fixing problems.

## Commit Workflow
1. **Create a branch**: From the main branch, create a new feature or fix branch. Initiate PULL/PUSH requests, which will then be reviewed and approved by the lead.
