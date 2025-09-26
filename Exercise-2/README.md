Exercise 2: Astronaut Daily Schedule

This project manages an astronaut's daily schedule using Java.

Output:

Astronaut Daily Schedule Organizer. Type 'help' 
>
help
Commands:
 add|description|HH:MM|HH:MM|priority  — add a task
 remove|description                     — remove a task by description
 view                                   — view all tasks sorted by start time
 help                                   — show this help
 exit                                   — exit app
>
add|Morning Exercise|06:00|07:00|High

Task added successfully. No conflicts.
>
add|Team Meeting|09:00|10:00|Medium

Task added successfully. No conflicts.
>
add|Breakfast|06:30|07:30|Low
Error: Task conflicts with existing task "Morning Exercise".
>
view
06:00 - 07:00: Morning Exercise [HIGH]
09:00 - 10:00: Team Meeting [MEDIUM]
>
remove|Team Meeting

Task removed successfully.
>
view

06:00 - 07:00: Morning Exercise [HIGH]
>
exit
Exiting. Goodbye.
