# Software-Engineering-group-4
Final Project

# AI-Empowered Personal Finance Tracker

## Description
The AI-Empowered Personal Finance Tracker is a desktop application developed for comprehensive personal finance management. This smart tool helps users track expenses, analyze spending patterns through AI insights, and adapt to regional financial habits such as seasonal spending during the Chinese New Year. Key features include:

- Dual Data Entry: Manually input transactions or import CSV/JSON files from Alipay or WeChat Pay.
- AI-Powered Categorization: Automatically classify expenses with the capability of manual correction.
- Spending Analytics: Receive AI-generated budget suggestions and savings recommendations.
- Local Financial Adaptation: Customize for China-specific spending patterns and festivals.

Built using the Agile methodology, this application combines AI automation with human validation to provide reliable financial insights while maintaining data privacy.

## Software Architecture
<!-- Software architecture diagram or description would be placed here -->

## Installation Instructions
1. Ensure that **OpenJDK** or a later version is installed.
2. Configure relevant libraries such as JavaFX.
3. Run the main.java file.

## Instructions

### Graphical Interface Launch


### Terminal Execution


## Data Management


## Team Roles and Responsibilities

### Login Module
**Lead:** Yibo Yang
**Responsibilities:**
- Develop the user authentication system.
- Implement the registration interface.
- Implement the login interface.
- Implement the password recovery interface.
- Validate local data security and session management.
**GitHub Branch:** `login`

### Main Interface Module
**Lead:** YuXing Wu, Xiaoyang Guo, Yihan Luo, Yuhong Zong
**Sub-Tasks:**
1. **Transaction Input Panel:**
   - Develop the manual entry functionality.
   - Integrate the CSV/JSON file auto-import parser.
2. **AI Classification & Insights:**
   - Deploy the transaction classification AI model.
   - Visualize spending patterns.
3. **Budget Forecast Dashboard:**
   - Generate monthly budget recommendations based on AI analysis.
   - Display savings goals and cost-saving strategies.
4. **Localization Rules:**
   - Configure regional spending habits (e.g., adjustments for the Lunar New Year).
   - Implement scenario-specific validations (e.g., for WeChat red envelope transactions).
**GitHub Branch:** `Main-Interface`

### User Settings & Documentation Module
**Lead:** Yuyang Lu
**Responsibilities:**
- Develop the user profile editing functionality.
- Write the user manual and built-in help documentation.
- Generate test datasets and validate data consistency.
**GitHub Branch:** `User-Information`
