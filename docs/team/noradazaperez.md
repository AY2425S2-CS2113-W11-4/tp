# Nora Daza Pérez - Project Portfolio Page

## Overview
TripBuddy is a simple expense-tracking application designed to help users manage costs for a single trip. It allows
travelers to log expenses, categorize them, and view a summary of their spendings in one place.

### Summary of Contributions
#### **Code Contributed**
This is my [Contribution Page](https://nus-cs2113-ay2425s2.github.io/tp-dashboard/?search=noradazaperez&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2025-02-21&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other)

#### Enhancements Implemented
I implemented the following commands: 
* The multi-currency support:
  * `view-currency` : a command that displays the exchange rate between the base currency and the available currencies
  * `set-base-currency` : a command that changes the base currency 
  * `Currency` class that represents the different currencies 
* `view-history` : a command that displays the past expenses

Contributions to the UG: Which sections did you contribute to the UG?
#### Contributions to the DG: 
##### **Multi-currency support** 
I explained what the different features the multi-currency support has:
* How the conversion logic works
* How does `view-currency` work using a sequence diagram 

##### **Design**
I explained the different aspects of the design, adding class diagrams for that, including:
* **CommandHandler**  : what the main functions of the `CommandHandler` are 
* **ExpenseManager**  : how the `ExpenseManager` class and the `Expense` class interact with each other
* **InputHandler**    : how the `Parser` and the `Command` classes work 
* **Ui**              : what the main functions of the `Ui` are 

##### **Diagrams**
I was in charge of doing both the class and sequence diagrams in the developer's guide. I decided to create a main
architecture diagram explaining how the system works in a high-level and afterwards, explain each module with its 
own detailed class diagrams. 

#### Contributions to the overall design
Proposed a more robust and less coupled system where we would use the singleton design only in the ExpenseManager. 
The rest of the classes do not depend on each other and are easily testable and scalable. 

### Contributions to team-based tasks
The tasks I did included:
* Releasing v2.0 and closing the milestone 
* Adding issues to the issue tracker 
* Documenting the target user profile 

### Testing
Coded the tests on the methods I implemented as well as the `Ui` and storage 
functions. 

Testing on the ExpenseManager module, and the modules that used it was a little bit
tricky because we had to delete all the information in the singleton class after each test. 

### Mentoring contributions
* Brainstorming for new enhancements for v2.0 
* Updating the new features list 

