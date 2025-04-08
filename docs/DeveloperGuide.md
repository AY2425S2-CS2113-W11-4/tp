# Developer Guide
<!-- TOC -->
<!-- /TOC -->

## Acknowledgements

- [JSON-java](https://github.com/stleary/JSON-java): a third-party library for
JSON conversion and parsing.
---
## Design

This section describes some details on the design.

TripBuddy allows user interactions via a CLI, which is activated by `TripBuddy`.

The main framework consists of four layers: `Ui`, `Logic`, `Model` and `Storage`.

<img src="diagrams/architecture_diagram.jpg" alt="GeneralDesign" width="700">

This layered design results in less conflicts while developing multiple
features, as well as ensuring testability of different modules.

In a high level, we can find the folowing classes:  

<div style="text-align: center;">
  <img src="diagrams/class/TripBuddyClassDiagram.png" alt="GeneralDesign" width="200">
</div>

---

### Ui 
This is made of one class: `Ui`. 


The `Ui` class is responsible for displaying messages to the user via the command-line interface (CLI). It handles:
* Printing prompts and responses for user interactions.
* Displaying success and error messages.
* Formatting outputs for clarity and readability.
* Ensuring smooth user experience by maintaining a consistent UI flow.

---
### Logic - User Input
These classes are responsible for making sense of the user's input and creating 
`Command`s that will be useful for the model.

The following classes are the ones dealing with the logic.

<img src="diagrams/class/CommandDiagram.png" alt="Command Classes" width=400>


#### CommandHandler

`CommandHandler` is responsible for the following tasks:
- Collecting and manipulating data from `ExpenseManager`

- Processing the output into `String` messages to be shown on UI

Parameters of `CommandHandler` methods should be parsed by `InputHandler` already. Every method corresponds to a
command that can be done in the system.

For example, in the following sequence diagram, we show how `handlerAddExpense` works.
<img src="diagrams/sequence/AddExpense.png" alt="Add Expense Diagram" width=600>


#### InputHandler

`InputHandler` is responsible for the following tasks:

- Analyzing user input and converting arguments into `Command` objects

- Invoking `CommandHandler` methods for command execution.

- Handling exceptions caused by user actions.

Inside this logic, we also find other classes.


##### Command
This class keeps the information of a Command, such as, `view-currency` or `add-expense`.
It has the following attributes:
- Keyword: this keeps the information of the type of command (eg. `view-currency`)
- OptList: this keeps a list with the possible optional tokens (eg. `add-expense AMOUNT CURRENCY`)
- OptMap: this keeps information about the optional tokens. The keys are the names of the optional values
  (eg. '-c' if we want to input a currency when using 'add-expense') and the values are the actual values inputted
  by the user.

##### Keyword
This is an enumeration that indicates the different types of commands there are available.

#### Option
This indicates the different arguments a command can have.  
Note: if their value is empty ("") they are mandatory arguments.

#### Parser
The main tasks for this class are to:
* Parse the user's input to a `Command` object
* Check whether an argument is optional or not

---

### Model - Expenses
In order to implement the model of the app, we have used an `ExpenseManager` that keeps the information of the 
trip expenses, together with the `Expense` class, and `Currency` enumeration. 

<div style="text-align: center;">
  <img src="diagrams/class/ExpenseDiagram.png" alt="GeneralDesign" width="400">
</div>


#### ExpenseManager

`ExpenseManager` stores all user data, and has direct CRUD access to them. It saves the **budget**, the **total expense**
of the user so far, and the list of **categories**.

Using a singleton for the ExpenseManager was a design decision aimed at ensuring that there's only one consistent 
source of truth for all expense-related data throughout the application. In a smaller application, using a singleton 
minimizes the complexity of passing instances around. 

Return values of methods of `ExpenseManager` are unprocessed, i.e. not parsed
into `String` or other formats for UI output.



It manipulates the array of expenses and has methods to do the following tasks:
* Adds an expense
* Deletes an expense
* Retrieves the maximum expense
* Retrieves the minimum expense
* Retrieves expenses by category or date

#### Expense
This is a class that saves the data of a specific expense made by the user. It holds important information such as
the name, the amount and the date the expenditure was made. The use of the category is optional.

The amount of the expense is calculated in the base currency. You can change the base currency using the command
`set-base-currency`.

#### Currency
This is an enumeration class explaining all the available currencies. The base currency is the one whose exchange rate
is one. The default base currency is SGD.


---

### Storage 
TripBuddy persists user data (budget, expenses, and categories) by serializing it into a structured JSON file.

The storage module is responsible for persisting and retrieving the application's data in JSON format. 
It comprises two main classes: **DataHandler** and **FileHandler**. 

While these classes form the core of our storage solution, DataHandler also relies on **ExpenseManager** 
(implemented as a singleton) to access and update the current state of expense data.

#### FileHandler

The main purpose is:
- Provides file operations for reading from and writing to JSON files.
- Ensures that all necessary directories exist before writing and handles the low-level I/O operations.

**Key Methods:**

- **`readJsonObject(String path)`**  
  Reads a JSON file from the specified path, aggregates its content into a string using a `Scanner`, and then parses 
the string into a `JSONObject`.

- **`writeJsonObject(String path, JSONObject data)`**  
  Writes a formatted (pretty-printed) JSON string to the specified path. If the file’s parent directories are missing, 
they are automatically created. Logging is performed to capture operations such as directory initialization.

  
#### DataHandler

The main purpose is:
- Coordinates data persistence by converting the state of the ExpenseManager into JSON for saving, as well 
as reconstructing that state from JSON during loading.
- Acts as the bridge between the in-memory data (via ExpenseManager) and the physical file system (via FileHandler).

**Key Methods:**

- **`saveData(String path, ExpenseManager expenseManager)`**
  - Converts the current state of the ExpenseManager (including budget, currency, categories, and expense list) 
into a structured JSON object.
  - Delegates the file-writing operation to FileHandler.
  - Logs progress at key steps (e.g., when converting budgets, categories, and expenses).


- **`loadData(String path)`**
  - Reads the JSON file from the provided path.
  - Updates the ExpenseManager with the loaded data, handling cases of missing or malformed fields.
  - Aggregates error messages when issues occur, ensuring robust error reporting without halting the application.



<div style="text-align: center;">
  <img src="diagrams/class/StorageDiagram.png" alt="GeneralDesign" width="400">
</div>



#### JSON file structure

Fields:
* currency (String): The base currency used for all budget and expense tracking. This is stored using the standard 
currency code (e.g., "USD", "SGD").
* budget (Double): The total budget allocated for the trip, rounded to two decimal places.
* categories (Array<String>): A list of category names that expenses can be grouped under.
* expenses (Array<Object>): List of recorded expenses, each represented as an object. Each expense contains:
  * name (String): The identifier of the expense. 
  * amount (Double): The cost of the expense. 
  * category (String): The category this expense belongs to. Can be empty if uncategorized.
  * dateTime (String): The date and time in yyyy-MM-dd HH:mm:ss format of when the expense was recorded or modified.

---

### Exceptions 
We have defined different exception types to ensure that all errors are properly covered. 
<div style="text-align: center;">
  <img src="diagrams/class/ExceptionDiagram.png" alt="GeneralDesign" width="400">
</div>

This diagram defines the custom exception hierarchy used in TripBuddy and shows the `ExceptionHandler` class that centrally manages error messages.

### Exception Hierarchy

- **`<<abstract>> TripBuddyException`**  
  The root for all custom TripBuddy exceptions.

- **`<<abstract>> InvalidCommandException`**  
  Base for command-related errors.
  - **`InvalidKeywordException`**: Raised for unrecognized keywords (stores the problematic keyword).
  - **`InvalidArgumentException`**: Raised for invalid command arguments (stores the offending argument).
  - **`MissingOptionException`**: Raised when a required option is absent (stores missing option details).

- **`DataLoadingException`**  
  Signals errors during data loading (e.g., JSON parsing issues) and extends `TripBuddyException`.

### ExceptionHandler Class

Provides centralized methods for handling exceptions, such as:
- `handleInvalidKeywordException`
- `handleInvalidArgumentException`
- `handleMissingOptionException`
- Other methods to handle array index issues, date parsing, file not found, and general exceptions.

This structure simplifies error reporting and ensures consistent exception handling throughout the application.

## Implementation
This section describes an explanation on some of the implemented features. 

### Min-expense
Allows users to be informed on the lowest expense made in real-time.

### Max-expense
Allows users to be informed on the highest expense made in real-time

### Multi-currency support 
Allows users to have multi-currency expenses in the app.
#### **Step 1**: Conversion rates storage
We use a simple dictionary that maps currency codes to their conversion rates relative to the base
currency.

* For example:

```json
{
    "SGD": 1.0,
    "EUR": 1.1,
    "JPY": 0.009,
    "GBP": 1.3
}
``` 

We can also see the current currency rates available in our app using the command 
`view-currency`. This will display the above `json`. 

Moreover, we can also change the base currency. In order to do this, we will:
1. Recalculate existing expenses: all previously recorded expenses need to be recalculated to reflect their values 
in the new base currency. 
2. Update the budget: update the value of the budget so that is consistent with the current currency.
3. Recalculate Conversion Rates: divide all the values in the conversion dictionary by the current rate to that 
currency. 

We can see how we do it in the following sequence diagram. 
<div style="text-align: center;">
<img src="diagrams/sequence/SetBaseCurrency.png" alt="Set Base Currency Diagram" width="700">
</div>

#### **Step 2**: Conversion logic
When an expense is added:
1. If a different currency is given, a conversion is needed
2. Multiply the expense amount by the conversion rate
3. The value in the base 


#### **Alternatives Considered**
- Keep the original value of the expense as well as the converted value. 

## Product scope
### Target user profile

**Target user profile**
- Individuals who are planning a trip or currently traveling.
- Budget-travelers who intend to establish a budget and track their expenses effectively.
- Users who prefer to use keyboard-based interactions over mouse-based navigation.
- Users who are comfortable using command-line interface (CLI) applications.

### Value proposition

TripBuddy helps travelers stay on top of their finances by simplifying expense tracking 
and budgeting during trips. TripBuddy provides an intuitive platform to log expenses and 
track spending in real time. By eliminating the confusion of manual calculations, TripBuddy
ensures a stress-free travel experience where everyone stays financially organized and accountable.
With features like real-time summaries, total spending insights, and visibility into your highest
and lowest expenses, TripBuddy empowers users to make informed financial decisions on the go.

## User Stories

| Version | As a ...        | I want to ...                          | So that I can ...                                               |
|---------|-----------------|----------------------------------------|-----------------------------------------------------------------|
| v1.0    | new user        | see usage instructions                 | refer to them when I forget how to use the app                  |
| v1.0    | budget-traveler | set a travel budget                    | monitor spending to avoid overspending during the trip          |
| v1.0    | user            | add an expense                         | track my expenses and total remaining budget.                   |
| v1.0    | user            | delete an expense                      | correct an earlier mistake.                                     |
| v1.0    | user            | view my remaining budget               | see how much money I have left to spend.                        |
| v1.0    | user            | view past expenses                     | review my past expenses for accuracy                            |
| v2.0    | user            | adjust my total budget                 | modify my spending habits accordingly.                          |
| v2.0    | user            | see total amount spent                 | understand my overall spending up to date                       |
| v2.0    | user            | see minimum and maximum expense        | identify the smallest and largest purchases I've made           |
| v2.0    | user            | add an expense in a different currency | add expenses in the local currency and not think about the rate |
| v2.0    | user            | save/load the information of a trip    | i don't have to keep adding expenses every time                 |
| v2.1    | user            | set the timestamp of an expense        | accurately reflect when the expense actually occurred           |

## Non-Functional Requirements

* Performance: The application should process user commands within 1 second.
* Usability: The system should have an intuitive CLI interface with clear error messages and user guidance.
* Reliability: TripBuddy should be able to handle at least 100 expense entries without crashing.
* Portability: The application should be compatible with Windows, macOS, and Linux environments.
* Scalability: The system should allow future enhancements, such as more expense categories, without significant rework.

## Glossary

* *expense* - a record of a purchase made by the user, stored as an `Expense`
entity.
* *budget* – a spending limit set by the user to track expenses and manage finances during a trip.
* *category* – a classification assigned to an expense (e.g., Food, Transport) to help users organize their spending.
* *currency* – The unit of money used for recording and viewing expenses. All values are shown in a single reference currency.
* *base currency* – The default currency used to calculate and display all financial information consistently.
* *timestamp* – The date and time when an expense was recorded or changed.

## Instructions for manual testing

Testing Budgeting Features
- Set a budget using the `set-budget` command and verify that the displayed amount is correct.
- Add and delete expenses to ensure that the remaining budget updates accordingly.
- Exceed the budget and check if the system notifies the user about overspending.
- Change the base currency using `set-base-currency` and confirm that the budget and expenses are recalculated properly.

Testing Expense Management
- Add expenses using `add-expense` with and without categories.
- Use `list-expense` to verify that all expenses are listed with accurate amounts.
- Apply `create-category` and assign categories using `set-category`.
- Use `search` to find expenses by partial name match.
- Use `set-time` to modify the timestamp of an existing expense and confirm the updated value is reflected.
- Use `filter-date` with a valid date range to ensure that only expenses within the range are shown.
- Use `max-expense` and `min-expense` to verify the correct expense is identified.

Testing Data Persistence
- Add multiple expenses and categories, then quit the program to trigger auto-saving.
- Restart the program and confirm that all data was correctly loaded from the save file.
- Intentionally corrupt the save file or remove fields and verify that the system handles this gracefully with a message.

Testing Command Handling
- Execute each supported command and confirm it performs as expected.
- Provide invalid commands, missing arguments, or incorrect formats and ensure the system responds with clear error messages.
- Include extra inputs (e.g., `quit now`) and confirm the app ignores them gracefully.

Verifying CLI Feedback
- Confirm that user actions result in clear and informative messages (e.g., budget set, expense added).
- Test the welcome and exit messages on application start and quit.
- Check that currency formatting, dates, and categories are consistently displayed.

Checking Error and Exception Handling
- Try adding an expense with a missing name or duplicate name.
- Provide a negative or very large number for budget or amount.
- Assign a category to a non-existent expense.
- Use an incorrect timestamp format in `set-time` or `filter-date`.
- Force a save/load operation on invalid data and ensure the program remains stable.

Edge Case and Robustness Testing
- Add extremely long or empty strings for names and categories.
- Use mixed-case, numeric, or symbolic inputs.
- Run commands with missing flags, extra spaces, or unexpected inputs to ensure the app handles them cleanly.