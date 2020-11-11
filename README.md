# ATM_CS2_Constraints

INDIVIDUAL CONSTRAINTS:
  1. Store the information of the customer in a file or database and retrieve it.
3 COMMON CONSTRAINTS:
  1.  Add phone number to get OTP as a two-step verification process for every transaction
  2.  Add methods to change the pin and other basic details of the customer.
  3.  An Option of transfering money from one account to other by entering the account number and ifsc code.
  
This is a menu driven program.

Handling the input and output is as follows:

1. The program checks if there is any information filled inside the database before hand. If not it fills the database with some pre-defined values.

2. Now the admin is prompted to set a password for the ATM and then the total cash inside it.

3. Then the admin is asked to set the ATM in working mode (from where the actual system starts). The working mode has two option 1. Admin and 2. Customers

4. Password is asked while entering to the Admin mode

5. The functionalities of the admin are 
	a. Shutdown the System 
	b. Add the cash 
	c. Show the current cash
  d. Add a customer to the database (Takes name, phone number, ifsc code, opening balance of the customer as input. The account number, pin are assigned by the system)
  e. Display all the accounts present in the database. (All the details of the customer are displayed except the PIN number)
(Note : This program takes a good care of the matching of two account number i.e, does not assign same account number to two different persons)

6. Account number and PIN is asked while entering the customer mode and is checked in the database for a match.

7. The functionalities of the customer are 
	a. view my balance 
	b. deposit funds 
	c. withdraw amount : If the withdrawl amount is greater than the cash inside the ATM or the balance of the customer then the process is halted with an error message and continued to the next step.
  d. transfer funds: If the amount is more than the balance of the customer the process is discontinued by showing error message.
  
8.Then the customer is prompted to logout or continue.

This program has 
    1. ATM class to handle all the funtionalities of an ATM
    2. Database class which initialises the database for the first time.
    3. User class which handles the functionalities of a customer like deposit, withdraw, transfer etc
    4. User_Account class which stores the data of the customer for temoparary accessibility.
