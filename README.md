---

# To-Do List Application (Java Swing + SQLite)

A simple desktop To-Do List app built with Java Swing for GUI and SQLite for data storage.

---

## Features

* Display task list showing only task names
* View and edit task details: name, description, done status
* Add new tasks with default values
* Delete tasks
* Persist tasks locally using SQLite database
* Responsive UI with adjustable font sizes
* Buttons: Save, Delete, Add New Task, Quit

---

## How It Works

* On start, connects to SQLite DB and loads all tasks.
* Task list on the left displays task names only.
* Selecting a task shows its details on the right panel.
* Editing fields and clicking **Save** updates the task in DB.
* **Delete** removes the selected task.
* **Add New Task** creates a new default task in DB and selects it.
* **Quit** closes the app.

---

## Technologies Used

* Java SE (Swing GUI)
* SQLite (embedded local database)
* JDBC for database connectivity

---

## Structure

* `Main.java` — GUI setup, event handlers, and main app logic
* `Task.java` — POJO representing a Task (id, name, description, done)
* `TaskDAO.java` — Database operations: CRUD for tasks
* `Database.java` — Singleton for SQLite connection management

---

## Requirements

* Java 11 or higher
* SQLite JDBC driver (sqlite-jdbc.jar)
* IDE or command line capable of compiling and running Java apps

---

## How to Run

1. Compile all `.java` files and include SQLite JDBC in classpath.
2. Run `Main` class.
3. The GUI window opens showing task list and details panel.

---

## Notes

* Database file `tasks.db` is created in project directory if not existing.
* Tasks are saved persistently between sessions.
* UI fonts and layout optimized for readability and ease of use.

---
