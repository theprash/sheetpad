**Work in progress!**

Spreadsheet pros:
- Reactive and declarative in nature.
    - Cells contain derived data according to a formula definition. Data changes cascade down to derived data immediately, keeping everything up to date at all times.
- Well understood by most people.
- Convenient to use:
    - When opening a new spreadsheet, many cells already exist and tables can be entered immediately.

Spreadsheet cons:

Core features of Sheetpad:

- Every data item is named (in square brackets)
    - Unnamed items are given temporary, but visible names, e.g. [unnamed item 1]

- Items can be entered explicitly or derived from other data in a formula.

- Items can be derived from items that are defined further down.

- Data types:
    String
    Number
    Table

- An example pad in mock text format:
```
[widget price] = 8

[widgets bought] = table:
    | [Customer] | [Quantity] | [Paid] (([widgets bought].[Quantity] * [widget price]) * (1 - [discount])) |
    --------------------------------------------------------------------------------------------------------
    | Prash      | 4          | 28.8                                                                       |
    | Steph      | 10         | 72                                                                         |

[discount] = 10/100
```
