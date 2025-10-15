---
layout: page
title: Developer Guide
---

- Table of Contents
  {:toc}

---

## **Acknowledgements**

- SE-EDU Address Book (Level 4) https://se-education.org/addressbook-level4/

---

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

---

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams are in this document `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.

</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The **_Architecture Diagram_** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/CourseBook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/CourseBook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.

- At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
- At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

- [**`UI`**](#ui-component): The UI of the App.
- [**`Logic`**](#logic-component): The command executor.
- [**`Model`**](#model-component): Holds the data of the App in memory.
- [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The _Sequence Diagram_ below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

- defines its _API_ in an `interface` with the same name as the Component.
- implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/CourseBook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/CourseBook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/CourseBook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

- executes user commands using the `Logic` component.
- listens for changes to `Model` data so that the UI can be updated with the modified data.
- keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
- depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/CourseBook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `CourseBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:

- When called upon to parse a user command, the `CourseBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `CourseBookParser` returns back as a `Command` object.
- All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component

**API** : [`Model.java`](https://github.com/se-edu/CourseBook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />

The `Model` component,

- stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
- stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
- stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
- does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `CourseBook`, which `Person` references. This allows `CourseBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>

### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/CourseBook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,

- can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
- inherits from both `CourseBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
- depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.coursebook.commons` package.

---

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedCourseBook`. It extends `CourseBook` with an undo/redo history, stored internally as an `CourseBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

- `VersionedCourseBook#commit()` — Saves the current address book state in its history.
- `VersionedCourseBook#undo()` — Restores the previous address book state from its history.
- `VersionedCourseBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitCourseBook()`, `Model#undoCourseBook()` and `Model#redoCourseBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedCourseBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitCourseBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `CourseBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitCourseBook()`, causing another modified address book state to be saved into the `CourseBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitCourseBook()`, so the address book state will not be saved into the `CourseBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoCourseBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial CourseBook state, then there are no previous CourseBook states to restore. The `undo` command uses `Model#canUndoCourseBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Logic.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

Similarly, how an undo operation goes through the `Model` component is shown below:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Model.png)

The `redo` command does the opposite — it calls `Model#redoCourseBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `CourseBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone CourseBook states to restore. The `redo` command uses `Model#canRedoCourseBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitCourseBook()`, `Model#undoCourseBook()` or `Model#redoCourseBook()`. Thus, the `CourseBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitCourseBook()`. Since the `currentStatePointer` is not pointing at the end of the `CourseBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

- **Alternative 1 (current choice):** Saves the entire address book.

  - Pros: Easy to implement.
  - Cons: May have performance issues in terms of memory usage.

- **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  - Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  - Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_

---

## **Documentation, logging, testing, configuration, dev-ops**

- [Documentation guide](Documentation.md)
- [Testing guide](Testing.md)
- [Logging guide](Logging.md)
- [Configuration guide](Configuration.md)
- [DevOps guide](DevOps.md)

---

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* is a university student
* has a need to manage a significant number of academic contacts (classmates, professors, TAs)
* takes multiple courses/modules each semester
* wants to easily identify and connect with classmates in specific courses
* prefers desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps
* values quick access to course-related contact information
* seeks academic collaboration and support from peers

**Value proposition**: CourseBook helps university students efficiently manage and organise their academic contacts by course affiliation, making it easy to identify classmates, access instructor information, and foster academic collaboration within specific modules.

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                    | I want to …​                                   | So that I can…​                                                        |
|----------|--------------------------------------------|------------------------------------------------|------------------------------------------------------------------------|
| `* * *`  | user                                       | launch coursebook from my laptop               | use it                                                                 |
| `* * *`  | new user                                   | see usage instructions                         | refer to instructions when I forget how to use the App                 |
| `* * *`  | user                                       | add a new person                               | keep track of my academic contacts                                     |
| `* * *`  | user                                       | delete a person                                | remove entries that I no longer need                                   |
| `* * *`  | user                                       | edit a contact                                 | keep contact information up to date                                    |
| `* * *`  | user                                       | input all the courses that I am taking        | identify friends taking the same courses as me                         |
| `* * *`  | user                                       | view contacts by course                        | easily find classmates for specific modules                            |
| `* *`    | time-conscious user                        | search for friends by name or keyword          | find a person quickly                                                  |
| `* *`    | user                                       | see my friends who have common courses with me | identify and contact them for study groups or collaboration            |
| `* *`    | user                                       | differentiate courses by colours               | visually identify which courses my friends take easily                 |
| `* *`    | user with many persons in the address book | keep a list of favourite contacts              | find my close friends easily                                           |
| `* *`    | user                                       | see each course's professor and TA contact information | contact them to ask for help more easily                     |
| `* *`    | user                                       | receive course recommendations based on what my friends are taking | discover relevant modules easily                        |
| `* *`    | user                                       | filter contacts by academic year              | connect with peers at similar academic levels                          |
| `*`      | user                                       | export course contact lists                   | share them with study group members                                    |                    |
| `*`      | user                                       | view course statistics                         | see how many friends are in each course                                |                              |                           |

### Use cases

(For all use cases below, the **System** is the `CourseBook` and the **Actor** is the `user`, unless specified otherwise)

**Use case: Delete a person**

**MSS**

1.  User requests to list persons
2.  CourseBook shows a list of persons
3.  User requests to delete a specific person in the list
4.  CourseBook deletes the person

    Use case ends.

**Extensions**

- 2a. The list is empty.

  Use case ends.

- 3a. The given index is invalid.

  - 3a1. CourseBook shows an error message.

    Use case resumes at step 2.

**Use case: Add Contact**

**MSS**

1. Student enters: add PERSON_NAME ph\PHONE email\EMAIL.
2. CourseBook validates the name, phone, and email.
3. CourseBook adds the contact to in-memory data.
4. CourseBook updates the UI to show the new contact.
5. CourseBook persists the change to the JSON file.

   Use case ends.

**Extensions**

- 1a. Missing name (e.g., add only):

  - 1a1. CourseBook shows error: "Please specify the contact name to be added!"

    Use case ends.

- 2a. Duplicate name exists (case/trim insensitive, internal spaces sensitive):

  - 2a1. CourseBook shows: "Friend <name> already exists! Please try a different name."
  - 2a2. CourseBook displays the existing user's details.

    Use case ends.

- 2b. Invalid email/phone format (if you validate):

  - 2b1. CourseBook shows field-specific error.

    Use case resumes at step 1.

**Use case: Tag Contact with Course**

**MSS**

1. Student enters: take COURSE \name PERSON_NAME.
2. CourseBook normalizes COURSE (trim + uppercase).
3. CourseBook locates PERSON_NAME (trim/case insensitive, internal spaces significant).
4. CourseBook adds COURSE to the contact's course list.
5. UI shows the updated contact.
6. CourseBook persists the change.

   Use case ends.

**Extensions**

- 1a. Missing \name flag (e.g., take CS2103T John Doe):

  - 1a1. Error: "Missing \name!"

    Use case ends.

- 1b. \name provided but empty (e.g., take CS2103T \name):

  - 1b1. Error: "Please specify the name!"

    Use case ends.

- 3a. Contact not found (e.g., different internal spacing):

  - 3a1. Error: "No contacts found! Please specify correct contact names."

    Use case ends.

- 4a. Course already tagged for that contact:

  - 4a1. CourseBook informs: "Course already recorded for this contact."

    Use case ends.

**Use case: View Contacts by Course**

**MSS**

1. Student enters: view COURSE.
2. CourseBook normalizes COURSE (trim + uppercase).
3. CourseBook finds contacts tagged with COURSE.
4. UI lists the matching contacts (name, phone, email).

   Use case ends.

**Extensions**

- 1a. Missing course (e.g., view):

  - 1a1. Error: "Please specify the course!"

    Use case ends.

- 3a. Course does not exist / no contact has it (e.g., view johngreen):

  - 3a1. Error: "Course does not exist, please enter a valid course."

    Use case ends.

- 4a. Large result set:

  - 4a1. CourseBook paginates or scrolls list (if supported).

    Use case ends.

**Use case: Delete Contact**

**MSS**

1. Student enters: delete PERSON_NAME.
2. CourseBook searches for matching names (trim/case insensitive, internal spaces significant).
3. If exactly one match, CourseBook deletes that contact.
4. UI reflects the removal.
5. CourseBook persists the change.

   Use case ends.

**Extensions**

- 1a. Missing name (e.g., delete only):

  - 1a1. Error: "Please specify the contact name to be deleted!"

    Use case ends.

- 2a. Multiple matches (duplicate names):

  - 2a1. CourseBook lists matches with indices.
  - 2a2. Student enters the index to delete.
  - 2a3. CourseBook deletes the selected contact, updates UI, and saves.

    Use case ends.

- 2b. No match (e.g., internal spacing differs):

  - 2b1. Error: "No contacts found! Please specify correct contact names."

    Use case ends.

**Use case: Auto-Save on Change (Internal)**

**MSS**

1. A data-mutating use case completes successfully.
2. CourseBook serializes current state to a cleanly structured JSON file.
3. On next app start, CourseBook loads this file and renders the latest contacts.

   Use case ends.

**Extensions**

- 2a. Disk write error:

  - 2a1. CourseBook shows a clear error and advises retry or checking file permissions.
  - 2a2. In-memory state remains; user can retry operation or exit safely.

**Use case: Exit Application**

**MSS**

1. Student enters: exit.
2. CourseBook confirms any pending in-memory state has already been saved (UC05).
3. CourseBook terminates.

   Use case ends.

**Extensions**

- 1a. Extra arguments (e.g., exit now):

  - 1a1. Error: "The 'exit' command does not take any arguments."
  - 1a2. App remains open.

    Use case ends.

- 1b. Mixed case / whitespace (e.g., EXIT):

  - 1b1. CourseBook trims and treats as valid; resume Step 2.

**Use case: Identify Who To Ask for a Module (End-to-End "Value Prop" Flow)**

**MSS**

1. Student enters: view COURSE (e.g., view CS2103T).
2. CourseBook normalizes COURSE and lists matching contacts with phone/email.
3. Student selects a contact and copies a detail (e.g., email) from the UI.
4. Student contacts the friend outside CourseBook (e.g., email/WhatsApp).

   Use case ends.

**Extensions**

- 1a. Course not found or nobody tagged yet:

  - 1a1. Error: "Course does not exist, please enter a valid course."
  - 1a2. (Optional helper) CourseBook hints: "Try take CS2103T \name John Doe to tag a friend first."

    Use case ends.

- 2a. Many contacts:

  - 2a1. (Optional) CourseBook supports filtering or sorting (future).

    Use case ends.

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2.  Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  The application should aim for less than 1 hour of downtime per month to ensure continuous availability for users
5.  A user should be able to complete all tasks using the command line interface
6.  Developers should resolve critical bugs within a day of detection. Quick issue resolution is vital for maintaining a positive user experience.
7.  The application should not interfere with existing antivirus software. This compatibility is essential for ensuring seamless integration into users' workflows.
8.  Results of commands should be returned within 1 second to ensure a smooth user experience. If results take too long, users may become frustrated and abandon the search.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Private contact detail**: A contact detail that is not meant to be shared with others
* **Actor**: A user or external entity that interacts with the system
* **CourseBook**: The system that stores and manages contact information
* **Contact**: A person whose details are stored in the address book
* **Main Success Scenario (MSS)**: The typical flow of events in a use case when everything goes as expected
* **Parser**: The component that interprets user input and converts it into a command that the program can execute

---

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
      Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
