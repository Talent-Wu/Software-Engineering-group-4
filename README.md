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
**Lead:** [Name]  
**Responsibilities:**
- Develop user authentication system
- Implement registration interface
- Implement login interface
- Implement password recovery interface
- Validate local data security and session management  
**GitHub Branch:** `login`

### Main Interface Module
**Lead:** [Name]  
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
**Lead:** [Name]  
**Responsibilities:**
- Develop user profile editing functionality
- Write user manual and built-in help documentation
- Generate test datasets and validate data consistency  
**GitHub Branch:** `User-Information`