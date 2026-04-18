# Pomodoro Technique App
> A simple Android productivity timer built for a Mobile Programming assignment.

---

## Overview

This is a straightforward implementation of the [Pomodoro Technique](https://en.wikipedia.org/wiki/Pomodoro_Technique)  a time management method that breaks work into focused 25-minute intervals, separated by short breaks.

Built as a college assignment, the app covers the core loop: task management, countdown timer, and session switching.

---

## Features

- Countdown timer with Start / Pause control
- Pomodoro, Short Break, and Long Break modes
- Task list with estimated pomodoro counts
- Mark tasks as complete with visual strikethrough feedback
- Auto-advances to the next task upon completion

---

## Architecture

I made an honest attempt at separating concerns using an **MVSRC-inspired structure**:

```
com.mika.pomodorotechniqueapp
├── model/         # Task.java — plain data object
├── repository/    # TaskRepository.java — in-memory data source
├── controller/    # TaskController.java — business logic layer
├── utility/       # TaskAdapter.java — ListView adapter
└── MainActivity.java
```

No database is used — tasks are stored in memory via an `ArrayList`. For the scope of this assignment, a full SQLite or Room implementation would be overkill.

---

## Honest Notes

This is my first Android project. Coming from a backend background (Spring Boot, Laravel), the UI layer was unfamiliar territory — and it shows in places. Some logic that ideally belongs in the controller ended up bleeding into `MainActivity.java`. That's a known trade-off I intend to correct in future projects.

UI design is not my strong suit, and I won't pretend otherwise. The layout is functional and intentionally minimal `LinearLayout` all the way down. What I do care about is clean separation of concerns, and that is where most of my effort went.

Android development sits outside my primary domain of backend engineering, but I find genuine value in understanding the full stack — even the parts that aren't my speciality.

---

## Tech Stack

| | |
|---|---|
| Language | Java |
| Min SDK | Android 8.0 (API 26) |
| Layout | LinearLayout / RelativeLayout |
| Data | In-memory ArrayList |
| IDE | Android Studio |

---

## Room for Improvement

- Persist tasks across sessions (SharedPreferences or Room)
- Proper MVSRC separation — strip MainActivity down to a pure View
- Session counter for the long break cycle (every 4 pomodoros)
- Notification / alarm when a session ends
- Estimated finish time calculation

---

*Built by Mika — Information Systems, Fourth Semester.*