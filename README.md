# Procurement order tracking system
This is a procurement order tracking system developed as part of the CT038-3-2-OODJ (Object Oriented Development with Java) group assignment for the Asia Pacific University (APU) in 2024.

## How to run the program
Run program in the terminal using Shift + F6

## Project description
The procurement order tracking system aims to provide functionalities for different types of users within the system, including sales manager, purchase manager, inventory manager, and finance manager. Each user role has specific tasks and features they can perform within the system.

## User Roles and Functionalities
### Sales Manager (SM)
• List of Items (View)
• Daily Item-wise Sales Entry (Add/Save/Delete/Edit) – Retails
• Sales Report
• Stock Level
• Create Requisition (if an item is below Reorder Level)
• List of Purchaser Orders (View)
### Purchase Manager (PM)
• List of Items (View)
• List of Suppliers (View)
• Create and View Requisition created by SM
• Generate Purchase Order (Add/Save/Delete/Edit)
• List of Purchaser Orders (View)
### Inventory Manager (IM)
• Item Entry (Add /Edit/ Delete/Modify)
• Supplier Entry (Add / Edit / Delete / Modify)
• View Stock Levels: Monitor and track the current stock of items in the inventory.
• Update Stock Levels – after stock items are received from supplier.
• Add, save, edit, or delete stock information as needed.
### Finance Manager (FM)
• Verify Purchase Orders for Payment (View POs) – Approve / Reject
• Check Stock Status – updated by IM for the item which POs issued
• Make Payment – Update the PO status
• View Supplier Payment Status: Track and view the payment history and status of suppliers.
### Administrators (AM)
Administrator should have the rights to access and update all the application functionalities and data. They are the authorized personnel to create the above mentioned five types of users involved in the POTS system i.e. registration of users.
