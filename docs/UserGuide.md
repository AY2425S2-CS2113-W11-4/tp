# User Guide

## Introduction

TripBuddy is a simple expense-tracking application designed to help users manage costs for a single trip. It allows 
travelers to log expenses, categorize them, and view a summary of their spendings in one place. TripBuddy ensures 
budgeting is easy and hassle-free, so travelers can focus on enjoying their trip.

## Setup Guide

- Make sure Java 17 is installed on your device.
- Download `[CS2113-W11-4][tripbuddy].jar` from the [release site](https://github.com/AY2425S2-CS2113-W11-4/tp/releases)
- Run the program with command:
```
java -jar [CS2113-W11-4][tripbuddy].jar
```

## Notes about the command format

- Words in UPPER_CASE are the parameters to be supplied by the user.
e.g. in `set-budget AMOUNT`, AMOUNT is a parameter which can be used as `set-budget 1000`.
- Square brackets `[]` indicate optional elements that can be omitted.
- Extraneous parameters will be ignored and have no effects.
(e.g. if the command specifies quit 123, it will be interpreted as quit.)
- **Avoid using excessively larger/small values or overlength names.**
- **Avoid exit with `Ctrl+C` or force killing the process to prevent data loss.**

## Supported Currencies

Note that exchange rates are hard-coded to comply with CS2113 tP restrictions.

- MYR
- PHP
- SGD
- THB
- USD
- EUR
- JPY
- AUD
- CAD
- CNY
- HKD
- INR
- NZD
- CHF
- TWD
- ZAR
- GBP

## Features 

### View tutorial : `tutorial`

Displays a message explaining full features.

Format: `tutorial`

### Set Base Currency: `set-base-currency`

Set the new currency.
- By default, the base currency is SGD.

Format: `set-base-currency CURRENCY`

### View Currency: `view-currency`
    
Displays the actual rates between currencies with the base currency.

Format: `view-currency`

### Setting budget : `set-budget`

Sets a total budget for this trip.
- Default budget is 1000 in base currency.

Format: `set-budget BUDGET_NUMBER`

Examples of usage:
- `set-budget 750`
- `set-budget 1000`

### View Budget: `view-budget`

Check your remaining budget.

Format: `view-budget`

### Add Expense: `add-expense`

Adds an expense to the trip and automatically updates the remaining budget, taking into account 
the new expenditure.

- `AMOUNT` is in base currency.

Format: `add-expense EXPENSE_NAME -a AMOUNT [-c CATEGORY]`

Examples of usage:
- `add-expense mcdonalds -a 5`
- `add-expense capybara museum -a 10000 -c Activities`

### Delete Expense: `delete-expense`

Removes an expense from the trip.

Format: `delete-expense EXPENSE_NAME`

Examples of usage:
- delete-expense the-plaza-hotel

### Edit Amount: `edit-amount`

Edits the amount associated with an expense. Overrides the previous amount.

Format: `edit-amount EXPENSE_NAME -a AMOUNT`

- `AMOUNT` must be a positive number.

Examples of usage:
- add-expense breakfast -a 10
- edit-amount breakfast -a 13

### List Expense: `list-expense`

Display all expenses, or expenses under a category if CATEGORY is given, and the sum of recorded expenses.

Format: `list-expense [CATEGORY]`

### Search Expense: `search`

Displays expenses that include the given search word.

Format: `search SEARCHWORD`

Examples of usage:
- `search shopping`

### Max/min Expense: `max-expense`/`min-expense`

Display an expense with the highest/lowest amount.

Format: `max-expense`/`min-expense`

### Filter Date: `filter-date`

Get all expenses within date range, inclusive.

Format: `filter-date -f yyyy-MM-dd HH:mm:ss -t yyyy-MM-dd HH:mm:ss` 

### Create Category: `create-category`

Creates a category for storing expenses, such as accommodation or food.

Format:  `create-category NAME`

Examples of usage:
- `create-category Accommodation`
- `create-category food and drink`

### Delete Category: `delete-category`

Deletes a category from the category list.

- The category must be empty (have no associated expenses) in order to be deleted.

Format:  `delete-category NAME`

Examples of usage:
- `delete-category Accommodation`

### Set Category: `set-cateogry`

Set the category for a particular expense that has been already inputted by the user. This command will override a 
prior category that was set for that specific expense.

Format: `set-category EXPENSE_NAME -c CATEGORY`

- If CATEGORY does not exist in the existing record of categories, then a new category will be created with
the specified name.

Examples:
- `set-category mcdonalds -c food`

### Clear Category: `clear-cateogry`

Clear the category for a particular expense that has been already inputted by the user, meaning the expense will no
longer belong to any category.

Format: `clear-category EXPENSE_NAME`

Examples:
- `clear-category greek-meal`

### Set Time: `set-time`

Updates the timestamp for an existing expense to a custom date and time.

Format: `set-time EXPENSE_NAME -t yyyy-MM-dd HH:mm:ss`

Examples:
- `set-time dinner -t 2024-03-20 18:45:00`

### View Categories: `view-categories`

Display all categories.

Format: `view-categories`

### Clear: `clear`
    
Clear all past expenses and categories.
- Budget remains the same.

Format: `clear`

### Exit the program: `quit`

Exit the program.

Format: `quit`

### Saving the Data

TripBuddy persists user data (budget, expenses, and categories) in the hard disk automatically after any command that 
changes the data. There is no need to save manually.

Usage:
* The JSON file `[JAR file location]/tripbuddy_data.json` is written to disk when the user exits the application.
* It is loaded during application startup to restore the previous session.

## FAQ
**Q:** How do I transfer my data to another Computer?

**A:** Install the app in the other computer and overwrite the empty data file it creates with the file that contains the 
data of your previous TripBuddy home folder.

## Known issues
- Avoid using excessively large/small values or overlength names to ensure correct parsing.
- Avoid exit with `Ctrl+C` or force killing the process to prevent data loss. Instead, use the `quit` command to ensure 
all data is correctly written to disk.

## Command Summary

### Formatting
| **Action**        | **Format**                                                  |
|-------------------|-------------------------------------------------------------|
| View tutorial     | `tutorial`                                                  |
| Set Base Currency | `set-base-currency CURRENCY`                                |
| View Currency     | `view-currency`                                             |
| Set Budget        | `set-budget BUDGET_NUMBER`                                  |
| View Budget       | `view-budget`                                               |
| Add Expense       | `add-expense EXPENSE_NAME -a AMOUNT [-c CATEGORY]`          |
| Delete Expense    | `delete-expense EXPENSE_NAME`                               |
| Edit Amount       | `edit-amount EXPENSE_NAME -a AMOUNT`                        |
| List Expense      | `list-expense [CATEGORY]`                                   |
| Search Expense    | `search SEARCHWORD`                                         |
| Max Expense       | `max-expense`                                               |
| Min Expense       | `min-expense`                                               |
| Filter Date       | `filter-date -f yyyy-MM-dd HH:mm:ss -t yyyy-MM-dd HH:mm:ss` |
| Create Category   | `create-category NAME`                                      |
| Delete Category   | `delete-category NAME`                                      |
| Set Category      | `set-category EXPENSE_NAME -c CATEGORY`                     |
| Clear Category    | `clear-category EXPENSE_NAME`                               |
| Set Time          | `set-time EXPENSE_NAME -t yyyy-MM-dd HH:mm:ss`              |
| View Categories   | `view-categories`                                           |
| Clear All         | `clear`                                                     |
| Exit Program      | `quit`                                                      |

### Examples
| **Action**               | **Examples**                                                                |
|--------------------------|-----------------------------------------------------------------------------|
| `tutorial`               | `tutorial`                                                                  |
| `set-base-currency`      | `set-base-currency USD`                                                     |
| `view-currency`          | `view-currency`                                                             |
| `set-budget`             | `set-budget 2050`                                                           |
| `view-budget`            | `view-budget`                                                               |
| `add-expense`            | `add-expense mcdonalds -a 5`<br>`add-expense museum -a 10000 -c Activities` |
| `delete-expense`         | `delete-expense mcdonalds`                                                  |
| `edit-amount`            | `edit-amount mcdonalds -a 7`                                                |
| `list-expense`           | `list-expense`<br>`list-expense Activities`                                 |
| `search`                 | `search restaurant`                                                         |
| `max-expense`            | `max-expense`                                                               |
| `min-expense`            | `min-expense`                                                               |
| `filter-date`            | `filter-date -f 2025-04-01 00:00:00 -t 2025-04-05 23:59:59`                 |
| `create-category`        | `create-category Accommodation`<br>`create-category food and drink`         |
| `delete-category`        | `delete-category Accommodation`                                             |
| `set-category`           | `set-category the-plaza-hotel -c Accommodation`                             |
| `clear-category`         | `clear-category the-plaza-hotel`                                            |
| `set-time`               | `set-time mcdonalds -t 2024-03-20 18:45:00`                                 |
| `view-categories`        | `view-categories`                                                           |
| `clear`                  | `clear`                                                                     |
| `quit`                   | `quit`                                                                      |

