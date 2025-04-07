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

Format: `add-expense NAME_EXPENSE -a AMOUNT [-c CATEGORY]`

Examples of usage:
- `add-expense mcdonalds -a 5`
- `add-expense capybara museum -a 10000 -c Activities`

### Delete Expense: `delete-expense`

Removes an expense from the trip.

Format: `delete-expense NAME_EXPENSE`

Examples of usage:
- delete-expense the-plaza-hotel

### List Expense: `list-expense`

Show all expenses, or expenses under a category if CATEGORY is given, and the sum of recorded expenses.

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

### Set Category: `set-cateogry`

Set the category for a particular expense that has been already inputted by the user. This command will override a 
prior category that was set for that specific expense.

Format: `set-category NAME_EXPENSE -c CATEGORY`

- If CATEGORY does not exist in the existing record of categories, then a new category will be created with
the specified name.

Examples:
- `set-category mcdonalds -c food`

### Set Time: `set-time`

Updates the timestamp for an existing expense to a custom date and time.

Format: `set-time NAME_EXPENSE -t yyyy-MM-dd HH:mm:ss`

Examples:
- `set-time dinner -t 2024-03-20 18:45:00`

### View Categories: `view-categories`

Display all categories.

Format: `view-categories`

### Clear: `clear`
    
Clear all past expenses and categories.
- Still keeps the budget.

Format: `clear`

### Exit the program: `quit`

Exit the program.

Format: `quit`
